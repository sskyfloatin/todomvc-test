package com.how.autotest.todomvc_test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.how.autotest.todomvc_test.testConfigs.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.util.stream.IntStream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
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
    public void assertNoTodosOnFreshOpen() {
        givenAppOpened();

        add(/*empty*/);

        assertNoTodos();
    }

    @Test
    public void createOneTodo() {
        givenAppOpened();

        add("a");

        assertTodos("a");
        assertActiveItemsLeft(1);
    }

    @Test
    public void createManyTodos() {
        givenAppOpened();

        add("a", "b", "c");

        assertTodos("a", "b", "c");
        assertActiveItemsLeft(3);
    }

    @Test
    public void editTodo() {
        givenAppOpenedWith("a", "b", "c");

        edit("b", "b edited");

        assertTodos("a", "b edited", "c");
        assertActiveItemsLeft(3);
    }

    @Test
    public void editTodoUsingTab() {
        givenAppOpenedWith("a", "b", "c");

        editWithTab("b", "b edited");

        assertTodos("a", "b edited", "c");
        assertActiveItemsLeft(3);
    }

    @Test
    public void cancelEdit() {
        givenAppOpenedWith("a", "b", "c");

        cancelEdit("a", "to be canceled");

        assertTodos("a", "b", "c");
        assertActiveItemsLeft(3);
    }

    @Test
    public void completeTodo() {
        givenAppOpenedWith("a", "b", "c");

        toggle("c");

        assertCompleted("c");
        assertActiveItemsLeft(2);
    }

    @Test
    public void completeAll() {
        givenAppOpenedWith("a", "b", "c");

        toggleAll();

        assertActive(/*empty*/);
        assertCompleted("a", "b", "c");
        assertActiveItemsLeft(0);
    }

    @Test
    public void completeAllWithSomeCompleted() {
        givenAppOpenedWith("a", "b", "c");

        toggle("b");

        toggleAll();

        assertActive(/*empty*/);
        assertCompleted("a", "b", "c");
        assertActiveItemsLeft(0);
    }

    @Test
    public void activateTodo() {
        givenAppOpenedWith("a", "b", "c");

        toggle("b");
        toggle("b");

        assertCompleted(/*empty*/);
        assertActive("a", "b", "c");
        assertActiveItemsLeft(3);
    }

    @Test
    public void activateAllTodos() {
        givenAppOpenedWith("a", "b", "c");

        toggleAll();

        assertActive(/*empty*/);
        assertCompleted("a", "b", "c");
        assertActiveItemsLeft(0);

        toggleAll();

        assertCompleted(/*empty*/);
        assertActive("a", "b", "c");
        assertActiveItemsLeft(3);
    }

    @Test
    public void clearCompletedTodo() {
        givenAppOpenedWith("a", "b", "c");

        toggle("c");
        clearCompleted();

        assertTodos("a", "b");
        assertActiveItemsLeft(2);
    }

    @Test
    public void clearAllCompletedTodos() {
        givenAppOpenedWith("a", "b", "c");

        toggleAll();
        clearCompleted();

        assertNoTodos();
    }

    @Test
    public void deleteTodo() {
        givenAppOpenedWith("a", "b", "c");

        delete("b");

        assertTodos("a", "c");
        assertActiveItemsLeft(2);
    }

    @Test
    public void deleteAllTodos() {
        givenAppOpenedWith("a", "b", "c");

        delete("a", "b", "c");

        assertNoTodos();
    }

    @Test
    public void deleteTodoByEditToBlank() {
        givenAppOpenedWith("a", "b", "c");

        edit("b", " ");

        assertTodos("a", "c");
        assertActiveItemsLeft(2);
    }

    @Test
    public void assertTabOrder() {
        givenAppOpenedWith("a", "b", "c");

        pressTabNumberOfTimes(1);
        pressSpace();

        assertActiveItemsLeft(0);

        pressSpace();

        assertActiveItemsLeft(3);

        pressTabNumberOfTimes(3);
        pressSpace();
        clearCompleted();

        assertTodos("a", "b");
        assertActiveItemsLeft(2);
    }

    @Test
    public void editCompleteTodo() {
        givenAppOpenedWith("a", "b", "c");
        toggle("a");

        edit("a", "a edited");
        assertTodos("a edited", "b", "c");
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

    private void assertNoTodos() {
        $$("#todo-list>li").shouldHaveSize(0);
    }

    private void assertActiveItemsLeft(int count) {
        $("#todo-count strong").shouldHave(text(String.valueOf(count)));
    }

    private void toggle(String todo) {
        todos.findBy(text(todo)).find(".toggle").click();
    }

    private void pressTabNumberOfTimes(int number) {
        IntStream.range(0, number).forEach(n -> Selenide.actions().sendKeys(Keys.TAB).perform());
    }

    private void pressSpace() {
        Selenide.actions().sendKeys(Keys.SPACE).perform();
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

    private void assertCompleted(String... texts) {
        if(isListNotEmpty(texts)) {
            this.todos.filterBy(cssClass("completed")).shouldHave(texts(texts));
        }
    }

    private void assertActive(String... texts) {
        if(isListNotEmpty(texts)) {
            this.todos.filterBy(cssClass("active")).shouldHave(texts(texts));
        }
    }

    private boolean isListNotEmpty(String[] texts) {
        return texts.length > 0;
    }

    private void editWithTab(String oldText, String newText) {
        startEditing(oldText, newText).pressTab();
    }

    private void cancelEdit(String oldText, String newText) {
        startEditing(oldText, newText).pressEscape();
    }

    private void delete(String... texts) {
        for (String text : texts) {
            todos.findBy(text(text)).hover().find(".destroy").click();
        }
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