package ru.netology.web;

import com.codeborne.selenide.Configuration;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class RequestCardTest {

    private static final String DATE_PATTERN = "dd.MM.yyyy";

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--remote-allow-origins=*"
        );
        Configuration.browserCapabilities = options;
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfulRequestCard() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        String planningDate = getDate(3);
        $("[data-test-id=date] input.input__control").setValue(planningDate);
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content")
                .shouldHave(exactText("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    void shouldFailWhenInputIsEmpty() {
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=city].input_invalid").shouldHave(exactText("Поле обязательно для заполнения"), Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWhenCityNotInList() {
        $("[data-test-id=city] input").setValue("Чехов");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=city].input_invalid").shouldHave(exactText("Доставка в выбранный город недоступна"), Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWhenDateIsEmpty() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"), Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWhenDateIsEarlyThen3Days() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(2));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"), Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWhenDateInPast() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(-1));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"), Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWhenNameIsEmpty() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(partialText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFailWhenNameContainsWrongSymbols() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий'");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(partialText("Имя и Фамилия указаные неверно"));
    }

    @Test
    void shouldFailWhenNameContainsNotCyrillicSymbols() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=name] input[name=name]").setValue("Vasiliy Ivanov-Petrov");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(partialText("Имя и Фамилия указаные неверно"));
    }

    @Test
    void shouldFailWhenPhoneIsEmpty() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(partialText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFailWhenPhoneNotStartsWithPlus() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(partialText("Телефон указан неверно"));
    }

    @Test
    void shouldFailWhenPhoneContainsMoreThan10Numbers() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+791632223220");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(partialText("Телефон указан неверно"));
    }

    @Test
    void shouldFailWhenPhoneContainsLessThan10Numbers() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+9163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(partialText("Телефон указан неверно"));
    }

    @Test
    void shouldFailWhenPhoneContainsSymbols() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+7 (916) 322 23 22");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(partialText("Телефон указан неверно"));
    }

    @Test
    void shouldFailWhenAgreementCheckboxNotSet() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input.input__control").setValue(getDate(3));
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }

    private String getDate(long days) {
        return getDate(days, DATE_PATTERN);
    }

    private String getDate(long days, String pattern) {
        return DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(days)), pattern);
    }
}

