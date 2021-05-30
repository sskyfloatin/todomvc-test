package com.how.autotest.todomvc_test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.how.autotest.todomvc_test.testConfigs.AtTodoMvcWithClearedStorageBeforeEachTest;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoMvcTest extends AtTodoMvcWithClearedStorageBeforeEachTest {

    @Test
    public void filterTodos() {
        add("a", "b", "c");

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