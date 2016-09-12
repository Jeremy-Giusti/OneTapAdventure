package fr.giusti.onetapadventure.repository;

import java.util.ArrayList;

import fr.giusti.onetapadventure.gameObject.rules.OnGameEndListener;
import fr.giusti.onetapadventure.gameObject.rules.Rule;
import fr.giusti.onetapadventure.gameObject.rules.Rules;
import fr.giusti.onetapadventure.gameObject.rules.eConditionType;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;
import fr.giusti.onetapadventure.callback.OnRuleAccomplishedListener;
import fr.giusti.onetapadventure.commons.Constants;

/**
 * Created by jérémy on 09/09/2016.
 */
public class RuleRepo {

    public static Rules getLvl_1x1_Rules(final OnGameEndListener gameEndListener) {
        Rule masterRule = new Rule(eConditionType.DEFEAT, eConditions.MOB_AWAY, 0, 20);
        Rule endRule = new Rule(eConditionType.END, eConditions.MOB_COUNTDOWN, 0, 20);

        OnRuleAccomplishedListener accomplishedBehavior = new OnRuleAccomplishedListener() {
            @Override
            public void onMasterRuleAccomplished(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                gameEndListener.onGameEnd(eConditionType.DEFEAT, Constants.getLevelId(1,1),0);
            }

            @Override
            public void onTimerEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                gameEndListener.onGameEnd(eConditionType.NULL, Constants.getLevelId(1,1),0);
            }

            @Override
            public void onGameEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries) {
                gameEndListener.onGameEnd(eConditionType.VICTORY, Constants.getLevelId(1,1),1000);
            }
        };


        return new Rules(masterRule,accomplishedBehavior,endRule );
    }


    /**
     * all around ruleaccomplish listener (calculate score and win depending on rules accomplished)
     * @param gameEndListener
     * @return
     */
    //TODO
    private static OnRuleAccomplishedListener getDefaultBehavior(OnGameEndListener gameEndListener){
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
