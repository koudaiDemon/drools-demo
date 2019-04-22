package com.drools.ruleengine.db;

import java.util.List;

/**
 * @author cWww
 * @Title RuleDAO
 * @Description RuleDAO
 * @date: 2019/4/16  16:10
 */
public interface RuleDAO {

    /**
     * findAllActiveRules
     * @return 激活Rule
     */
    List<String> findAllActiveRules();

}
