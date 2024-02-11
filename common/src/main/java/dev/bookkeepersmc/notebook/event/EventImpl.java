package dev.bookkeepersmc.notebook.event;

import com.google.common.collect.MapMaker;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class EventImpl {

    private static final Set<ArrayEvent<?>> ARRAY_EVENTS = Collections.newSetFromMap(new MapMaker().weakKeys().makeMap());

    private EventImpl() {}

    public static <T> Event<T> create(Class<? super T> type, Function<T[], T> factory) {
        ArrayEvent<T> event = new ArrayEvent<>(type, factory);
        ARRAY_EVENTS.add(event);
        return event;
    }
}
