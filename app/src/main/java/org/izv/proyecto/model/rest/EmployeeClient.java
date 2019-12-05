package org.izv.proyecto.model.rest;

import org.izv.proyecto.model.data.Empleado;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EmployeeClient {
    @DELETE("empleado/{id}")
    Call<Long> delete(@Path("id") long id);

    @GET("empleado/{id}")
    Call<Empleado> get(@Path("id") long id);

    @GET("empleado")
    Call<List<Empleado>> getAll();

    @POST("empleado")
    Call<Long> post(@Body Empleado empleado);

    @PUT("empleado/{id}")
    Call<Long> put(@Path("id") long id, @Body Empleado empleado);
}
