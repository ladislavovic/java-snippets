package cz.kul.snippets.java._24_cryptography;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Main_Cryptography {

    public static void main(String[] args) throws Exception {
        rsa();
        //        aes();
    }

    private static void rsa() throws Exception {
        final int KEY_SIZE = 2048;
        //        final int KEY_SIZE = 1024;
        KeyPair pair = generateKeyPair(KEY_SIZE);
        Cipher cipher = Cipher.getInstance("RSA");

        String str = "This is my secure string";
        cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
        byte[] opened = str.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = cipher.doFinal(opened);

        cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
        byte[] decrypted = cipher.doFinal(encrypted);
        String decryptedStr = new String(decrypted, StandardCharsets.UTF_8);

        System.out.println(str);
        System.out.println(decryptedStr);

        System.out.println("Str opened lenght: " + str.length());
        System.out.println("Str decryp lenght: " + decryptedStr.length());
        System.out.println("byte openr lenght: " + opened.length);
        System.out.println("byte encry lenght: " + encrypted.length);
        System.out.println("byte decry lenght: " + decrypted.length);
    }

    private static KeyPair generateKeyPair(int size) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(size);
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }

    public static void aes() throws Exception {
        String key = "Bar12345Bar12345"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV

        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        String str = "This is my long string and now is even longer and longer";
        byte[] opened = str.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = cipher.doFinal(opened);

        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] decrypted = cipher.doFinal(encrypted);
        String decryptedStr = new String(decrypted, StandardCharsets.UTF_8);

        System.out.println(str);
        System.out.println(decryptedStr);

        System.out.println("Str opened lenght: " + str.length());
        System.out.println("Str decryp lenght: " + decryptedStr.length());
        System.out.println("byte openr lenght: " + opened.length);
        System.out.println("byte encry lenght: " + encrypted.length);
        System.out.println("byte decry lenght: " + decrypted.length);

        assertEquals(str, decryptedStr);
    }

}
