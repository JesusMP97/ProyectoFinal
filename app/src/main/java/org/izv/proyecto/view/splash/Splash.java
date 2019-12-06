package org.izv.proyecto.view.splash;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import org.izv.proyecto.Login;

public class Splash extends AsyncTask<Void, Integer, Integer> {
    private int cont = 0;
    private boolean loading;
    private TypedArray loadingBg;
    private ImageView ivLoading;
    private AlertDialog loadingDialog;
    private OnSplash onSplash;

    public Splash(boolean loading, TypedArray loadingBg, ImageView ivLoading, AlertDialog loadingDialog, OnSplash onSplash) {
        this.loading = loading;
        this.loadingBg = loadingBg;
        this.ivLoading = ivLoading;
        this.loadingDialog = loadingDialog;
        this.onSplash = onSplash;
    }

    public Splash setLoading(boolean loading) {
        this.loading = loading;
        return this;
    }

    public boolean isLoading() {
        return loading;
    }

    @Override
    protected Integer doInBackground(final Void... voids) {
        while (loading) {
            if (cont >= loadingBg.length()) {
                cont = 0;
            }
            try {
                Thread.sleep(50);
                publishProgress(cont);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cont++;
        }
        return cont;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        ivLoading.setImageDrawable(loadingBg.getDrawable(values[0]));
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        onSplash.onFinished();
    }
}