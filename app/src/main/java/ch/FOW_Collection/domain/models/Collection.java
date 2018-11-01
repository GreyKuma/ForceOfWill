package ch.FOW_Collection.domain.models;


import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
//@RequiredArgsConstructor

public class Collection {
    public static final String FIRST_COLLECTION = User.COLLECTION;
    public static final String SECOND_COLLECTION = "collection";
    public static final String FIELD_CARD_ID = "cardID";

    @Exclude
    private String id;



}
