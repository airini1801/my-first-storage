package pro.xway.file_storage.exception;

public class ApplicationException extends RuntimeException {

    private final ExceptionEnum exception;

    public ApplicationException(ExceptionEnum exception) {
        super(exception.getMessage());
        this.exception = exception;
    }

    public ExceptionEnum getException() {
        return exception;
    }
}
