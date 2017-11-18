package fr.giusti.onetapengine.callback;

import java.util.ArrayList;

import fr.giusti.onetapengine.rules.Rule;

/**
 * Created by jérémy on 08/09/2016.
 */
public interface OnRuleAccomplishedListener {
    void onMasterRuleAccomplished(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries);
    void onTimerEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries);
    void onGameEnded(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries);
}
