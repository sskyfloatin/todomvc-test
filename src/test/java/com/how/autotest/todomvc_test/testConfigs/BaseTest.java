package com.how.autotest.todomvc_test.testConfigs;

import com.codeborne.selenide.Configuration;

public class BaseTest {

     {
        Configuration.fastSetValue = true;

        Configuration.baseUrl = System.getProperty("selenide.baseUrl",
                "http://todomvc4tasj.herokuapp.com");

        Configuration.timeout = Long.parseLong(System.getProperty("selenide.timeout", "6000"));
     }
}
