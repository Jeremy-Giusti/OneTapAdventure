package fr.giusti.onetapengine.rules;

/**
 * Created by jérémy on 08/09/2016.
 */
@Deprecated
public class OldRule {
    private final String idName;
    public final eRuleResult type;
    public final eConditions condition;
    public boolean isNumericalCondition = true;
    private long valueInt = 0;
    private long goalInt = 0;
    private String goalStr = "";
    private IRuleProgressListener listener;


    /**
     * id
     * <br>the kind of result this rule will return if condition is met
     * <br>the kind of event this rule should listening to
     * <br> current mProgress
     * <br> if value match mGoal then ruleResult is returned
     */
    @Deprecated
    private OldRule(RuleBuilder builder) {
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
     * @param goalStr   the string to match to get the ruleResult as result
     * @param condition the kind of event this rule should listening to
     * @param type      the kind of result this rule will return if condition is met
     */
    @Deprecated
    public OldRule(String idName, String goalStr, eConditions condition, eRuleResult type) {
        this.idName = idName;
        this.goalStr = goalStr;
        this.condition = condition;
        this.type = type;
        isNumericalCondition = false;
    }


    @Deprecated
    public eRuleResult ruleProgress(long value) {
        if (condition == eConditions.MOB_COUNT)
            return (value == goalInt) ? type : eRuleResult.NULL;
        if (condition == eConditions.TIMER) {
            valueInt = value;
        }else{
            valueInt += value;
        }

        onProgress(null);
        if(condition == eConditions.TIMER){
            return (valueInt > goalInt) ? type : eRuleResult.NULL;

        }else {
            return (valueInt == goalInt) ? type : eRuleResult.NULL;
        }
    }
    @Deprecated

    public eRuleResult ruleProgress(String value) {
        onProgress(value);
        return (value.equals(goalStr)) ? type : eRuleResult.NULL;
    }

    @Deprecated
    private void onProgress(String value) {
        if (listener != null) {
            if (isNumericalCondition) {
                if (eConditions.TIMER == condition) {
                    //FIXME better time mProgress display
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

    @Deprecated
    public void setListener(IRuleProgressListener listener) {
        this.listener = listener;
        onProgress("_");
    }

    @Deprecated
    public String getIdName() {
        return idName;
    }

    @Deprecated
    public static class RuleBuilder {
        private final String idName;
        private final eRuleResult type;
        private final eConditions condition;
        private boolean isNumericalCondition = true;
        private int valueInt = 0;
        private int goalInt = 0;
        private String goalStr = "";

        public RuleBuilder(String idName, eRuleResult type, eConditions condition) {
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

        public OldRule build() {
            return new OldRule(this);
        }

    }
}
