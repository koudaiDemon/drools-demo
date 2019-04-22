package com.drools.ruleengine.pojo;

/**
 * @author cWww
 * @Title SessionType
 * @Description kieSession类型
 * @date: 2019/4/16  15:25
 */
public enum SessionType {

    /**
     * 无状态的session
     */
    STATELESS("stateless"),
    /**
     * 有状态的session
     */
    STATEFUL("stateful");

    private String code;

    /**
     * Creates a new enum value for this enum type.
     *
     * @param code the enum value code
     */
    private SessionType(final String code)
    {
        this.code = code.intern();
    }

}
