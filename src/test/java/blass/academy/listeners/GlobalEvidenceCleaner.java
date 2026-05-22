package blass.academy.listeners;

import blass.academy.utils.FileManager;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GlobalEvidenceCleaner implements BeforeAllCallback {
    private static boolean cleaned = false; // asegura que corra solo una vez

    @Override
    public void beforeAll(ExtensionContext context){
        if (cleaned) return;
        FileManager.eliminarEvidencias();
        cleaned = true;
    }
}