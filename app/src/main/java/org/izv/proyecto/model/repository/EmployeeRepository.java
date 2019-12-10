package org.izv.proyecto.model.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.izv.proyecto.model.data.Empleado;
import org.izv.proyecto.model.rest.EmployeeClient;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeRepository implements Repository.Data<Empleado> {
    private static final String CONEXION = "Conexion failure";
    private static final long EMPTY = 0;
    private static final String TAG = EmployeeRepository.class.getName();
    private MutableLiveData<List<Empleado>> all = new MutableLiveData<>();
    private EmployeeClient client;
    private Repository.OnFailureListener onFailureListener;
    private Retrofit retrofit;

    public EmployeeRepository(String url) {
        Log.v("xyz", url + "ads");
        retrieveApiClient(url);
        fetchAll();
    }

    private void retrieveApiClient(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/ProyectoFinal/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(EmployeeClient.class);
    }

    public void setUrl(String url) {
        retrieveApiClient(url);
        fetchAll();
    }

    public void setOnFailureListener(Repository.OnFailureListener onFailureListener) {
        this.onFailureListener = onFailureListener;
    }

    @Override
    public void add(Empleado object) {
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
                if (t instanceof SocketTimeoutException) {
                    onFailureListener.onConnectionFailure();
                }
            }
        });
    }

    @Override
    public void delete(Empleado object) {
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
                if (t instanceof SocketTimeoutException) {
                    onFailureListener.onConnectionFailure();
                }
            }
        });
    }

    @Override
    public void fetchAll() {
        Call<List<Empleado>> call = client.getAll();
        call.enqueue(new Callback<List<Empleado>>() {
            @Override
            public void onResponse(Call<List<Empleado>> call, Response<List<Empleado>> response) {
                Log.v(TAG, response.raw().toString());
                all.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Empleado>> call, Throwable t) {
                all = new MutableLiveData<>();
                if (t instanceof SocketTimeoutException || t instanceof UnknownHostException) {
                    onFailureListener.onConnectionFailure();
                }
            }
        });
    }

    @Override
    public MutableLiveData<List<Empleado>> getAll() {
        fetchAll();
        return all;
    }

    @Override
    public void update(Empleado object) {
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
                Log.v("abc", t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    onFailureListener.onConnectionFailure();
                }
            }
        });
    }

    @Override
    public void upload(File file) {

    }
}
