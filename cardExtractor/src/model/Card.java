package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Card implements Serializable {
    public int idNumeric;
    public String idStr;

    public MultiLanguageString name;
    public MultiLanguageString flavor;

    public List<CardAbilityPropEntry> ability;

    public String rarity;
    public String imageSrcUrl;
    public String imageStorageUrl;

    public List<CardCostProp> cost;
    public Integer atk;
    public Integer def;

    public Integer editionId;

    public List<Integer> typeIds;
    public List<Integer> raceIds;
    public List<Integer> attributeIds;

    public float avgRating = (float) 0.0;

    public Card() {
        /*
        name = new MultiLanguageString();
        flavor = new MultiLanguageString();
        typeIds = new ArrayList<>();
        raceIds = new ArrayList<>();
        attributeIds = new ArrayList<>();*/
    }
}
