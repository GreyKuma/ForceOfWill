package ch.FOW_Collection.presentation.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ch.FOW_Collection.domain.models.MyCard;
import ch.FOW_Collection.domain.models.User;
import ch.FOW_Collection.presentation.profile.mycollection.MyCollectionActivity;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

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
import ch.FOW_Collection.presentation.profile.mycollection.MyCollectionActivity;
import ch.FOW_Collection.presentation.profile.myratings.MyRatingsActivity;
import ch.FOW_Collection.presentation.profile.mywishlist.WishlistActivity;

/**
 * Because the profile view is not a whole activity but rendered as part of the MainActivity in a tab, we use a so-called fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    @BindView(R.id.userProfileImageView)
    ImageView userProfileImageView;

    @BindView(R.id.userProfileNameText)
    TextView userProfileNameText;

    @BindView(R.id.myCollectionCount)
    TextView myCollectionCount;

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
        model.getCurrentUser().observe(this, this::updateUser);

        return rootView;
    }

    private void updateMyCollectionCount(List<MyCard> myCollection) {
        myCollectionCount.setText(String.valueOf(myCollection.size()));
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

    @OnClick(R.id.myCollection)
    public void handleCollectionClick(View view) {
        Intent intent = new Intent(getActivity(), MyCollectionActivity.class);
        startActivity(intent);
    }

    private void updateUser(User user) {
        if (user != null) {
            userProfileNameText.setText(user.getName());
            GlideApp.with(this).load(Uri.parse(user.getPhoto())).apply(new RequestOptions().circleCrop()).into(userProfileImageView);

            user.getWishlist().removeObservers(this);
            user.getWishlist().observe(this, this::updateWishlistCount);

            user.getRatings().removeObservers(this);
            user.getRatings().observe(this, this::updateRatingsCount);

            user.getMyCards().observe(this, this::updateMyCollectionCount);
        }
    }

    private void updateRatingsCount(List<Rating> ratings) {
        myRatingsCount.setText(String.valueOf(ratings.size()));
    }

    private void updateWishlistCount(List<Wish> wishes) {
        myWishlistCount.setText(String.valueOf(wishes.size()));
    }

}
