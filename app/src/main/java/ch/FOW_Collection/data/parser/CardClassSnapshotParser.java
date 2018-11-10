package ch.FOW_Collection.data.parser;

import android.util.Log;
import androidx.annotation.NonNull;
import ch.FOW_Collection.data.repositories.*;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.CardAbility;
import ch.FOW_Collection.domain.models.CardCost;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Iterator;

public class CardClassSnapshotParser extends EntityClassSnapshotParser<Card> {
    public static final String TAG = "CardClassSnapshotParser";

    public CardClassSnapshotParser() {
        super(Card.class);
    }

    @NonNull
    @Override
    public Card parseSnapshot(@NonNull DocumentSnapshot snapshot) {
        return parseCard(super.parseSnapshot(snapshot));
    }

    public Card parseCard(Card card) {
        Log.d(TAG, "Parsing started for \"" + card.getId() + "\"");

        // we just create new Repositories because we knew, its static behind and we
        //   are on the same layerLevel
        if (card.getEditionId() != null) {
            CardEditionsRepository cardEditionsRepository = new CardEditionsRepository();
            card.setEdition(cardEditionsRepository.getEditionById(card.getEditionId()));
        }

        if (card.getRaceIds() != null) {
            CardRaceRepository cardRaceRepository = new CardRaceRepository();
            card.setRaces(cardRaceRepository.getRacesByIds(card.getRaceIds()));
        }

        if (card.getTypeIds() != null) {
            CardTypeRepository cardTypeRepository = new CardTypeRepository();
            card.setTypes(cardTypeRepository.getCardTypesByIds(card.getTypeIds()));
        }

        CardAttributeRepository cardAttributeRepository = new CardAttributeRepository();
        if (card.getCost() != null) {
            Iterator<CardCost> it = card.getCost().iterator();
            while (it.hasNext()) {
                CardCost current = it.next();
                if (current.getTypeId() != null) {
                    current.setType(cardAttributeRepository.getCardAttributeById(current.getTypeId()));
                }
            }
        }

        if (card.getAbility() != null) {
            CardAbilityTypeRepository cardAbilityTypeRepository = new CardAbilityTypeRepository();

            Iterator<CardAbility> it = card.getAbility().iterator();
            while (it.hasNext()) {
                CardAbility current = it.next();
                if (current.getTypeId() != null) {
                    current.setType(cardAbilityTypeRepository.getAbilityTypeById(current.getTypeId()));

                    if (current.getCost() != null) {
                        Iterator<CardCost> itCost = current.getCost().iterator();
                        while (it.hasNext()) {
                            CardCost currentCost = itCost.next();
                            if (currentCost != null && currentCost.getTypeId() != null) {
                                currentCost.setType(cardAttributeRepository.getCardAttributeById(currentCost.getTypeId()));
                            }
                        }
                    }
                }
            }
        }

        RatingsRepository ratingsRepository = new RatingsRepository();
        card.setRatings(ratingsRepository.getRatingsByCardId(card.getId()));

        return card;
    }
}