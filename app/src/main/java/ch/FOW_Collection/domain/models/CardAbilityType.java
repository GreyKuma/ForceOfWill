package ch.FOW_Collection.domain.models;

import com.google.firebase.firestore.Exclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CardAbilityType extends MultiLanguageString implements Entity, Serializable {
    public static final String COLLECTION = "CardAbilityType";

    @Exclude
    private String id;
}
