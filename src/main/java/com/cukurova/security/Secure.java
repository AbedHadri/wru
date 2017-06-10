package com.cukurova.security;

import com.cukurova.model.TokenModel;
import com.cukurova.model.UserModel;
import com.cukurova.utils.Conn;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

//@DenyAll
//@Singleton
public class Secure {

    private String token;
    Conn conn = new Conn();

    public String getToken() {
        return this.token;
    }

    public String getKeyByToken(String clientToken) throws SQLException {
        if (clientToken == null) {
            return null;
        }

        ResultSet rs = conn.sqlExecuteSelect("SELECT ANAHTAR FROM KULL_KARTI WHERE KULL_ID = ?;", clientToken);
        String newSecStr;
        if (rs.next()) {
            newSecStr = rs.getString("ANAHTAR");
            return newSecStr;
        }
        return null;
    }

    public String getSaltByToken(String clientToken) throws SQLException {
        if (clientToken == null) {
            return null;
        }

        ResultSet rs = conn.sqlExecuteSelect("SELECT SALT FROM KULL_KARTI WHERE KULL_ID = ?", clientToken);
        String newSecStr = null;
        if (rs.next()) {
            newSecStr = rs.getString("SALT");
        }
        return newSecStr;
    }

    public String hash(String plaintext, String saltOrg) throws Exception {
        MessageDigest md = null;
        plaintext = plaintext + saltOrg;
        try {
            md = MessageDigest.getInstance("SHA-256"); //step 2
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(e.getMessage());
        }
        try {
            md.update(plaintext.getBytes("UTF-8")); //step 3
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e.getMessage());
        }
        byte[] hash = md.digest(plaintext.getBytes(StandardCharsets.UTF_8));
        byte[] b2 = new byte[hash.length + 1];//stringify
        b2[0] = 1;
        System.arraycopy(hash, 0, b2, 1, hash.length);
        return new BigInteger(b2).toString(36); //step 6
    }

    public synchronized String encrypt(String text, String encryptKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(encryptKey);
// rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        Cipher cipher = Cipher.getInstance("AES");

        //ENCRYPTING STARTS HERE!
        cipher.init(Cipher.ENCRYPT_MODE, originalKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        //stringify
        byte[] b2 = new byte[encrypted.length + 1];
        b2[0] = 1;
        System.arraycopy(encrypted, 0, b2, 1, encrypted.length);
        return new BigInteger(b2).toString(36);
    }

    public String decrypt(String encryptedOrg, String key) throws Exception {
        if (encryptedOrg == null || key == null) {
            return null;
        }

        byte[] keyBytes = Base64.getDecoder().decode(key);
// rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        //convet input to byte array
        byte[] b2 = new BigInteger(encryptedOrg, 36).toByteArray();
        byte[] dec = Arrays.copyOfRange(b2, 1, b2.length);

        cipher.init(Cipher.DECRYPT_MODE, originalKey);
        //DECRYPTING HERE!
        return new String(cipher.doFinal(dec));
    }

//cryptographically secure
    public String csRandomString(int numChars) {

        char[] VALID_CHARACTERS = "LMefg_GBwxCY4ZaFHb@c79Rd+DEIuSTUhijVWrstklmNOPQnopq68Ayz05vX12-3".toCharArray();

        SecureRandom srand = new SecureRandom();
        Random rand = new Random();
        char[] buff = new char[numChars];

        for (int i = 0; i < numChars; ++i) {
            // reseed rand once you've used up all available entropy bits
            if ((i % 10) == 0) {
                rand.setSeed(srand.nextLong()); // 64 bits of random!
            }
            buff[i] = VALID_CHARACTERS[rand.nextInt(VALID_CHARACTERS.length)];
        }
        return new String(buff);
    }

    public String csRandomNumericString(int ofChars) {

        char[] VALID_CHARACTERS = "123789456".toCharArray();

        SecureRandom srand = new SecureRandom();
        Random rand = new Random();
        char[] buff = new char[ofChars];

        for (int i = 0; i < ofChars; ++i) {
            // reseed rand once you've used up all available entropy bits
            if ((i % 10) == 0) {
                rand.setSeed(srand.nextLong()); // 64 bits of random!
            }
            buff[i] = VALID_CHARACTERS[rand.nextInt(VALID_CHARACTERS.length)];
        }
        return new String(buff);
    }

    public String simpleRand() {
        SecureRandom secureRandom = new SecureRandom();

        return new BigInteger(256, secureRandom).toString(32);
    }

    public int numberRandom(int min, int max) {

        SecureRandom rand = new SecureRandom();
        return rand.nextInt((max - min) + 1) + min;

    }

    public String aesKeyGenerator() {

        SecretKey secretKey;
        try {
            secretKey = KeyGenerator.getInstance("AES").generateKey();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
            return null;//exit the method
        }
        // get base64 encoded version of the key
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return encodedKey;

    }

    boolean tokenExists(String token) throws SQLException {

        return new TokenModel().hasDuplicatedToken(token);
    }

//    UserSingle getOwnerOfToken(String token) throws SQLException {
//        TokenData tokenData = new TokenData();
//        
//        UserSingle rslt = tokenData.getUserDataByPublicToken(token);
// 
//        return rslt;
//    }
}
