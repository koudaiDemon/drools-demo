package com.drools.ruleengine.init.impl;

import com.drools.ruleengine.init.RuleEngineBootstrap;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;

/**
 * @author cWww
 * @Title RuleEngineBootstrapImpl
 * @Description 规则引擎实现类
 * @date: 2019/4/16  15:21
 */
@Slf4j
public class RuleEngineBootstrapImpl implements RuleEngineBootstrap<KieServices,KieContainer> {
    private static final String GROUP_ID = "com.drools";
    private static final String ARTIFACT_ID = "ruleEngine";
    private static final String VERSION = "1.0";

    @Override
    public void start() {

    }

    @Override
    public KieServices getEngineServices() {
        return KieServices.get();
    }

    @Override
    public void warmUpRuleEngineContainer(KieContainer kieContainer) {
        kieContainer.newKieSession();
    }
}
