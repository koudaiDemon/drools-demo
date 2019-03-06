package com.drools.db;

import com.drools.fact.order.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author cWww
 * @Title DBService
 * @Description 数据库数据ORM
 * @date: 2019/3/6  14:13
 */
@Slf4j
public class DBService {

    private static Collection<Order> orders = new ArrayList<>();
    private static Collection<Product> products = new ArrayList<>();
    private static Collection<User> users = new ArrayList<>();
    private static final Integer COUNT = 10;

    static {
        init();
    }

    private static void init(){
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        final User user1 = new User("0001","1号先生",20);
        final User user2 = new User("0002","2号先生",17);
        final User user3 = new User("0003","3号先生",30);
        final User user4 = new User("0004","4号先生",42);
        users.addAll(Arrays.asList(user1,user2,user3,user4));

        final Product product1 = new Product("product1","product1",null,true);
        final Price price11 = new Price("product1-1",BigDecimal.valueOf(100.0),"CNY");
        final Price price12 = new Price("product1-2",BigDecimal.valueOf(70.0),"EUR");
        product1.setPrices(Arrays.asList(price11,price12));
        final Product product2 = new Product("product2","product2",null,false);
        final Price price21 = new Price("product2-1",BigDecimal.valueOf(110.0),"CNY");
        final Price price22 = new Price("product2-2",BigDecimal.valueOf(60.0),"EUR");
        product2.setPrices(Arrays.asList(price21,price22));
        final Product product3 = new Product("product3","product3",null,true);
        final Price price31 = new Price("product3-1",BigDecimal.valueOf(120.0),"CNY");
        final Price price32 = new Price("product3-2",BigDecimal.valueOf(50.0),"EUR");
        product3.setPrices(Arrays.asList(price31,price32));
        final Product product4 = new Product("product4","product4",null,false);
        final Price price41 = new Price("product4-1",BigDecimal.valueOf(130.0),"CNY");
        final Price price42 = new Price("product4-2",BigDecimal.valueOf(90.0),"EUR");
        product4.setPrices(Arrays.asList(price41,price42));
        products.addAll(Arrays.asList(product1,product2,product3,product4));

        try {
            final Order order1 = new Order("order1",null,user1,null,format.parse("2018-11-02 07:08:02"),null);
            final OrderEntry orderEntry11 = new OrderEntry(1,product1,BigDecimal.valueOf(100.0),10L,order1,false,null);
            final OrderEntry orderEntry12 = new OrderEntry(2,product2,BigDecimal.valueOf(80.0),12L,order1,false,null);
            order1.setOrderEntries(Arrays.asList(orderEntry11,orderEntry12));
            final Order order2 = new Order("order2",null,user1,null,format.parse("2018-12-02 07:08:02"),null);
            final OrderEntry orderEntry21 = new OrderEntry(1,product3,BigDecimal.valueOf(70.0),10L,order2,false,null);
            final OrderEntry orderEntry22 = new OrderEntry(2,product4,BigDecimal.valueOf(90.0),12L,order2,false,null);
            order2.setOrderEntries(Arrays.asList(orderEntry21,orderEntry22));
            orders.addAll(Arrays.asList(order1,order2));
        } catch (Exception e){
            log.error("Exception",e);
        }
    }

    /**
     * 获取所有订单
     * @return 所有订单
     */
    public static Collection<Order> getOrders(){
        return orders;
    }

    /**
     * 通过code获取订单
     * @param code code
     * @return 订单
     */
    public static Order getOrderByCode(String code){
        for (Order order : orders) {
            if (code.equals(order.getCode())) {
                return order;
            }
        }
        return null;
    }

    /**
     * 获取所有商品
     * @return 所有商品
     */
    public static Collection<Product> getProducts(){
        return products;
    }

    /**
     * 通过code获取商品
     * @param code code
     * @return 商品
     */
    public static Product getProductByCode(String code){
        for (Product product : products) {
            if (code.equals(product.getCode())) {
                return product;
            }
        }
        return null;
    }

    /**
     * 获取所有用户
     * @return 所有用户
     */
    public static Collection<User> getUsers(){
        return users;
    }

    /**
     * 通过uid获取用户
     * @param uid uid
     * @return 用户
     */
    public static User getUserByUid(String uid){
        for (User user : users) {
            if (uid.equals(user.getUid())) {
                return user;
            }
        }
        return null;
    }

}
