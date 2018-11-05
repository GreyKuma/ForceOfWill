package ch.FOW_Collection.domain.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.lifecycle.LiveData;
import com.google.firebase.firestore.Exclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCost implements Serializable, Parcelable {
    Integer typeId;

    @Exclude
    LiveData<CardAttribute> type;

    Integer count;

    /*********************************/

    protected CardCost(Parcel in) {
        typeId = in.readInt();
        typeId = typeId == -1 ? null : typeId;

        count = in.readInt();
        count = count == -1 ? null : count;
    }

    public static final Creator<CardCost> CREATOR = new Creator<CardCost>() {
        @Override
        public CardCost createFromParcel(Parcel in) {
            return new CardCost(in);
        }

        @Override
        public CardCost[] newArray(int size) {
            return new CardCost[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(typeId != null ? typeId : -1);
        dest.writeInt(count != null ? count : -1);
    }
}
