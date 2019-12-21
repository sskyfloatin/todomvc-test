package com.todomvcTest;

import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TodoMvcTest {

    @Test
    public void basicTaskActions() throws InterruptedException {

        open("http://todomvc4tasj.herokuapp.com/");
        Wait().until(jsReturnsValue("return $._data($('#clear-completed').get(0), 'events').hasOwnProperty('click')"));

        //Create
        $("#new-todo").append("a").pressEnter();
        $("#new-todo").append("b").pressEnter();
        $("#new-todo").append("c").pressEnter();
        $$("#todo-list>li").shouldHave(texts("a", "b", "c"));

        //Edit
        $$("#todo-list>li").findBy(text("b")).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing")).find(".edit")
                .append(" Edited").pressEnter();

        //Complete
        $$("#todo-list>li").findBy(text("b Edited")).find(".toggle").click();
        $("#clear-completed").click();
        $$("#todo-list>li").shouldHave(texts("a", "c"));

        //Cancel while editing
        $$("#todo-list>li").findBy(text("a")).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing")).find(".edit").append(" to be canceled").pressEscape();

        //Delete
        $$("#todo-list>li").findBy(text("a")).hover().find(".destroy").click();
        $$("#todo-list>li").shouldHave(texts("c"));
    }
}
