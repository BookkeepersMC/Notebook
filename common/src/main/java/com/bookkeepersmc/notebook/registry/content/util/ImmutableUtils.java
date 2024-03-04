/*
 * Copyright (c) 2023, 2024 BookkeepersMC under the MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
