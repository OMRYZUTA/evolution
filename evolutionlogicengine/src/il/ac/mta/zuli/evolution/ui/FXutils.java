package il.ac.mta.zuli.evolution.ui;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FXutils {

    public static <K, V> String myMapToString(Map<K, V> map) {
        StringBuilder sb = new StringBuilder();
        for (V value : map.values()) {
            sb.append(value + System.lineSeparator());
        }

        return sb.toString();
    }

    public static String myListToString(List<?> values) {
        StringBuilder sb = new StringBuilder();
        for (Object object : values) {
            sb.append(object + System.lineSeparator());
        }

        return sb.toString();
    }

    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public static boolean isNullOrEmpty(String str) {
        if (str == null) {
            return true;
        }

        return str.trim().isEmpty();
    }

    public static String getToRootError(Throwable e){
        StringBuilder sb = new StringBuilder();
        Throwable root = findThrowableRootCause(e);
        Throwable currError = e;

        while (!currError.equals(root)) {
            sb.append(currError.getMessage() + System.lineSeparator());
            currError = currError.getCause();
        }

        sb.append(root.getMessage());

        return e.getMessage()+". "+ sb;
    }
    private static Throwable findThrowableRootCause(Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable rootCause = throwable;

        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }

        return rootCause;
    }
}
