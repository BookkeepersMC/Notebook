package com.bookkeepersmc.notebook.neoforge;

import com.bookkeepersmc.notebook.NotebookCommon;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(NotebookCommon.MOD_ID)
public class NotebookNeoForge {

    public NotebookNeoForge(IEventBus modEventBus) {
        NotebookCommon.init();
    }
}