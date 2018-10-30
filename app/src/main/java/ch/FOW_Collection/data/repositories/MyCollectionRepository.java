package ch.FOW_Collection.data.repositories;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.MyCardClassSnapshotParser;
import ch.FOW_Collection.data.parser.WishClassSnapshotParser;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.*;
import ch.FOW_Collection.domain.models.Collection;
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
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.combineLatest;

import java.util.*;

import static androidx.lifecycle.Transformations.map;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.combineLatest;

public class MyCollectionRepository {
    public LiveData<List<MyCard>> getCollectionByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(Collection.
                FIRST_COLLECTION + "/" + userId).orderBy(Card.CARD_ID, Query.Direction.DESCENDING),
                new MyCardClassSnapshotParser());
    }
}