package ch.FOW_Collection.presentation.profile.mywishlist;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.MainViewModel;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class WishlistViewModel extends MainViewModel {
    private static final String TAG = "WishlistViewModel";

    public LiveData<List<Pair<Wish, Card>>> getMyWishlistWithCards() {
        return wishlistRepository.getMyWishlistWithCards(currentUserId, cardsRepository.getAllCards());
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUserId().getValue(), itemId);
    }
}