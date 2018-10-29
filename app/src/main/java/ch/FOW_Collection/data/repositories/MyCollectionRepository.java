package ch.FOW_Collection.data.repositories;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.models.*;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.models.Entity;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.Wish;

import static androidx.lifecycle.Transformations.map;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.combineLatest;

import java.util.*;

import static androidx.lifecycle.Transformations.map;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.combineLatest;

public class MyCollectionRepository {
    private static List<MyCard> getMyCards(Triple<List<Wish>, List<Rating>, HashMap<String, Card>> input) {
        List<Wish> wishlist = input.getLeft();
        List<Rating> ratings = input.getMiddle();
        HashMap<String, Card> cards = input.getRight();

        ArrayList<MyCard> result = new ArrayList<>();
        Set<String> cardsAlreadyOnTheList = new HashSet<>();
        for (Wish wish : wishlist) {
            String cardId = wish.getCardId();
            result.add(new MyCardFromWishlist(wish, cards.get(cardId)));
            cardsAlreadyOnTheList.add(cardId);
        }

//        for (Rating rating : ratings) {
//            String cardId = rating.getCardId();
//            if (cardsAlreadyOnTheList.contains(cardId)) {
//                // if the card is already on the wish list, don't add it again
//            } else {
//                result.add(new MyCardFromRating(rating, cards.get(cardId)));
//                // we also don't want to see a rated card twice
//                cardsAlreadyOnTheList.add(cardId);
//            }
//        }
        Collections.sort(result, (r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        return result;
    }


    public LiveData<List<MyCard>> getMyCards(LiveData<List<Card>> allCards, LiveData<List<Wish>> myWishlist,
                                             LiveData<List<Rating>> myRatings) {
        return map(combineLatest(myWishlist, myRatings, map(allCards, Entity::entitiesById)),
                MyCollectionRepository::getMyCards);
    }

    public LiveData<List<MyCard>> getMyCollection(LiveData<List<Wish>> myWishlist){
        LiveData<List<MyCard>> myCollection;
        // TODO implement return of Collection
        return null;
    }
}