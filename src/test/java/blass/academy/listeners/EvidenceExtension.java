package blass.academy.listeners;

import blass.academy.utils.FileManager;
import blass.academy.utils.Logs;
import blass.academy.utils.PageProvider;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class EvidenceExtension implements AfterTestExecutionCallback {
    @Override
    public void afterTestExecution(ExtensionContext context) {
        Logs.flushToAllure();
        if (context.getExecutionException().isPresent()) {
            FileManager.obtenerEvidenciasLocales(getFolderName(context));
            FileManager.obtenerEvidenciasAllure();
        }
        Logs.debug("Quitando el page del provider");
        PageProvider.removePage();
    }

    private String getFolderName(ExtensionContext context) {
        return context.getDisplayName()
                .replace(" ", "_")
                .replace("page_", "")
                .replace("(Page)", "")
                .replace("()", "");
    }
}
