package ch.FOW_Collection.presentation.profile.mywishlist;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.repositories.BeersRepository;
import ch.FOW_Collection.data.repositories.CurrentUser;
import ch.FOW_Collection.data.repositories.WishlistRepository;
import ch.FOW_Collection.domain.models.Beer;
import ch.FOW_Collection.domain.models.Wish;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class WishlistViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "WishlistViewModel";

    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final WishlistRepository wishlistRepository;
    private final BeersRepository beersRepository;

    public WishlistViewModel() {
        wishlistRepository = new WishlistRepository();
        beersRepository = new BeersRepository();

        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Pair<Wish, Beer>>> getMyWishlistWithBeers() {
        return wishlistRepository.getMyWishlistWithBeers(currentUserId, beersRepository.getAllBeers());
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }

}