package com.drools.app;

import com.drools.BaseTest;
import com.drools.db.DBService;
import com.drools.fact.order.Order;
import com.drools.service.CalculationService;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;


/**
 * @author cWww
 * @Title PromotionApp
 * @Description 促销APP
 * @date: 2019/3/6  15:26
 */
@Slf4j
public class PromotionApp extends BaseTest {

    public void promotion(){
        final KieSession kieSession = getKieSession("promotion-group","promotion");
        final Order order = DBService.getOrderByCode("order1");
        CalculationService.calculation(order);
        log.info("order:{}",order);
        final FactHandle handle =  kieSession.insert(order);
        log.info("handle,{}",handle.toExternalForm());
        final int count = kieSession.fireAllRules();
        CalculationService.calculation(order);
        log.info("count,{},order:{}",count,order);
        kieSession.dispose();
    }

}
