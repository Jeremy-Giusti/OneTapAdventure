package fr.giusti.onetapadventure.gameObject.rules;

/**
 * Created by jérémy on 08/09/2016.
 */
public class Rule {
    private final String idName;
    public final eConditionType type;
    public final eConditions condition;
    public boolean isNumericalCondition = true;
    private int valueInt = 0;
    private int goalInt = 0;
    private String goalStr = "";
    private IRuleProgressListener listener;


    public Rule(String idName, eConditionType type, eConditions condition) {
        this.idName = idName;
        this.type = type;
        this.condition = condition;
    }

    /**
     *
     * @param idName id
     * @param type the kind of result this rule will return if condition is met
     * @param condition the kind of event this rule should listening to
     * @param valueInt current progress
     * @param goalInt if value match goal then type is returned
     */
    public Rule(String idName, eConditionType type, eConditions condition, int valueInt, int goalInt) {
        this.idName = idName;
        this.type = type;
        this.condition = condition;
        this.valueInt = valueInt;
        this.goalInt = goalInt;
        isNumericalCondition = true;
    }


    /**
     *
     * @param idName id
     * @param goalStr the string to match to get the type as result
     * @param condition the kind of event this rule should listening to
     * @param type the kind of result this rule will return if condition is met
     */
    public Rule(String idName, String goalStr, eConditions condition, eConditionType type) {
        this.idName = idName;
        this.goalStr = goalStr;
        this.condition = condition;
        this.type = type;
        isNumericalCondition = false;
    }

    public int getValueInt() {
        return valueInt;
    }

    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }

    public int getGoalInt() {
        return goalInt;
    }

    public void setGoalInt(int goalInt) {
        this.goalInt = goalInt;
    }

    public String getGoalStr() {
        return goalStr;
    }

    public void setGoalStr(String goalStr) {
        this.goalStr = goalStr;
    }

    public IRuleProgressListener getListener() {
        return listener;
    }

    public eConditionType ruleProgress(int value) {
        if (condition == eConditions.MOB_COUNT)
            return (value == goalInt) ? type : eConditionType.NULL;

        valueInt+=value;
        onProgress(null);
        return (valueInt == goalInt) ? type : eConditionType.NULL;
    }

    public eConditionType ruleProgress(String value) {
        onProgress(value);
        return (value.equals(goalStr)) ? type : eConditionType.NULL;
    }

    private void onProgress(String value) {
        if (listener != null) {
            if (isNumericalCondition) {
                if (goalInt == 0) {
                    listener.onRuleProgress(idName, "" + valueInt);
                } else {
                    listener.onRuleProgress(idName, "" + valueInt + "/" + goalInt);
                }
            } else {
                listener.onRuleProgress(idName, goalStr + "/" + value);
            }
        }
    }

    public void setListener(IRuleProgressListener listener) {
        this.listener = listener;
        onProgress("_");
    }

    public String getIdName() {
        return idName;
    }
}
