package ch.FOW_Collection.domain.models;

import java.util.Date;

import lombok.Data;

@Data
public class MyBeerFromRating implements MyBeer {
    private Rating rating;
    private Beer beer;

    public MyBeerFromRating(Rating rating, Beer beer) {
        this.rating = rating;
        this.beer = beer;
    }

    @Override
    public String getBeerId() {
        return rating.getBeerId();
    }

    @Override
    public Date getDate() {
        return rating.getCreationDate();
    }
}
