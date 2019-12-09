package com.selenideintro.xpathtask;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class TodoMvc {

    @Test
    void completeTask() throws InterruptedException {
        open("https://todomvc.com/examples/emberjs");

        $(By.xpath("//input[@id='new-todo']")).val("a").pressEnter();
        $(By.xpath("//input[@id='new-todo']")).val("b").pressEnter();
        $(By.xpath("//input[@id='new-todo']")).val("c").pressEnter();
        $$(By.xpath("//*[@id='todo-list']/li")).shouldHave(texts("a", "b", "c"));

        $(By.xpath("//*[@id='todo-list']/*[.//text()='b']//*[@class='toggle']")).click();
        $(By.xpath("//*[@id='todo-list']//li[contains(@class,'completed')]")).shouldHave(text("b"));
        $$(By.xpath("//*[@id='todo-list']//li[@class='ember-view']")).shouldHave(texts("a", "c"));
    }
}
