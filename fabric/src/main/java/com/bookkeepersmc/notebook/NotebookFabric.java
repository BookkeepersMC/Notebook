package com.bookkeepersmc.notebook;

import net.fabricmc.api.ModInitializer;

public class NotebookFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        NotebookCommon.init();
    }
}
