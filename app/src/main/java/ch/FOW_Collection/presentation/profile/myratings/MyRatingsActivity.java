package ch.FOW_Collection.presentation.profile.myratings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.User;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.cardDetails.CardDetailsActivity;

public class MyRatingsActivity extends AppCompatActivity implements OnMyRatingItemInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyRatingsViewModel model;
    private MyRatingsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ratings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_myratings));


        model = ViewModelProviders.of(this).get(MyRatingsViewModel.class);
        model.getMyRatingsWithWishes().observe(this, this::updateMyRatings);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyRatingsRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

    }

    private void updateMyRatings(List<Pair<Rating,Wish>> entries) {
        adapter.submitList(entries);
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
    public void onMoreClickedListener(Rating item) {
        Intent intent = new Intent(this, CardDetailsActivity.class);
        intent.putExtra(CardDetailsActivity.ITEM_ID, item.getCardId());
        startActivity(intent);
    }

    @Override
    public void onWishClickedListener(Rating item) {
        model.toggleItemInWishlist(item.getCardId());
    }
}
