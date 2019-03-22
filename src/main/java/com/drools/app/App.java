package com.drools.app;

import com.drools.BaseTest;
import com.drools.fact.Address;
import com.drools.fact.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.command.CommandFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        final Person person = new Person("name","123456",50,new Date(),Boolean.FALSE,null);
        final FactHandle handle =  kieSession.insert(person);
        log.info("handle,{}",handle.toExternalForm());
        final int count = kieSession.fireAllRules();
        log.info("count,{},person:{}",count,person);
        kieSession.dispose();
    }

    public void testStateless(){
        final StatelessKieSession statelessKieSession = getStatelessKieSession();
        final Person person = new Person("狗蛋1","123456",21,new Date(),Boolean.FALSE,null);
        final Person person1 = new Person("狗蛋2","123456",25,new Date(),Boolean.FALSE,null);
        statelessKieSession.execute(Arrays.asList(person,person1));
    }

    public void buspass(){
        final KieSession kieSession = getKieSession();
        final Person person = new Person("123","123456",21,new Date(),Boolean.FALSE,null);
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

    public void fromAndLock(){
        final KieSession kieSession = getKieSession("test-from");
        final Person person = new Person("123","123456",21,new Date(),Boolean.FALSE,null);
        person.setAddresses(Arrays.asList(new Address("NC"),new Address("Raleigh")));

        final FactHandle handle =  kieSession.insert(person);
        log.info("handle,{}",handle.toExternalForm());
        final int count = kieSession.fireAllRules();
        log.info("count:{},person:{}",count,person);
        kieSession.dispose();
        log.info("person:{}",person);
    }

    @Test
    public void moreOneThen(){
        final KieSession kieSession = getKieSession();
        final Person person = new Person("123","123456",31,new Date(),Boolean.FALSE,null);
        //goudan
        //final Person person = new Person("goudan","123456",21,new Date(),Boolean.FALSE,null);
        //koudai
        //final Person person = new Person("koudai","123456",21,new Date(),Boolean.FALSE,null);
        final FactHandle handle =  kieSession.insert(person);
        log.info("handle,{}",handle.toExternalForm());
        final int count = kieSession.fireAllRules();
        log.info("count,{},person:{}",count,person);
        kieSession.dispose();
    }

    @Test
    public void query(){
        final KieSession kieSession = getKieSession();
        final Person person = new Person("123","123456",31,new Date(),Boolean.FALSE,null);
        final Person p = new Person("123","123456",40,new Date(),Boolean.FALSE,null);
        kieSession.insert(person);
        kieSession.insert(p);
        final QueryResults queryResults = kieSession.getQueryResults("people over the age of 30");
        log.info("people over the age of 30,size:{}",queryResults.size());
        for (QueryResultsRow row : queryResults) {
            final Person person1 = (Person)row.get("person");
            log.info("row,person:{}",person1);
        }
        kieSession.dispose();
    }

    @Test
    public void factType() throws IllegalAccessException, InstantiationException {
        final KieContainer kieContainer = getKieContainer();
        final FactType factType = kieContainer.getKieBase("rools").getFactType("rools", "Child");
        int age = 1;
        final KieSession kieSession = kieContainer.newKieSession("all-rules");
        while (true) {
            final Object child = factType.newInstance();
            kieSession.insert(child);
            log.info("开始触发规则,当前时间:{},年龄:{}",LocalDateTime.now(),age);
            kieSession.fireAllRules();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("InterruptedException",e);
            }
            age += 2;
            if (age >= 30) {
                break;
            }
        }
        kieSession.dispose();
    }

    @Test
    public void verify(){
        //规则校验
        //用于验证当前环境下规则是否正确运行
        final KieContainer kieContainer = getKieContainer();
        final Results verify = kieContainer.verify();
        final List<Message> messages = verify.getMessages();
        for (Message message : messages) {
            log.info("message:{}",message);
        }
    }

    @Test
    public void base(){
        final KieServices kieServices = this.getKieServices();
        final KieRepository repository = kieServices.getRepository();
        //获取maven仓库的指定Drools-jar包
        final ReleaseId releaseId = kieServices.newReleaseId("com.drools", "drools-demo", "1.0-SNAPSHOT");
        final KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        //获取默认版本ID
        final ReleaseId defaultReleaseId = repository.getDefaultReleaseId();
        //获取当前库中指定的KieModule
        final KieModule kieModule = repository.getKieModule(kieServices.newReleaseId("com.drools", "drools-demo", "1.0-SNAPSHOT"));

        final Collection<String> kieBaseNames = kieContainer.getKieBaseNames();
        for (String name : kieBaseNames) {
            final KieBaseModel kieBaseModel = kieContainer.getKieBaseModel(name);
            final KieBase kieBase = kieContainer.getKieBase(name);
        }
        final KieSession kieSession = kieContainer.newKieSession("all-rules");
        kieSession.fireAllRules();
        kieSession.dispose();
    }


}
