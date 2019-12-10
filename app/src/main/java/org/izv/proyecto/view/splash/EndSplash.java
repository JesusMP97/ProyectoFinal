package org.izv.proyecto.view.splash;

import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

public class EndSplash extends AsyncTask<Void, Integer, Void> {
    private int cont = 0;
    private boolean loading = true, loading2 = false;
    private OnSplash onSplash;
    private static final long DELAY = 50;

    public EndSplash(OnSplash onSplash) {
        this.onSplash = onSplash;
    }

    public EndSplash setLoading(boolean loading) {
        this.loading = loading;
        return this;
    }

    public EndSplash setLoading2(boolean loading2) {
        this.loading2 = loading2;
        return this;
    }

    @Override
    protected Void doInBackground(final Void... voids) {
        while (loading || loading2) {
            try {
                Thread.sleep(DELAY);
                publishProgress(cont);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);
        onSplash.onFinished();
    }
}