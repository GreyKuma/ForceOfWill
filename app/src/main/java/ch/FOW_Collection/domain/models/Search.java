package ch.FOW_Collection.domain.models;

import com.google.firebase.firestore.Exclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Search implements Entity {
    public static final String COLLECTION = "searches";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_CREATION_DATE = "creationDate";

    @Exclude
    private String id;
    @NonNull
    private String userId;
    @NonNull
    private String term;

    private Date creationDate = new Date();
}
