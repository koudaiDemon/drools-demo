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
    private KieContainer kieContainer;
    private KieServices kieServices;

    {
        final KieServices kieServices = KieServices.get();
        final KieContainer kieContainer = kieServices.getKieClasspathContainer();
        this.kieContainer = kieContainer;
        this.kieServices = kieServices;
        kieContainerSessionsPool = kieContainer.newKieSessionsPool(POOL_SIZE);
        String format = "yyyy-MM-dd HH:mm:ss";
        System.setProperty("drools.dateformat", format);
    }

    protected KieContainer getKieContainer(){
        return kieContainer;
    }

    protected KieServices getKieServices(){
        return this.kieServices;
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

    /**
     * notice:由于通过kieContainerSessionsPool创建以后的session默认为stateful
     * 然后将stateful的结果包装为stateless
     * @return
     */
    protected StatelessKieSession getStatelessKieSession(String sessionName){
        return kieContainerSessionsPool.newStatelessKieSession(sessionName);
    }

}
