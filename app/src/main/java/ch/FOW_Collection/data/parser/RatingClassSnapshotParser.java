package ch.FOW_Collection.data.parser;

import android.util.Log;
import androidx.annotation.NonNull;
import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.data.repositories.UserRepository;
import ch.FOW_Collection.domain.models.Rating;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class RatingClassSnapshotParser extends EntityClassSnapshotParser<Rating> {
    public static final String TAG = "RatingClassSnapshParser";

    public RatingClassSnapshotParser() {
        super(Rating.class);
    }

    @NonNull
    @Override
    public Rating parseSnapshot(@NonNull DocumentSnapshot snapshot) {
        return parseRating(super.parseSnapshot(snapshot));
    }

    public Rating parseRating(Rating rating) {
        Log.d(TAG, "Parsing started for \"" + rating.getId() + "\"");

        if (rating.getCardId() != null) {
            CardsRepository cardsRepository = new CardsRepository();
            rating.setCard(cardsRepository.getCardById(rating.getCardId()));
        }

        if (rating.getUserId() != null) {
            UserRepository userRepository = new UserRepository();
            rating.setUser(userRepository.getUserById(rating.getUserId()));
        }

        return rating;
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
