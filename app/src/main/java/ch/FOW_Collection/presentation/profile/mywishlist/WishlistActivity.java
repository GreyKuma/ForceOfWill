package ch.FOW_Collection.presentation.profile.mywishlist;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
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
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.MainViewModel;
import ch.FOW_Collection.presentation.cardDetails.CardDetailsActivity;
import ch.FOW_Collection.presentation.shared.IWishClickedListener;
import lombok.val;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WishlistActivity extends AppCompatActivity implements IWishClickedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    View emptyView;

    private MainViewModel model;
    private WishlistRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_wishlist));


        model = ViewModelProviders.of(this).get(MainViewModel.class);
        //model.getMyWishlistWithCards().observe(this, this::updateWishlist);
        model.getMyWishlist().observe(this, this::updateWishlist);

        val layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new WishlistRecyclerViewAdapter(this, model);

        recyclerView.setAdapter(adapter);

    }

    private void updateWishlist(List<Wish> entries) { //(List<Pair<Wish, Card>> entries) {
        List<String> entriesIds = new ArrayList<>();
        Iterator<Wish> it = entries.iterator();
        while (it.hasNext()) {
            entriesIds.add(it.next().getId());
        }

        adapter.submitList(entriesIds);
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
    public void onCardSelectedListener(ImageView animationSource, Card card) {
        Intent intent = new Intent(this, CardDetailsActivity.class);
        intent.putExtra(CardDetailsActivity.ITEM_ID, card.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, animationSource, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onWishClickedListener(Card card) {
        model.toggleItemInWishlist(card.getId());
    }
}
