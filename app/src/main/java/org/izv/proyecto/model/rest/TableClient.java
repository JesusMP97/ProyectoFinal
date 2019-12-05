package org.izv.proyecto.model.rest;

import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.model.data.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TableClient {
    @DELETE("mesa/{id}")
    Call<Long> delete(@Path("id") long id);

    @GET("mesa/{id}")
    Call<Mesa> get(@Path("id") long id);

    @GET("mesa")
    Call<List<Mesa>> getAll();

    @POST("mesa")
    Call<Long> post(@Body Mesa table);

    @PUT("mesa/{id}")
    Call<Long> put(@Path("id") long id, @Body Mesa table);
}
