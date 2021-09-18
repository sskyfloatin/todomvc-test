package com.how.autotest.todomvc_test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.how.autotest.todomvc_test.testConfigs.BaseTest;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.Wait;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class TodoMvcTest extends BaseTest {

    @Test
    public void filterTodos() {
        givenAppOpenedWith("a", "b", "c");
        toggle("b");

        filterActive();
        assertTodos("a", "c");

        filterCompleted();
        assertTodos("b");

        filterAll();
        assertTodos("a", "b", "c");
    }

    @Test
    public void basicTodoManagement() {
        givenAppOpenedWith("a", "b", "c");

        add("a", "b", "c");
        assertTodos("a", "b", "c");

        edit("b", "b edited");

        toggle("b edited");
        clearCompleted();
        assertTodos("a", "c");

        cancelEdit("a", "to be canceled");

        delete("a");
        assertTodos("c");
    }

    private final ElementsCollection todos  = $$("#todo-list>li");

    private void givenAppOpenedWith(String... texts) {
        openApp();
        add(texts);
    }

    private void openApp() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserLocalStorage();
        }
        open("/");
        Wait().until(jsReturnsValue(
                "return $._data($('#clear-completed').get(0), 'events').hasOwnProperty('click')"));
    }

    private void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }

    private void assertTodos(String... texts) {
        todos.filterBy(visible).shouldHave(texts(texts));
    }

    private void toggle(String todo) {
        todos.findBy(text(todo)).find(".toggle").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private SelenideElement startEditing(String oldText, String newText) {
        todos.findBy(text(oldText)).doubleClick();
        return todos.findBy(cssClass("editing")).find(".edit").setValue(newText);
    }

    private void edit(String oldText, String newText) {
        startEditing(oldText, newText).pressEnter();
    }

    private void cancelEdit(String oldText, String newText) {
        startEditing(oldText, newText).pressEscape();
    }

    private void delete(String text) {
        todos.findBy(text(text)).hover().find(".destroy").click();
    }

    private void filterActive() {
        $("[href='#/active']").click();
    }

    private void filterCompleted() {
        $("[href='#/completed']").click();
    }

    private void filterAll() {
        $("[href='#/']").click();
    }
}