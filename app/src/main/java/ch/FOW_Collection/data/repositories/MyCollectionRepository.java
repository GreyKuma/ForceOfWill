package ch.FOW_Collection.data.repositories;

import android.util.Log;
import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.MyCardClassSnapshotParser;
import ch.FOW_Collection.data.parser.WishClassSnapshotParser;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.*;
import ch.FOW_Collection.domain.models.Collection;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
import static androidx.lifecycle.Transformations.switchMap;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.combineLatest;

import java.util.*;

import static androidx.lifecycle.Transformations.map;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.combineLatest;

public class MyCollectionRepository {
    public static final String TAG = "MyCollectionRepository";
    public static LiveData<List<MyCard>> getCollectionByUser(String userId) {
        if(userId == null){
            return null;
        }
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance()
                .collection(Collection.FIRST_COLLECTION + "/" + userId + "/" + Collection.SECOND_COLLECTION)
                .orderBy(Card.CARD_ID, Query.Direction.DESCENDING),
                new MyCardClassSnapshotParser());
    }

    public Task<Void> toggleCardInCollection(String userId, String cardId){
        Log.d(TAG,"UserId: " + userId + ", cardId: " + cardId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cardRef = db
                .collection(Collection.FIRST_COLLECTION + "/" + userId + "/" + Collection.SECOND_COLLECTION)
                .document(cardId);

        return cardRef.get().continueWithTask(task -> {
            if(task.isSuccessful() && task.getResult().exists()){
                return cardRef.delete();
            } else if (task.isSuccessful()){
                return cardRef.set(new MyCard(cardId));
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<List<MyCard>> getMyCollection(LiveData<String> currentUserId){
        return switchMap(currentUserId, MyCollectionRepository::getCollectionByUser);
    }

    public Task<Void> addOneToCardAmount(String userId, MyCard myCard, final String type){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cardRef = db
                .collection(Collection.FIRST_COLLECTION + "/" + userId + "/" + Collection.SECOND_COLLECTION)
                .document(myCard.getCardId());

        return cardRef.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                if (type == MyCard.FIELD_AMOUNT_NORMAL){
                    return cardRef.update(MyCard.FIELD_AMOUNT_NORMAL, myCard.getAmountNormal()+1);
                }else if(type == MyCard.FIELD_AMOUNT_FOIL){
                    return cardRef.update(MyCard.FIELD_AMOUNT_FOIL, myCard.getAmountFoil()+1);
                }else{
                    return null;
                }
            } else {
                throw task.getException();
            }
        });
    }

    public Task<Void> subOneFromCardAmount(String userId, MyCard myCard, final String type){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cardRef = db
                .collection(Collection.FIRST_COLLECTION + "/" + userId + "/" + Collection.SECOND_COLLECTION)
                .document(myCard.getCardId());

        return cardRef.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                if (type == MyCard.FIELD_AMOUNT_NORMAL){
                    return cardRef.update(MyCard.FIELD_AMOUNT_NORMAL, myCard.getAmountNormal()-1);
                }else if(type == MyCard.FIELD_AMOUNT_FOIL){
                    return cardRef.update(MyCard.FIELD_AMOUNT_FOIL, myCard.getAmountFoil()-1);
                }else{
                    return null;
                }
            } else {
                throw task.getException();
            }
        });
    }
}