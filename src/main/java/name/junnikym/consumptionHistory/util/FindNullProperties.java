package name.junnikym.consumptionHistory.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FindNullProperties {

	public static <S_T, D_T> D_T convertWithoutNull (S_T source, D_T destination) throws InvocationTargetException, IllegalAccessException {
		Class sourceClass = source.getClass();
		Class destinationClass = destination.getClass();

		String fieldNames[] = Arrays
				.stream(sourceClass.getDeclaredFields())
				.map(Field::getName)
				.toArray(String[]::new);

		Map<String, Method> sourceMethods = Arrays
				.stream(sourceClass.getDeclaredMethods())
				.collect(Collectors.toMap(Method::getName, x->x));

		Map<String, Field> destinationFields = Arrays
				.stream(destinationClass.getDeclaredFields())
				.collect(Collectors.toMap(Field::getName, x->x));

		for (String it : fieldNames) {

			// Source 내부 Getter 함수 찾기
			Method getter = sourceMethods.get(
					"get"+it.substring(0, 1).toUpperCase() + it.substring(1));
			if(getter == null)
				continue;

			// 값의 NULL 여부 확인
			Object object = getter.invoke(source);
			if(object == null)
				continue;

			// Destination 의 값 수정
			Field target = destinationFields.get(it);
			if(target == null)
				continue;

			target.setAccessible(true);
			target.set(destination, object);
		}

		return destination;
	}

}
