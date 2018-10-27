package ch.FOW_Collection.presentation.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.MainViewModel;
import ch.FOW_Collection.presentation.profile.mybeers.MyBeersActivity;
import ch.FOW_Collection.domain.models.MyBeer;
import ch.FOW_Collection.presentation.profile.myratings.MyRatingsActivity;
import ch.FOW_Collection.presentation.profile.mywishlist.WishlistActivity;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Because the profile view is not a whole activity but rendered as part of the MainActivity in a tab, we use a so-called fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    @BindView(R.id.userProfileImageView)
    ImageView userProfileImageView;

    @BindView(R.id.userProfileNameText)
    TextView userProfileNameText;

    @BindView(R.id.myBeersCount)
    TextView myBeersCount;

    @BindView(R.id.myFridgeCount)
    TextView myFridgeCount;

    @BindView(R.id.myRatingsCount)
    TextView myRatingsCount;

    @BindView(R.id.myWishlistCount)
    TextView myWishlistCount;

    private MainViewModel model;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        /* Fragments also have a layout file, this one is in res/layout/fragment_profile_screen.xml: */
        View rootView = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        ButterKnife.bind(this, rootView);

        model = ViewModelProviders.of(this).get(MainViewModel.class);

        model.getMyWishlist().observe(this, this::updateWishlistCount);
//        model.getMyRatings().observe(this, this::updateRatingsCount);
//        model.getMyBeers().observe(this, this::updateMyBeersCount);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            userProfileNameText.setText(name);
            Uri photoUrl = user.getPhotoUrl();
            GlideApp.with(this).load(photoUrl).apply(new RequestOptions().circleCrop()).into(userProfileImageView);
        }

        return rootView;
    }

    private void updateMyBeersCount(List<MyBeer> myBeers) {
        myBeersCount.setText(String.valueOf(myBeers.size()));
    }

    @OnClick(R.id.myRatings)
    public void handleMyRatingsClick(View view) {
        Intent intent = new Intent(getActivity(), MyRatingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.myWishlist)
    public void handleMyWishlistClick(View view) {
        Intent intent = new Intent(getActivity(), WishlistActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.myBeers)
    public void handleDecksClick(View view) {
        Intent intent = new Intent(getActivity(), MyBeersActivity.class);
        startActivity(intent);
    }

//    private void updateRatingsCount(List<Rating> ratings) {
//        myRatingsCount.setText(String.valueOf(ratings.size()));
//    }

    private void updateWishlistCount(List<Wish> wishes) {
        myWishlistCount.setText(String.valueOf(wishes.size()));
    }

}