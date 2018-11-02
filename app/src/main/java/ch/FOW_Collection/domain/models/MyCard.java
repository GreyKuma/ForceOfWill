package ch.FOW_Collection.domain.models;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@IgnoreExtraProperties
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class MyCard implements Entity {
    public static final String COLLECTION = "collection";
    public static final String FIELD_ID = "id";
    public static final String FIELD_CARD_ID = "id";
    public static final String FIELD_AMOUNT_NORMAL = "amountNormal";
    public static final String FIELD_AMOUNT_FOIL = "amountFoil";

    @Exclude
    private String id;
    @NonNull
    private String cardId;
    @Exclude
    private LiveData<Card> card;

    private int amountNormal;

    private int amountFoil;

    public int totalAmount(){return amountNormal+amountFoil;}

}

