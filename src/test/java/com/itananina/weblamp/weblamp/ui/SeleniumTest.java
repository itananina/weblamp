package com.itananina.weblamp.weblamp.ui;

import com.itananina.weblamp.weblamp.AbstractSpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTest {
    //@Test
    void test() {
        //given
        System.setProperty("webdriver.chrome.driver", "../chromedriver_96/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        //when
        driver.get("http://localhost:8192/weblamp");

        driver.findElement(By.id("login")).click();

        //then
        Assertions.assertEquals("http://localhost:8192/weblamp/#!/login", driver.getCurrentUrl());
        driver.close();
    }

}
