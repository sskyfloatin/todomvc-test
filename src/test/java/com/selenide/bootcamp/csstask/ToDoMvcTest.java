package com.selenideintro.csstask;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;

public class ToDoMvcTest {

    @Test
    void completeTask() {

        open("https://todomvc.com/examples/emberjs");

        $("#new-todo").val("a").pressEnter();
        $("#new-todo").val("b").pressEnter();
        $("#new-todo").val("c").pressEnter();
        $$("#todo-list li").shouldHave(texts("a", "b", "c"));

        $("#todo-list li:nth-child(2) .toggle").click();
        $$("#todo-list li.completed").shouldHave(texts("b"));
        $$("#todo-list li:not(.completed)").shouldHave(texts("a", "c"));
    }
}
