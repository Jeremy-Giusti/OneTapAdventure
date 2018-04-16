package fr.giusti.onetapengine.rules;

import java.util.ArrayList;
import java.util.HashMap;

import fr.giusti.onetapengine.callback.OnBoardEventListener;
import fr.giusti.onetapengine.callback.OnRuleAccomplishedListener;
import fr.giusti.onetapengine.entity.GameMob;

/**
 * Created by jérémy on 08/09/2016.
 * administrate a set of rules,<br>
 * notify them with corresponding event/progress
 */
public class RulesManager implements OnBoardEventListener {
    private HashMap<eConditions, ArrayList<Rule>> indexedRuleList = new HashMap<>();
    private ArrayList<Rule> accomplishedRules = new ArrayList<>();
    private Rule masterRule;
    private Rule timerRule = null;
    private OnRuleAccomplishedListener listener;


    public RulesManager(Rule masterRule, OnRuleAccomplishedListener ruleAchievedBehavior, Rule... ruleList) {
        this.masterRule = masterRule;
        this.listener = ruleAchievedBehavior;
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

    public boolean setRuleListener(String ruleName, IRuleProgressListener ruleListener) {

        if (timerRule != null && timerRule.getIdName().equals(ruleName)) {
            timerRule.setListener(ruleListener);
            return true;
        }
        for (ArrayList<Rule> ruleList : indexedRuleList.values()) {
            for (Rule rule : ruleList) {
                if (ruleName.equals(rule.getIdName())) {
                    rule.setListener(ruleListener);
                    return true;
                }
            }
        }
        return false;
    }


    private void onRuleAccomplished(Rule rule) {
        if (rule.equals(masterRule)) {
            listener.onMasterRuleAccomplished(rule, timerRule, accomplishedRules);
        }
        if (rule.equals(timerRule)) {
            listener.onTimerEnded(rule, timerRule, accomplishedRules);
        } else if (rule.ruleResult == eRuleResult.END || rule.ruleResult == eRuleResult.END_DEFEAT) {
            accomplishedRules.add(rule);
            listener.onGameEnded(rule, timerRule, accomplishedRules);
        } else {
            accomplishedRules.add(rule);
        }
    }


    @Override
    public void firstUpdate() {
        //nothing ?
    }

    @Override
    public void onTimeProgress(long progress) {
        if (timerRule == null) return;
        //find if a rule is linked to the condition and test it
        eRuleResult result;
        result = timerRule.ruleProgress(progress);

        if (result != eRuleResult.NULL) {
            onRuleAccomplished(timerRule);
        }
    }

    @Override
    public void onMobEvent(eConditions reason, GameMob mob) {
        //dispatch event.
        switch (reason) {
            case MOB_DEATH:
                onMobDeath(mob);
                onMobCountDown(mob);
                break;
            case MOB_AWAY:
                onMobGetAway(mob);
                onMobCountDown(mob);
                break;
            case NEW_MOB:
                onNewMob(mob);
                break;
        }
    }

    @Override
    public void onMobCountChange(long count) {
        //find if a rule is linked to the condition and test it
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.MOB_COUNT);
        Rule rule;
        eRuleResult result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            result = rule.ruleProgress(count);

            if (result != eRuleResult.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

    public void onMobDeath(GameMob deadMob) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.MOB_DEATH);
        Rule rule;
        eRuleResult result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            if (! rule.ruleType.equals(String.class)) result = rule.ruleProgress(1);
            else result = rule.ruleProgress(deadMob.getIdName());
            if (result != eRuleResult.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

    public void onNewMob(GameMob deadMob) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.NEW_MOB);
        Rule rule;
        eRuleResult result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            if (! rule.ruleType.equals(String.class)) result = rule.ruleProgress(1);
            else result = rule.ruleProgress(deadMob.getIdName());
            if (result != eRuleResult.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }


    public void onMobGetAway(GameMob mobAway) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.MOB_AWAY);
        Rule rule;
        eRuleResult result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            if (! rule.ruleType.equals(String.class)) result = rule.ruleProgress(1);
            else result = rule.ruleProgress(mobAway.getIdName());
            if (result != eRuleResult.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

    public void onMobCountDown(GameMob mob) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.MOB_COUNTDOWN);
        Rule rule;
        eRuleResult result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            result = rule.ruleProgress(1);
            if (result != eRuleResult.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

    @Override
    public void onScoreChange(long add) {
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.SCORE);
        Rule rule;
        eRuleResult result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            result = rule.ruleProgress(add);
            if (result != eRuleResult.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

}
