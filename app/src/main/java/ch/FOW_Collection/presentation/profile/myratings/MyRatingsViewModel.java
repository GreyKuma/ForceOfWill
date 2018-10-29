package ch.FOW_Collection.presentation.profile.myratings;

import android.util.Pair;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.repositories.CurrentUser;
import ch.FOW_Collection.data.repositories.RatingsRepository;
import ch.FOW_Collection.data.repositories.WishlistRepository;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.Wish;

public class MyRatingsViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "MyBeersViewModel";

    private final LiveData<List<Rating>> myRatings;
    private final LiveData<List<Wish>> myWishlist;
    private final WishlistRepository wishlistRepository;
    private final RatingsRepository ratingsRepository;
    private final MutableLiveData<String> currentUserId;

    public MyRatingsViewModel() {
        wishlistRepository = new WishlistRepository();
        ratingsRepository = new RatingsRepository();
        currentUserId = new MutableLiveData<>();
        myWishlist = wishlistRepository.getMyWishlist(currentUserId);
        myRatings = ratingsRepository.getMyRatings(currentUserId);

        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Rating>> getMyRatings() {
        return myRatings;
    }

    public void toggleItemInWishlist(String beerId) {
        wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), beerId);
    }

    public LiveData<List<Pair<Rating, Wish>>> getMyRatingsWithWishes() {
        return ratingsRepository.getMyRatingsWithWishes(currentUserId, getMyWishlist());
    }

    public LiveData<List<Wish>> getMyWishlist() {
        return myWishlist;
    }
}