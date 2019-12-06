package org.izv.proyecto.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private Retrofit retrofit;

    protected Repository(String url) {
        retrieveApiClient(url);
    }

    protected void retrieveApiClient(String url) {
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/ProyectoFinal/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    protected void setUrl(String url) {
        retrieveApiClient(url);
    }

    protected Retrofit getRetrofit() {
        return retrofit;
    }


    public interface Data<T> {
        void add(T object);

        void delete(T object);

        void fetchAll();

        MutableLiveData<List<T>> getAll();

        void update(T object);

        void upload(File file);
    }

    public interface OnFailureListener {
        void onConnectionFailure();
    }
}
