package com.drools.fact;

import lombok.Data;

/**
 * @author cWww
 * @Title Config
 * @Description 配置,主要是用于规则
 * @date: 2019/5/28  16:42
 */
@Data
public class Config {
    private String ruleCode;
    private Integer maxAllowedRuns = 0;
    private Integer currentRuns = 0;
}
