package org.izv.proyecto.model.data;

public class Empleado {
    private long id;
    private String login;
    private String clave;
    private String nombre;
    private String apellido1;
    private String apellido2;

    public long getId() {
        return id;
    }

    public Empleado setId(long id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Empleado setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getClave() {
        return clave;
    }

    public Empleado setClave(String clave) {
        this.clave = clave;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Empleado setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getApellido1() {
        return apellido1;
    }

    public Empleado setApellido1(String apellido1) {
        this.apellido1 = apellido1;
        return this;
    }

    public String getApellido2() {
        return apellido2;
    }

    public Empleado setApellido2(String apellido2) {
        this.apellido2 = apellido2;
        return this;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", clave='" + clave + '\'' +
                '}';
    }
}
