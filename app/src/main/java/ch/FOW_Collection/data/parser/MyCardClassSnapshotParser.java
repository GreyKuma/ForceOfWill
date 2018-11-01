package ch.FOW_Collection.data.parser;

import android.util.Log;
import androidx.annotation.NonNull;
import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.domain.models.MyCard;
import com.google.firebase.firestore.DocumentSnapshot;

public class MyCardClassSnapshotParser extends EntityClassSnapshotParser<MyCard>{
    public static final String TAG = "WishClassSnapshotParser";

    public MyCardClassSnapshotParser() {
        super(MyCard.class);
    }

    @NonNull
    @Override
    public MyCard parseSnapshot(@NonNull DocumentSnapshot snapshot) {
        MyCard myCard = super.parseSnapshot(snapshot);
        return parseMyCard(myCard);
    }

    public MyCard parseMyCard(MyCard myCard) {
        Log.d(TAG, "Parsing started for \"" + myCard.getId() + "\"");

        if (myCard.getCardId() != null){
            CardsRepository cardsRepository = new CardsRepository();
            myCard.setCard(cardsRepository.getCardById(myCard.getCardId()));
        }
        return myCard;
    }
}
