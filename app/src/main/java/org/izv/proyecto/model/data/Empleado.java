package org.izv.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Empleado implements Parcelable {
    public static final Creator<Empleado> CREATOR = new Creator<Empleado>() {
        @Override
        public Empleado createFromParcel(Parcel in) {
            return new Empleado(in);
        }

        @Override
        public Empleado[] newArray(int size) {
            return new Empleado[size];
        }
    };
    private String apellido1;
    private String apellido2;
    private String clave;
    private long id;
    private String login;
    private String nombre;

    public Empleado() {

    }

    protected Empleado(Parcel in) {
        id = in.readLong();
        login = in.readString();
        clave = in.readString();
        nombre = in.readString();
        apellido1 = in.readString();
        apellido2 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(login);
        dest.writeString(clave);
        dest.writeString(nombre);
        dest.writeString(apellido1);
        dest.writeString(apellido2);
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", clave='" + clave + '\'' +
                '}';
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

    public String getClave() {
        return clave;
    }

    public Empleado setClave(String clave) {
        this.clave = clave;
        return this;
    }

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

    public String getNombre() {
        return nombre;
    }

    public Empleado setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }
}
