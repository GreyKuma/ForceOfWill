package ch.FOW_Collection.domain.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

import ch.FOW_Collection.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CardAttribute extends MultiLanguageString implements Entity, Serializable {
    public static final String COLLECTION = "CardAttribute";

    @Exclude
    private String id;

    @Exclude
    public int getDrawableId() {
        return getDrawableIdByDe(this.getDe());
    }
    private static int getDrawableIdByDe(String de) {
        switch (de) {
            case "Licht": return R.drawable.ic_card_attribute_light;
            case "Finsternis": return R.drawable.ic_card_attribute_shadow;
            case "Feuer": return R.drawable.ic_card_attribute_fire;
            case "Wasser": return R.drawable.ic_card_attribute_water;
            case "Wind": return R.drawable.ic_card_attribute_wind;
            default: return 0;
        }
    }
}
