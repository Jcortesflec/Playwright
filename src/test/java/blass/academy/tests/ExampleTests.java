package blass.academy.tests;

import blass.academy.utils.BaseTest;
import blass.academy.utils.Logs;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class  ExampleTests extends BaseTest {
    @BeforeEach
    void setUp(Page page) {
        Logs.info("Navegando a la pagina");
        page.navigate("/");
    }

    @Test
    void ejemploTest(Page page) {
        //page.pause();
        Logs.info("Verificando el texto");
        assertThat(page.getByText("Web Automation en Blass")).isVisible();
        assertThat(page.getByText("Generics")).isVisible();
        //page.getByText("Generics").click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Login")).click();
        page.getByTestId("username-input").fill("blass_academy");
        page.getByTestId("password-input").fill("web_blass");
        assertThat(page.getByTestId("submit-button")).isEnabled();
        page.getByTestId("submit-button").click();

    }

    @Test
    @Disabled("Ejemplo que siempre falla")
    void ejemploFallidoTest(Page page) {
        Logs.info("Verificando el texto");
        assertThat(page.getByText("Web Automation en Blass 2")).isVisible();
    }
}