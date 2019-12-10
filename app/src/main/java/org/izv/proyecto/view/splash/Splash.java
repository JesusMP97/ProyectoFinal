package org.izv.proyecto.view.splash;

import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

public class Splash extends AsyncTask<Void, Integer, Integer> {
    private int cont = 0;
    private boolean loading = true, loading2 = false;
    private TypedArray loadingBg;
    private ImageView ivLoading;
    private OnSplash onSplash;
    private static final long DELAY = 50;
    private static final int DEFAULT_VALUE = 0;

    public Splash(TypedArray loadingBg, ImageView ivLoading, AlertDialog loadingDialog, OnSplash onSplash) {
        this.loadingBg = loadingBg;
        this.ivLoading = ivLoading;
        this.onSplash = onSplash;
    }

    public Splash setLoading(boolean loading) {
        this.loading = loading;
        return this;
    }

    public Splash setLoading2(boolean loading2) {
        this.loading2 = loading2;
        return this;
    }

    @Override
    protected Integer doInBackground(final Void... voids) {
        while (loading || loading2) {
            if (cont >= loadingBg.length()) {
                cont = DEFAULT_VALUE;
            }
            try {
                Thread.sleep(DELAY);
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