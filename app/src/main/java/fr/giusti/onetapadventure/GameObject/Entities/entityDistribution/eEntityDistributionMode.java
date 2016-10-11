package fr.giusti.onetapadventure.gameObject.entities.entityDistribution;

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
    ALL_AT_ONCE,
    ONE_BY_ONE_ORDERED,
    GROUPED_ORDERED,
    GROUPED_SEMIRANDOM,
    GROUPED_RANDOM,
    ONE_BY_ONE_RANDOM;
}
