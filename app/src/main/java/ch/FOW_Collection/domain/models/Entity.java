package ch.FOW_Collection.domain.models;

import java.util.HashMap;
import java.util.List;

public interface Entity {

    String getId();

    void setId(String id);

    static <T extends Entity> HashMap<String, T> entitiesById(List<T> entries) {
        HashMap<String, T> byId = new HashMap<>();
        for (T entry : entries) {
            byId.put(entry.getId(), entry);
        }
        return byId;
    }
}
