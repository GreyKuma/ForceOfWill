package ch.FOW_Collection.presentation.cardDetails;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import ch.FOW_Collection.domain.models.*;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.presentation.cardDetails.createRating.CreateCardRatingActivity;
import ch.FOW_Collection.presentation.shared.CardImageLoader;

import static ch.FOW_Collection.presentation.utils.DrawableHelpers.setDrawableTint;

public class CardDetailsActivity extends AppCompatActivity implements OnRatingLikedListener {

    public static final String ITEM_ID = "item_id";
    private static final String TAG = "CardDetailsActivity";
    private static final int RATING_REQUEST = 1337;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.cardImage)
    ImageView imageView;

    @BindView(R.id.cardAvgRating)
    TextView avgRating;

    @BindView(R.id.cardNumRatings)
    TextView numRatings;

    @BindView(R.id.cardRatingBar)
    RatingBar ratingBar;

    @BindView(R.id.cardName)
    TextView cardName;

    @BindView(R.id.cardType)
    LinearLayout cardType;

    @BindView(R.id.cardCost)
    LinearLayout cardCost;

    @BindView(R.id.cardRace)
    LinearLayout cardRace;

    @BindView(R.id.cardAtk)
    LinearLayout cardAtk;

    @BindView(R.id.cardDef)
    LinearLayout cardDef;

    @BindView(R.id.cardAbility)
    LinearLayout cardAbility;

    @BindView(R.id.wishlist)
    ToggleButton wishlist;

    @BindView(R.id.cardId)
    TextView cardId;

    @BindView(R.id.cardEdition)
    TextView cardEdition;

    @BindView(R.id.addRatingBar)
    RatingBar addRatingBar;

    @BindView(R.id.addRatingExplanation)
    TextView addRatingExplanation;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RatingsRecyclerViewAdapter adapter;
    private CardDetailsViewModel model;

    private BottomSheetDialog dialog;
    private View bottomSheetView;
    private ToggleButton addToCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: " + (savedInstanceState != null ? "true" : "false"));

        setContentView(R.layout.activity_card_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toolbar.setTitleTextColor(Color.alpha(0));

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            onBackPressed();
            return;
        }
        String cardId = extras.getString(ITEM_ID);

        model = ViewModelProviders.of(this).get(CardDetailsViewModel.class);
        model.setCardId(cardId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RatingsRecyclerViewAdapter(this, model.getCurrentUserId());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        model.getCard().observe(this, this::updateCard);
        model.getRatings().observe(this, this::updateRatings);
        //new RatingsRepository().getRatingsByUserIdAndCardId(FirebaseAuth.getInstance().getCurrentUser().getUid(), cardId)
        //        .observe(this, this::updateOwnRatings);
        model.getOwnRating().observe(this, this::updateOwnRatings);
        model.getWish().observe(this, this::toggleWishlistView);

        model.getMyCollection().observe(this, this::toggleCollectionButton);
        dialog = new BottomSheetDialog(this);
        bottomSheetView = getLayoutInflater().inflate(R.layout.single_bottom_sheet_dialog, null);
        dialog.setContentView(bottomSheetView);
        addToCollection = bottomSheetView.findViewById(R.id.addToCollection);

        recyclerView.setAdapter(adapter);
        addRatingBar.setOnRatingBarChangeListener(this::addNewRating);
    }

    private void addNewRating(RatingBar ratingBar, float v, boolean b) {
        /*Observer<Card> os = new Observer<Card>() {
            @Override
            public void onChanged(Card card) {*/
        if (model.getCard().getValue() != null) {
            //model.getCard().removeObserver(this);

            Intent intent = new Intent(CardDetailsActivity.this, CreateCardRatingActivity.class);
            intent.putExtra(CreateCardRatingActivity.ITEM_CARD, model.getCard().getValue());
            if (model.getOwnRating().getValue() != null) {
                intent.putExtra(CreateCardRatingActivity.ITEM_RATING, model.getOwnRating().getValue());
            }
            intent.putExtra(CreateCardRatingActivity.RATING_VALUE, v);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CardDetailsActivity.this, addRatingBar, "ownRating");
            // startActivity(intent, options.toBundle());
            startActivityForResult(intent, RATING_REQUEST, options.toBundle());
        }
           /* }
        };
        if (model.getCard().getValue() != null) {
            os.onChanged(model.getCard().getValue());
        } else {
            model.getCard().observe(this, os);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RATING_REQUEST && resultCode == RESULT_OK) {
            // todo dont know what now to do..
            //Rating rating = data.getParcelableExtra(CreateCardRatingActivity.ITEM_RATING);
            //Log.d(TAG, "RatingResult: " + rating.getId());
        }
    }


    private void updateCard(Card card) {
        // This values are static and will not change. (except ownRating)
        // Its this function is more a promise..

        // render meta
        toolbar.setTitle(card.getName().getDe());
        cardName.setText(card.getName().getDe());
        cardId.setText(card.getId());

        ratingBar.setNumStars(5);
        ratingBar.setRating(card.getAvgRating());
        avgRating.setText(getResources().getString(R.string.fmt_avg_rating, card.getAvgRating()));
        if(card.getNumRatings() == 0){
            numRatings.setText(R.string.fmt_no_ratings);
        }else{
            numRatings.setText(getResources().getQuantityString(R.plurals.fmt_num_ratings, card.getNumRatings(), card.getNumRatings()));
        }

        // prepare LayoutParams for the labels
        LinearLayout.LayoutParams labelLayoutParams = new LinearLayout.LayoutParams(
                Math.round(getResources().getDimension(R.dimen.activity_card_detail_label_width)),
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // render ATK
        if (card.getAtk() != null && card.getAtk() > 0) {
            // clear LinearLayout
            cardAtk.removeAllViews();

            // append marginTop
            appendMarginTop(cardAtk);

            // titleText
            TextView textCardAtk = new TextView(this);
            textCardAtk.setText("ATK:");
            textCardAtk.setLayoutParams(labelLayoutParams);
            cardAtk.addView(textCardAtk);
            textCardAtk.requestLayout();

            // value
            TextView valCardAtk = new TextView(this);
            valCardAtk.setText(Integer.toString(card.getAtk()));
            valCardAtk.setLayoutParams(labelLayoutParams);
            cardAtk.addView(valCardAtk);
            valCardAtk.requestLayout();
        }

        // render DEF
        if (card.getDef() != null && card.getDef() > 0) {
            // clear LinearLayout
            cardDef.removeAllViews();

            // append marginTop
            appendMarginTop(cardDef);

            // titleText
            TextView textCardDef = new TextView(this);
            textCardDef.setText("DEF:");
            textCardDef.setLayoutParams(labelLayoutParams);
            cardDef.addView(textCardDef);
            textCardDef.requestLayout();

            // value
            TextView valCardDef = new TextView(this);
            valCardDef.setText(Integer.toString(card.getDef()));
            valCardDef.setLayoutParams(labelLayoutParams);
            cardDef.addView(valCardDef);
            valCardDef.requestLayout();
        }

        // For the ForeignValues, we observe once to keep it nice and clean.

        // render CardEdition
        if (card.getEdition() != null) {
            // ObserveOnce of CardEdition
            final Observer<CardEdition> osCE = new Observer<CardEdition>() {
                @Override
                public void onChanged(CardEdition ce) {
                    // get and set data
                    cardEdition.setText(ce.getDe());
                    // remove itself
                    card.getEdition().removeObserver(this);
                }
            };
            card.getEdition().observe(this, osCE);
        }

        // render Image of Card
        GlideApp.with(this)
                .load(FirebaseStorage.getInstance().getReference().child(card.getImageStorageUrl()))
                .apply(new RequestOptions().centerInside())
                .apply(CardImageLoader.imageRenderer)
                // for the animation
                .dontAnimate()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(imageView);

        // render CardTypes
        if (card.getTypes() != null) {
            // ObserveOnce of CardType
            final Observer<List<CardType>> osCT = new Observer<List<CardType>>() {
                @Override
                public void onChanged(List<CardType> cardTypes) {
                    // when not loaded, result is empty
                    if (cardTypes.size() > 0) {
                        // remove itself (Observer)
                        card.getTypes().removeObserver(this);

                        // clear LinearLayout
                        cardType.removeAllViews();

                        // append marginTop
                        appendMarginTop(cardType);

                        // titleText
                        TextView textCardType = new TextView(CardDetailsActivity.this);
                        textCardType.setText("Typ:");
                        textCardType.setLayoutParams(labelLayoutParams);
                        CardDetailsActivity.this.cardType.addView(textCardType);
                        textCardType.requestLayout();

                        // for each CardType
                        Iterator<CardType> cardTypeIt = cardTypes.iterator();
                        while (cardTypeIt.hasNext()) {
                            CardType cardType = cardTypeIt.next();

                            TextView text = new TextView(CardDetailsActivity.this);
                            text.setText(cardType.getDe());
                            CardDetailsActivity.this.cardType.addView(text);
                            text.requestLayout();
                        }
                    }
                }
            };
            card.getTypes().observe(this, osCT);
        }

        // render CardRace
        if (card.getRaces() != null) {
            // ObserveOnce of CardType
            final Observer<List<CardRace>> osCR = new Observer<List<CardRace>>() {
                @Override
                public void onChanged(List<CardRace> cardRaces) {
                    // when not loaded, result is empty
                    if (cardRaces.size() > 0) {
                        // remove itself (Observer)
                        card.getRaces().removeObserver(this);

                        // clear LinearLayout
                        cardRace.removeAllViews();

                        // append marginTop
                        appendMarginTop(cardRace);

                        // titleText
                        TextView textCardCost = new TextView(CardDetailsActivity.this);
                        textCardCost.setText("Rasse:");
                        textCardCost.setLayoutParams(labelLayoutParams);
                        cardRace.addView(textCardCost);
                        textCardCost.requestLayout();

                        // for each CardRace
                        Iterator<CardRace> cardTypeIt = cardRaces.iterator();
                        while (cardTypeIt.hasNext()) {
                            CardRace cardRace = cardTypeIt.next();

                            TextView text = new TextView(CardDetailsActivity.this);
                            text.setText(cardRace.getDe());
                            CardDetailsActivity.this.cardRace.addView(text);
                            text.requestLayout();
                        }
                    }
                }
            };
            card.getRaces().observe(this, osCR);
        }

        // render CardCost
        if (card.getCost() != null && card.getCost().size() > 0) {
            // clear LinearLayout
            cardCost.removeAllViews();

            // append marginTop
            appendMarginTop(cardCost);

            // titleText
            TextView textCardCost = new TextView(this);
            textCardCost.setText("Kosten:");
            textCardCost.setLayoutParams(labelLayoutParams);
            this.cardCost.addView(textCardCost);
            textCardCost.requestLayout();

            // for each CardCost
            Iterator<CardCost> cardCostIt = card.getCost().iterator();
            while (cardCostIt.hasNext()) {
                CardCost cardCost = cardCostIt.next();
                if (cardCost.getCount() != null && cardCost.getCount() > 0 && cardCost.getType() != null) {
                    // ObserveOnce of CardAttribute of CardCost
                    final Observer<CardAttribute> osCA = new Observer<CardAttribute>() {
                        @Override
                        public void onChanged(CardAttribute cardCostType) {
                            // request successful?
                            if (cardCostType != null) {
                                // remove itself (Observer)
                                cardCost.getType().removeObserver(this);

                                // make LayoutParams
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        Math.round(getResources().getDimension(R.dimen.activity_card_detail_attribute_size)),
                                        Math.round(getResources().getDimension(R.dimen.activity_card_detail_attribute_size)));
                                layoutParams.rightMargin = Math.round(getResources().getDimension(R.dimen.activity_card_detail_attribute_margin));

                                // get and set data
                                for (int i = 0; i < cardCost.getCount(); i++) {
                                    // drawable present?
                                    if (cardCostType.getDrawableId() != 0) {
                                        // id found, add imageView
                                        ImageView image = new ImageView(CardDetailsActivity.this);
                                        image.setImageDrawable(ContextCompat.getDrawable(CardDetailsActivity.this, cardCostType.getDrawableId()));
                                        image.setLayoutParams(layoutParams);
                                        CardDetailsActivity.this.cardCost.addView(image);
                                        image.requestLayout();
                                    } else {
                                        // id not found, add number (AnyAttribute)
                                        View circle = getViewCardAttributeCircle(
                                                CardDetailsActivity.this,
                                                Integer.toString(cardCost.getCount()));
                                        circle.setLayoutParams(layoutParams);
                                        CardDetailsActivity.this.cardCost.addView(circle);
                                        circle.requestLayout();
                                        // break out of loop
                                        break;
                                    }
                                }
                            }
                        }
                    };
                    cardCost.getType().observe(this, osCA);
                }
            }
        }

        // render cardAbility
        if (card.getAbility() != null && card.getAbility().size() > 0) {
            // clear LinearLayout
            cardAbility.removeAllViews();

            // append marginTop
            appendMarginTop(cardAbility);

            //
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            // for each CardAbility
            Iterator<CardAbility> cardAbilityIt = card.getAbility().iterator();
            while (cardAbilityIt.hasNext()) {
                AbilityTextView abilityTextView = new AbilityTextView(this, cardAbilityIt.next());
                abilityTextView.setLayoutParams(layoutParams);
                cardAbility.addView(abilityTextView);
                abilityTextView.requestLayout();
            }
        }
    }

    private void updateOwnRatings(Rating rating) {
        if (rating != null) {
            addRatingBar.setOnRatingBarChangeListener(null);

            addRatingBar.setRating(rating.getRating());
            addRatingExplanation.setText("Bewertung durch klicken und ziehen ändern.");

            addRatingBar.setOnRatingBarChangeListener(this::addNewRating);
        }
    }

    private void appendMarginTop(LinearLayout layout) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        layoutParams.topMargin = Math.round(getResources().getDimension(R.dimen.activity_card_detail_margin_top));
        layout.setLayoutParams(layoutParams);
    }

    private static View getViewCardAttributeCircle(CardDetailsActivity context, String text) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.rgb(0, 0, 0));

        LinearLayout view = new LinearLayout(context);
        view.setBackground(shape);

        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextColor(Color.rgb(255, 255, 255));
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);

        view.addView(textView);

        return view;
    }

    private void updateRatings(List<Rating> ratings) {
        adapter.submitList(new ArrayList<>(ratings));
    }


    @Override
    public void onRatingLikedListener(Rating rating) {
        model.toggleLike(rating);
    }

    @OnClick(R.id.wishlist)
    public void onWishClickedListener(View view) {
        model.toggleItemInWishlist(model.getCard().getValue().getId());
        /*
         * We won't get an update from firestore when the wish is removed, so we need to reset the UI state ourselves.
         * */
        if (!wishlist.isChecked()) {
            toggleWishlistView(null);
        }
    }

    private void toggleWishlistView(Wish wish) {
        if (wish != null) {
            int color = getResources().getColor(R.color.colorPrimaryLight);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(true);
        } else {
            int color = getResources().getColor(android.R.color.darker_gray);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(false);
        }
    }

    @OnClick(R.id.actionsButton)
    public void showBottomSheetDialog() {
        toggleCollectionButton(model.getMyCollection().getValue());
        dialog.show();
    }

    private void toggleCollectionButton(List<MyCard> myCards) {
        String cardId;
        if(model.getCard().getValue() != null){
            cardId = model.getCard().getValue().getIdStr();
        }else{
            cardId = "";
        }
        addToCollection.setOnClickListener(v -> {
            Log.d("FRIDGE", "FRIDGE");
            model.toggleItemInCollection(cardId);
            coloriseCollectionButton();
        });
        if(myCards != null){
            for (MyCard myCard : myCards) {
                if (cardId.equals(myCard.getCardId())) {
                    addToCollection.setChecked(true);
                    coloriseCollectionButton();
                    break;
                } else {
                    addToCollection.setChecked(false);
                    coloriseCollectionButton();
                }
            }
        }
    }

    private void coloriseCollectionButton(){
        if(addToCollection.isChecked()){
            int color = getResources().getColor(R.color.colorPrimaryLight);
            setDrawableTint(addToCollection, color);
        }else{
            int color = getResources().getColor(android.R.color.darker_gray);
            setDrawableTint(addToCollection, color);
        }
    }

//    private void toggleCollectionView(boolean deleted){
//        if(deleted){
//            int color = getResources().getColor(android.R.color.darker_gray);
//            setDrawableTint(addToCollection);
//        }
//    }

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
}
