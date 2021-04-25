package com.todomvcTest;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.todomvcTest.testConfigs.AtTodoMvcWithClearedStorageBeforeEachTest;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class TodoMvcTestIndependanceDay extends AtTodoMvcWithClearedStorageBeforeEachTest {

    @Test
    public void filterTodos() {

        add("a", "b", "c");

        toggle("b");

        sortByType("active");
        assertTodosActive("a", "c");

        sortByType("completed");
        assertTodosCompleted("b");
    }

    @Test
    public void basicTodoActions() {

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


    private final ElementsCollection todoList = $$("#todo-list>li");

    private final ElementsCollection todoListActive = $$("#todo-list>li.active");

    private final ElementsCollection todoListCompleted = $$("#todo-list>li.completed");

    private void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }

    private void assertTodos(String... texts) {
        todoList.shouldHave(texts(texts));
    }

    private void assertTodosActive(String... texts) {
        todoListActive.shouldHave(texts(texts));
    }

    private void assertTodosCompleted(String... texts) {
        todoListCompleted.shouldHave(texts(texts));
    }

    private void toggle(String todo) {
        todoList.findBy(text(todo)).find(".toggle").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private SelenideElement startEditing(String oldText, String newText) {
        todoList.findBy(text(oldText)).doubleClick();
        return todoList.findBy(cssClass("editing")).find(".edit").setValue(newText);
    }

    private void edit(String oldText, String newText) {
        startEditing(oldText, newText).pressEnter();
    }

    private void cancelEdit(String oldText, String newText) {
        startEditing(oldText, newText).pressEscape();
    }

    private void delete(String text) {
        todoList.findBy(text(text)).hover().find(".destroy").click();
    }

    private void sortByType(String type) {
        $("[href='#/" + type + "']").click();
    }
}