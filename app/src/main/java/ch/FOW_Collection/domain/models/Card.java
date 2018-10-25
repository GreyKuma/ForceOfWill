package ch.FOW_Collection.domain.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
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
    private String idStr;

    private MultiLanguageString name;
    private MultiLanguageString flavor;

    private Integer atk;
    private Integer def;

    private String rarity;

    private Integer editionId;
    @Exclude
    private FirestoreQueryLiveData<CardEdition> edition;

    private List<Integer> typeIds;
    @Exclude
    private List<CardType> types;

    private List<Integer> raceIds;
    @Exclude
    private List<CardRace> races;

    private List<Integer> attributeIds;
    @Exclude
    private List<CardAttribute> attributes;

    private List<CardAbility> ability;
    private List<CardCost> cost;

    private String imageStorageUrl;
    private String imageSrcUrl;

    public static final String FIELD_RATING = "avgRating";
    private float avgRating;
    private int numRatings;



}
