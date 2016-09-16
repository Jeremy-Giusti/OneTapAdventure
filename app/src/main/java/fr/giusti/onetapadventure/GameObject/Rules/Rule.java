package fr.giusti.onetapadventure.gameObject.rules;

/**
 * Created by jérémy on 08/09/2016.
 */
public class Rule {
    public static final String BOARD_EMPTY_RULE_KEY = "boardEmpty";
    private final String idName;
    private final boolean reduce;
    public eConditionType type;
    public eConditions condition;
    private int valueInt = 0;
    private int goalInt = 0;
    private String goalStr = "";
    public boolean intCondition = true;
    private IRuleProgressListener listener;


    public Rule(String idName, eConditionType type, eConditions condition, int valueInt, int goalInt) {
        this.idName = idName;
        this.type = type;
        this.condition = condition;
        this.valueInt = valueInt;
        this.goalInt = goalInt;
        intCondition = true;
        reduce = (valueInt > goalInt);
    }


    public Rule(String idName, String goalStr, eConditions condition, eConditionType type) {
        this.idName = idName;
        this.goalStr = goalStr;
        this.condition = condition;
        this.type = type;
        intCondition = false;
        reduce = false;
    }

    public eConditionType ruleProgress(int value) {
        if (condition == eConditions.MOB_COUNT)
            return (value == goalInt) ? type : eConditionType.NULL;

        if (reduce) valueInt--;
        else valueInt++;
        onProgress(null);
        return (valueInt == goalInt) ? type : eConditionType.NULL;
    }

    public eConditionType ruleProgress(String value) {
        onProgress(value);
        return (value.equals(goalStr)) ? type : eConditionType.NULL;
    }

    private void onProgress(String value) {
        if (listener != null) {
            if (intCondition) {
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
