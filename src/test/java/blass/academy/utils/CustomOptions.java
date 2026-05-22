package blass.academy.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

import java.util.List;

public class CustomOptions implements OptionsFactory {
    @Override
    public Options getOptions() {
        final var options = new Options()
                .setLaunchOptions(createLaunchOptions()) //launch options
                .setContextOptions(createContextOptions()) //context options
                .setHeadless(Boolean.parseBoolean(Config.get("headless"))) //para que se renderice el browser
                .setBrowserName(Config.get("browser")) //chromium, firefox, webkit;
                .setTestIdAttribute(Config.get("test.id"))
                .setBaseUrl(Config.get("base.url"));

        final var channel = Config.get("channel");
        if (channel != null && !channel.isBlank()) {
            options.setChannel(channel);
        }

        return options;
    }

    //opciones del browser
    private BrowserType.LaunchOptions createLaunchOptions() {
        final var arguments = List.of(
                "--start-maximized",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );

        return new BrowserType
                .LaunchOptions()
                .setSlowMo(Integer.parseInt(Config.get("slow.mo")))
                .setArgs(arguments);
    }

    //opciones del browser context
    private Browser.NewContextOptions createContextOptions() {
        final var contextOptions = new Browser.NewContextOptions()
                .setViewportSize(null)
                .setIgnoreHTTPSErrors(true);

        if (Boolean.parseBoolean(Config.get("headless"))) {
            // En modo headless, forzamos el Viewport leyendo tu config.properties
            final var windowWidth = Integer.parseInt(Config.get("window.width"));
            final var windowHeight = Integer.parseInt(Config.get("window.height"));
            contextOptions.setViewportSize(windowWidth, windowHeight);
        } else {
            // En modo visible, anulamos el Viewport para que --start-maximized llene toda la pantalla
            contextOptions.setViewportSize(null);
        }

        return contextOptions;
    }
}