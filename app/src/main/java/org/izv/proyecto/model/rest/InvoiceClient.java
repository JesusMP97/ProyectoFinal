package org.izv.proyecto.model.rest;

import androidx.lifecycle.MutableLiveData;

import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface InvoiceClient {
    @DELETE("factura/{id}")
    Call<Long> delete(@Path("id") long id);

    @GET("factura/{id}")
    Call<Factura> get(@Path("id") long id);

    @GET("factura")
    Call<List<Factura>> getAll();

    @POST("factura")
    Call<Long> post(@Body Factura invoice);

    @PUT("factura/{id}")
    Call<Long> put(@Path("id") long id, @Body Factura invoice);
}
