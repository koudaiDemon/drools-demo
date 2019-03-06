package com.drools.fact.order;

import lombok.*;

import java.util.List;

/**
 * @author cWww
 * @Title Product
 * @Description 商品
 * @date: 2019/3/6  14:03
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String code;
    private String name;
    private List<Price> prices;
    private Boolean net;
}
