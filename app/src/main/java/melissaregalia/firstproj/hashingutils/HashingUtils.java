package melissaregalia.firstproj.hashingutils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by melissaregalia on 5/22/15.
 */
public class HashingUtils {

    private static final String HASH_ALGORITHM = "SHA-256";

    public static String hash(String s) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            return new String(messageDigest.digest(s.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
