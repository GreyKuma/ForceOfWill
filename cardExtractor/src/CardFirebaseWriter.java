import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import model.Card;
import model.CardRaw;
import model.CardSimpleProp;
import model.ModelHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class CardFirebaseWriter {
    private static final String DATABASE_URL = "https://fowtest-f30af.firebaseio.com/";
    private static String name;
    private static Firestore database;
    // private static CollectionReference collection;

    private static CardFirestoreWriter firestore;
    private static ModelHelper modelHelper;

    CardFirebaseWriter(String pathToServiceAccountKey) throws IOException {
        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream(pathToServiceAccountKey);
        } catch (IOException e) {
            throw new IOException("Make sure to have a ServiceAccountFile.\n" +
                    "Get one from the console:\n" +
                    "See https://firebase.google.com/docs/admin/setup#initialize_the_sdk", e);
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(DATABASE_URL)
                .setStorageBucket(CardFirestoreWriter.STORAGE_URL)
                .build();

        FirebaseApp.initializeApp(options);
        database = FirestoreClient.getFirestore();

        firestore = new CardFirestoreWriter(StorageClient.getInstance().bucket());

        // name = "ImportTest_" + new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        // collection = database.collection(name);

        modelHelper = new ModelHelper();

        // test

        database.getCollections().forEach(c -> {
            System.out.println("CollectionId: " + c.getId());
        });
        System.out.println("CardFirebaseWriter ready!");
    }

    //int i = 0;
    public long append(CardRaw cardRaw) throws ExecutionException, InterruptedException {
        //if (i++ < 20) {
            Card card = modelHelper.processCardRaw(cardRaw);
            // firestore.append(card);
            // return 0;
            card.imageStorageUrl = "Cards/" + card.idStr + "_full";

            Map<String, Object> cardFields = new HashMap<>();
            cardFields.put("idNumeric", card.idNumeric);
            cardFields.put("idStr", card.idStr);

            Map<String, String> name = new HashMap<String, String>();
            name.put("en", card.name.en);
            name.put("de", card.name.de);
            cardFields.put("name", name);

            if (card.flavor != null) {
                Map<String, String> flavor = new HashMap<String, String>();
                flavor.put("en", card.flavor.en);
                flavor.put("de", card.flavor.de);
                cardFields.put("flavor", flavor);
            }
            if (card.rarity != null && !card.rarity.equals("")) cardFields.put("rarity", card.rarity);
            if (card.imageSrcUrl != null && !card.imageSrcUrl.equals(""))
                cardFields.put("imageSrcUrl", card.imageSrcUrl);
            if (card.imageStorageUrl != null && !card.imageStorageUrl.equals(""))
                cardFields.put("imageStorageUrl", card.imageStorageUrl);

            //card.avgRating = (float)Math.floor((Math.random() * 400 + 100)) / 100;
            cardFields.put("avgRating", Math.floor((Math.random() * 400 + 100)) / 100);

            if (card.atk != null) cardFields.put("atk", card.atk);
            if (card.def != null) cardFields.put("def", card.def);
            if (card.editionId != null) cardFields.put("editionId", card.editionId);

            if (card.typeIds != null && card.typeIds.size() > 0) cardFields.put("typeIds", card.typeIds);
            if (card.raceIds != null && card.raceIds.size() > 0) cardFields.put("raceIds", card.raceIds);
            if (card.attributeIds != null && card.attributeIds.size() > 0)
                cardFields.put("attributeIds", card.attributeIds);

            if (card.ability != null && card.ability.size() > 0) {
                List<Map<String, Object>> abilities = new ArrayList<>();
                card.ability.forEach(a -> {
                    Map<String, Object> abilitie = new HashMap<>();
                    if (a.value != null) {
                        Map<String, String> value = new HashMap<String, String>();
                        value.put("en", a.value.en);
                        value.put("de", a.value.de);
                        abilitie.put("value", value);
                    }
                    if (a.typeId != null) abilitie.put("typeId", a.typeId);
                    if (a.cost != null && a.cost.size() > 0) {
                        List<Map<String, Integer>> costs = new ArrayList<>();
                        a.cost.forEach(c -> {
                            Map<String, Integer> cost = new HashMap<>();
                            cost.put("count", c.count);
                            cost.put("typeId", c.typeId);
                            costs.add(cost);
                        });
                    }
                    abilities.add(abilitie);
                });
                cardFields.put("ability", abilities);
            }

            if (card.cost != null) {
                List<Map<String, Integer>> costs = new ArrayList<>();
                card.cost.forEach(c -> {
                    Map<String, Integer> cost = new HashMap<>();
                    cost.put("typeId", c.typeId);
                    cost.put("count", c.count);
                    costs.add(cost);
                });
                cardFields.put("cost", costs);
            }


            ApiFuture<WriteResult> result =
                    // collection
                    // .document("data")
                    database
                            .collection("Cards")
                            .document(card.idStr)
                            .set(cardFields);

            return result.get().getUpdateTime().getSeconds();
        //}
        //return 0;*/
    }

    public void close() throws Exception {
        System.out.println("  Insert CardTypes:");
        for (CardSimpleProp cardSimpleProp : modelHelper.cardTypes) {
            System.out.println("   [" + cardSimpleProp.id + "]:{" +
                    //collection
                    //.document("data")
                    database
                    .collection("CardType")
                    .document(Integer.toString(cardSimpleProp.id))
                    .set(cardSimpleProp.value).get().getUpdateTime() +
                    "} \"" + cardSimpleProp.value.de + "\" | \"" + cardSimpleProp.value.en + "\"");
        }

        System.out.println("  Insert CardEdition:");
        for (CardSimpleProp cardSimpleProp : modelHelper.cardEdition) {
            System.out.println("   [" + cardSimpleProp.id + "]:{" +
                    //collection
                    //.document("data")
                    database
                    .collection("CardEdition")
                    .document(Integer.toString(cardSimpleProp.id))
                    .set(cardSimpleProp.value).get().getUpdateTime() +
                    "} \"" + cardSimpleProp.value.de + "\" | \"" + cardSimpleProp.value.en + "\"");
        }

        System.out.println("  Insert CardAttributes:");
        for (CardSimpleProp cardSimpleProp : modelHelper.cardAttribute) {
            System.out.println("   [" + cardSimpleProp.id + "]:{" +
                    //collection
                    //.document("data")
                    database
                        .collection("CardAttribute")
                        .document(Integer.toString(cardSimpleProp.id))
                        .set(cardSimpleProp.value).get().getUpdateTime() +
                    "} \"" + cardSimpleProp.value.de + "\" | \"" + cardSimpleProp.value.en + "\"");
        }

        System.out.println("  Insert CardRace:");
        for (CardSimpleProp cardSimpleProp : modelHelper.cardRace) {
            System.out.println("   [" + cardSimpleProp.id + "]:{" +
                    //collection
                    //.document("data")
                    database
                    .collection("CardRace")
                    .document(Integer.toString(cardSimpleProp.id))
                    .set(cardSimpleProp.value).get().getUpdateTime() +
                    "} \"" + cardSimpleProp.value.de + "\" | \"" + cardSimpleProp.value.en + "\"");
        }

        System.out.println("  Insert CardAbilityType:");
        for (CardSimpleProp cardSimpleProp : modelHelper.cardAbilityType) {
            System.out.println("   [" + cardSimpleProp.id + "]:{" +
                    //collection
                    //.document("data")
                    database
                            .collection("CardAbilityType")
                            .document(Integer.toString(cardSimpleProp.id))
                            .set(cardSimpleProp.value).get().getUpdateTime() +
                    "} \"" + cardSimpleProp.value.de + "\" | \"" + cardSimpleProp.value.en + "\"");
        }

        database.close();
    }
}
