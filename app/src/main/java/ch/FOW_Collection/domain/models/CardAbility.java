package ch.FOW_Collection.domain.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardAbility implements Entity, Serializable {
    @Exclude
    private String id;

    private Integer typeId;

    @Exclude
    private CardAbilityType type;
    List<CardCost> cost;
    private MultiLanguageString value;
}
