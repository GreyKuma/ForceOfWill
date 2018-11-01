package ch.FOW_Collection.domain.models;

import androidx.lifecycle.LiveData;
import com.google.firebase.firestore.Exclude;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class User implements Entity {
    public static final String COLLECTION = "users";
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PHOTO = "photo";

    @Exclude
    private String id;
    private String name;
    private String photo;

    @Exclude
    private LiveData<List<Rating>> ratings;

    @Exclude
    private LiveData<List<Wish>> wishlist;

    @Exclude
    private LiveData<List<MyCard>> myCards;
}
