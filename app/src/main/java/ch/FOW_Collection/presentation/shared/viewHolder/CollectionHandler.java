package ch.FOW_Collection.presentation.shared.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.MyCard;
import ch.FOW_Collection.presentation.shared.ICollectionInteractionListener;

import static ch.FOW_Collection.presentation.utils.DrawableHelpers.setDrawableTint;

public interface CollectionHandler {
    default void bindCollectionHandler(View view, MyCard myCard, ICollectionInteractionListener listener) {
        ToggleButton collectionStatus = view.findViewById(R.id.removeFromCollection);
        Button normal1Down = view.findViewById(R.id.normal1Down);
        Button normal1Up = view.findViewById(R.id.normal1Up);
        Button foil1Down = view.findViewById(R.id.foil1Down);
        Button foil1Up = view.findViewById(R.id.foil1Up);
        TextView normalAmount = view.findViewById(R.id.normalAmount);
        TextView foilAmount = view.findViewById(R.id.foilAmount);

        normalAmount.setText(Integer.toString(myCard.getAmountNormal()));
        normal1Up.setOnClickListener(v -> listener.onCollectionNormalUpClickedListener(myCard));
        normal1Down.setOnClickListener(v -> listener.onCollectionNormalDownClickedListener(myCard));

        foilAmount.setText(Integer.toString(myCard.getAmountFoil()));
        foil1Up.setOnClickListener(v -> listener.onCollectionFoilUpClickedListener(myCard));
        foil1Down.setOnClickListener(v -> listener.onCollectionFoilDownClickedListener(myCard));

        collectionStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                setDrawableTint(buttonView, buttonView.getResources().getColor(R.color.colorPrimaryLight));
            } else {
                setDrawableTint(buttonView, buttonView.getResources().getColor(android.R.color.darker_gray));
            }
        });

        collectionStatus.setOnClickListener(v -> listener.onCollectionRemoveClickedListener(myCard));
        collectionStatus.setChecked(myCard != null);
    }
}
