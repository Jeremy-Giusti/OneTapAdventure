package fr.giusti.onetapadventure.repository;

import java.util.ArrayList;

import fr.giusti.onetapadventure.repository.levelsData.infinitelvl.InfiniteLvlConstant;
import fr.giusti.onetapadventure.repository.levelsData.Lvl1Constant;
import fr.giusti.onetapadventure.repository.levelsData.Lvl2Constant;
import fr.giusti.onetapadventure.repository.levelsData.Lvl3Constant;
import fr.giusti.onetapengine.callback.OnGameEndListener;
import fr.giusti.onetapengine.callback.OnRuleAccomplishedListener;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.rules.Rule;
import fr.giusti.onetapengine.rules.RuleFactory;
import fr.giusti.onetapengine.rules.RulesManager;
import fr.giusti.onetapengine.rules.eConditions;
import fr.giusti.onetapengine.rules.eRuleResult;

/**
 * Created by jérémy on 09/09/2016.
 * Create specific ruleManagers with their set of rules
 */
public class RuleRepo {


    public static RulesManager getInfiniteLvlRules(OnGameEndListener endListener) {
        RuleFactory ruleFactory = new RuleFactory();
        Rule masterRule = ruleFactory.getNumericaleRule(InfiniteLvlConstant.MASTER_RULE, eConditions.MOB_COUNT, eRuleResult.END, InfiniteLvlConstant.MASTER_RULE_VALUE);


        String gameId = GameConstant.getLevelId(0, 0);
        OnRuleAccomplishedListener accomplishedBehavior = getDefaultBehavior(endListener, gameId);
        return new RulesManager(masterRule, accomplishedBehavior);
    }


    public static RulesManager getLvl_1x1_Rules(final OnGameEndListener gameEndListener) {

        RuleFactory ruleFactory = new RuleFactory();

        Rule masterRule = ruleFactory.getIncrementaleRule(Lvl1Constant.ESCAPING_MOB_RULE, eConditions.MOB_AWAY, eRuleResult.FAIL, Lvl1Constant.MAX_MOB_AWAY);

        Rule endRule = ruleFactory.getIncrementaleRule(Lvl1Constant.LEVEL_END_RULE, eConditions.MOB_COUNTDOWN, eRuleResult.END, Lvl1Constant.MOB_TOTAL_NB);

        String gameId = GameConstant.getLevelId(1, 1);
        OnRuleAccomplishedListener accomplishedBehavior = getDefaultBehavior(gameEndListener, gameId);
        return new RulesManager(masterRule, accomplishedBehavior, endRule);
    }


    public static RulesManager getLvl_1x2_Rules(OnGameEndListener endListener) {
        String gameId = GameConstant.getLevelId(1, 2);

        RuleFactory ruleFactory = new RuleFactory();
        Rule masterRule = ruleFactory.getIncrementaleRule(Lvl2Constant.ESCAPING_MOB_RULE, eConditions.MOB_AWAY, eRuleResult.FAIL, Lvl2Constant.MAX_MOB_AWAY);

        Rule endRule = ruleFactory.getNumericaleRule(Lvl2Constant.TIMER_RULE, eConditions.TIMER, eRuleResult.VICTORY, Lvl2Constant.TIMER_END_VALUE);

        OnRuleAccomplishedListener accomplishedBehavior = getDefaultBehavior(endListener, gameId);
        return new RulesManager(masterRule, accomplishedBehavior, endRule);
    }

    public static RulesManager getLvl_1x3_Rules(OnGameEndListener endListener) {

        RuleFactory ruleFactory = new RuleFactory();
        String gameId = GameConstant.getLevelId(1, 3);
        Rule masterRule = ruleFactory.getIncrementaleRule(Lvl3Constant.SUCCESS_SCORE_RULE, eConditions.SCORE, eRuleResult.VICTORY, Lvl3Constant.SUCCESS_SCORE_RULE_VALUE);

        Rule defeatRule = ruleFactory.getNumericaleRule(Lvl3Constant.DEFEAT_SCORE_RULE, eConditions.SCORE, eRuleResult.END_DEFEAT, Lvl3Constant.DEFEAT_SCORE_RULE_VALUE);

        OnRuleAccomplishedListener accomplishedBehavior = getDefaultBehavior(endListener, gameId);
        return new RulesManager(masterRule, accomplishedBehavior, defeatRule);
    }


    /**
     * all around ruleaccomplish listener (calculate score and win depending on rules accomplished)
     *
     * @param gameEndListener
     * @return
     */
    private static OnRuleAccomplishedListener getDefaultBehavior(final OnGameEndListener gameEndListener, final String lvlId) {
        return new OnRuleAccomplishedListener() {
            @Override
            public void onMasterRuleAccomplished(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                //to implement
                boolean masterRuleSucces = masterRule.ruleResult == eRuleResult.VICTORY;
                int score = calculateScoreResult(masterRuleSucces, false, secondaries);
                gameEndListener.onGameEnd(masterRule.ruleResult, lvlId, score);
            }

            @Override
            public void onTimerEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                boolean TimerRuleSucces = timerRule.ruleResult == eRuleResult.VICTORY;
                int score = calculateScoreResult(false, TimerRuleSucces, secondaries);
                gameEndListener.onGameEnd(timerRule.ruleResult, lvlId, score);
            }

            @Override
            public void onGameEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                int score = calculateScoreResult(false, false, secondaries);
                gameEndListener.onGameEnd(secondaries.get(secondaries.size() - 1).ruleResult, lvlId, score);
            }

            private int calculateScoreResult(boolean masterRuleSucces, boolean TimerRuleSucces, ArrayList<Rule> secondaries) {
                int score = 0;
                score += (masterRuleSucces) ? 1000 : 0;
                score += (TimerRuleSucces) ? 1000 : 0;

                for (Rule rule : secondaries) {
                    if (rule.ruleResult == eRuleResult.VICTORY) {
                        score += 100;
                    } else if (rule.ruleResult == eRuleResult.FAIL) {
                        score -= 100;
                    } else if (rule.ruleResult == eRuleResult.END_DEFEAT) {
                        score -= 1000;
                    }
                }

                return score;
            }
        };

    }


}
