package org.jastacry.layer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public abstract class AbsCipherLayer extends AbsLayer {

	protected PBEKeySpec pbeKeySpec;
	protected PBEParameterSpec pbeParamSpec;
	protected SecretKeyFactory keyFac;
	protected SecretKey pbeKey;
	protected String ALG;

	public AbsCipherLayer() {
		super();
	}

	@Override
	public void encStream(InputStream is, OutputStream os) throws IOException {
	    // Create PBE Cipher
	    Cipher pbeCipher;
		try {
			pbeCipher = Cipher.getInstance(ALG);
	        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
	
	        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
	        int nRead;
	        byte[] data = new byte[1024];
	
	        while ((nRead = is.read(data, 0, data.length)) != -1) {
	          buffer.write(data, 0, nRead);
	        }
	
	        buffer.flush();
	
	        byte[] bInput = buffer.toByteArray();
	        
	        // Encrypt the cleartext
	        byte[] ciphertext = pbeCipher.doFinal(bInput);
	        os.write(ciphertext);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void decStream(InputStream is, OutputStream os) throws IOException {
	    // Create PBE Cipher
	    Cipher pbeCipher;
		try {
			pbeCipher = Cipher.getInstance(ALG);
	        pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
	
	        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
	        int nRead;
	        byte[] data = new byte[1024];
	
	        while ((nRead = is.read(data, 0, data.length)) != -1) {
	          buffer.write(data, 0, nRead);
	        }
	
	        buffer.flush();
	
	        byte[] bInput = buffer.toByteArray();
	        
	        // Encrypt the cleartext
	        byte[] ciphertext = pbeCipher.doFinal(bInput);
	        os.write(ciphertext);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
	}

}