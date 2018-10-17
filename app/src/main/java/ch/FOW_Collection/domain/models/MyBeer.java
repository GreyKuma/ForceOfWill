package ch.beerpro.domain.models;

import ch.beerpro.domain.models.Beer;

import java.util.Date;

public interface MyBeer {
    String getBeerId();

    Beer getBeer();

    Date getDate();
}
