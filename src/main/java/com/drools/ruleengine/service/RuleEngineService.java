package com.drools.ruleengine.service;


import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;

/**
 * @author cWww
 * @Title RuleEngineService
 * @Description RuleEngineService
 * @date: 2019/4/16  15:55
 */
public interface RuleEngineService<RELEASEID, CONTAINER> {

    /**
     * 根据releaseId获取对应容器
     * @param releaseId releaseId
     * @return CONTAINER 容器
     */
    CONTAINER getActiveContainer(RELEASEID releaseId);

    /**
     * 创建KieModule
     * @param kieModuleModel
     * @param releaseId
     * @param rules
     * @return
     */
    KieModule createKieModule(KieModuleModel kieModuleModel,
                              ReleaseId releaseId,
                              String... rules);

}
