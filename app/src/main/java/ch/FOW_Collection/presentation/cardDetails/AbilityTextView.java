package ch.FOW_Collection.presentation.cardDetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import ch.FOW_Collection.R;
import ch.FOW_Collection.data.repositories.CardAbilityTypeRepository;
import ch.FOW_Collection.data.repositories.CardAttributeRepository;
import ch.FOW_Collection.domain.models.CardAbility;
import ch.FOW_Collection.domain.models.CardAbilityType;
import ch.FOW_Collection.domain.models.CardAttribute;
import ch.FOW_Collection.domain.models.MultiLanguageString;

// Ideas stolen from https://stackoverflow.com/questions/15352496/how-to-add-image-in-a-textview-text
@SuppressLint("AppCompatCustomView")
public class AbilityTextView extends TextView {
    public AbilityTextView(Context context, CardAbility cardAbility) {
        this(context, cardAbility, null);
    }

    public AbilityTextView(Context context, CardAbility cardAbility, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setCardAbility(cardAbility);
    }

    public AbilityTextView(Context context, CardAbility cardAbility, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCardAbility(cardAbility);
    }

    public AbilityTextView(Context context, CardAbility cardAbility, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setCardAbility(cardAbility);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        // only filter value if its SPANNABLE
        if (type == BufferType.SPANNABLE) {
            // find all placeholders
            List<Pair<Pair<Integer, Integer>, Pair<String, Integer>>> found = find((Spannable) text);
            for (int i = found.size() - 1; i >= 0; i--) {
                // test if span is set
                ReplacementSpan[] spans = ((Spannable) text).getSpans(found.get(i).first.first, found.get(i).first.second, ReplacementSpan.class);
                if (spans.length == 0) {
                    // span unset.. remove placeholder
                    SpannableStringBuilder buffer = new SpannableStringBuilder();
                    buffer.append(text.subSequence(0, found.get(i).first.first));
                    buffer.append(text.subSequence(found.get(i).first.second, text.length()));
                    text = buffer;
                }
            }
        }
        // set the text
        super.setText(text, type);
    }

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();

    private void setCardAbility(CardAbility cardAbility) {
        // add placeholder for the AbililityType
        String text =
                (cardAbility.getTypeId() != null ? ("{{Ability:" + cardAbility.getTypeId() + "}}") : "") +
                        (cardAbility.getValue() != null ? cardAbility.getValue().getDe() : "");
        Spannable spannable = spannableFactory.newSpannable(text);

        // render the text
        renderSpan(spannable);
        setText(spannable, BufferType.SPANNABLE);
    }

    private List<Pair<Pair<Integer, Integer>, Pair<String, Integer>>> find(final Spannable spannable) {
        Pattern ref = Pattern.compile("\\{\\{([^}:]*):([^}]*)\\}\\}");
        Matcher matcher = ref.matcher(spannable);

        // find all placeholder
        List<Pair<Pair<Integer, Integer>, Pair<String, Integer>>> found = new ArrayList<>();
        while (matcher.find()) {
            found.add(new Pair<>(
                    new Pair<>(matcher.start(), matcher.end()),
                    new Pair<>(matcher.group(1), Integer.parseInt(matcher.group(2)))));
        }
        return found;
    }

