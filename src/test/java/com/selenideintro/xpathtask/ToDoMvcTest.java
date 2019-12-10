package com.selenideintro.xpathtask;

import com.selenideintro.common.selectors.XPath;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;

public class ToDoMvcTest {

    @Test
    void completeTask() {

        open("https://todomvc.com/examples/emberjs");

        $x(("//*[@id='new-todo']")).val("a").pressEnter();
        $x(("//*[@id='new-todo']")).val("b").pressEnter();
        $x(("//*[@id='new-todo']")).val("c").pressEnter();
        $$x(("//*[@id='todo-list']/*")).shouldHave(texts("a", "b", "c"));

        $x(("//*[@id='todo-list']/*[.//text()='b']//*[@class='toggle']")).click();
        $$x(("//*[@id='todo-list']//li" + XPath.filterBy.cssClass("completed"))).shouldHave(texts("b"));
        $$x(("//*[@id='todo-list']//li" + XPath.filterBy.noCssClass("completed"))).shouldHave(texts("a", "c"));
    }
}
