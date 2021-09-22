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
        givenAppOpened();

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

    @Test
    public void uncompleteTodo() {
        givenAppOpenedWith("a");
        toggle("a");

        filterCompleted();
        assertTodos("a");

        toggle("a");
        filterActive();
        assertTodos("a");
    }

    @Test
    public void completeAllTodos() {
        givenAppOpenedWith("a", "b", "c");

        toggleAll();
        filterCompleted();
        assertTodos("a", "b", "c");
    }

    @Test
    public void uncompleteAllTodos() {
        givenAppOpenedWith("a", "b", "c");

        toggleAll();
        filterCompleted();
        toggleAll();

        filterActive();
        assertTodos("a", "b", "c");
    }

    @Test
    public void clearCompleteTodo() {
        givenAppOpenedWith("a", "b", "c");
        toggle("a");

        delete("a");
        assertTodos("b", "c");
    }

    @Test
    public void clearAllTodos() {
        givenAppOpenedWith("a", "b", "c");
        toggleAll();

        clearCompleted();
        assertTodoListEmpty();
    }

    @Test
    public void editCompleteTodo() {
        givenAppOpenedWith("a", "b", "c");
        toggle("a");

        edit("a", "abc");
        assertTodos("abc", "b", "c");
    }

    private final ElementsCollection todos  = $$("#todo-list>li");

    private void givenAppOpenedWith(String... texts) {
        givenAppOpened();
        add(texts);
    }

    private void givenAppOpened() {
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

    private void assertTodoListEmpty() {
        $$("#todo-list>li").shouldHaveSize(0);
    }

    private void toggle(String todo) {
        todos.findBy(text(todo)).find(".toggle").click();
    }

    private void toggleAll() {
        $("#toggle-all").click();
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