package org.izv.proyecto.model.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.rest.CommandClient;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommandRepository implements Repository.Data<Comanda> {
    private static final long EMPTY = 0;
    private static final String TAG = CommandRepository.class.getName();
    private MutableLiveData<List<Comanda>> all = new MutableLiveData<>();
    private CommandClient client;
    private Repository.OnFailureListener onFailureListener;
    private Retrofit retrofit;

    public CommandRepository(String url) {
        retrieveApiClient(url);
        fetchAll();
    }

    private void retrieveApiClient(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/ProyectoFinal/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(CommandClient.class);
    }

    public void setUrl(String url) {
        retrieveApiClient(url);
    }

    @Override
    public void add(Comanda object) {
        Call<Long> call = client.post(object);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                long result = response.body();
                Log.v(TAG, String.valueOf(result));
                if (result > EMPTY) {
                    fetchAll();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v(TAG, t.getCause().getMessage());
            }
        });
    }

    @Override
    public void delete(Comanda object) {
        Call<Long> call = client.delete(object.getId());
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                long result = response.body();
                Log.v(TAG, String.valueOf(result));
                if (result > EMPTY) {
                    fetchAll();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v(TAG, t.getCause().getMessage());
            }
        });
    }

    @Override
    public void fetchAll() {
        Call<List<Comanda>> call = client.getAll();
        call.enqueue(new Callback<List<Comanda>>() {
            @Override
            public void onResponse(Call<List<Comanda>> call, Response<List<Comanda>> response) {
                Log.v(TAG, response.raw().toString());
                all.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Comanda>> call, Throwable t) {
                Log.v(TAG, t.getCause().getMessage());
                all = new MutableLiveData<>();
            }
        });
    }

    @Override
    public MutableLiveData<List<Comanda>> getAll() {
        fetchAll();
        return all;
    }

    @Override
    public void update(Comanda object) {
        Call<Long> call = client.put(object.getId(), object);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                long result = response.body();
                Log.v(TAG, String.valueOf(result));
                if (result > EMPTY) {
                    fetchAll();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v(TAG, t.getCause().getMessage());
            }
        });
    }

    @Override
    public void upload(File file) {

    }
}
