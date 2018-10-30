package ch.FOW_Collection.domain.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.lifecycle.LiveData;
import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating implements Entity, Parcelable {
    public static final String COLLECTION = "ratings";
    public static final String FIELD_CARD_ID = "cardId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_PHOTO = "photo";
    public static final String FIELD_RATING = "rating";
    public static final String FIELD_COMMENT = "comment";
    public static final String FIELD_LIKES = "likes";
    public static final String FIELD_CREATION_DATE = "creationDate";

    @Exclude
    private String id;

    private String cardId;
    @Exclude
    private LiveData<Card> card;

    private String userId;
    @Exclude
    private LiveData<User> user;

    private String photo;
    private float rating;
    private String comment;

    /**
     * We use a Map instead of an Array to be able to query it.
     *
     * @see <a href="https://firebase.google.com/docs/firestore/solutions/arrays#solution_a_map_of_values"/>
     */
    private Map<String, Boolean> likes;
    private Date creationDate;


    public static String generateId(String userId, String cardId) {
        return String.format("%s_%s", userId, cardId);
    }
    /************/


    protected Rating(Parcel in) {
        id = in.readString();
        cardId = in.readString();
        userId = in.readString();
        photo = in.readString();
        rating = in.readFloat();
        comment = in.readString();
        likes = in.readHashMap(Boolean.class.getClassLoader());
        creationDate = new Date(in.readLong());
    }

    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cardId);
        dest.writeString(userId);
        dest.writeString(photo);
        dest.writeFloat(rating);
        dest.writeString(comment);
        dest.writeMap(likes);
        dest.writeLong(creationDate.getTime());
    }
}
