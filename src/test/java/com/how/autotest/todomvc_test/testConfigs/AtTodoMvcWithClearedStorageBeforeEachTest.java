package com.how.autotest.todomvc_test.testConfigs;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class AtTodoMvcWithClearedStorageBeforeEachTest extends BaseTest {

    @BeforeAll
    public static void openApp() {
        open("");
        Wait().until(jsReturnsValue("return $._data($('#clear-completed').get(0), 'events').hasOwnProperty('click')"));
    }

    @BeforeEach
    public void clearStorage() {
        Selenide.clearBrowserLocalStorage();
        Selenide.refresh();
    }
}
