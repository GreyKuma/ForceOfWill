package ch.FOW_Collection.domain.models;


import lombok.Data;

import java.util.Date;

@Data
public class MyCardFromWishlist implements MyCard {
    private Wish wish;
    private Card card;

    public MyCardFromWishlist(Wish wish, Card card) {
        this.wish = wish;
        this.card = card;
    }

    @Override
    public String getCardId() {
        return wish.getCardId();
    }

    @Override
    public Date getDate() {
        return wish.getAddedAt();
    }
}
