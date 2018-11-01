package ch.FOW_Collection.data.parser;

import android.util.Log;
import androidx.annotation.NonNull;
import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.data.repositories.RatingsRepository;
import ch.FOW_Collection.data.repositories.UserRepository;
import ch.FOW_Collection.data.repositories.WishlistRepository;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserClassSnapshotParser extends EntityClassSnapshotParser<User> {
    public static final String TAG = "UserClassSnapshParser";

    public UserClassSnapshotParser() {
        super(User.class);
    }

    @NonNull
    @Override
    public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {
        return parseUser(super.parseSnapshot(snapshot));
    }

    public User parseUser(User user) {
        Log.d(TAG, "Parsing started for \"" + user.getId() + "\"");

        RatingsRepository ratingsRepository = new RatingsRepository();
        user.setRatings(ratingsRepository.getRatingsByUserId(user.getId()));

        WishlistRepository wishlistRepository = new WishlistRepository();
        user.setWishlist(wishlistRepository.getWishlistByUserId(user.getId()));
        
        return user;
    }

    public Map<String, Object> parseMap(Rating rating) {
        Map<String, Object> ret = new HashMap<>();

        ret.put(Rating.FIELD_CARD_ID, rating.getCardId());
        ret.put(Rating.FIELD_USER_ID, rating.getUserId());

        if (rating.getPhoto() != null && !rating.getPhoto().equals("")) {
            ret.put(Rating.FIELD_PHOTO, rating.getPhoto());
        }

        ret.put(Rating.FIELD_RATING, rating.getRating());
        ret.put(Rating.FIELD_COMMENT, rating.getComment());
        ret.put(Rating.FIELD_LIKES, rating.getLikes());
        ret.put(Rating.FIELD_CREATION_DATE, rating.getCreationDate());

        return ret;
    }
}
