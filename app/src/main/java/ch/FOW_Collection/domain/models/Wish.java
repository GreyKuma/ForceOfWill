package ch.FOW_Collection.domain.models;

import androidx.lifecycle.LiveData;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@IgnoreExtraProperties
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Wish implements Entity {

    public static final String COLLECTION = "wishes";
    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_CARD_ID = "id";
    public static final String FIELD_ADDED_AT = "addedAt";

    /**
     * The id is formed by `$userId_$id` to make queries easier.
     */
    @Exclude
    private String id;
    @NonNull
    private String userId;
    @Exclude
    private LiveData<User> user;
    @NonNull
    private String cardId;
    @Exclude
    private LiveData<Card> card;
    @NonNull
    private Date addedAt;

    public static String generateId(String userId, String cardId) {
        return String.format("%s_%s", userId, cardId);
    }
}
