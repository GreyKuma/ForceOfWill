import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import model.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CardFirestoreWriter {
    public static final String STORAGE_URL = "fowtest-f30af.appspot.com";
    private final Bucket bucket;

    CardFirestoreWriter(Bucket bucket) {
        this.bucket = bucket;
    }

    public static InputStream getStreamFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            return connection.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void append(Card card) {
        //String[] path = card.imageSrcUrl.split(".");

        String type = "image/jpeg"; //+ path[path.length-1];

        Blob ret = this.bucket.create("Cards/" + card.idStr + "_full", getStreamFromURL(card.imageSrcUrl),type);
        card.imageStorageUrl = ret.getName();
    }

    void close() {

    }
}
