package fr.giusti.onetapengine.entity.distribution;

/**
 * Created by jgiusti on 26/09/2016.
 */

/**
 * ALL_AT_ONCE all entity from list <br>
 * ONE_BY_ONE_ORDERED sequentially pick one entity from list <br>
 * ONE_BY_ONE_RANDOM one random entity from list <br>
 * GROUPED_ORDERED sequentially pick a group of entity from list <br>
 * GROUPED_RANDOM pick random entities from list<br>
 * GROUPED_SEMIRANDOM picks a group of following entity from an random index of the list<br>
 */
public enum eEntityDistributionMode {
    /**
     * release everything at once
     */
    ALL_AT_ONCE,
    /**
     * release one at a time starting from the beginning of the list
     */
    ONE_BY_ONE_ORDERED,
    /**
     * release groupSize at a time starting from the beginning of the list
     */
    GROUPED_ORDERED,
    /**
     * release groupSize at a chunk of the list which is a multiple of groupSize
     */
    GROUPED_SEMIRANDOM,
    /**
     * release groupSize picked from anywhere in the list (one by one)
     */
    GROUPED_RANDOM,
    /**
     *  release one at a time picked from anywhere in the list
     */
    ONE_BY_ONE_RANDOM;
}
