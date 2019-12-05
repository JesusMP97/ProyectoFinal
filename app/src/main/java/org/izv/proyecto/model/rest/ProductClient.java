package org.izv.proyecto.model.rest;

import org.izv.proyecto.model.data.Empleado;
import org.izv.proyecto.model.data.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductClient {
    @DELETE("producto/{id}")
    Call<Long> delete(@Path("id") long id);

    @GET("producto/{id}")
    Call<Producto> get(@Path("id") long id);

    @GET("producto")
    Call<List<Producto>> getAll();

    @POST("producto")
    Call<Long> post(@Body Producto product);

    @PUT("producto/{id}")
    Call<Long> put(@Path("id") long id, @Body Producto product);
}
