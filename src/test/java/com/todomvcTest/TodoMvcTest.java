package com.todomvcTest;

import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TodoMvcTest {

    @Test
    public void basicTaskActions() {

        open("http://todomvc4tasj.herokuapp.com/");
        Wait().until(jsReturnsValue("return $._data($('#clear-completed').get(0), 'events').hasOwnProperty('click')"));

        // create
        $("#new-todo").append("a").pressEnter();
        $("#new-todo").append("b").pressEnter();
        $("#new-todo").append("c").pressEnter();
        $$("#todo-list>li").shouldHave(texts("a", "b", "c"));

        // edit
        $$("#todo-list>li").findBy(text("b")).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing")).find(".edit")
                .append(" edited").pressEnter();

        // complete and clear
        $$("#todo-list>li").findBy(text("b edited")).find(".toggle").click();
        $("#clear-completed").click();
        $$("#todo-list>li").shouldHave(texts("a", "c"));

        // cancel editing
        $$("#todo-list>li").findBy(text("a")).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing")).find(".edit").append(" to be canceled").pressEscape();

        // delete
        $$("#todo-list>li").findBy(text("a")).hover().find(".destroy").click();
        $$("#todo-list>li").shouldHave(texts("c"));
    }
}
