package com.todomvcTest;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.collections.ExactTexts;
import com.codeborne.selenide.conditions.Attribute;
import com.selenide.bootcamp.common.selectors.XPath;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import javax.lang.model.element.Element;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TodoMvcTest {

    @Test
    public void commonUserFlow() {

        open("http://todomvc4tasj.herokuapp.com/");
        Wait().until(jsReturnsValue("return $._data($('#clear-completed').get(0), 'events').hasOwnProperty('click')"));

        //Verify create
        $("#new-todo").append("a").pressEnter();
        $("#new-todo").append("b").pressEnter();
        $("#new-todo").append("c").pressEnter();
        $$("#todo-list>li").shouldHave(exactTexts("a", "b", "c"));

        //verify edit
        $$("#todo-list>li").findBy(Condition.text("a")).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing")).find(".edit")
                .append(" Edited").pressEnter();
        $$("#todo-list>li").shouldHave(texts("a Edited", "b", "c"));

        //verify complete
        $$("#todo-list>li").findBy(text("b")).find(".toggle").click();
        $$("#todo-list>li.completed").shouldHave(texts("b"));
        $$("#todo-list>li").filterBy(not(cssClass("completed")))
                .shouldHave(texts("a Edited", "c"));

        //verify delete
        $$("#todo-list>li").findBy(exactText("a Edited")).hover().find(".destroy").click();
        $$("#todo-list>li").shouldHave(texts("b","c"));

        //verify escape while editing
        $$("#todo-list>li").findBy(Condition.text("c")).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing")).find(".edit").append(" Edited").pressEscape();
        $$("#todo-list>li").findBy(Condition.text("c")).should(exist);

    }
}
