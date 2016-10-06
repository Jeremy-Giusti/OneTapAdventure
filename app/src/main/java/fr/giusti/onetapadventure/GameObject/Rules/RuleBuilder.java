package fr.giusti.onetapadventure.gameObject.rules;

/**
 * Created by jgiusti on 06/10/2016.
 */

public class RuleBuilder {
    private final Rule rule;

    public RuleBuilder(String idName, eConditionType type, eConditions condition) {
        rule = new Rule(idName, type, condition);
    }

    public RuleBuilder setNumericalCondition(int goal, int startValue) {
        rule.setGoalInt(goal);
        rule.setValueInt(startValue);
        rule.isNumericalCondition = true;
        return this;
    }

    public RuleBuilder setStringCondition(String goal) {
        rule.setGoalStr(goal);
        rule.isNumericalCondition = false;
        return this;
    }


}
