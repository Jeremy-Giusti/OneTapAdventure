package fr.giusti.onetapadventure.repository;

import java.util.ArrayList;

import fr.giusti.onetapadventure.callback.OnRuleAccomplishedListener;
import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.gameObject.rules.OnGameEndListener;
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
        Rule masterRule = new Rule(Lvl1Constant.ESCAPING_MOB_RULE, eConditionType.DEFEAT, eConditions.MOB_AWAY, 0, Lvl1Constant.MOB_NB / 4);
        Rule endRule = new Rule(Lvl1Constant.LEVEL_END_RULE, eConditionType.END, eConditions.MOB_COUNTDOWN, 0, Lvl1Constant.MOB_NB);

        OnRuleAccomplishedListener accomplishedBehavior = new OnRuleAccomplishedListener() {
            @Override
            public void onMasterRuleAccomplished(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                gameEndListener.onGameEnd(eConditionType.DEFEAT, Constants.getLevelId(1, 1), 0);
            }

            @Override
            public void onTimerEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                //Should not happen
                gameEndListener.onGameEnd(eConditionType.NULL, Constants.getLevelId(1, 1), 0);
            }

            @Override
            public void onGameEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                gameEndListener.onGameEnd(eConditionType.VICTORY, Constants.getLevelId(1, 1), 1000);
            }
        };


        return new RulesManager(masterRule, accomplishedBehavior, endRule);
    }


    /**
     * all around ruleaccomplish listener (calculate score and win depending on rules accomplished)
     *
     * @param gameEndListener
     * @return
     */
    //TODO
    private static OnRuleAccomplishedListener getDefaultBehavior(OnGameEndListener gameEndListener) {
        return new OnRuleAccomplishedListener() {
            @Override
            public void onMasterRuleAccomplished(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                //to implement
            }

            @Override
            public void onTimerEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                //to implement
            }

            @Override
            public void onGameEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                //to implement
            }
        };

    }


}
