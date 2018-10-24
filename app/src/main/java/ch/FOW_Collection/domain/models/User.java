package ch.FOW_Collection.domain.models;

import com.google.firebase.firestore.Exclude;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
