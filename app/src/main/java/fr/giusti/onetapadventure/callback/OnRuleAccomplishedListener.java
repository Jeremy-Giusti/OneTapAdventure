package fr.giusti.onetapadventure.callback;

import android.util.Pair;

import java.util.ArrayList;

import fr.giusti.onetapadventure.GameObject.Rules.Rule;
import fr.giusti.onetapadventure.GameObject.Rules.eConditionType;

/**
 * Created by jérémy on 08/09/2016.
 */
public interface OnRuleAccomplishedListener {
    void onMasterRuleAccomplished(Rule masterRule, Rule timerRule, ArrayList<Rule> secondaries);
    void onTimerEnded(Rule masterRule,Rule timerRule, ArrayList<Rule> secondaries);
    void onGameEnded(Rule masterRule,Rule timerRule, ArrayList<Rule> secondaries);
}
