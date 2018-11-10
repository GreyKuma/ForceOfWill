package ch.FOW_Collection.presentation.explore.search;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.MyCard;
import ch.FOW_Collection.presentation.cardDetails.CardDetailsActivity;
import ch.FOW_Collection.presentation.explore.search.cards.SearchResultFragment;
import ch.FOW_Collection.presentation.explore.search.suggestions.SearchSuggestionsFragment;
import ch.FOW_Collection.presentation.profile.mycollection.MyCollectionViewModel;
import ch.FOW_Collection.presentation.shared.ICardSelectedListener;
import ch.FOW_Collection.presentation.shared.ICollectionInteractionListener;
import com.google.android.material.tabs.TabLayout;
import com.google.common.base.Strings;

public class SearchActivity extends AppCompatActivity
        implements SearchResultFragment.OnItemSelectedListener, SearchSuggestionsFragment.OnItemSelectedListener, ICardSelectedListener,
        ICollectionInteractionListener {

    private SearchViewModel searchViewModel;
    private ViewPagerAdapter adapter;
    private EditText searchEditText;
    private MyCollectionViewModel myCollectionViewModel;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String text = searchEditText.getText().toString();
                handleSearch(text);
                addSearchTermToUserHistory(text);
            }
            return false;
        });

        findViewById(R.id.clearFilterButton).setOnClickListener(view -> {
            searchEditText.setText(null);
            handleSearch(null);
        });

        ViewPager viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setSaveFromParentEnabled(false);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        myCollectionViewModel = ViewModelProviders.of(this).get(MyCollectionViewModel.class);
    }

    private void handleSearch(String text) {
        searchViewModel.setSearchTerm(text);
        myCollectionViewModel.setSearchTerm(text);
        adapter.setShowSuggestions(Strings.isNullOrEmpty(text));
        adapter.notifyDataSetChanged();
    }

    private void addSearchTermToUserHistory(String text) {
        searchViewModel.addToSearchHistory(text);
    }

    @Override
    public void onSearchResultListItemSelected(View animationSource, Card card) {
        Intent intent = new Intent(this, CardDetailsActivity.class);
        intent.putExtra(CardDetailsActivity.ITEM_ID, card.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, animationSource, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onSearchSuggestionListItemSelected(String text) {
        searchEditText.setText(text);
        searchEditText.setSelection(text.length());
        hideKeyboard();
        handleSearch(text);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        }
    }

    @Override
    public void onCardSelectedListener(ImageView photo, Card card) {
        Intent intent = new Intent(this, CardDetailsActivity.class);
        intent.putExtra(CardDetailsActivity.ITEM_ID, card.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, photo, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onCollectionRemoveClickedListener(MyCard card) {
        searchViewModel.toggleItemInWishlist(card.getCardId());
    }

    @Override
    public void onCollectionNormalUpClickedListener(MyCard myCard) {
        //TODO onClickListener for CardAmount
    }

    @Override
    public void onCollectionNormalDownClickedListener(MyCard myCard) {
        //TODO onClickListener for CardAmount
    }

    @Override
    public void onCollectionFoilUpClickedListener(MyCard myCard) {
        //TODO onClickListener for CardAmount
    }

    @Override
    public void onCollectionFoilDownClickedListener(MyCard myCard) {
        //TODO onClickListener for CardAmount
    }
}
