package ch.FOW_Collection.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

import androidx.lifecycle.LiveData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardAbility implements Entity, Serializable, Parcelable {
    @Exclude
    private String id;

    private Integer typeId;

    @Exclude
    private LiveData<CardAbilityType> type;

    List<CardCost> cost;
    private MultiLanguageString value;

    /*******************************************/

    protected CardAbility(Parcel in) {
        id = in.readString();
        if (in.readByte() == 0) {
            typeId = null;
        } else {
            typeId = in.readInt();
            typeId = typeId == -1 ? null : typeId;
        }
        cost = in.readArrayList(CardCost.class.getClassLoader());
        value = in.readParcelable(MultiLanguageString.class.getClassLoader());
    }

    public static final Creator<CardAbility> CREATOR = new Creator<CardAbility>() {
        @Override
        public CardAbility createFromParcel(Parcel in) {
            return new CardAbility(in);
        }

        @Override
        public CardAbility[] newArray(int size) {
            return new CardAbility[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(typeId != null ? typeId : -1);
        dest.writeArray(cost != null ? cost.toArray() : null);
        dest.writeParcelable(value, 0);
    }
}
