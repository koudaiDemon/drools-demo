package com.drools.fact;

import lombok.*;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@Role(value = Role.Type.EVENT)
@Expires(value = "2s")
public class Person implements Serializable {

    private String username;
    private String password;
    private Integer age;
    private Date birthday;
    private Boolean married;
    private List<Address> addresses;

}
