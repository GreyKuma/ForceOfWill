package ch.FOW_Collection.domain.models;

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
public class MyCard implements Entity {
    public static final String COLLECTION = "collection";
    public static final String FIELD_ID = "id";
    public static final String FIELD_CARD_ID = "id";

    @Exclude
    private String id;
    @NonNull
    private String cardId;
    @Exclude
    private LiveData<Card> card;

}

