package com.drools.app;

import com.drools.BaseTest;
import com.drools.fact.Address;
import com.drools.fact.Person;
import lombok.extern.slf4j.Slf4j;
import org.drools.template.ObjectDataCompiler;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author cWww
 * @Title
 * @Description
 * @date: 2019/2/25  15:18
 */
@Slf4j
public class App extends BaseTest {


    /**
     * 基础
     */
    @Test
    public void testBase(){
        final KieSession kieSession = getKieSession();
        for (int i = 0 ; i < 10 ; i++) {
            kieSession.getAgenda().getAgendaGroup("base");
            final int count = kieSession.fireAllRules();
            log.info("count,{}",count);
        }
        kieSession.dispose();
    }

    /**
     * 测试有状态的session
     */
    public void testStateful(){
        final KieSession kieSession = getKieSession("fact-handler-group");
        final Person person = new Person("name","123456",50,new Date(),Boolean.FALSE,null);
        final FactHandle handle =  kieSession.insert(person);
        log.info("handle,{}",handle.toExternalForm());
        final int count = kieSession.fireAllRules();
        log.info("count,{},person:{}",count,person);
        kieSession.dispose();
    }

    /**
     * 测试无状态的session
     */
    public void testStateless(){
        final StatelessKieSession statelessKieSession = getKieContainer().newStatelessKieSession("statelessSession");
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

    /**
     * 被动查询
     */
    public void passiveQuery(){
        final StatelessKieSession statelessKieSession = getStatelessKieSession("statelessSessionWithoutType");
        statelessKieSession.execute(Arrays.asList(Integer.valueOf(1),"1"));
    }

    /**
     * 测试from语句和lock语句
     */
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

    /**
     * 多个then语句
     */
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

    /**
     * kie 查询,可以用于查询对象
     */
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

    /**
     * factType,在drools文件中创建基本fact对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
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

    /**
     * 规则校验
     * 用于验证当前环境下规则是否正确运行
     */
    @Test
    public void verify(){
        final KieContainer kieContainer = getKieContainer();
        final Results verify = kieContainer.verify();
        final List<Message> messages = verify.getMessages();
        for (Message message : messages) {
            log.info("message:{}",message);
        }
    }

    /**
     * 获取kie基本信息
     */
    @Test
    public void base(){
        final KieServices kieServices = this.getKieServices();
        final KieRepository repository = kieServices.getRepository();
        //获取maven仓库的指定Drools-jar包
        final ReleaseId releaseId = kieServices.newReleaseId("com.drools", "drools-demo", "1.0-SNAPSHOT");
        //获取kie容器
//        final KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        //获取默认版本ID
        final ReleaseId defaultReleaseId = repository.getDefaultReleaseId();

        //获取当前库中指定的KieModule
        final KieModule kieModule = repository.getKieModule(releaseId);
        log.info("kieModule:{}",kieModule);
//        //获取所有的kieBase名称
//        final Collection<String> kieBaseNames = kieContainer.getKieBaseNames();
//        for (String name : kieBaseNames) {
//            //获取kieBase
//            final KieBaseModel kieBaseModel = kieContainer.getKieBaseModel(name);
//            //获取session
//            final Map<String, KieSessionModel> kieSessionModels = kieBaseModel.getKieSessionModels();
//            final KieBase kieBase = kieContainer.getKieBase(name);
//        }
//        final KieSession kieSession = kieContainer.newKieSession("all-rules");
//        kieSession.fireAllRules();
//        kieSession.dispose();
    }

    /**
     * kieScanner()
     * @throws InterruptedException
     */
    @Test
    public void kieScanner() throws InterruptedException {
        final KieServices kieServices = this.getKieServices();
        //获取maven仓库的指定Drools-jar包
        final ReleaseId releaseId = kieServices.newReleaseId("com.drools", "drools-demo", "1.0-SNAPSHOT");
        //获取kie容器
        final KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        //扫描器
        final KieScanner kieScanner = kieServices.newKieScanner(kieContainer);
        //1s扫描一次
        kieScanner.start(1000);
        final KieSession kieSession = kieContainer.newKieSession("all-rules");
        while (true) {
            Thread.sleep(5000);
            kieSession.fireAllRules();
        }
    }




}
