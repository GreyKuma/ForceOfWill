package ch.FOW_Collection.presentation.profile.myratings;

import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.shared.ICardSelectedListener;
import ch.FOW_Collection.presentation.shared.IWishClickedListener;

import java.util.List;

public class MyRatingsActivity extends AppCompatActivity implements IWishClickedListener {

    @BindView(R.id.emptyView)
    View emptyView;

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

    private void updateMyRatings(List<Pair<Rating, Wish>> entries) {
        adapter.submitList(entries);
        if (entries.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
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
    public void onCardSelectedListener(ImageView imageView, Card card) {
        ICardSelectedListener.DefaultBehavior(this, imageView, card);
    }

    @Override
    public void onWishClickedListener(Card card) {
        model.toggleItemInWishlist(card.getId());
    }
}
