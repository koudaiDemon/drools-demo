package com.drools.app;

import com.drools.BaseTest;
import com.drools.fact.Person;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.common.InternalAgenda;
import org.drools.core.event.DebugAgendaEventListener;
import org.drools.core.event.DebugProcessEventListener;
import org.drools.core.event.DefaultRuleRuntimeEventListener;
import org.drools.template.ObjectDataCompiler;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.kiebase.BeforeKiePackageRemovedEvent;
import org.kie.api.event.kiebase.DefaultKieBaseEventListener;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cWww
 * @Title App2
 * @Description 应用2
 * @date: 2019/4/12  13:05
 */
@Slf4j
public class App2 extends BaseTest {

    /**
     * kieHelper实例
     */
    @Test
    public void kieHelper(){
        final KieHelper helper = new KieHelper();
        helper.addResource(ResourceFactory.newClassPathResource("base/base1.drl"),ResourceType.BRL);
        final KieBase kieBase = helper.build();
        final KieSession kieSession = kieBase.newKieSession();
        kieSession.fireAllRules();
    }

    /**
     * kieBaseEvent:KieBase event
     */
    @Test
    public void kieBaseEvent(){
        final KieHelper helper = new KieHelper();
        helper.addResource(ResourceFactory.newClassPathResource("base/base1.drl"),ResourceType.BRL);
        helper.addResource(ResourceFactory.newClassPathResource("base/base2.drl"),ResourceType.BRL);
        final KieBase kieBase = helper.build();
        kieBase.addEventListener(new DefaultKieBaseEventListener(){
            @Override
            public void beforeKiePackageRemoved(BeforeKiePackageRemovedEvent event) {
                super.beforeKiePackageRemoved(event);
                log.info("beforeKiePackageRemoved:{}",event.getKiePackage().getName());
            }
        });
        kieBase.removeKiePackage("base");
        final KieSession kieSession = kieBase.newKieSession();
        kieSession.fireAllRules();
    }

    /**
     * KieSession event
     */
    @Test
    public void kieSessionEvent(){
        final KieHelper helper = new KieHelper();
        helper.addResource(ResourceFactory.newClassPathResource("base/base1.drl"),ResourceType.BRL);
        helper.addResource(ResourceFactory.newClassPathResource("base/base2.drl"),ResourceType.BRL);
        final KieBase kieBase = helper.build();
        final KieSession kieSession = kieBase.newKieSession();

        kieSession.addEventListener(new DefaultRuleRuntimeEventListener(){
            @Override
            public void objectInserted(final ObjectInsertedEvent event) {
                log.info("objectInserted:{}",event);
                // intentionally left blank
            }
        });
        kieSession.addEventListener(new DebugAgendaEventListener());
        kieSession.addEventListener(new DebugProcessEventListener());
        kieSession.fireAllRules();
//        kieSession.startProcess()
//        ((InternalAgenda)kieSession.getAgenda());
        Person person = new Person();
        person.setUsername("test");
        kieSession.insert(person);
        kieSession.fireAllRules();
    }

    /**
     * 根据模板创建drl文件
     * 1.DataProvider
     * DataProvider dataProvider = new ArrayDataProvider(new String[][]{
     *       {"1", "Tom"}, {"2", "Lucy"}
     * });
     * DataProviderCompiler compiler = new DataProviderCompiler();
     * 2.excel
     * <kbase packages="com.template" name="test-template">
     *     <ruleTemplate dtable="template/template.xls" template="template/template.drt" row="2" col="1"/>
     * </kbase>
     * 3.ObjectDataCompiler(如下)
     * 4.数据库:ResultSetGenerator
     */
    @Test
    public void template(){
        final InputStream resourceAsStream = App2.class.getResourceAsStream("/template/template.drt");
        final List<Person> list = new ArrayList<>();
        Person person = new Person();
        person.setAge(3);
        person.setUsername("test");

        Person person1 = new Person();
        person1.setAge(5);
        person1.setUsername("demo");

        list.add(person);
        list.add(person1);

        final ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();
        final String compile = objectDataCompiler.compile(list, resourceAsStream);
        log.info(compile);
    }


    @Test
    public void cep() throws InterruptedException {
//        final KieBaseConfiguration kieBaseConfiguration = getKieServices().newKieBaseConfiguration();
//        kieBaseConfiguration.setOption(EventProcessingOption.STREAM);
//        final KieBase kieBase = kieContainer.newKieBase("rule-cep", kieBaseConfiguration);

        final KieContainer kieContainer = getKieContainer();
        final KieBase cep = kieContainer.getKieBase("rule-cep");
        final KieSession kieSession = cep.newKieSession();

        Person person = new Person();
        person.setAge(5);
        person.setUsername("test");
        kieSession.insert(person);
        log.info("person");

        Thread.sleep(2000);

        Person person1 = new Person();
        person1.setAge(3);
        person1.setUsername("demo");
        log.info("person1");

        Person person2 = new Person();
        person2.setAge(7);
        person2.setUsername("demo2");
        log.info("person2");

        kieSession.insert(person1);
        kieSession.insert(person2);

        kieSession.fireAllRules();

    }

    public static void main(String[] args) {

        DynamicDrlApp dynamicDrlApp = new DynamicDrlApp();

        for (int i = 0 ; i < 10 ;i ++) {
            new Thread(()->{
                log.info(Thread.currentThread().getName()+"========start=======");
                KieSession kieSession = dynamicDrlApp.getKieContainer().newKieSession();
                kieSession.fireAllRules();
                kieSession.dispose();
                log.info(Thread.currentThread().getName()+"========end=======");
            }).start();
        }

        KieSession kieSession = dynamicDrlApp.update().newKieSession();

        kieSession.fireAllRules();
        kieSession.dispose();

    }



    @Test
    public void dynamic(){

        final URL resource = App2.class.getResource("");
        resource.getPath();

    }


}
