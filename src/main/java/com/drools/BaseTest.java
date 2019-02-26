package com.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
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

    protected KieSession getKieSession(String agendaGroup){
        KieSession kieSession = getKieSession();
        kieSession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
        return kieSession;
    }

    protected KieSession getKieSession(){
        KieServices kieServices = KieServices.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        return kieContainer.newKieSession("all-rules");
    }

    protected StatelessKieSession getStatelessKieSession(){
        KieServices kieServices = KieServices.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        return kieContainer.newStatelessKieSession("statelessSession");
    }

}
