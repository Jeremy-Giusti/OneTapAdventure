package fr.giusti.onetapadventure.repository;

import java.util.ArrayList;

import fr.giusti.onetapengine.callback.OnGameEndListener;
import fr.giusti.onetapengine.callback.OnRuleAccomplishedListener;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.rules.Rule;
import fr.giusti.onetapengine.rules.RulesManager;
import fr.giusti.onetapengine.rules.eConditionType;
import fr.giusti.onetapengine.rules.eConditions;
import fr.giusti.onetapadventure.repository.levelsData.Lvl1Constant;
import fr.giusti.onetapadventure.repository.levelsData.Lvl2Constant;
import fr.giusti.onetapadventure.repository.levelsData.Lvl3Constant;

/**
 * Created by jérémy on 09/09/2016.
 */
public class RuleRepo {

    public static RulesManager getLvl_1x1_Rules(final OnGameEndListener gameEndListener) {
        Rule masterRule = new Rule.RuleBuilder(Lvl1Constant.ESCAPING_MOB_RULE, eConditionType.FAIL, eConditions.MOB_AWAY)
                .setNumericalCondition( Lvl1Constant.MAX_MOB_AWAY, 0)
                .build();

       // Rule masterRule = new Rule(Lvl1Constant.ESCAPING_MOB_RULE, eConditionType.FAIL, eConditions.MOB_AWAY, Lvl1Constant.MAX_MOB_AWAY, 0);
        Rule endRule = new Rule.RuleBuilder(Lvl1Constant.LEVEL_END_RULE, eConditionType.END, eConditions.MOB_COUNTDOWN)
                .setNumericalCondition( 0, Lvl1Constant.MOB_TOTAL_NB)
                .build();
        //Rule endRule = new Rule(Lvl1Constant.LEVEL_END_RULE, eConditionType.END, eConditions.MOB_COUNTDOWN, 0, Lvl1Constant.MOB_NB);
        String gameId = GameConstant.getLevelId(1, 1);
        OnRuleAccomplishedListener accomplishedBehavior = getDefaultBehavior(gameEndListener, gameId);
        return new RulesManager(masterRule, accomplishedBehavior, endRule);
    }


    public static RulesManager getLvl_1x2_Rules(OnGameEndListener endListener) {
        String gameId = GameConstant.getLevelId(1, 2);
        Rule masterRule = new Rule.RuleBuilder(Lvl2Constant.ESCAPING_MOB_RULE, eConditionType.FAIL, eConditions.MOB_AWAY)
                .setNumericalCondition( Lvl2Constant.MAX_MOB_AWAY, 0)
                .build();

        Rule endRule = new Rule.RuleBuilder(Lvl2Constant.TIMER_RULE, eConditionType.VICTORY, eConditions.TIMER)
                .setNumericalCondition( Lvl2Constant.TIMER_END_VALUE, 0)
                .build();

        OnRuleAccomplishedListener accomplishedBehavior = getDefaultBehavior(endListener, gameId);
        return new RulesManager(masterRule, accomplishedBehavior, endRule);
    }

    public static RulesManager getLvl_1x3_Rules(OnGameEndListener endListener) {
        String gameId = GameConstant.getLevelId(1, 3);
        Rule masterRule = new Rule.RuleBuilder(Lvl3Constant.SUCCESS_SCORE_RULE, eConditionType.VICTORY, eConditions.SCORE)
                .setNumericalCondition( Lvl3Constant.SUCCESS_SCORE_RULE_VALUE, 0)
                .build();

        Rule defeatRule = new Rule.RuleBuilder(Lvl3Constant.DEFEAT_SCORE_RULE, eConditionType.END_DEFEAT, eConditions.SCORE)
                .setNumericalCondition( Lvl3Constant.DEFEAT_SCORE_RULE_VALUE, 0)
                .build();

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
                boolean masterRuleSucces = masterRule.type == eConditionType.VICTORY;
                int score = calculateScoreResult(masterRuleSucces, false, secondaries);
                gameEndListener.onGameEnd(masterRule.type, lvlId, score);
            }

            @Override
            public void onTimerEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                boolean TimerRuleSucces = timerRule.type == eConditionType.VICTORY;
                int score = calculateScoreResult(false, TimerRuleSucces, secondaries);
                gameEndListener.onGameEnd(timerRule.type, lvlId, score);
            }

            @Override
            public void onGameEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                int score = calculateScoreResult(false, false, secondaries);
                gameEndListener.onGameEnd(secondaries.get(secondaries.size() - 1).type, lvlId, score);
            }

            private int calculateScoreResult(boolean masterRuleSucces, boolean TimerRuleSucces, ArrayList<Rule> secondaries) {
                int score = 0;
                score += (masterRuleSucces) ? 1000 : 0;
                score += (TimerRuleSucces) ? 1000 : 0;

                for (Rule rule : secondaries) {
                    if (rule.type == eConditionType.VICTORY) {
                        score += 100;
                    } else if (rule.type == eConditionType.FAIL) {
                        score -= 100;
                    }else if (rule.type == eConditionType.END_DEFEAT) {
                        score -= 1000;
                    }
                }

                return score;
            }
        };

    }



}
