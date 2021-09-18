package il.ac.mta.zuli.evolution.engine.exceptions;


public class InvalidOperationException extends RuntimeException
{
    public InvalidOperationException(String errorMessage) {
        super(errorMessage);
    }
    public InvalidOperationException(String str, Throwable e){
        super(str, e);
    }
}