package ch.FOW_Collection.presentation.explore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.CardEdition;
import ch.FOW_Collection.presentation.utils.BackgroundImageProvider;

import java.util.List;

public class CardEditionsRecyclerViewAdapter
        extends RecyclerView.Adapter<CardEditionsRecyclerViewAdapter.ViewHolder> {
    private final static String TAG = "CardEditionsRecyclerVie";
    private final CardEditionsFragment.OnItemSelectedListener listener;
    private List<CardEdition> cardEditions;

    public CardEditionsRecyclerViewAdapter(CardEditionsFragment.OnItemSelectedListener listener) {
        super();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_explore_card_editions_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onBindViewHolder(holder, position, cardEditions.get(position));
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position, CardEdition cardEdition) {
        Log.d(TAG, "Bind \"" + cardEdition.getId() + "\"");
        holder.bind(cardEdition, position, listener);
    }

    @Override
    public int getItemCount() {
        if (cardEditions != null) {
            return cardEditions.size();
        }
        return 0;
    }

    public void submitList(List<CardEdition> cardEditions) {
        this.cardEditions = cardEditions;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.cardImage)
        ImageView cardImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(CardEdition item, int position, CardEditionsFragment.OnItemSelectedListener listener) {
            content.setText(item.getDe());
            Context resources = itemView.getContext();
            cardImage.setImageDrawable(BackgroundImageProvider.getBackgroundImage(resources, position + 10));
            if (listener != null) {
                itemView.setOnClickListener(v -> listener.onCardEditionSelected(item));
            }
        }
    }
}
