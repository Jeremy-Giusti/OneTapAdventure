package fr.giusti.onetapengine.rules;

/**
 * Created by jérémy on 18/11/2017.
 *
 * create base rules logic
 */

public class RuleFactory {

    /**
     *
     * just a dumb rule that compare 2 numerical value (progress and goal, if progress >= goal => result)
     *
     * @param ruleId must be unique on the board
     * @param condition which king of event this rule will listen to
     * @param result which kind of event this rule will send when met
     * @param goal the condition that need to be met for the rule to lauch it's result
     * @return
     */
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

    /**
     *
     * rule that addition each progress until it equal or exceed the goal
     *
     * @param ruleId must be unique on the board
     * @param condition which king of event this rule will listen to
     * @param result which kind of event this rule will send when met
     * @param goal the condition that need to be met for the rule to lauch it's result
     * @return
     */
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

    /**
     * just a dumb rule that compare 2 numerical value (progress and goal, if progress = goal => result)
     *
     * @param ruleId must be unique on the board
     * @param condition which king of event this rule will listen to
     * @param result which kind of event this rule will send when met
     * @param goal the condition that need to be met for the rule to lauch it's result
     * @return
     */
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
