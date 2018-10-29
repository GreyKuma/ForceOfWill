package ch.FOW_Collection.data.parser;

import android.util.Log;
import androidx.annotation.NonNull;
import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Entity;
import ch.FOW_Collection.domain.models.Wish;
import com.firebase.ui.firestore.ClassSnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;

public class WishClassSnapshotParser extends EntityClassSnapshotParser<Wish> {
    public static final String TAG = "WishClassSnapshotParser";

    public WishClassSnapshotParser() {
        super(Wish.class);
    }

    @NonNull
    @Override
    public Wish parseSnapshot(@NonNull DocumentSnapshot snapshot) {
        Wish wish = super.parseSnapshot(snapshot);

        return parseWish(wish);
    }

    public Wish parseWish(Wish wish) {
        Log.d(TAG, "Parsing started for \"" + wish.getId() + "\"");

        if (wish.getCardId() != null) {
            CardsRepository cardsRepository = new CardsRepository();
            wish.setCard(cardsRepository.getCardById(wish.getCardId()));
        }

        return wish;
    }
}
