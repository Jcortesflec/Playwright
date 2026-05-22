package blass.academy.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {
    public static void obtenerEvidenciasLocales(String nombre) {
        if (PageProvider.pageEsValido()) {
            tomarScreenshot(nombre);
            obtenerPageSource(nombre);
        }
    }

    public static void obtenerEvidenciasAllure() {
        if (PageProvider.pageEsValido()) {
            tomarScreenshotAllure();
            obtenerPageSourceAllure();
        }
    }

    public static void eliminarEvidencias() {
        try {
            Logs.debug("Borrando las carpetas de evidencias");
            FileUtils.deleteDirectory(new File(Config.get("path.evidencias")));
        } catch (IOException ioException) {
            Logs.error("Error al borrar la evidencia anterior: %s", ioException.getLocalizedMessage());
        }
    }

    private static void tomarScreenshot(String screenshotName) {
        Logs.debug("Tomando screenshot");
        try {
            final var screenshotDir = Paths.get(Config.get("path.evidencias"), screenshotName);
            Files.createDirectories(screenshotDir);

            final var screenshotPath = screenshotDir.resolve("screenshot.png");

            PageProvider.getPage().screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));

            Logs.debug("Screenshot guardado en: %s", screenshotPath.toAbsolutePath());
        } catch (Exception e) {
            Logs.error("No se pudo tomar screenshot: %s", e.getMessage());
        }
    }

    private static void obtenerPageSource(String name) {
        Logs.debug("Guardando page source");
        try {
            // Crea la carpeta evidencePath/name si no existe
            final var htmlDir = Paths.get(Config.get("path.evidencias"), name);
            Files.createDirectories(htmlDir);

            // Nombre de archivo con timestamp para no sobrescribir
            final var htmlPath = htmlDir.resolve("page-source.html");

            // Obtiene el HTML actual de la página
            final var rawHtml = (String) PageProvider.getPage().evaluate(
                    "() => new XMLSerializer().serializeToString(document)"
            );

            final var doc = Jsoup.parse(rawHtml);
            doc.outputSettings()
                    .syntax(Document.OutputSettings.Syntax.html) // asegura HTML
                    .escapeMode(Entities.EscapeMode.xhtml)       // opcional
                    .charset(StandardCharsets.UTF_8)
                    .indentAmount(2)
                    .prettyPrint(true);

            final var prettyHtml = doc.outerHtml();

            Files.writeString(htmlPath, prettyHtml, StandardCharsets.UTF_8);

            Logs.debug("Page source guardado en: %s", htmlPath.toAbsolutePath());
        } catch (Exception e) {
            Logs.error("No se pudo guardar el page source: %s", e.getMessage());
        }
    }

    @SuppressWarnings("all")
    @Attachment(value = "screenshot", type = "image/png")
    private static byte[] tomarScreenshotAllure() {
        Logs.debug("Tomando Screenshot para Allure");
        return PageProvider.getPage().screenshot();
    }

    @SuppressWarnings("all")
    @Attachment(value = "pageSource", type = "text/plain", fileExtension = "txt")
    private static String obtenerPageSourceAllure() {
        Logs.debug("Tomando PageSource para Allure");
        return PageProvider.getPage().content();
    }
}
