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


    /**
     * id
     * <br>the kind of result this rule will return if condition is met
     * <br>the kind of event this rule should listening to
     * <br> current progress
     * <br> if value match goal then type is returned
     */
    private Rule(RuleBuilder builder) {
        this.idName = builder.idName;
        this.type = builder.type;
        this.condition = builder.condition;
        this.valueInt = builder.valueInt;
        this.goalInt = builder.goalInt;
        this.goalStr = builder.goalStr;
        this.isNumericalCondition = builder.isNumericalCondition;
    }


    /**
     * @param idName    id
     * @param goalStr   the string to match to get the type as result
     * @param condition the kind of event this rule should listening to
     * @param type      the kind of result this rule will return if condition is met
     */
    public Rule(String idName, String goalStr, eConditions condition, eConditionType type) {
        this.idName = idName;
        this.goalStr = goalStr;
        this.condition = condition;
        this.type = type;
        isNumericalCondition = false;
    }


    public eConditionType ruleProgress(int value) {
        if (condition == eConditions.MOB_COUNT)
            return (value == goalInt) ? type : eConditionType.NULL;
        if (condition == eConditions.TIMER) {
            valueInt = value;
        }else{
            valueInt += value;
        }

        onProgress(null);
        if(condition == eConditions.TIMER){
            return (valueInt > goalInt) ? type : eConditionType.NULL;

        }else {
            return (valueInt == goalInt) ? type : eConditionType.NULL;
        }
    }

    public eConditionType ruleProgress(String value) {
        onProgress(value);
        return (value.equals(goalStr)) ? type : eConditionType.NULL;
    }

    private void onProgress(String value) {
        if (listener != null) {
            if (isNumericalCondition) {
                if (eConditions.TIMER == condition) {
                    //FIXME better time progress display
                    listener.onRuleProgress(idName, "" + valueInt / 1000 + "/" + goalInt / 1000);
                } else {
                    if (goalInt == 0) {
                        listener.onRuleProgress(idName, "" + valueInt);
                    } else {
                        listener.onRuleProgress(idName, "" + valueInt + "/" + goalInt);
                    }
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

    public static class RuleBuilder {
        private final String idName;
        private final eConditionType type;
        private final eConditions condition;
        private boolean isNumericalCondition = true;
        private int valueInt = 0;
        private int goalInt = 0;
        private String goalStr = "";

        public RuleBuilder(String idName, eConditionType type, eConditions condition) {
            this.idName = idName;
            this.type = type;
            this.condition = condition;
        }

        public RuleBuilder setNumericalCondition(int goal, int startValue) {
            this.goalInt = goal;
            this.valueInt = startValue;
            this.isNumericalCondition = true;
            return this;
        }

        public RuleBuilder setStringCondition(String goal) {
            this.goalStr = goal;
            this.isNumericalCondition = false;
            return this;
        }

        public Rule build() {
            return new Rule(this);
        }

    }
}
