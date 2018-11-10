package ch.FOW_Collection.presentation.profile.myratings;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.MainViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static androidx.lifecycle.Transformations.map;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.combineLatest;

public class MyRatingsViewModel extends MainViewModel {
    private static final String TAG = "MyRatingsViewModel";

    public LiveData<List<Pair<Rating, Wish>>> getMyRatingsWithWishes() {
        return getMyRatingsWithWishes(currentUserId, getMyWishlist());
    }

    public LiveData<List<Wish>> getMyWishlist() {
        return myWishlist;
    }

    public LiveData<List<Pair<Rating, Wish>>> getMyRatingsWithWishes(LiveData<String> currentUserId,
                                                                     LiveData<List<Wish>> myWishlist) {
        return map(combineLatest(getMyRatings(), myWishlist), input -> {
            List<Rating> ratings = input.first;

            // Optimization: also do this in a transformation
            List<Wish> wishes = input.second == null ? Collections.emptyList() : input.second;
            HashMap<String, Wish> wishesByItem = new HashMap<>();
            for (Wish wish : wishes) {
                wishesByItem.put(wish.getCardId(), wish);
            }

            ArrayList<Pair<Rating, Wish>> result = new ArrayList<>();
            for (Rating rating : ratings) {
                Wish wish = wishesByItem.get(rating.getCardId());
                result.add(Pair.create(rating, wish));
            }
            return result;
        });
    }
}