package com.drools.fact.order;

import lombok.*;

/**
 * @author cWww
 * @Title User
 * @Description 用户
 * @date: 2019/3/6  14:03
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String uid;
    private String name;
    private Integer age;
}
