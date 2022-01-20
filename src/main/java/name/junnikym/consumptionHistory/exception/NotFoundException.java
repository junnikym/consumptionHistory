package name.junnikym.consumptionHistory.exception;

public class NotFoundException extends RuntimeException {

	public static final int StatusCode = 404;

	public NotFoundException(String target) {
		super("Can not find "+target);
	}

}
