import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubmitFormTest {

    Person person = DataGenerator.Generator.generatePerson("ru");

    private void setCityByList(String cityName){
        $("[data-test-id='city'] .input__control").setValue(cityName.substring(0, 2));
        ElementsCollection cityElements = $$("[class='menu-item__control']");
        cityElements.find(text(cityName)).click();
    }

    private void setDateByDatePicker(GregorianCalendar calendar){
        GregorianCalendar now = new GregorianCalendar();

        $("[data-test-id='date'] [type='button']").click();
        if(calendar.get(Calendar.MONTH) != now.get(Calendar.MONTH)){
            $("[class='calendar__arrow calendar__arrow_direction_right']").click();
        }
        $(byText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)))).click();
    }

    private String getFormatDate(GregorianCalendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(calendar.getTime());
    }

    @Test
    void ReplanMeetingFormTest(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 3);
        String date = getFormatDate(calendar);

        open("http://localhost:9999");
        $("[data-test-id='city'] .input__control").setValue(person.getCity());
        setDateByDatePicker(calendar);
        $("[data-test-id='name'] .input__control").setValue(person.getName());
        $("[data-test-id='phone'] .input__control").setValue(person.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[role='button']").submit();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));

        String text = $("[data-test-id='success-notification'] .notification__content").getText().replaceAll("\\s\\s*", " ");
        assertEquals("Встреча успешно запланирована на1111 " + date, text);

        calendar.add(Calendar.DATE, 2);
        setDateByDatePicker(calendar);
        $("[role='button']").submit();
        $(withText("Необходимо подтверждение")).shouldBe(visible, Duration.ofSeconds(15));
        text = $("[data-test-id='replan-notification'] .notification__content").getText().replaceAll("\\s\\s*", " ");
        assertEquals("У вас уже запланирована встреча на другую дату. Перепланировать? Перепланировать", text);

        ElementsCollection buttons = $$("[role='button']");
        buttons.find(text("Перепланировать")).click();

        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        text = $("[data-test-id='success-notification'] .notification__content").getText().replaceAll("\\s\\s*", " ");
        assertEquals("Встреча успешно запланирована на " + getFormatDate(calendar), text);
    }
}
