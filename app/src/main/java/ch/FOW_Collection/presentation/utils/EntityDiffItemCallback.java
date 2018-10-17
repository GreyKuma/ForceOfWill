package ch.beerpro.presentation.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import ch.beerpro.domain.models.Entity;

public class EntityDiffItemCallback<T extends Entity> extends DiffUtil.ItemCallback<T> {
    @Override
    public boolean areItemsTheSame(@NonNull T oldE, @NonNull T newE) {
        return oldE.getId().equals(newE.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull T oldE, @NonNull T newE) {
        return oldE.equals(newE);
    }
}
