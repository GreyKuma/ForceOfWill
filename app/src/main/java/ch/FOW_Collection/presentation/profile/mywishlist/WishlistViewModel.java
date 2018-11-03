package ch.FOW_Collection.presentation.profile.mywishlist;

import android.util.Pair;

import ch.FOW_Collection.presentation.MainViewModel;
import com.google.android.gms.tasks.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.data.repositories.CurrentUser;
import ch.FOW_Collection.data.repositories.WishlistRepository;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Wish;

public class WishlistViewModel extends MainViewModel {
    private static final String TAG = "WishlistViewModel";

    public LiveData<List<Pair<Wish, Card>>> getMyWishlistWithCards() {
        return wishlistRepository.getMyWishlistWithCards(currentUserId, cardsRepository.getAllCards());
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUserId().getValue(), itemId);
    }
}