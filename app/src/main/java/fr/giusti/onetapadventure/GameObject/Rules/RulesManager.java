package fr.giusti.onetapadventure.gameObject.rules;

import java.util.ArrayList;
import java.util.HashMap;

import fr.giusti.onetapadventure.callback.OnBoardEventListener;
import fr.giusti.onetapadventure.callback.OnRuleAccomplishedListener;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;

/**
 * Created by jérémy on 08/09/2016.
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

        if(timerRule.getIdName().equals(ruleName)){
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
        } else if (rule.type == eConditionType.END) {
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
    public void onTimeProgress(int progress) {
        if (timerRule == null) return;
        //find if a rule is linked to the condition and test it
        eConditionType result;
        result = timerRule.ruleProgress(progress);

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

        //find if a rule is linked to the condition and test it
        ArrayList<Rule> conditionRule = indexedRuleList.get(eConditions.MOB_COUNT);
        Rule rule;
        eConditionType result;
        for (int i = 0; i < conditionRule.size(); i++) {
            rule = conditionRule.get(i);
            result = rule.ruleProgress(count);

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
            if (rule.isNumericalCondition) result = rule.ruleProgress(1);
            else result = rule.ruleProgress(deadMob.getIdName());
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
            if (rule.isNumericalCondition) result = rule.ruleProgress(1);
            else result = rule.ruleProgress(deadMob.getIdName());
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
            if (rule.isNumericalCondition) result = rule.ruleProgress(1);
            else result = rule.ruleProgress(mobAway.getIdName());
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
            result = rule.ruleProgress(-1);
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
            result = rule.ruleProgress(add);
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
            result = rule.ruleProgress(remove);
            if (result != eConditionType.NULL) {
                conditionRule.remove(rule);
                i--;
                onRuleAccomplished(rule);
            }
        }
    }

}
