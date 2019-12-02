package org.izv.proyecto.old;

import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;

import androidx.appcompat.app.AppCompatActivity;

import org.izv.proyecto.old.handler.FingerPrintHandler;

import java.security.KeyStore;

import javax.crypto.Cipher;

public class FingerPrint extends AppCompatActivity {
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private static final String KEY_NAME = "androidKey";

    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
//        }
//        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

//    private Login checkFingerPrintRequirements() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
//            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//            //Device has Fingerprint Scanner
//            if (!fingerprintManager.isHardwareDetected()) {
//                Toast.makeText(this, getString(R.string.deviceHardwareNotDetected), Toast.LENGTH_SHORT).show();
//                //Have permission to use fingerprint scanner in the app
//            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, getString(R.string.fingerprintPermissionNotGranted), Toast.LENGTH_SHORT).show();
//                //Lock screen is secured with atleast 1 type of lock
//            } else if (!keyguardManager.isKeyguardSecure()) {
//                Toast.makeText(this, getString(R.string.lockScreenIsSecured), Toast.LENGTH_SHORT).show();
//                // Atleast 1 Fingerprint is registered
//            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
//                Toast.makeText(this, getString(R.string.unregisteredFingerprint), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, getString(R.string.accessApp), Toast.LENGTH_SHORT).show();
//                generateKey();
//                if (cipherInit()) {
//                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
//                    FingerPrintHandler fingerprintHandler = new FingerPrintHandler(this);
//                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
//                }
//            }
//
//        }
//        return this;
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    private Login generateKey() {
//        try {
//            keyStore = KeyStore.getInstance("AndroidKeyStore");
//            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//            keyStore.load(null);
//            keyGenerator.init(new
//                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
//                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//                    .setUserAuthenticationRequired(true)
//                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
//                    .build());
//            keyGenerator.generateKey();
//        } catch (KeyStoreException | IOException | CertificateException
//                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
//                | NoSuchProviderException e) {
//            e.printStackTrace();
//        }
//        return this;
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    public boolean cipherInit() {
//        boolean result = true;
//        try {
//            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
//            throw new RuntimeException(getString(R.string.getCipherError), e);
//        }
//        try {
//            keyStore.load(null);
//            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
//                    null);
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//        } catch (KeyPermanentlyInvalidatedException e) {
//            result = false;
//        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
//            throw new RuntimeException(getString(R.string.failedCipherError), e);
//        }
//        return result;
//    }
}
