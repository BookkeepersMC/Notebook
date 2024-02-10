package dev.bookkeepersmc.notebook.registry.content.util;

import dev.bookkeepersmc.notebook.NotebookCommon;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Array;
import java.util.function.Function;

public class ArrayEvent<T> extends Event<T> {
    private final Function<T[], T> factory;
    private final Object lock = new Object();
    private T[] handlers;

    public ArrayEvent(Class<? super T> type, Function<T[], T> factory) {
        this.factory = factory;
        this.handlers = (T[]) Array.newInstance(type, 0);
        update();
    }
    void update() {this.invoker = factory.apply(handlers);}

    @Override
    public void register(T listener) {
        register(new ResourceLocation(NotebookCommon.MOD_ID, "default"), listener);
    }
}
