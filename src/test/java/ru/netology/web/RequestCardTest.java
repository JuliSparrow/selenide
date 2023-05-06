package ru.netology.web;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class RequestCardTest {

    @BeforeEach
    public void init() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfulRequestCard() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=notification].notification_visible .notification__title").shouldHave(exactText("Успешно!"), Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWhenInputIsEmpty() {
        // очищаем поле даты
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(2)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().minusDays(1)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $(".checkbox[data-test-id=agreement]").click();
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(partialText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFailWhenPhoneNotStartsWithPlus() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        // очищаем поле даты
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
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
        while (!Objects.requireNonNull($("[data-test-id=date] input.input__control").getValue()).isBlank()) {
            $("[data-test-id=date] input.input__control").press(Keys.BACK_SPACE);
        }
        String date = DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(3)), "dd.MM.yyyy");
        $("[data-test-id=date] input.input__control").setValue(date);
        $("[data-test-id=name] input[name=name]").setValue("Иванов-Петров Василий");
        $("[data-test-id=phone] input[name=phone]").setValue("+79163222322");
        $$(".button .button__text").find(exactText("Забронировать")).click();
        $("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }


}

