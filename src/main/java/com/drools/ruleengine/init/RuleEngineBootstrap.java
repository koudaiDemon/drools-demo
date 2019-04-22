package com.drools.ruleengine.init;


/**
 * @author cWww
 * @Title RuleEngineBootstrap
 * @Description RuleEngineBootstrap
 * @date: 2019/4/16  15:08
 */
public interface RuleEngineBootstrap<SERVICES,CONTAINER> {

    /**
     * 启动规则引擎
     */
    void start();

    /**
     * 获取引擎Service
     * @return getEngineServices
     */
    SERVICES getEngineServices();

    /**
     * 预热引擎容器
     * drools中创建引擎容器为及其消耗资源的动作,所以尽量减少container的创建
     * @param container container容器
     */
    void warmUpRuleEngineContainer(CONTAINER container);

}
