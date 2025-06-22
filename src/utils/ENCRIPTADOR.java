package utils;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.postgresql.util.Base64;
public class ENCRIPTADOR {
public static String encripta(String original){
	IvParameterSpec iv = new IvParameterSpec("RandomInitVector".getBytes(StandardCharsets.UTF_8));
	SecretKeySpec skeySpec = new SecretKeySpec("ThisIsASecretKey".getBytes(StandardCharsets.UTF_8),"AES" );
    Cipher cipher = null;
	try {
		cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidAlgorithmParameterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	byte[] encripted = null;
	try {
		encripted = cipher.doFinal(original.getBytes());
	} catch (IllegalBlockSizeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (BadPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return Base64.encodeBytes(encripted);
}
public static String desEncripta(String encriptado){
	IvParameterSpec iv = new IvParameterSpec("Random".getBytes(StandardCharsets.UTF_8));
	SecretKeySpec skeySpec = new SecretKeySpec("SecretKey".getBytes(StandardCharsets.UTF_8),"AES" );
    Cipher cipher = null;
	try {
		cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidAlgorithmParameterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	byte[] original = null;
	try {
		original = cipher.doFinal(Base64.decode(encriptado));
	} catch (IllegalBlockSizeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (BadPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return new String(original);
}
}
