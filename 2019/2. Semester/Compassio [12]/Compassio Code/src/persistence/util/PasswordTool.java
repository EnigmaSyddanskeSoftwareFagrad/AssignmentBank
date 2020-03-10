package persistence.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Handles salting and passwords in relation to the users.
 * 
 * @author Mads Holm Jensen
 * @author Morten Kargo Lyngesen
 */
public class PasswordTool {
    
    /**
     * Returns a new salt.
     * @return a new salt.
     */
    public static byte[] generateSalt() {
        SecureRandom secRan = new SecureRandom();
        byte[] salt = new byte[128];
        secRan.nextBytes(salt);
        return salt;
    }

    /**
     *
     * @param password password to be hashed.
     * @param salt randomly generated salt.
     * @return hashed password.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec).getEncoded();
    }
    
}
