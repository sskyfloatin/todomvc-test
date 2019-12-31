package com.todomvcTest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TodoMvcTest {

    private final ElementsCollection tasks = $$("#todo-list>li");

    @Test
    public void basicTaskActions() {

        open("http://todomvc4tasj.herokuapp.com/");
        Wait().until(jsReturnsValue("return $._data($('#clear-completed').get(0), 'events').hasOwnProperty('click')"));

        // create
        add("a","b","c");
        assertTodos("a", "b", "c");

        // edit
        startEditing("b"," edited").pressEnter();

        // complete and clear
        findTaskByText("b edited").find(".toggle").click();

        $("#clear-completed").click();
        assertTodos("a", "c");

        // cancel editing
        startEditing("a"," to be canceled").pressEscape();

        // delete
        findTaskByText("a").hover().find(".destroy").click();
        assertTodos("c");
    }

    private SelenideElement findTaskByText(String string) {
        return tasks.findBy(text(string));
    }

    private void add(String... texts) {
        for (String text: texts){
            $("#new-todo").append(text).pressEnter();
        }
    }
    private SelenideElement startEditing(String todoText, String editingText){
        findTaskByText(todoText).doubleClick();
        tasks.findBy(cssClass("editing")).find(".edit")
                .append(editingText);
        return tasks.findBy(cssClass("editing")).find(".edit");

    }
    private void assertTodos(String...string){
        tasks.shouldHave(texts(string));

    }
}