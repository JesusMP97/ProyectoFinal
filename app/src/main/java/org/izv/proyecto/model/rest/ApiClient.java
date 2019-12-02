package org.izv.proyecto.model.rest;

import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Empleado;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.model.data.Producto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiClient {
    @DELETE("empleado/{id}")
    Call<Long> deleteEmpleado(@Path("id") long id);

    @GET("empleado/{id}")
    Call<Empleado> getEmpleado(@Path("id") long id);

    @GET("empleado")
    Call<List<Empleado>> getEmpleados();

    @POST("empleado")
    Call<Long> postEmpleado(@Body Empleado empleado);

    @PUT("empleado/{id}")
    Call<Long> putEmpleado(@Path("id") long id, @Body Empleado empleado);

    @DELETE("producto/{id}")
    Call<Long> deleteProducto(@Path("id") long id);

    @GET("producto/{id}")
    Call<Producto> getProducto(@Path("id") long id);

    @GET("producto")
    Call<List<Producto>> getProductosPorCategoria();

    @GET("producto/categorias/{categoria?}")
    Call<List<String>> getCategorias();

    @GET("producto/categoria/{nombre}")
    Call<List<Producto>> getProductosPorCategoria(@Path("nombre") String name);

    @GET("producto/subcategoria/{nombre}")
    Call<List<Producto>> getProductosPorSubCategoria(@Path("nombre") String name);

    //@GET("producto/categoria/{nombre}")
    //Call<List<String>> getCategorias(@Path("nombre") Object nombre);

    @POST("producto")
    Call<Long> postProducto(@Body Producto producto);

    @PUT("producto/{id}")
    Call<Long> putProducto(@Path("id") long id, @Body Producto producto);

    @DELETE("factura/{id}")
    Call<Long> deleteFactura(@Path("id") long id);

    @GET("factura/{id}")
    Call<Factura> getFactura(@Path("id") long id);

    @GET("factura")
    Call<List<Factura>> getFacturas();

    @POST("factura")
    Call<Long> postFactura(@Body Factura factura);

    @PUT("factura/{id}")
    Call<Long> putFactura(@Path("id") long id, @Body Factura factura);

    @DELETE("comanda/{id}")
    Call<Long> deleteComanda(@Path("id") long id);

    @GET("comanda/{id}")
    Call<Comanda> getComanda(@Path("id") long id);

    @GET("comanda")
    Call<List<Comanda>> getComandas();

    @POST("comanda")
    Call<Long> postComanda(@Body Comanda comanda);

    @PUT("comanda/{id}")
    Call<Long> putComanda(@Path("id") long id, @Body Comanda comanda);

    @DELETE("mesa/{id}")
    Call<Long> deleteMesa(@Path("id") long id);

    @GET("mesa/{id}")
    Call<Mesa> getMesa(@Path("id") long id);

    @GET("mesa")
    Call<List<Mesa>> getMesas();

    @POST("mesa")
    Call<Long> postMesa(@Body Mesa mesa);

    @PUT("mesa/{id}")
    Call<Long> putMesa(@Path("id") long id, @Body Mesa mesa);
}
