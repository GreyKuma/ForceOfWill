package ch.FOW_Collection.domain.models;

import com.google.firebase.database.Exclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyCard implements Entity, Serializable {
    public static final String FIELD_ID = "id";

    @Exclude
    private String id;

}

