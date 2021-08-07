package il.ac.mta.zuli.evolution.engine.exceptions;


public class EmptyCollectionException extends IllegalArgumentException
{
    public EmptyCollectionException(String errorMessage) {
        super(errorMessage);
    }
    public EmptyCollectionException(String str, Throwable e){
        super(str, e);
    }
}