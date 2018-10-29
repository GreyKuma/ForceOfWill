package ch.FOW_Collection.domain.models;

import lombok.Data;

import java.util.Date;
@Data
public class MyCardFromRating implements MyCard {
    private Rating rating;
    private Card card;

    public MyCardFromRating(Rating rating, Card card) {
        this.rating = rating;
        this.card = card;
    }

    @Override
    public String getCardId() {
//        return rating.getCardId();
        return null;
    }

    @Override
    public Date getDate() {
        return rating.getCreationDate();
    }
}
