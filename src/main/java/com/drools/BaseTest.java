package com.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieContainerSessionsPool;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

/**
 * @author cWww
 * @Title
 * @Description
 * @date: 2019/2/25  15:34
 */
@Slf4j()
public class BaseTest {

    /**
     * The default pool size
     * 16
     */
    private static final Integer POOL_SIZE = 1 << 4;

    private KieContainerSessionsPool kieContainerSessionsPool;

    {
        final KieServices kieServices = KieServices.get();
        final KieContainer kieContainer = kieServices.getKieClasspathContainer();
        kieContainerSessionsPool = kieContainer.newKieSessionsPool(POOL_SIZE);
        String format = "yyyy-MM-dd hh:mm:ss";
        System.setProperty("drools.dateformat", format);
    }

    protected KieSession getKieSession(String agendaGroup){
        KieSession kieSession = getKieSession();
        kieSession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
        return kieSession;
    }

    protected KieSession getKieSession(String agendaGroup,String sessionName){
        KieSession kieSession = getKieSessions(sessionName);
        kieSession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
        return kieSession;
    }

    protected KieSession getKieSession(){
        return kieContainerSessionsPool.newKieSession("all-rules");
    }

    protected KieSession getKieSessions(String sessionName){
        return kieContainerSessionsPool.newKieSession(sessionName);
    }

    protected StatelessKieSession getStatelessKieSession(){
        return kieContainerSessionsPool.newStatelessKieSession("statelessSession");
    }

}
