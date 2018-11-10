package ch.FOW_Collection.domain.models;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiLanguageString implements Parcelable {
    private String de;
    private String en;

    protected MultiLanguageString(Parcel in) {
        de = in.readString();
        en = in.readString();
    }

    public static final Creator<MultiLanguageString> CREATOR = new Creator<MultiLanguageString>() {
        @Override
        public MultiLanguageString createFromParcel(Parcel in) {
            return new MultiLanguageString(in);
        }

        @Override
        public MultiLanguageString[] newArray(int size) {
            return new MultiLanguageString[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof MultiLanguageString))
            return false;
        if (obj == this)
            return true;
        //TODO catch if names are null
        if (this.de != null && ((MultiLanguageString) obj).de != null &&
                this.en != null && ((MultiLanguageString) obj).en != null) {
            return this.de.equals(((MultiLanguageString) obj).de) && this.en.equals(((MultiLanguageString) obj).en);
        } else if (this.de != null && ((MultiLanguageString) obj).de != null) {
            return this.de.equals(((MultiLanguageString) obj).de);
        } else if (this.en != null && ((MultiLanguageString) obj).en != null) {
            return this.en.equals(((MultiLanguageString) obj).en);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (de + en).hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(de);
        dest.writeString(en);
    }
}
