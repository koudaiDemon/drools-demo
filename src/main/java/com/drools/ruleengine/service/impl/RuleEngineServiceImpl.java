package com.drools.ruleengine.service.impl;

import com.drools.ruleengine.init.RuleEngineBootstrap;
import com.drools.ruleengine.service.RuleEngineService;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;

/**
 * @author cWww
 * @Title RuleEngineServiceImpl
 * @Description RuleEngineServiceImpl RuleEngine实现类
 * @date: 2019/4/17  13:10
 */
@Slf4j
public class RuleEngineServiceImpl implements RuleEngineService<ReleaseId,KieContainer> {

    private RuleEngineBootstrap<KieServices,KieContainer> ruleEngineBootstrap;

    @Override
    public KieContainer getActiveContainer(ReleaseId releaseId) {
        final KieServices engineServices = ruleEngineBootstrap.getEngineServices();
        if (null == engineServices) {
            throw new RuntimeException("Rule Engine Service Is Null,Rule Engine Not Start");
        }
        return engineServices.newKieContainer(releaseId);
    }

    @Override
    public KieModule createKieModule(KieModuleModel kieModuleModel, ReleaseId releaseId, String... rules) {
        

        return null;
    }

}
