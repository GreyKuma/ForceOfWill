package ch.FOW_Collection.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiLanguageString {
    private String de;
    private String en;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof MultiLanguageString))
            return false;
        if (obj == this)
            return true;
        return this.de.equals(((MultiLanguageString)obj).de) && this.en.equals(((MultiLanguageString)obj).en);
    }

    @Override
    public int hashCode() {
        return (de+en).hashCode();
    }
}
