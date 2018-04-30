package fr.giusti.onetapengine.entity.distribution;

import android.os.AsyncTask;

import java.util.ArrayList;

import fr.giusti.onetapengine.callback.SpawnerListener;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by giusti on 24/02/2018.<br>
 * this can be used to follow events of two different type (such as timer:long and mobCount:int)<br><br>
 * implementation:<br>
 * <pre class="prettyprint">
 * private class mobCountTimerSpawner extends MultiTypeEntitySpawner&lt;Long, Integer&gt; {
 *
 *     {@literal @}Override
 *     public ArrayList<Entity> onConditionProgress(Long cdtProgress,eConditions conditionType) ;
 *         mConditionProgress += cdtProgress;
 *         return null;
 *     }
 *
 * //   /!\ this second type of onConditionProgress absoltely need to be explicitely implemented
 * //  also only one of the 2 onConditionProgress need to be able to send result
 * //  otherwise the result may be sent 2 time on the same tick
 *
 *     public ArrayList<Entity> onConditionProgress(Int cdtProgress,eConditions conditionType) ;
 *         conditionProgress2 += cdtProgress2;
 *
 *         if(areConditionMet()){
 *          resetProgress()
 *          return onConditionMet();
 *         }else{
 *          return null;
 *         }
 *     }
 * }
 * </pre>
 */

public abstract class MultiTypeEntitySpawner<T,K> extends EntitySpawner<T> {
    /**
     * the condition mGoal if the mProgress match this value, a entity spawn will be triggered
     * and reset mProgress to initialValue
     */
    protected final K conditionGoalValue2;
    /**
     * the initial mProgress
     */
    protected final K initialProgressValue2;
    protected K conditionProgress2 ;


    public MultiTypeEntitySpawner(eConditions[] conditionTypes, eEntityDistributionMode distibutionMode, T conditionGoalValue, T initialProgressValue, K conditionGoalValue2, K initialProgressValue2) {
        super(conditionTypes, distibutionMode, conditionGoalValue, initialProgressValue);
        this.conditionGoalValue2=conditionGoalValue2;
        this.initialProgressValue2=initialProgressValue2;
        this.conditionProgress2 = initialProgressValue2;
    }
}
