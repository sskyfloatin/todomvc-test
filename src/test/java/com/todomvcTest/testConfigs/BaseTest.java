package com.todomvcTest.testConfigs;

import com.codeborne.selenide.Configuration;

import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class BaseTest {

    public void openApp() {
        open(Configuration.baseUrl);
        Wait().until(jsReturnsValue
                ("return $._data($('#clear-completed').get(0), 'events').hasOwnProperty('click')"));
    }

    {
        Configuration.fastSetValue = true;

        Configuration.baseUrl = System.getProperty("selenide.baseUrl",
                "http://todomvc4tasj.herokuapp.com/");

        Configuration.timeout = Long.parseLong(System.getProperty("selenide.timeout", "6000"));
    }
}
