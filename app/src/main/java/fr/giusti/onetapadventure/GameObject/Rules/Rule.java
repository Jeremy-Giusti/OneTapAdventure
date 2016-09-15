package fr.giusti.onetapadventure.gameObject.rules;

/**
 * Created by jérémy on 08/09/2016.
 */
public class Rule {
    public static final String BOARD_EMPTY_RULE_KEY = "boardEmpty";
    public eConditionType type;
    public eConditions condition;
    private int valueInt = 0;
    private int goalInt = 0;
    private String goalStr = "";
    public boolean intCondition = true;


    public Rule(eConditionType type, eConditions condition, int valueInt, int goalInt) {
        this.type = type;
        this.condition = condition;
        this.valueInt = valueInt;
        this.goalInt = goalInt;
        intCondition = true;
    }


    public Rule(String goalStr, eConditions condition, eConditionType type) {
        this.goalStr = goalStr;
        this.condition = condition;
        this.type = type;
        intCondition = false;
    }

    public eConditionType ruleProgress(int value, boolean reduce) {
        if (condition == eConditions.MOB_COUNT)
            return (value == goalInt) ? type : eConditionType.NULL;

        if (reduce) valueInt--;
        else value++;
        return (valueInt == goalInt) ? type : eConditionType.NULL;
    }

    public eConditionType ruleProgress(String value) {
        return (value.equals(goalStr)) ? type : eConditionType.NULL;
    }


}
