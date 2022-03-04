package ca.uhn.fhir.jpa.starter.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName : RSAUtil.java
 * @Description : RSA 암호화 관련
 * @Modification
 * 
 *               <pre>
 *  수정일		수정자	수정내용
 * ---------- ------- ------------------------
 * 2022.02.15	정주혁	최초작성
 *               </pre>
 * 
 * @author joohyukjung
 * @since 2022-02-15
 * @version 1.0
 */

@Slf4j
public class RSAUtil {
	
	private static final String Algorithm = "RSA";
	/**
	 * 공개키로 RSA 암호화 수행
	 * @param plainText 암호화할 문자열
	 * @param publicKey 공개키
	 * @return String 암호화된 문자열
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static String encryptRSA(String plainText, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {	
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		byte[] bytePlain = cipher.doFinal(plainText.getBytes());
		return Base64.getEncoder().encodeToString(bytePlain);
	}
	
	public static String encryptRSA(String plainText, String base64PublicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {	
		PublicKey publicKey = toPublicKeyFromBase64(base64PublicKey);
		
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		byte[] bytePlain = cipher.doFinal(plainText.getBytes());
		return Base64.getEncoder().encodeToString(bytePlain);
	}
	
	/**
	 * 개인키로 RSA 복호화 수행
	 * @param encrypted 암호화된 문자열
	 * @param privateKey 개인키
	 * @return String 복호화된 문자열
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public static String decryptRSA(String encrypted, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		
		byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
		byte[] bytePlain = cipher.doFinal(byteEncrypted);
		return new String(bytePlain, "utf-8");
	}
	
	
	/**
	 * Base64로 인코딩된 공개키를 publicKey로 변환
	 * @param base64PublicKey Base64로 인코딩된 공개키
	 * @return PublicKey
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static PublicKey toPublicKeyFromBase64(String base64PublicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
		byte[] decodedBase64PubKey = Base64.getDecoder().decode(base64PublicKey);
		return KeyFactory.getInstance(Algorithm)
				.generatePublic(new X509EncodedKeySpec(decodedBase64PubKey));
		
	}
	
	/**
	 * Base64로 인코딩된 개인키를 privateKey로 변환
	 * @param base64PrivateKey Base64로 인코딩된 개인키
	 * @return PrivateKey
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static PrivateKey toPrivateKeyFromBase64(String base64PrivateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
		byte[] decodedBase64PrivateKey = Base64.getDecoder().decode(base64PrivateKey);
		return KeyFactory.getInstance(Algorithm)
				.generatePrivate(new PKCS8EncodedKeySpec(decodedBase64PrivateKey));
	}
	
		
	public static PublicKey toPublicKeyFromXml(String xmlPublicKey) 
			throws SAXException, IOException, ParserConfigurationException, InvalidKeySpecException, NoSuchAlgorithmException {
		Document doc = CommonUtil.xmlStringToDoc(xmlPublicKey);
		String strModulus = doc.getElementsByTagName("Modulus").item(0).getTextContent();
		String strExponent = doc.getElementsByTagName("Exponent").item(0).getTextContent();
		
		byte[] modulus = org.apache.commons.codec.binary.Base64.decodeBase64(strModulus);
		byte[] exponent = org.apache.commons.codec.binary.Base64.decodeBase64(strExponent);
		
		return toPublicKey(modulus, exponent);
	}
	
	public static PublicKey toPublicKey(byte[] modulus, byte[] exponent) throws InvalidKeySpecException, NoSuchAlgorithmException {
		BigInteger bigModulus = new BigInteger(1, modulus);
		BigInteger bigExponent = new BigInteger(1, exponent);
		RSAPublicKeySpec spec = new RSAPublicKeySpec(bigModulus, bigExponent);
		return KeyFactory.getInstance(Algorithm).generatePublic(spec);
	}
	
	public static RSAPublicKeySpec toPublicKeySpec(PublicKey publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
		return KeyFactory.getInstance(Algorithm).getKeySpec(publicKey, RSAPublicKeySpec.class);
	}
	
	public static KeyPair genRSAKeyPair(int keyBit) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Algorithm);
		keyPairGenerator.initialize(keyBit);
		return keyPairGenerator.genKeyPair();
	}
	
	public static String toXmlFromBase64(String base64PublicKey) {
		try {
			PublicKey publicKey;
			publicKey = toPublicKeyFromBase64(base64PublicKey);
			RSAPublicKeySpec spec = toPublicKeySpec(publicKey);
			byte[] modulus = removeSigPaddingOfBigInteger(spec.getModulus().toByteArray());
			byte[] exponent = spec.getPublicExponent().toByteArray();
			
			String strModulus = org.apache.commons.codec.binary.Base64.encodeBase64String(modulus);
			String strExponent = org.apache.commons.codec.binary.Base64.encodeBase64String(exponent);
			
			StringBuilder publicKeyASxml = new StringBuilder("<RSAKeyValue><Modulus>");
			publicKeyASxml.append(strModulus);
			publicKeyASxml.append("</Modulus><Exponent>");
			publicKeyASxml.append(strExponent);
			publicKeyASxml.append("</Exponent></RSAKeyValue>");
			
			return publicKeyASxml.toString();
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			return "<RSAKeyValue><Modulus></Modulus><Exponent></Exponent></RSAKeyValue>";
		}	
	}
	
	private static byte[] removeSigPaddingOfBigInteger(byte[] a) {
		if(a[0] == 0) {
			byte[] tmp = new byte[a.length - 1];
			System.arraycopy(a, 1, tmp, 0, tmp.length);
			return tmp;
		}
		return a;
	}
}
