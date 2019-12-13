package com.todomvcTest;

import com.selenide.bootcamp.common.selectors.XPath;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class PocTest {

    @Test
    public void basicTestCase() {

        open("http://todomvc4tasj.herokuapp.com/");
        Wait().until(jsReturnsValue("return $._data($('#clear-completed').get(0), 'events').hasOwnProperty('click')"));

        $("#new-todo").append("a").pressEnter();
        $("#new-todo").append("b").pressEnter();
        $("#new-todo").append("c").pressEnter();
        $$("#todo-list>li").shouldHave(exactTexts("a","b","c"));

        $("#todo-list>li:nth-of-type(1)").doubleClick();
        $x("//*[@id='todo-list']/li[.//text()='a']/input").append("b").pressEnter();
        $("#todo-list>li:nth-of-type(2)").doubleClick();
        $x("//*[@id='todo-list']/li[.//text()='b']/input").append("c").pressEnter();
        $("#todo-list>li:nth-of-type(3)").doubleClick();
        $x("//*[@id='todo-list']/li[.//text()='c']/input").append("d").pressEnter();
        $$("#todo-list>li").shouldHave(exactTexts("ab","bc","cd"));

        $("#todo-list li:nth-child(2) .toggle").click();
        $$("#todo-list li.completed").shouldHave(texts("bc"));

        $("#todo-list li:nth-child(2) .toggle").click();
        $$("#todo-list li:not(.completed)").shouldHave(texts("ab","bc","cd"));

        $("#toggle-all").click();
        $$("#todo-list li.completed").shouldHave(texts("ab","bc","cd"));

        $("#toggle-all").click();
        $$("#todo-list li:not(.completed)").shouldHave(texts("ab","bc","cd"));

        $("#todo-list li:nth-child(2) .toggle").click();
        $x("//*[@href='#/active']").click();
        $$("#todo-list li:not(.completed)").shouldHave(texts("ab","cd"));

        $x("//*[@href='#/completed']").click();
        $$("#todo-list li.completed").shouldHave(texts("bc"));

        $x("//*[@href='#/']").click();
        $("#clear-completed").click();
        $$("#todo-list li").shouldHave(texts("ab","cd"));

        $x("//*[@id='todo-list']/li[.//text()='ab']").hover();
        $x("//*[@id='todo-list']/li[.//text()='ab']/*/*" + XPath.filterBy.cssClass("destroy")).click();
        $$("#todo-list>li").shouldHave(texts("cd"));

    }
}
