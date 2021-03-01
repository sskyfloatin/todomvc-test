package com.todomvcTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TodoMvcTest {

    private final ElementsCollection todoList = $$("#todo-list>li");

    @Test
    public void basicTodoActions() {

        Configuration.fastSetValue = true;

        openApp();

        add("a", "b", "c");
        assertTodos("a", "b", "c");

        edit("b", "b edited");

        complete("b edited");
        assertTodos("a", "c");

        cancelEdit("a", " to be canceled");

        delete("a");
        assertTodos("c");
    }

    private void openApp() {
        open("http://todomvc4tasj.herokuapp.com/");
        Wait().until(jsReturnsValue("return $._data($('#clear-completed').get(0), 'events').hasOwnProperty('click')"));
    }

    private void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }

    private void assertTodos(String... string) {
        todoList.shouldHave(texts(string));
    }

    private SelenideElement edit(String todoText, String editingText) {
        todoList.findBy(text(todoText)).doubleClick();
        return findTodoByCssClass().setValue(editingText).pressEnter();
    }

    private void complete(String todoText) {
        todoList.findBy(text(todoText)).find(".toggle").click();
        $("#clear-completed").click();
    }

    private SelenideElement cancelEdit(String todoText, String editingText) {
        todoList.findBy(text(todoText)).doubleClick();
        return findTodoByCssClass().setValue(editingText).pressEscape();
    }

    private void delete(String todoText) {
        todoList.findBy(text(todoText)).hover().find(".destroy").click();
    }

    private SelenideElement findTodoByCssClass() {
        return todoList.findBy(cssClass("editing")).find(".edit");
    }
}