package com.oms.networkutil.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Util {
    private Util() {
    }

    public static <T> List<T> getSnapshot(Collection<T> other) {

        List<T> result = new ArrayList<>(other.size());
        for (T item : other) {
            result.add(item);
        }
        return result;
    }
}
