package name.junnikym.consumptionHistory.exception;

public class AlreadyExistsException extends RuntimeException {

	public static final int StatusCode = 409;

	public AlreadyExistsException(String target) {
		super(target+" is already exists");
	}

}
