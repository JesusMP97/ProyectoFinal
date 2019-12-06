package org.izv.proyecto.model.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.rest.InvoiceClient;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoiceRepository implements Repository.Data<Factura> {

    private static final long EMPTY = 0;
    private static final String TAG = InvoiceRepository.class.getName();
    private MutableLiveData<List<Factura>> all = new MutableLiveData<>();
    private InvoiceClient client;
    private Retrofit retrofit;
    private Repository.OnFailureListener onFailureListener;
    private MutableLiveData<Long> invoiceId = new MutableLiveData<>();

    public InvoiceRepository(String url) {
        retrieveApiClient(url);
        fetchAll();
    }

    public void setOnFailureListener(Repository.OnFailureListener onFailureListener) {
        this.onFailureListener = onFailureListener;
    }

    private void retrieveApiClient(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/ProyectoFinal/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(InvoiceClient.class);
    }

    public void setUrl(String url) {
        retrieveApiClient(url);
    }

    @Override
    public void add(Factura object) {
        Call<Long> call = client.post(object);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.body() != null) {
                    long result = response.body();
                    Log.v(TAG, String.valueOf(result));
                    invoiceId.setValue(response.body());
                    if (result > EMPTY) {
                        fetchAll();
                    }
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
    public void delete(Factura object) {
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
        Call<List<Factura>> call = client.getAll();
        call.enqueue(new Callback<List<Factura>>() {
            @Override
            public void onResponse(Call<List<Factura>> call, Response<List<Factura>> response) {
                Log.v(TAG, response.raw().toString());
                all.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Factura>> call, Throwable t) {
                all = new MutableLiveData<>();
                if (t instanceof SocketTimeoutException) {
                    onFailureListener.onConnectionFailure();
                }
            }
        });
    }

    @Override
    public MutableLiveData<List<Factura>> getAll() {
        fetchAll();
        return all;
    }


    @Override
    public void update(Factura object) {
        Call<Long> call = client.put(object.getId(), object);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.body() != null) {
                    long result = response.body();
                    Log.v(TAG, String.valueOf(result));
                    if (result > EMPTY) {
                        fetchAll();
                    }
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
    public void upload(File file) {

    }

    public MutableLiveData<Long> getInvoiceId() {
        return invoiceId;
    }
}
