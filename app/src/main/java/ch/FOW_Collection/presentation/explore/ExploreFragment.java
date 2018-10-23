package ch.FOW_Collection.presentation.explore;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.MainViewModel;
import ch.FOW_Collection.presentation.explore.search.SearchActivity;
import ch.FOW_Collection.presentation.shared.CardListFragment;
import ch.FOW_Collection.presentation.utils.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.Query;

/**
 * This fragment is the first fragment shown in the {@link ch.FOW_Collection.presentation.MainActivity}. It lets users
 * "discover" beers by implementing a search (which simply opens the {@link SearchActivity} and two nested tabs where
 * users can browse beer categories and breweries.
 */
public class ExploreFragment extends Fragment {

    private static final String TAG = "HomeScreenActSearch";
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    /**
     * Fragments all need to have an empty constructor because the system might have the instantiate them.
     */
    public ExploreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, rootView);

        /*
         * We get the same ViewModel as the MainActivity, and because the MainActivity is already running we get the
         * same instance and can share the data of the MainActivity.
         * */
        MainViewModel model = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        /*
         * Because the two fragments are nested in this fragment, we have to use the getChildFragmentManager.
         * Otherwise strange rendering and other bugs can occur at runtime.
         *
         * The two fragments will communicate directly with the MainActivity (that's why the MainActivity implements
         * two listener interfaces), bypassing this fragment.
         * */
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        // adapter.addFragment(new CardPopularFragment(), "Beliebte");
        adapter.addFragment(new CardListFragment(), "Beliebte");
        adapter.addFragment(new CardEditionsFragment(), "Editionen");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    /**
     * This method is called when the user clicks the beerSearchButton in fragment_explore. We also use ButterKnife's
     * {@link OnClick} annotation to keep the keep the code shorter.
     * <p>
     * When the activity is started, the shared element (the animationSource) will be animated. It's not actually a
     * shared element in the sense that both layouts somehow share the same instance, we can simply designate two
     * elements as shared by giving both the same sharedElementName, i.e., "search" in this case.
     * <p>
     * See <a href="https://guides.codepath.com/android/shared-element-activity-transition">this blog</a> for details
     * on how it works.
     *
     * @param animationSource
     */
    @OnClick(R.id.beerSearchButton)
    public void openSearchActivity(View animationSource) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(getActivity(), animationSource, "search");
        startActivity(intent, options.toBundle());
    }
}
