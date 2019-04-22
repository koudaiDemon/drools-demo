package com.drools.app;

import com.drools.BaseTest;
import com.drools.fact.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.time.Calendar;
import org.quartz.impl.calendar.WeeklyCalendar;

/**
 * @author cWww
 * @Title AttributeAppTest
 * @Description 用于Drools各种属性
 * @date: 2019/3/12  14:48
 */
@Slf4j
public class AttributeAppTest extends BaseTest {

    @Test
    public void testAttribute() throws Exception{
        final KieSession kieSessions = this.getKieSessions("rule-attribute");
//        kieSessions.getAgenda().getAgendaGroup("rule-attribute-timer").setFocus();

        Person person = new Person();
        person.setAge(0);
        final FactHandle handle = kieSessions.insert(person);
        new Thread(()->{
            kieSessions.fireUntilHalt();
        }).start();

        for (int i = 1 ; i <= 10 ;i++) {
            Thread.sleep(1000);
            person.setAge(i);
            kieSessions.update(handle,person);
        }
        kieSessions.halt();
    }

    @Test
    public void testCalendars(){
        final KieSession kieSessions = this.getKieSessions("rule-attribute");
        kieSessions.getCalendars().set("weekday", CALENDAR);
        kieSessions.fireAllRules();
        kieSessions.dispose();
    }

    private static final Calendar CALENDAR = (timestamp) -> {
        WeeklyCalendar weeklyCalendar = new WeeklyCalendar();
        weeklyCalendar.setDaysExcluded(new boolean[]{false,false,false,false,false,false,false});
//        weeklyCalendar.setDayExcluded(java.util.Calendar.TUESDAY,true);
        return weeklyCalendar.isTimeIncluded(timestamp);
    };

}
