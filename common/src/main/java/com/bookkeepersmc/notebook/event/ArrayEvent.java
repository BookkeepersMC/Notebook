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

package com.bookkeepersmc.notebook.event;

import com.bookkeepersmc.notebook.NotebookCommon;
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
