package ch.FOW_Collection.domain.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.CardClassSnapshotParser;
import com.google.firebase.firestore.Exclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card implements Entity, Parcelable { // Serializable
    public static final String COLLECTION = "Cards";
    public static final String CARD_ID = "id";

    @Exclude
    private String id;
    private int idNumeric;
    private String idStr;

    private MultiLanguageString name;
    private MultiLanguageString flavor;

    private Integer atk;
    private Integer def;

    private String rarity;

    private Integer editionId;
    @Exclude
    private LiveData<CardEdition> edition;

    private List<Integer> typeIds;
    @Exclude
    private LiveData<List<CardType>> types;

    private List<Integer> raceIds;
    @Exclude
    private LiveData<List<CardRace>> races;

    private List<CardAbility> ability;
    private List<CardCost> cost;

    private String imageStorageUrl;
    private String imageSrcUrl;

    public static final String FIELD_RATING = "avgRating";
    private float avgRating;
    private int numRatings;

    @Exclude
    private LiveData<List<Rating>> ratings;

    @Exclude
    private LiveData<List<Wish>> wishes;

    /*****************************************************/

    protected Card(Parcel in) {
        id = in.readString();
        idStr = in.readString();
        idNumeric = in.readInt();
        rarity = in.readString();
        imageStorageUrl = in.readString();
        imageSrcUrl = in.readString();
        avgRating = in.readFloat();
        numRatings = in.readInt();

        atk = in.readInt();
        atk = atk == -1 ? null : atk;

        def = in.readInt();
        def = def == -1 ? null : def;

        editionId = in.readInt();
        editionId = editionId == -1 ? null : editionId;

        name = in.readParcelable(MultiLanguageString.class.getClassLoader());
        flavor = in.readParcelable(MultiLanguageString.class.getClassLoader());
        typeIds = in.readArrayList(Integer.class.getClassLoader());
        raceIds = in.readArrayList(Integer.class.getClassLoader());
        //ability = in.readArrayList(CardAttribute.class.getClassLoader());
        ability = in.createTypedArrayList(CardAbility.CREATOR);
        //cost = in.readArrayList(CardCost.class.getClassLoader());
        cost = in.createTypedArrayList(CardCost.CREATOR);

        // and parse me please
        new CardClassSnapshotParser().parseCard(this);
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(idStr);
        dest.writeInt(idNumeric);
        dest.writeString(rarity);
        dest.writeString(imageStorageUrl);
        dest.writeString(imageSrcUrl);
        dest.writeFloat(avgRating);
        dest.writeInt(numRatings);
        dest.writeInt(atk != null && atk > 0 ? atk : -1);
        dest.writeInt(def != null && def > 0 ? def : -1);
        dest.writeInt(editionId != null ? editionId : -1);
        dest.writeParcelable(name, 0);
        dest.writeParcelable(flavor, 0);
        dest.writeList(typeIds);
        dest.writeList(raceIds);
        dest.writeTypedList(ability);
        dest.writeTypedList(cost);
    }
}
