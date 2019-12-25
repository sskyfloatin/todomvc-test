package com.todomvcTest;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
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
        add("a");
        add("b");
        add("c");
        assertText(texts("a", "b", "c"));

        // edit
        doubleClick("b");
        tasks.findBy(cssClass(editedTask)).find(".edit")
                .append(" edited").pressEnter();

        // complete & clear
        tasks.findBy(text("b edited")).find(".toggle").click();
        $("#clear-completed").click();
        assertText(texts("a", "c"));

        // cancel editing
        doubleClick("a");
        tasks.findBy(cssClass(editedTask)).find(".edit").append(" to be canceled").pressEscape();

        // delete
        tasks.findBy(text("a")).hover().find(".destroy").click();
        assertText(texts("c"));
    }

    private void doubleClick(String task) {
        tasks.findBy(text(task)).doubleClick();
    }

    final String editedTask = "editing";

    private final ElementsCollection tasks = $$("#todo-list>li");

    private void assertText(CollectionCondition c) {
        tasks.shouldHave(c);
    }

    private void add(String text) {
        $("#new-todo").append(text).pressEnter();
    }
}
