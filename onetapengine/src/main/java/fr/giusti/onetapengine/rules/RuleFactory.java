package fr.giusti.onetapengine.rules;

/**
 * Created by jérémy on 18/11/2017.
 */

public class RuleFactory {

    public Rule<Long> getNumericaleRule(String ruleId, eConditions condition, eRuleResult result, long goal) {
        return new Rule<Long>(Long.class, ruleId, result, condition, goal, 0l) {
            @Override
            protected eRuleResult processRuleProgress(Long progress) {
                mProgress = progress;
                return (mProgress > mGoal) ? ruleResult : eRuleResult.NULL;
            }

            @Override
            protected String getDisplayableProgress() {
                return "" + mProgress + "/" + mGoal;
            }
        };
    }

    public Rule<Integer> getIncrementaleRule(String ruleId, eConditions condition, eRuleResult result, Integer goal) {
        return new Rule<Integer>(Integer.class, ruleId, result, condition, goal, 0) {
            @Override
            protected eRuleResult processRuleProgress(Integer progress) {
                mProgress += progress;
                return (mProgress > mGoal) ? ruleResult : eRuleResult.NULL;
            }

            @Override
            protected String getDisplayableProgress() {
                return "" + mProgress + "/" + mGoal;
            }
        };
    }

    public Rule<String> getStringRule(String ruleId, eConditions condition, eRuleResult result, String goal) {
        return new Rule<String>(String.class, ruleId, result, condition, goal, "") {
            @Override
            protected eRuleResult processRuleProgress(String progress) {
                mProgress = progress;
                return (mProgress.equals(mGoal)) ? ruleResult : eRuleResult.NULL;
            }

            @Override
            protected String getDisplayableProgress() {
                return mGoal;
            }
        };
    }


}
