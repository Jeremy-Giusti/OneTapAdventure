package fr.giusti.onetapadventure.callback;

import java.util.ArrayList;

import fr.giusti.onetapadventure.GameObject.Rules.Rule;

/**
 * Created by jérémy on 08/09/2016.
 */
public interface OnRuleAccomplishedListener {
    void onRuleAccomplished(Rule masterRule,Rule timerRule, ArrayList<Rule> secondaries);
    void onTimerEnded(Rule masterRule,Rule timerRule, ArrayList<Rule> secondaries);
}
