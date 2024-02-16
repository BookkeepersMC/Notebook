package com.bookkeepersmc.notebook.registry.content.util;

import java.util.*;
import java.util.function.*;

public class ImmutableUtils {
    public static <T> Set<T> getAsSet(Supplier<Set<T>> getter, Consumer<Set<T>> setter) {
        Set<T> set = getter.get();
        if (!(set instanceof HashSet)) {
            setter.accept(set = new HashSet<>(set));
        }
        return set;
    }
    public static <T> List<T> getAsList(Supplier<List<T>> getter, Consumer<List<T>> setter) {
        List<T> set = getter.get();

        if (!(set instanceof ArrayList)) {
            setter.accept(set = new ArrayList<>(set));
        }

        return set;
    }

    public static <K, V> Map<K, V> getAsMap(Supplier<Map<K, V>> getter, Consumer<Map<K, V>> setter) {
        Map<K, V> map = getter.get();

        if (!(map instanceof HashMap)) {
            setter.accept(map = new HashMap<>(map));
        }

        return map;
    }
}
