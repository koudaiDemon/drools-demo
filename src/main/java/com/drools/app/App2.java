package com.drools.app;

import com.drools.BaseTest;
import com.drools.fact.Config;
import com.drools.fact.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.drools.core.common.InternalAgenda;
import org.drools.core.common.InternalFactHandle;
import org.drools.core.event.DebugAgendaEventListener;
import org.drools.core.event.DebugProcessEventListener;
import org.drools.core.event.DefaultRuleRuntimeEventListener;
import org.drools.core.event.rule.impl.AfterActivationFiredEventImpl;
import org.drools.template.ObjectDataCompiler;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.kiebase.BeforeKiePackageRemovedEvent;
import org.kie.api.event.kiebase.DefaultKieBaseEventListener;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.Match;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        final FactHandle insert = kieSession.insert(new String("aaaaaa"));
        log.info("factHandle:{}",insert.getClass());
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

    @Test
    public void config(){
        //用于测试config,指定code的rule限制执行次数
        final String ruleCode = "testRuleCode";
        final KieHelper helper = new KieHelper();
        helper.addResource(ResourceFactory.newClassPathResource("base/base2.drl"),ResourceType.BRL);
        final KieBase kieBase = helper.build();
        final KieSession kieSession = kieBase.newKieSession();
//        kieSession.addEventListener(new DebugAgendaEventListener(){
//            @Override
//            public void afterMatchFired(AfterMatchFiredEvent event) {
//                AfterActivationFiredEventImpl firedEvent = (AfterActivationFiredEventImpl)event;
//                final Match match = firedEvent.getMatch();
//                final Optional<Config> configFromMatch = getConfigFromMatch(match);
//                if (configFromMatch.isPresent()) {
//                    final Config config = configFromMatch.get();
//                    config.getCurrentRuns();
//                }
//            }
//        });
        final Person person = new Person();
        person.setAge(27);
        final Config config = new Config();
        config.setRuleCode(ruleCode);
        config.setMaxAllowedRuns(4);
        final FactHandle insert = kieSession.insert(person);
        final FactHandle con = kieSession.insert(config);
        log.info("factHandle:{}",insert.getClass());
        log.info("factHandle:{}",con.getClass());
        kieSession.fireAllRules((match)->{
            final Optional<Config> configFromMatch = getConfigFromMatch(match);
            if (configFromMatch.isPresent()) {
                final Integer currentRuns = configFromMatch.get().getCurrentRuns();
                final Integer maxAllowedRuns = configFromMatch.get().getMaxAllowedRuns();
                return currentRuns == null || maxAllowedRuns == null || currentRuns.compareTo(maxAllowedRuns) < 0;
            }
            return true;
        });
    }

    @Test
    public void testCount(){
        //限制规则触发次数
        final KieHelper helper = new KieHelper();
        helper.addResource(ResourceFactory.newClassPathResource("base/base2.drl"),ResourceType.BRL);
        final KieBase kieBase = helper.build();
        final KieSession kieSession = kieBase.newKieSession();
        final Person person = new Person();
        person.setAge(23);
        final FactHandle insert = kieSession.insert(person);
        log.info("factHandle:{}",insert.getClass());
        kieSession.fireAllRules(2);
    }

    private static Optional<Config> getConfigFromMatch(final Match match){
        final Object code = match.getRule().getMetaData().get("ruleCode");
        final List<Config> collect = match.getFactHandles().stream().
                filter((fact) -> fact instanceof InternalFactHandle).
                map((fact) -> ((InternalFactHandle) fact).getObject()).
                filter((fact) -> fact instanceof Config).
                map((fact) -> (Config) fact).
                collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            return collect.stream().
                    filter(c -> c.getRuleCode().equals(code)).
                    findFirst();
        }
        return Optional.empty();
    }


}
