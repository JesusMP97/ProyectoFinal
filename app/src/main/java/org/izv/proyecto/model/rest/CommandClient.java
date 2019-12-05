package org.izv.proyecto.model.rest;

import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Empleado;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommandClient {
    @DELETE("comanda/{id}")
    Call<Long> delete(@Path("id") long id);

    @GET("comanda/{id}")
    Call<Comanda> get(@Path("id") long id);

    @GET("comanda")
    Call<List<Comanda>> getAll();

    @POST("comanda")
    Call<Long> post(@Body Comanda command);

    @PUT("comanda/{id}")
    Call<Long> put(@Path("id") long id, @Body Comanda command);
}
