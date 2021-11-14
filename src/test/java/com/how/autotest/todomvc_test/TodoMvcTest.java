package com.how.autotest.todomvc_test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.how.autotest.todomvc_test.testConfigs.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

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

        assertNoTodos();
    }

    @Test
    public void addTodos() {
        givenAppOpened();

        add("a");
        assertTodos("a");
        assertActiveItemsLeft(1);

        add("b", "c");
        assertTodos("a", "b", "c");
        assertActiveItemsLeft(3);
    }

    @Test
    public void editTodo() {
        givenAppOpenedWith("a", "b", "c");

        assertActiveItemsLeft(3);
        edit("b", "b edited");
        assertTodos("a", "b edited", "c");
        assertActiveItemsLeft(3);
    }

    @Test
    public void deleteTodos() {
        givenAppOpenedWith("a", "b", "c");

        delete("a", "c");
        assertTodos("b");
        assertActiveItemsLeft(1);
    }

    @Test
    public void deleteTodoByEditToBlankString() {
        givenAppOpenedWith("a");

        edit("a", " ");

        assertNoTodos();
    }

    @Test
    public void deleteAllTodos() {
        givenAppOpenedWith("a", "b", "c");

        delete("a", "b", "c");
        assertNoTodos();
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
    public void completeTodo() {
        givenAppOpenedWith("a", "b", "c");
        toggle("c");

        assertActiveItemsLeft(2);

        clearCompleted();
        assertTodos("a", "b");
        assertActiveItemsLeft(2);
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

        toggle("b");

        assertActiveItemsLeft(2);

        toggleAll();
        assertActiveItemsLeft(0);
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

        clearCompleted();
        assertTodos("b", "c");
        assertActiveItemsLeft(2);
    }

    @Test
    public void clearAllTodos() {
        givenAppOpenedWith("a", "b", "c");
        toggleAll();

        assertActiveItemsLeft(0);

        clearCompleted();
        assertNoTodos();
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
        for (int i = 1; i <= number; i++) {
            Actions action = new Actions(WebDriverRunner.getWebDriver());
            action.sendKeys(Keys.TAB).perform();
        }
    }

    private void pressSpace() {
        Actions action = new Actions(WebDriverRunner.getWebDriver());
        action.sendKeys(Keys.SPACE).perform();
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