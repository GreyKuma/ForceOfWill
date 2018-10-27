package ch.FOW_Collection.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.CardClassSnapshotParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card implements Entity, Serializable, Parcelable {
    public static final String COLLECTION = "Cards";

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

    private List<Integer> attributeIds;
    @Exclude
    private LiveData<List<CardAttribute>> attributes;

    private List<CardAbility> ability;
    private List<CardCost> cost;

    private String imageStorageUrl;
    private String imageSrcUrl;

    public static final String FIELD_RATING = "avgRating";
    private float avgRating;
    private int numRatings;

    /*****************************************************/

    protected Card(Parcel in) {
        id = in.readString();
        idNumeric = in.readInt();
        idStr = in.readString();
        name = in.readParcelable(MultiLanguageString.class.getClassLoader());
        flavor = in.readParcelable(MultiLanguageString.class.getClassLoader());
        if (in.readByte() == 0) {
            atk = null;
        } else {
            atk = in.readInt();
            atk = atk == -1 ? null : atk;
        }
        if (in.readByte() == 0) {
            def = null;
        } else {
            def = in.readInt();
            def = def == -1 ? null : def;
        }
        rarity = in.readString();
        if (in.readByte() == 0) {
            editionId = null;
        } else {
            editionId = in.readInt();
            editionId = editionId == -1 ? null : editionId;
        }

        typeIds = in.readArrayList(Integer.class.getClassLoader());
        raceIds = in.readArrayList(Integer.class.getClassLoader());
        attributeIds = in.readArrayList(Integer.class.getClassLoader());
        ability = in.readArrayList(CardAttribute.class.getClassLoader());
        cost = in.readArrayList(CardCost.class.getClassLoader());

        imageStorageUrl = in.readString();
        imageSrcUrl = in.readString();
        avgRating = in.readFloat();
        numRatings = in.readInt();

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
        dest.writeParcelable(name, 0);
        dest.writeParcelable(flavor, 0);
        dest.writeInt(atk != null ? atk : -1);
        dest.writeInt(def != null ? def : -1);
        dest.writeString(rarity);
        dest.writeInt(editionId != null ? editionId : -1);
        dest.writeArray(typeIds != null ? typeIds.toArray() : null);
        dest.writeArray(raceIds != null ? raceIds.toArray() : null);
        dest.writeArray(attributeIds != null ? attributeIds.toArray() : null);
        dest.writeArray(ability != null ? ability.toArray() : null);
        dest.writeArray(cost != null ? cost.toArray() : null);
        dest.writeString(imageStorageUrl);
        dest.writeString(imageSrcUrl);
        dest.writeFloat(avgRating);
        dest.writeInt(numRatings);
    }
}
