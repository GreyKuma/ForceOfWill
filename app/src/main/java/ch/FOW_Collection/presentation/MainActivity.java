package ch.FOW_Collection.presentation;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.CardEdition;
import ch.FOW_Collection.presentation.cardDetails.CardDetailsActivity;
import ch.FOW_Collection.presentation.explore.CardEditionsFragment;
import ch.FOW_Collection.presentation.explore.CardPopularFragment;
import ch.FOW_Collection.presentation.explore.ExploreFragment;
import ch.FOW_Collection.presentation.profile.ProfileFragment;
import ch.FOW_Collection.presentation.ratings.RatingsFragment;
import ch.FOW_Collection.presentation.shared.cardList.ICardListFragmentListener;
import ch.FOW_Collection.presentation.splash.SplashScreenActivity;
import ch.FOW_Collection.presentation.utils.ViewPagerAdapter;

import com.bumptech.glide.request.transition.DrawableCrossFadeTransition;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.Query;

/**
 * The {@link MainActivity} is the entry point for logged-in users (actually, users start at the
 * {@link SplashScreenActivity}, but if they are still logged in they will get redirected to this activity.
 * <p>
 * The Activity has three tabs, each of which implemented by a fragment and held together by a {@link ViewPager}.
 */
public class MainActivity
        extends AppCompatActivity
        implements
            ICardListFragmentListener,
            CardPopularFragment.ICardPopularFragmentListener,
            CardEditionsFragment.OnItemSelectedListener {

    /**
     * We use ButterKnife's view injection instead of having to call findViewById repeatedly.
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*
         * The following ceremony is need to have the app logo set as the home button.
         * */
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.fow_logo); //beer_glass_icon);

        setupViewPager(viewPager, tabLayout);

        /*
         * Just a placeholder for your own ideas...
         * */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    private void setupViewPager(ViewPager viewPager, TabLayout tabLayout) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ExploreFragment());
        adapter.addFragment(new RatingsFragment());
        adapter.addFragment(new ProfileFragment());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_search_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_people_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person_black_24dp);

        /*
         * We want to change the title of the activity depending on the selected fragment. We can do this by
         * listening to the tabLayout's changes and setting the title accordingly:
         * */
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                switch (tab.getPosition()) {
                    case 0:
                        getSupportActionBar().setTitle("Entdecken");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Bewertungen");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Mein Profil");
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> {
            Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onCardEditionSelected(CardEdition cardEdition) {
        // TODO implement
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("onCardEditionSelected")
                .setMessage(cardEdition.getDe());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onShowMoreClick(View view) {
        // TODO implement
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("onShowMoreClick")
                .setMessage("You wanna see more?\nUnfortunately: Not implemented!");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public Query getData(String cardListId) {
        MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);
        return model.getCardsTopRatedQuery(12);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    @Override
    public void onMoreClickedListener(ImageView imageView, Card card) {
        // TODO implement
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("onCardSelected")
                .setMessage(card.getName().getDe());
        AlertDialog dialog = builder.create();
        dialog.show();*/
        Intent intent = new Intent(this, CardDetailsActivity.class);
        intent.putExtra(CardDetailsActivity.ITEM_ID, card.getId());
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(
                    this,
                        Pair.create(imageView,"image")
                );
        startActivity(intent, options.toBundle());

    }
}
