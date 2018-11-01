const functions = require("firebase-functions");

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require("firebase-admin");
admin.initializeApp();
const db = admin.firestore();

exports.updateRatings = functions.firestore
  .document("ratings/{ratingId}")
  .onWrite((snap, context) => {
    var newRating = null;
    if (snap.after && snap.after.data) {
      newRating = snap.after.data();
      console.log("newRating: ", newRating);
    }
    var oldRating = null;
    if (snap.before && snap.before.data) {
      oldRating = snap.before.data();
      console.log("oldRating: ", oldRating);
    }

    const ratingVal = newRating.rating;
    var cardRef = db.collection("Cards").doc(newRating.cardId);

    // Update aggregations in a transaction
    return db.runTransaction(transaction => {
      return transaction.get(cardRef).then(cardDoc => {
        const card = cardDoc.data();

        console.log("*** Running Transaction ***");
        console.log("card: ", card);
        var oldNumRatings = typeof card.numRatings === 'undefined' ? 0 : card.numRatings ;
        var newNumRatings = oldNumRatings + (oldRating ? 0 : 1);

        // Compute new average rating
        var oldRatingTotal = card.avgRating * (oldNumRatings - (oldRating ? 1 : 0));
        var newAvgRating = (oldRatingTotal + ratingVal) / newNumRatings;

        console.log("oldNumRatings: ", oldNumRatings);
        console.log("newNumRatings: ", newNumRatings);
        console.log("oldRatingTotal: ", oldRatingTotal);
        console.log("ratingVal: ", ratingVal);
        console.log("newAvgRating: ", newAvgRating);

        // Update restaurant info
        return transaction.update(cardRef, {
          avgRating: newAvgRating,
          numRatings: newNumRatings
        });
      });
    });
  });
/*

exports.updateRatingsUpdate = functions.firestore
  .document("ratings/{ratingId}")
  .onUpdate((snap, context) => {
    const newRating = snap.after.data();
    const ratingVal = newRating.rating;
    var cardRef = db.collection("Cards").doc(newRating.cardId);

    // Update aggregations in a transaction
    return db.runTransaction(transaction => {
      return transaction.get(cardRef).then(cardDoc => {
        const card = cardDoc.data();

        console.log("*** Running Transaction ***");
        console.log("card: ", card);
        var newNumRatings = (typeof card.numRatings === 'undefined') ? 1 : card.numRatings;

        // Compute new average rating
        var oldRatingTotal = card.avgRating * (newNumRatings - 1);
        var newAvgRating = (oldRatingTotal + ratingVal) / newNumRatings;

        console.log("newNumRatings: ", newNumRatings);
        console.log("ratingVal: ", ratingVal);
        console.log("oldRatingTotal: ", oldRatingTotal);
        console.log("newAvgRating: ", newAvgRating);

        // Update restaurant info
        return transaction.update(cardRef, {
          avgRating: newAvgRating,
          numRatings: newNumRatings
        });
      });
    });
  })

  exports.updateRatingsCreate = functions.firestore
  .document("ratings/{ratingId}")
  .onCreate((snap, context) => {
    const newRating = snap.data();
    const ratingVal = newRating.rating;
    var cardRef = db.collection("Cards").doc(newRating.cardId);

    // Update aggregations in a transaction
    return db.runTransaction(transaction => {
      return transaction.get(cardRef).then(cardDoc => {
        const card = cardDoc.data();

        console.log("*** Running Transaction ***");
        console.log("card: ", card);
        var newNumRatings = (typeof card.numRatings === 'undefined') ? 1 : card.numRatings + 1;

        // Compute new average rating
        var oldRatingTotal = card.avgRating * ((typeof card.numRatings === 'undefined') ? 0 : card.numRatings);
        var newAvgRating = (oldRatingTotal + ratingVal) / newNumRatings;

        console.log("newNumRatings: ", newNumRatings);
        console.log("ratingVal: ", ratingVal);
        console.log("oldRatingTotal: ", oldRatingTotal);
        console.log("newAvgRating: ", newAvgRating);

        // Update restaurant info
        return transaction.update(cardRef, {
          avgRating: newAvgRating,
          numRatings: newNumRatings
        });
      });
    });
  });*/
