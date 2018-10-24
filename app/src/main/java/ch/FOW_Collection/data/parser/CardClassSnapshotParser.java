package ch.FOW_Collection.data.parser;

import android.util.Log;

import com.firebase.ui.firestore.ClassSnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import ch.FOW_Collection.data.repositories.CardAbilityTypeRepository;
import ch.FOW_Collection.data.repositories.CardAttributeRepository;
import ch.FOW_Collection.data.repositories.CardEditionsRepository;
import ch.FOW_Collection.data.repositories.CardRaceRepository;
import ch.FOW_Collection.data.repositories.CardTypeRepository;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.CardAbility;
import ch.FOW_Collection.domain.models.CardAttribute;
import ch.FOW_Collection.domain.models.CardCost;
import ch.FOW_Collection.domain.models.CardRace;
import ch.FOW_Collection.domain.models.CardType;

public class CardClassSnapshotParser extends ClassSnapshotParser<Card> {
    public CardClassSnapshotParser() {
        super(Card.class);
    }

    @NonNull
    @Override
    public Card parseSnapshot(@NonNull DocumentSnapshot snapshot) {
        Card card = super.parseSnapshot(snapshot);
        card.setId(snapshot.getId());

        // we just create new Repositories because we knew, its static behind and we
        //   are on the same layerLevel
        if (card.getEditionId() != null) {
            CardEditionsRepository cardEditionsRepository = new CardEditionsRepository();
            card.setEdition(cardEditionsRepository.getEditionById(card.getEditionId()).getValue());
        }

        if (card.getRaceIds() != null) {
            CardRaceRepository cardRaceRepository = new CardRaceRepository();
            List<CardRace> cardRaces = new ArrayList<>();

            Iterator<Integer> it = card.getRaceIds().iterator();
            while(it.hasNext()) {
                cardRaces.add(cardRaceRepository.getRaceById(it.next()).getValue());
            }

            card.setRaces(cardRaces);
        }

        if (card.getAttributeIds() != null) {
            CardAttributeRepository cardAttributeRepository = new CardAttributeRepository();
            List<CardAttribute> cardAttributes = new ArrayList<>();

            Iterator<Integer> it = card.getAttributeIds().iterator();
            while(it.hasNext()) {
                cardAttributes.add(cardAttributeRepository.getAttributeById(it.next()).getValue());
            }

            card.setAttributes(cardAttributes);
        }

        CardTypeRepository cardTypeRepository = new CardTypeRepository();
        if (card.getTypeIds() != null) {
            List<CardType> cardTypes = new ArrayList<>();

            Iterator<Integer> it = card.getTypeIds().iterator();
            while(it.hasNext()) {
                cardTypes.add(cardTypeRepository.getTypeById(it.next()).getValue());
            }

            card.setTypes(cardTypes);
        }

        if (card.getCost() != null) {
            Iterator<CardCost> it = card.getCost().iterator();
            while(it.hasNext()) {
                CardCost current = it.next();
                if (current != null && current.getTypeId() != null) {
                    current.setType(
                        cardTypeRepository.getTypeById(current.getTypeId()).getValue()
                    );
                }
            }
        }

        if (card.getAbility() != null) {
            CardAbilityTypeRepository cardAbilityTypeRepository = new CardAbilityTypeRepository();

            Iterator<CardAbility> it = card.getAbility().iterator();
            while(it.hasNext()) {
                CardAbility current = it.next();
                if (current != null && current.getTypeId() != null) {
                    current.setType(
                            cardAbilityTypeRepository.getAbilityTypeById(current.getTypeId()).getValue()
                    );

                    if (current.getCost() != null) {
                        Iterator<CardCost> itCost = current.getCost().iterator();
                        while(it.hasNext()) {
                            CardCost currentCost = itCost.next();
                            if (currentCost != null && currentCost.getTypeId() != null) {
                                currentCost.setType(
                                        cardTypeRepository.getTypeById(current.getTypeId()).getValue()
                                );
                            }
                        }
                    }
                }
            }
        }

        Log.d("CardClassParser", "Parsed \"" + card.getId() + "\"");

        return card;
    }
}