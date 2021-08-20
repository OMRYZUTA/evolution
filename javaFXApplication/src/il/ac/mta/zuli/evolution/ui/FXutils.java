package il.ac.mta.zuli.evolution.ui;

import java.util.List;
import java.util.Map;

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
}
