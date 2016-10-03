package fr.giusti.onetapadventure.repository;

import java.util.ArrayList;

import fr.giusti.onetapadventure.callback.OnGameEndListener;
import fr.giusti.onetapadventure.callback.OnRuleAccomplishedListener;
import fr.giusti.onetapadventure.commons.GameConstant;
import fr.giusti.onetapadventure.gameObject.rules.Rule;
import fr.giusti.onetapadventure.gameObject.rules.RulesManager;
import fr.giusti.onetapadventure.gameObject.rules.eConditionType;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;
import fr.giusti.onetapadventure.repository.levelsData.Lvl1Constant;

/**
 * Created by jérémy on 09/09/2016.
 */
public class RuleRepo {

    public static RulesManager getLvl_1x1_Rules(final OnGameEndListener gameEndListener) {
        Rule masterRule = new Rule(Lvl1Constant.ESCAPING_MOB_RULE, eConditionType.DEFEAT, eConditions.MOB_AWAY, Lvl1Constant.MAX_MOB_AWAY, 0);
        Rule endRule = new Rule(Lvl1Constant.LEVEL_END_RULE, eConditionType.END, eConditions.MOB_COUNTDOWN, 0, Lvl1Constant.MOB_NB);

        OnRuleAccomplishedListener accomplishedBehavior = new OnRuleAccomplishedListener() {
            @Override
            public void onMasterRuleAccomplished(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                gameEndListener.onGameEnd(eConditionType.DEFEAT, GameConstant.getLevelId(1, 1), 0);
            }

            @Override
            public void onTimerEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                //Should not happen
                gameEndListener.onGameEnd(eConditionType.NULL, GameConstant.getLevelId(1, 1), 0);
            }

            @Override
            public void onGameEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                gameEndListener.onGameEnd(eConditionType.VICTORY, GameConstant.getLevelId(1, 1), 1000);
            }
        };


        return new RulesManager(masterRule, accomplishedBehavior, endRule);
    }

    public static RulesManager getLvl_1x2_Rules(OnGameEndListener endListener) {
        //TODO
        return null;
    }


    /**
     * all around ruleaccomplish listener (calculate score and win depending on rules accomplished)
     *
     * @param gameEndListener
     * @return
     */
    //TODO
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
                    } else if (rule.type == eConditionType.DEFEAT) {
                        score -= 100;
                    }
                }

                return score;
            }
        };

    }


}
