package ch.FOW_Collection.data.parser;

import android.util.Log;

import com.firebase.ui.firestore.ClassSnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import ch.FOW_Collection.data.repositories.CardAbilityTypeRepository;
import ch.FOW_Collection.data.repositories.CardAttributeRepository;
import ch.FOW_Collection.data.repositories.CardEditionsRepository;
import ch.FOW_Collection.data.repositories.CardRaceRepository;
import ch.FOW_Collection.data.repositories.CardTypeRepository;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.CardAbility;
import ch.FOW_Collection.domain.models.CardAbilityType;
import ch.FOW_Collection.domain.models.CardAttribute;
import ch.FOW_Collection.domain.models.CardCost;
import ch.FOW_Collection.domain.models.CardEdition;
import ch.FOW_Collection.domain.models.CardRace;
import ch.FOW_Collection.domain.models.CardType;

public class CardClassSnapshotParser extends ClassSnapshotParser<Card> {
    public static final String TAG = "CardClassSnapshotParser";

    public CardClassSnapshotParser() {
        super(Card.class);
    }

    @NonNull
    @Override
    public Card parseSnapshot(@NonNull DocumentSnapshot snapshot) {
        Card card = super.parseSnapshot(snapshot);
        card.setId(snapshot.getId());

        Log.d(TAG, "Parsing started for \"" + card.getId() + "\"");

        // we just create new Repositories because we knew, its static behind and we
        //   are on the same layerLevel
        if (card.getEditionId() != null) {
            CardEditionsRepository cardEditionsRepository = new CardEditionsRepository();
            card.setEdition(new FirestoreQueryLiveData<CardEdition>(
                    cardEditionsRepository.cardEditionByIdQuery(card.getEditionId()),
                    new EntityClassSnapshotParser<>(CardEdition.class)
            ));
                    /*
            // query CardEdition
            cardEditionsRepository.cardEditionByIdQuery(card.getEditionId()).get().addOnSuccessListener(doc -> {
                // set up a parser
                EntityClassSnapshotParser parser = new EntityClassSnapshotParser(CardEdition.class);
                // parse result and set it
                CardEdition cardEdition = (CardEdition) parser.parseSnapshot(doc);
                card.setEdition(cardEdition);

                Log.d(TAG, "Parsed \"CardEdition\" for \"" + card.getId() + "\": " + cardEdition.getDe());
            });*/
        }

        if (card.getRaceIds() != null) {
            CardRaceRepository cardRaceRepository = new CardRaceRepository();
            List<CardRace> cardRaces = new ArrayList<>();

            // set up a parser
            EntityClassSnapshotParser parser = new EntityClassSnapshotParser(CardRace.class);

            Iterator<Integer> it = card.getRaceIds().iterator();
            while(it.hasNext()) {
                // query CardEdition
                cardRaceRepository.getCardRaceByIdQuery(it.next()).get().addOnSuccessListener(doc -> {
                    // parse result and add it
                    CardRace cardRace = (CardRace) parser.parseSnapshot(doc);
                    cardRaces.add(cardRace);

                    Log.d(TAG, "Parsed \"CardRace\" for \"" + card.getId() + "\": " + cardRace.getDe());
                });
            }
            // set it
            card.setRaces(cardRaces);
        }

        if (card.getAttributeIds() != null) {
            CardAttributeRepository cardAttributeRepository = new CardAttributeRepository();
            List<CardAttribute> cardAttributes = new ArrayList<>();

            // set up a parser
            EntityClassSnapshotParser parser = new EntityClassSnapshotParser(CardRace.class);

            Iterator<Integer> it = card.getAttributeIds().iterator();
            while(it.hasNext()) {
                // query CardAttribute
                cardAttributeRepository.getCardAttributeByIdQuery(it.next()).get().addOnSuccessListener(doc -> {
                    // parse result and add it
                    CardAttribute cardAttribute = (CardAttribute) parser.parseSnapshot(doc);
                    cardAttributes.add(cardAttribute);

                    Log.d(TAG, "Parsed \"CardAttribute\" for \"" + card.getId() + "\": " + cardAttribute.getDe());
                });
            }
            // set it
            card.setAttributes(cardAttributes);
        }

        CardTypeRepository cardTypeRepository = new CardTypeRepository();
        // set up a parser
        EntityClassSnapshotParser cardTypeParser = new EntityClassSnapshotParser(CardType.class);

        if (card.getTypeIds() != null) {
            List<CardType> cardTypes = new ArrayList<>();

            Iterator<Integer> it = card.getTypeIds().iterator();
            while(it.hasNext()) {
                // query CardType
                cardTypeRepository.getCardTypeByIdQuery(it.next()).get().addOnSuccessListener(doc -> {
                    // parse result and add it
                    CardType cardType = (CardType) cardTypeParser.parseSnapshot(doc);
                    cardTypes.add(cardType);

                    Log.d(TAG, "Parsed \"CardType\" for \"" + card.getId() + "\": " + cardType.getDe());
                });
            }
            // set it
            card.setTypes(cardTypes);
        }

        if (card.getCost() != null) {
            Iterator<CardCost> it = card.getCost().iterator();
            while(it.hasNext()) {
                CardCost current = it.next();
                if (current.getTypeId() != null) {
                    // query CardType
                    cardTypeRepository.getCardTypeByIdQuery(current.getTypeId()).get().addOnSuccessListener(doc -> {
                        // parse result and set it
                        CardType cardType = (CardType) cardTypeParser.parseSnapshot(doc);
                        current.setType(cardType);

                        Log.d(TAG, "Parsed \"CardCostType\" for \"" + card.getId() + "\": " + cardType.getDe());
                    });
                }
            }
        }

        if (card.getAbility() != null) {
            CardAbilityTypeRepository cardAbilityTypeRepository = new CardAbilityTypeRepository();
            // set up a parser
            EntityClassSnapshotParser cardAbilityTypeParser = new EntityClassSnapshotParser(CardAbilityType.class);

            Iterator<CardAbility> it = card.getAbility().iterator();
            while(it.hasNext()) {
                CardAbility current = it.next();
                if (current.getTypeId() != null) {
                    cardAbilityTypeRepository.getCardAbilityTypeByIdQuery(current.getTypeId()).get().addOnSuccessListener(doc -> {
                        // parse result and set it
                        CardAbilityType cardAbilityType = (CardAbilityType) cardAbilityTypeParser.parseSnapshot(doc);
                        current.setType(cardAbilityType);

                        Log.d(TAG, "Parsed \"CardAbilityType\" for \"" + card.getId() + "\": " + cardAbilityType.getDe());
                    });

                    if (current.getCost() != null) {
                        Iterator<CardCost> itCost = current.getCost().iterator();
                        while(it.hasNext()) {
                            CardCost currentCost = itCost.next();
                            if (currentCost != null && currentCost.getTypeId() != null) {
                                // query CardType
                                cardTypeRepository.getCardTypeByIdQuery(current.getTypeId()).get().addOnSuccessListener(doc -> {
                                    // parse result and set it
                                    CardType cardType = (CardType) cardTypeParser.parseSnapshot(doc);
                                    currentCost.setType(cardType);

                                    Log.d(TAG, "Parsed \"CardAbilityCostType\" for \"" + card.getId() + "\": " + cardType.getDe());
                                });
                            }
                        }
                    }
                }
            }
        }

        return card;
    }
}