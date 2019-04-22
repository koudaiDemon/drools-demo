package com.drools.ruleengine.db.impl;

import com.drools.ruleengine.db.RuleDAO;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author cWww
 * @Title RuleDAOImpl
 * @Description RuleDAO实例
 * @date: 2019/4/16  16:11
 */
@Slf4j
public class RuleDAOImpl implements RuleDAO {

    @Override
    public List<String> findAllActiveRules() {

        final String rule1 = "package base;\n" +
                "dialect  \"mvel\"\n" +
                "\n" +
                "rule \"base1\"\n" +
                "    when\n" +
                "    then\n" +
                "        System.out.println(\"now1:\"+System.currentTimeMillis());\n" +
                "end";

        final String rule2 = "package base;\n" +
                "dialect  \"mvel\"\n" +
                "\n" +
                "rule \"base2\"\n" +
                "    when\n" +
                "    then\n" +
                "        System.out.println(\"now2:\"+System.currentTimeMillis());\n" +
                "end\n";

        return Arrays.asList(rule1,rule2);
    }
}
