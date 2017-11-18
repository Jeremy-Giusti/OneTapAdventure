package fr.giusti.onetapengine.rules;

/**
 * Created by jérémy on 18/11/2017.
 */

public abstract class Rule<T> {

    private final String idName;
    public final eRuleResult ruleResult;
    public final eConditions condition;
    protected T mGoal;
    protected T mProgress;
    private IRuleProgressListener listener;
    public Class<T> ruleType;


    public Rule(Class<T> type, String idName, eRuleResult ruleResult, eConditions condition, T goal, T progress) {
        ruleType = type;
        this.idName = idName;
        this.ruleResult = ruleResult;
        this.condition = condition;
        this.mGoal = goal;
        this.mProgress = progress;
        this.listener = listener;
    }

    public eRuleResult ruleProgress(T progress) {
        eRuleResult progressResult = processRuleProgress(progress);
        listener.onRuleProgress(idName, getDisplayableProgress());
        return progressResult;
    }

    protected abstract eRuleResult processRuleProgress(T progress);

    protected abstract String getDisplayableProgress();

    public void setListener(IRuleProgressListener listener) {
        this.listener = listener;
        listener.onRuleProgress(idName, getDisplayableProgress());
    }

    public String getIdName() {
        return idName;
    }

}