    private void renderSpan(final Spannable spannable) {
        List<Pair<Pair<Integer, Integer>, Pair<String, Integer>>> found = find(spannable);

        // for each placeholder
        for (int i = 0; i < found.size(); i++) {
            // todo inject ViewModel
            LiveData<? extends MultiLanguageString> req = (
                    found.get(i).second.first.toLowerCase().equals("ability") ?
                            new CardAbilityTypeRepository().getAbilityTypeById(found.get(i).second.second) :
                            new CardAttributeRepository().getCardAttributeById(found.get(i).second.second)
            );
            final Pair<Pair<Integer, Integer>, Pair<String, Integer>> dataFinal = found.get(i);
            // observeOnce!
            Observer<MultiLanguageString> sub = new Observer<MultiLanguageString>() {
                final Pair<Pair<Integer, Integer>, Pair<String, Integer>> data = dataFinal;

                @Override
                public void onChanged(MultiLanguageString value) {
                    if (value != null) {
                        // observeOnce!
                        req.removeObserver(this);

                        // is placeholder an ability?
                        if (value instanceof CardAbilityType) {
                            if (value.getEn().toLowerCase().startsWith("rest")) {
                                // we have an image for resting!
                                final Drawable drawable = getContext().getResources().getDrawable(R.drawable.ic_possa);
                                drawable.mutate();
                                drawable.setBounds(0, 0,
                                        (int) (getLineHeight()*1.1),
                                        getLineHeight());

                                spannable.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM),
                                        data.first.first,
                                        data.first.second,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else if (!value.getEn().toLowerCase().equals("always")){
                                // don't render "Dauerhaft"/"Always"....
                                // replace placeholder with CircleSpan

                                spannable.setSpan(new CircleSpan(
                                                value.getDe(),
                                                Color.rgb(255, 255, 255),
                                                //Color.rgb(0, 0, 0), 4,
                                                AbilityTextView.this.getTextColors().getDefaultColor(), 4,
                                                //Color.rgb(0, 0, 0), 16),
                                                AbilityTextView.this.getTextColors().getDefaultColor(), 16),
                                        data.first.first,
                                        data.first.second,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        } else if (value instanceof CardAttribute) {
                            // is placeholder an attribute?

                            // getDrawableId and set image
                            int id = ((CardAttribute) value).getDrawableId();
                            if (id != 0) {
                                final Drawable drawable = getContext().getResources().getDrawable(id);
                                drawable.mutate();
                                drawable.setBounds(0, 0, getLineHeight(), getLineHeight());

                                spannable.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM),
                                        data.first.first,
                                        data.first.second,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else {
                                // not image found, means any attribute, so we count how many
                                for (int i = 0; i < found.size(); i++) {
                                    // find yourself
                                    if (found.get(i).first.first.equals(data.first.first)) {
                                        Pair<Integer, Integer> curr = found.get(i).first;
                                        // get last placeholder with same values
                                        while (found.size() > ++i && found.get(i).first.first.equals(curr.second) &&
                                                found.get(i).second.first.equals(data.second.first) &&
                                                found.get(i).second.second.equals(data.second.second)) {
                                            curr = found.get(i).first;
                                        }

                                        // test if span is set
                                        ReplacementSpan[] spans =
                                                spannable.getSpans(curr.first, curr.second, ReplacementSpan.class);
                                        if (spans.length > 0) {
                                            // span is set, count value up
                                            CircleSpan span = (CircleSpan) spans[0];
                                            int val = Integer.parseInt((span).getText());
                                            span.setText(Integer.toString(val + 1));
                                        } else {
                                            // span unset, set it
                                            spannable.setSpan(
                                                    new CircleSpan(
                                                            "1",
                                                            Color.rgb(0, 0, 0),
                                                            Color.rgb(0, 0, 0), 0,
                                                            Color.rgb(255, 255, 255), 12),
                                                    curr.first,
                                                    curr.second,
                                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        // set the text, because we changed it
                        setText(spannable, BufferType.SPANNABLE);
                    }
                }
            };
            req.observeForever(sub);
        }
    }

    private static boolean addImages(Context context, Spannable spannable) {
        Pattern refImg = Pattern.compile("\\Q[img src=\\E([a-zA-Z0-9_]+?)\\Q/]\\E");
        boolean hasChanges = false;

        Matcher matcher = refImg.matcher(spannable);
        while (matcher.find()) {
            boolean set = true;
            for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class)) {
                if (spannable.getSpanStart(span) >= matcher.start()
                        && spannable.getSpanEnd(span) <= matcher.end()
                        ) {
                    spannable.removeSpan(span);
                } else {
                    set = false;
                    break;
                }
            }
            String resname = spannable.subSequence(matcher.start(1), matcher.end(1)).toString().trim();
            int id = context.getResources().getIdentifier(resname, "drawable", context.getPackageName());
            if (set) {
                hasChanges = true;
                spannable.setSpan(new ImageSpan(context, id),
                        matcher.start(),
                        matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }

        return hasChanges;
    }

    private static Spannable getTextWithImages(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addImages(context, spannable);
        return spannable;
    }

    static class CircleSpan extends ReplacementSpan {
        private String text;
        private final int backgroundColor;
        private final int borderColor;
        private final int borderWidth;
        private final int textColor;
        private final int padding;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public CircleSpan(String text, int backgroundColor, int borderColor, int borderWidth, int textColor, int padding) {
            this.text = text;
            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            this.borderWidth = borderWidth;
            this.textColor = textColor;
            this.padding = padding;
        }

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
            // round up
            return Math.round((paint.measureText(this.text, 0, this.text.length()) + (2 * padding) + (2 * borderWidth)) + 0.5f);
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            int size = getSize(paint, text, start, end, null);
            Log.d("AbilityTextView", "Draw \"" + this.text + "\" x:" + x + " top:" + top + " size:" + size + " bottom:" + bottom + " y:" + y);

            paint.setColor(borderColor);
            canvas.drawRoundRect(new RectF(x, top, x + size, bottom), size / 2, size / 2, paint);

            paint.setColor(backgroundColor);
            canvas.drawRoundRect(new RectF(x + borderWidth, top + borderWidth, x + size - borderWidth, bottom - borderWidth),
                    (size - (2 * borderWidth)) / 2, (size - (2 * borderWidth)) / 2, paint);

            //canvas.drawCircle(x + size / 2, (bottom - top) / 2, size / 2, paint);
            paint.setColor(textColor);
            canvas.drawText(this.text, 0, this.text.length(), x + padding + borderWidth, y - 2, paint);
        }
    }
}
