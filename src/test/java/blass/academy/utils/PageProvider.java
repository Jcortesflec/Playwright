package blass.academy.utils;

import com.microsoft.playwright.Page;

public class PageProvider {
    private static final ThreadLocal<Page> threadLocal = new ThreadLocal<>();

    public static void setPage(Page page) {
        threadLocal.set(page);
    }

    public static Page getPage() {
        return threadLocal.get();
    }

    public static void removePage() {
        threadLocal.remove();
    }

    public static boolean pageEsValido() {
        return threadLocal.get() != null;
    }
}
