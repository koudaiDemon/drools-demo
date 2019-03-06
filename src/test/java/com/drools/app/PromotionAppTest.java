package com.drools.app;

import com.drools.db.DBService;
import com.drools.fact.order.Order;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author cWww
 * @Title
 * @Description
 * @date: 2019/3/6  15:36
 */
public class PromotionAppTest {
    private PromotionApp promotionApp;

    @Before
    public void before(){
        promotionApp = new PromotionApp();
    }

    @Test
    public void promotion() {
        promotionApp.promotion();
//        final Order order = DBService.getOrderByCode("order1");
//        System.out.println(order);
    }
}