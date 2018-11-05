package ch.FOW_Collection.data.repositories;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.UserClassSnapshotParser;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.User;
import com.firebase.ui.firestore.ClassSnapshotParser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static androidx.lifecycle.Transformations.switchMap;

public class UserRepository {
    //region private static
    // For the pattern, we don't want to use static methods,
    // instead we define public / nonStatic after this region.
    private static final ClassSnapshotParser<User> parser = new UserClassSnapshotParser();

    /**
     * Get Query for all users.
     *
     * @return Query for all users.
     */
    private static Query allUsersQuery() {
        return FirebaseFirestore
                .getInstance()
                .collection(User.COLLECTION);
    }

    /**
     * Get LiveData of all users.
     *
     * @return LiveDataArray of all users.
     */
    private static FirestoreQueryLiveDataArray<User> allUsers() {
        return new FirestoreQueryLiveDataArray<>(
                allUsersQuery(), parser);
    }

    /**
     * Get DocumentReference of a single user.
     *
     * @param userId Id of the card.
     * @return DocumentReference of a single user.
     */
    private static DocumentReference userByIdDocRef(String userId) {
        return FirebaseFirestore
                .getInstance()
                .collection(User.COLLECTION)
                .document(userId);
    }

    /**
     * Get LiveData of a single user.
     *
     * @param userId Id of a user.
     * @return LiveData of a single user.
     */
    private static FirestoreQueryLiveData<User> userById(String userId) {
        return new FirestoreQueryLiveData<>(
                userByIdDocRef(userId), parser);
    }

    //endregion

    //region public / nonStatic accessor

    /**
     * Get LiveData of all users.
     *
     * @return LiveDataArray of all users.
     */
    public FirestoreQueryLiveDataArray<User> getAllUsers() {
        return allUsers();
    }

    /**
     * Get LiveData of a single user.
     *
     * @param userId Id of a user.
     * @return LiveData of a single user.
     */
    public FirestoreQueryLiveData<User> getUserById(String userId) {
        return userById(userId);
    }

    /**
     * Get LiveData of a single user.
     *
     * @param userId Id of a user.
     * @return LiveData of a single user.
     */
    public LiveData<User> getUserById(LiveData<String> userId) {
        return switchMap(userId, UserRepository::userById);
    }

    //endregion
}
