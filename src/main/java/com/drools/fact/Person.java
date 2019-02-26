package com.drools.fact;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cWww
 * @Title
 * @Description
 * @date: 2019/2/25  15:14
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {

    private String username;
    private String password;
    private Integer age;
    private Date birthday;
    private Boolean married;

}
