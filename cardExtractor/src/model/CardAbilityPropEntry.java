package model;

import java.io.Serializable;
import java.util.List;

public class CardAbilityPropEntry implements Serializable {
    public Integer typeId;
    public List<CardCostProp> cost;
    public MultiLanguageString value;
}
