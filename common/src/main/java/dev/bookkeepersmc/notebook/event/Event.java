package dev.bookkeepersmc.notebook.event;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;


@ApiStatus.NonExtendable
public abstract class Event<T> {
    protected volatile T invoker;

    public final T invoker() {
        return invoker;
    }
    public abstract void register(T listener);
    public static final ResourceLocation DEFAULT_PHASE = new ResourceLocation("fabric", "default");

    public void register(ResourceLocation phase, T listener) {
        register(listener);
    }

    public void addPhaseOrdering(ResourceLocation firstPhase, ResourceLocation secondPhase) {
    }
    public static final class Factory {

        public static <T> Event<T> createArrayBacked(Class<? super T> type, Function<T[], T> factory) {
            return EventImpl.create(type, factory);
        }
    }
}
