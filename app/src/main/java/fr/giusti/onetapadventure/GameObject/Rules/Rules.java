package fr.giusti.onetapadventure.gameObject.rules;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;

import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.callback.OnBoardEventListener;
import fr.giusti.onetapadventure.callback.OnRuleAccomplishedListener;

/**
 * Created by jérémy on 08/09/2016.
 */
public class Rules implements OnBoardEventListener {
    public static final int TIME_PROGRESS_FREQUENCY = 1000;
    private HashMap<eConditions, ArrayList<Rule>> indexedRuleList = new HashMap<>();
    private ArrayList<Rule> results = new ArrayList<>();
    private Rule masterRule;
    private Rule timerRule = null;
    private OnRuleAccomplishedListener listener;


    public Rules(Rule masterRule,OnRuleAccomplishedListener ruleAchievedBehavior, Rule... ruleList) {
        this.masterRule = masterRule;
        this.listener=ruleAchievedBehavior;
        for (eConditions condition : eConditions.values()) {
            indexedRuleList.put(condition, new ArrayList<Rule>());
        }

        if (masterRule.condition == eConditions.TIMER) {
            this.timerRule = masterRule;
        } else {
            indexedRuleList.get(masterRule.condition).add(masterRule);
        }

        for (Rule rule : ruleList) {
            if (rule.condition == eConditions.TIMER) {
                this.timerRule = rule;
            } else {
                indexedRuleList.get(rule.condition).add(rule);
            }
        }
    }

    public OnRuleAccomplishedListener getListener() {
        return listener;
    }

    public void setListener(OnRuleAccomplishedListener listener) {
        this.listener = listener;
    }

    private void onRuleAccomplished(Rule rule) {
        if (rule.equals(masterRule)) {
            listener.onMasterRuleAccomplished(rule, timerRule, results);
        }
        if (rule.equals(timerRule)) {
            listener.onTimerEnded(rule, timerRule, results);
        } else if (rule.type == eConditionType.END) {
            results.add(rule);
            listener.onGameEnded(rule, timerRule, results);
        } else {
            results.add(rule);
        }
    }


    @Override
    public void firstUpdate() {
        if (timerRule != null)
            new Runnable() {

                @Override
                public void run() {
                    if (timerRule != null) {
                        onTimeProgress(TIME_PROGRESS_FREQUENCY);
                        new Handler().postDelayed(this, TIME_PROGRESS_FREQUENCY);
                    }
                }
            }.run();
    }

    public void onTimeProgress(int progress) {
        //find if a rule is linked to the condition and test it
        eConditionType result;
        result = timerRule.checkCondition(progress, true);

        if (result != eConditionType.NULL) {
            onRuleAccomplished(timerRule);
        }
    }

    @Override
    public void onMobCountChange(int count, eConditions reason, GameMob mob) {
        //dispatch event.
        switch (reason) {
            case MOB_DEATH:
                onMobDeath(mob);
                break;
            case MOB_AWAY:
                onMobGetAway(mob);
                break;
            case NEW_MOB:
                onNewMob(mob);
                break;
        }

        //find if a rule is linked to the condition and test it
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.MOB_COUNT);
        Rule rule;
        eConditionType result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            result = rule.checkCondition(count, true);

            if (result != eConditionType.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

    public void onMobDeath(GameMob deadMob) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.MOB_DEATH);
        Rule rule;
        eConditionType result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            if (rule.intCondition) result = rule.checkCondition(1, true);
            else result = rule.checkCondition(deadMob.getIdName());
            if (result != eConditionType.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

    public void onNewMob(GameMob deadMob) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.NEW_MOB);
        Rule rule;
        eConditionType result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            if (rule.intCondition) result = rule.checkCondition(1, false);
            else result = rule.checkCondition(deadMob.getIdName());
            if (result != eConditionType.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }


    public void onMobGetAway(GameMob mobAway) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.MOB_AWAY);
        Rule rule;
        eConditionType result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            if (rule.intCondition) result = rule.checkCondition(1, true);
            else result = rule.checkCondition(mobAway.getIdName());
            if (result != eConditionType.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

    public void onMobCountDown(GameMob mob) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.MOB_COUNTDOWN);
        Rule rule;
        eConditionType result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            result = rule.checkCondition(1, true);
            if (result != eConditionType.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

    @Override
    public void onScorePlus(int add) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.SCORE);
        Rule rule;
        eConditionType result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            result = rule.checkCondition(add, false);
            if (result != eConditionType.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

    @Override
    public void onScoreMinus(int remove) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.SCORE);
        Rule rule;
        eConditionType result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            result = rule.checkCondition(remove, true);
            if (result != eConditionType.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

}
