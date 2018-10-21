package ch.FOW_Collection.domain.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.domain.utils.MultiLanguageString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card implements Entity, Serializable {
    public static final String COLLECTION = "Cards";

    @Exclude
    private String id;
    private int idNumeric;

    private MultiLanguageString name;
    private MultiLanguageString flavor;

    private Integer atk;
    private Integer def;

    private String rarity;

    private Integer editionId;
    private CardEdition edition;

    private List<Integer> typeIds;
    private List<CardType> types;

    private List<Integer> raceIds;
    private List<Integer> attributeIds;

    // private List<CardAbilityPropEntry> ability;
    // private List<CardCostProp> cost;

    private String imageStorageUrl;

    public static final String FIELD_RATING = "avgRating";
    private float avgRating;
    private int numRatings;
}
