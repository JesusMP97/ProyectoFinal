package org.izv.proyecto.model;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Empleado;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.model.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private static final String TAG = Repository.class.getName();
    private ApiClient apiClient;
    private MutableLiveData<List<Empleado>> liveEmployeeList = new MutableLiveData<>();
    private MutableLiveData<List<Factura>> liveInvoices;
    private MutableLiveData<List<Producto>> liveProductList = new MutableLiveData();
    private MutableLiveData<List<Mesa>> liveTableList = new MutableLiveData();
    private Retrofit retrofit;

    public Repository(String url) {
        liveInvoices = new MutableLiveData<>();
        retrieveApiClient(url);
        fetchInvoices();
    }

    public void add(Factura invoice) {
        Call<Long> call = apiClient.postFactura(invoice);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                long resultado = response.body();
                if (resultado > 0) {
                    fetchInvoices();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
            }
        });
    }

    public void add(Comanda command) {
        Call<Long> call = apiClient.postComanda(command);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
            }
        });
    }

    public void fetchEmployeeList() {
        Call<List<Empleado>> call = apiClient.getEmpleados();
        call.enqueue(new Callback<List<Empleado>>() {

            @Override
            public void onResponse(Call<List<Empleado>> call, Response<List<Empleado>> response) {
                liveEmployeeList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Empleado>> call, Throwable t) {
            }
        });
    }

    public void fetchInvoices() {
        Call<List<Factura>> call = apiClient.getFacturas();
        call.enqueue(new Callback<List<Factura>>() {

            @Override
            public void onResponse(Call<List<Factura>> call, Response<List<Factura>> response) {
                Log.v(TAG, response.raw().toString());
                liveInvoices.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Factura>> call, Throwable t) {
            }
        });
    }

    public void fetchProductList() {
        Call<List<Producto>> call = apiClient.getProductosPorCategoria();
        call.enqueue(new Callback<List<Producto>>() {

            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                liveProductList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
            }
        });
    }

    public void fetchTableList() {
        Call<List<Mesa>> call = apiClient.getMesas();
        call.enqueue(new Callback<List<Mesa>>() {

            @Override
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                Log.v(TAG, call.request().toString());
                liveTableList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Mesa>> call, Throwable t) {
                Log.v(TAG, t.getMessage());
            }
        });
    }

    public MutableLiveData<List<Empleado>> getLiveEmployeeList() {
        fetchEmployeeList();
        return liveEmployeeList;
    }

    public MutableLiveData<List<Factura>> getLiveInvoices() {
        fetchInvoices();
        return liveInvoices;
    }

    public MutableLiveData<List<Producto>> getLiveProductList() {
        fetchProductList();
        return liveProductList;
    }

    public MutableLiveData<List<Mesa>> getLiveTableList() {
        fetchTableList();
        return liveTableList;
    }

    private void retrieveApiClient(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/web/ProyectoFinal/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiClient = retrofit.create(ApiClient.class);
    }


    public void setUrl(String url) {
        retrieveApiClient(url);
    }

    public void update(Mesa table) {
        Call<Long> call = apiClient.putMesa(table.getId(), table);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
            }
        });
    }
}