package fr.giusti.onetapengine.rules;

/**
 * Created by jgiusti on 15/09/2016.
 */
public interface IRuleProgressListener {

    /**
     *
     * @param ruleId rule name/index
     * @param displayableProgress a string value that can be shown or logged
     * @param ruleCompletionPercent rule completion from 0 to 1
     */
    void onRuleProgress(String ruleId, String displayableProgress, double ruleCompletionPercent);
}
