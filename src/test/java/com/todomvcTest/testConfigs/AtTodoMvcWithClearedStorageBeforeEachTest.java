package com.todomvcTest.testConfigs;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;

public class AtTodoMvcWithClearedStorageBeforeEachTest extends BaseTest {

    @BeforeEach
    public void loadApp() {
        openApp();
        Selenide.refresh();
        Selenide.clearBrowserLocalStorage();
    }
}
