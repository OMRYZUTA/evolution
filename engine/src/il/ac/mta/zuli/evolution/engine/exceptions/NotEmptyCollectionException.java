package il.ac.mta.zuli.evolution.engine.exceptions;


public class NotEmptyCollectionException extends IllegalArgumentException
{
    public NotEmptyCollectionException(String errorMessage) {
        super(errorMessage);
    }
    public NotEmptyCollectionException(String str, Throwable e){
        super(str, e);
    }
}