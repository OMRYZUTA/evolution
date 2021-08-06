package il.ac.mta.zuli.evolution.engine.exceptions;

public class ValidationException extends RuntimeException
{
    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
    public ValidationException(String str, Throwable e){
        super(str, e);
    }
}
