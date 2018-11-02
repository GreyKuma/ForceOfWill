package ch.FOW_Collection.presentation.profile.mycollection;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.MyCard;
import ch.FOW_Collection.presentation.cardDetails.CardDetailsActivity;


public class MyCollectionActivity extends AppCompatActivity implements OnMyCardItemInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MyCollectionViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_mybeers));

        model = ViewModelProviders.of(this).get(MyCollectionViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_cards, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                model.setSearchTerm(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                model.setSearchTerm(null);
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            model.setSearchTerm(null);
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMoreClickedListener(ImageView photo, Card item) {
        Intent intent = new Intent(this, CardDetailsActivity.class);
        intent.putExtra(CardDetailsActivity.ITEM_ID, item.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, photo, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onWishClickedListener(Card item) {
        model.toggleCardInCollection(item.getId());
    }

    @Override
    public void onNormalUpClickedListener(MyCard myCard) {
        model.addOneToCardAmount(myCard, MyCard.FIELD_AMOUNT_NORMAL);
    }

    @Override
    public void onNormalDownClickedListener(MyCard myCard) {
        model.subOneFromCardAmount(myCard, MyCard.FIELD_AMOUNT_NORMAL);
    }

    @Override
    public void onFoilUpClickedListener(MyCard myCard) {
        model.addOneToCardAmount(myCard, MyCard.FIELD_AMOUNT_FOIL);
    }

    @Override
    public void onFoilDownClickedListener(MyCard myCard) {
        model.subOneFromCardAmount(myCard, MyCard.FIELD_AMOUNT_FOIL);
    }
}
