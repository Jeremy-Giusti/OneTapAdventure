package fr.giusti.onetapengine.rules;

/**
 * Created by jérémy on 18/11/2017.
 * Listen to board events and lauch event when event specificities are met
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

    /**
     * test is conditions are met and notify listener
     * @param progress
     * @return
     */
    public eRuleResult ruleProgress(T progress) {
        eRuleResult progressResult = processRuleProgress(progress);
        listener.onRuleProgress(idName, getDisplayableProgress(), getCompletionValue());
        return progressResult;
    }

    protected abstract double getCompletionValue();

    /**
     * to override, used to detrminate how condition can be met.
     * @param progress
     * @return
     */
    protected abstract eRuleResult processRuleProgress(T progress);

    /**
     * to override, used to make a displayable progress of this rule
     * @return
     */
    protected abstract String getDisplayableProgress();

    public void setListener(IRuleProgressListener listener) {
        this.listener = listener;
        listener.onRuleProgress(idName, getDisplayableProgress(), getCompletionValue());
    }

    public String getIdName() {
        return idName;
    }

}
