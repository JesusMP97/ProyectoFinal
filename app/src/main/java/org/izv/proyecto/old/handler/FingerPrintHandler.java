package org.izv.proyecto.old.handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.Toast;

import org.izv.proyecto.MainActivity;


@SuppressLint("NewApi")
public class FingerPrintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerPrintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        //this.update("There was an Auth Error. " + errString, false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Auth Failed, you are not an admin.", false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error: " + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Welcome admin.", true);
    }

    private void update(String error, boolean result) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        if (result) {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }
}