package ch.FOW_Collection.domain.models;

import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.Objects;

public class Like implements Entity {
    public static final String COLLECTION = "likes";
    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_RATING_ID = "ratingId";
    @Exclude

    private String id;
    private String userId;
    private String ratingId;
    private Date createdAt;

    public Like(String userId, String ratingId, Date createdAt) {
        this.userId = userId;
        this.ratingId = ratingId;
        this.createdAt = createdAt;
    }

    public Like() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Like like = (Like) o;
        return Objects.equals(id, like.id) && Objects.equals(userId, like.userId) &&
                Objects.equals(ratingId, like.ratingId) && Objects.equals(createdAt, like.createdAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, ratingId, createdAt);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String cardId) {
        this.id = cardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


}
