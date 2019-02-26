package com.drools.app;

import com.drools.BaseTest;
import com.drools.fact.Person;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.FactHandle;

import java.util.Arrays;
import java.util.Date;

/**
 * @author cWww
 * @Title
 * @Description
 * @date: 2019/2/25  15:18
 */
@Slf4j
public class App extends BaseTest {

    public void testStateful(){
        final KieSession kieSession = getKieSession("fact-handler-group");
        final Person person = new Person("123","123456",21,new Date(),Boolean.FALSE);
        final FactHandle handle =  kieSession.insert(person);
        log.info("handle,{}",handle.toExternalForm());
        final int count = kieSession.fireAllRules();
        log.info("count,{}",count);
        kieSession.dispose();
    }

    public void testStateless(){
        final StatelessKieSession statelessKieSession = getStatelessKieSession();
        final Person person = new Person("狗蛋1","123456",21,new Date(),Boolean.FALSE);
        final Person person1 = new Person("狗蛋2","123456",25,new Date(),Boolean.FALSE);
        statelessKieSession.execute(Arrays.asList(person,person1));
    }

    public void buspass(){
        final KieSession kieSession = getKieSession();
        final Person person = new Person("123","123456",21,new Date(),Boolean.FALSE);
        final FactHandle handle =  kieSession.insert(person);
        log.info("handle,{}",handle.toExternalForm());
        final int count = kieSession.fireAllRules();
        log.info("count,{}",count);
        kieSession.dispose();
    }


    public void passiveQuery(){
        final StatelessKieSession statelessKieSession = getStatelessKieSession();
        statelessKieSession.execute(Arrays.asList(Integer.valueOf(1),"1"));
    }

}
