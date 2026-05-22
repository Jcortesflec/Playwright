package blass.academy.utils;

import blass.academy.listeners.EvidenceExtension;
import blass.academy.listeners.GlobalEvidenceCleaner;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@UsePlaywright(CustomOptions.class)
@ExtendWith({EvidenceExtension.class, GlobalEvidenceCleaner.class})
public class BaseTest {
    @BeforeEach
    public void masterSetUp(Page page) {
        Logs.debug("Agregando el page al provider");
        PageProvider.setPage(page);
    }
}
