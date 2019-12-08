package org.izv.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Contenedor {

    private List<CommandDetail> commandDetailList;
    private List<Contenedor> containerList;
    private Factura invoice;
    private Mesa table;

    public Contenedor() {
        containerList = new ArrayList<>();
    }

    public Contenedor(Factura invoice, Mesa table, List<CommandDetail> commandDetailList) {
        this.invoice = invoice;
        this.table = table;
        this.commandDetailList = commandDetailList;
    }

    @Override
    public String toString() {
        return "Contenedor{" +
                "invoice=" + invoice +
                ", commandDetail=" + commandDetailList +
                ", containerList=" + containerList +
                '}';
    }

    public List<CommandDetail> getCommandDetail() {
        return commandDetailList;
    }

    public Contenedor setCommandDetail(List<CommandDetail> commandDetailList) {
        this.commandDetailList = commandDetailList;
        return this;
    }

    public List<CommandDetail> getCommandDetailList() {
        return commandDetailList;
    }

    public Contenedor setCommandDetailList(List<CommandDetail> commandDetailList) {
        this.commandDetailList = commandDetailList;
        return this;
    }

    public List<Contenedor> getContainerList() {
        return containerList;
    }

    public Contenedor setContainerList(List<Contenedor> containerList) {
        this.containerList = containerList;
        return this;
    }

    public Factura getInvoice() {
        return invoice;
    }

    public Contenedor setInvoice(Factura invoice) {
        this.invoice = invoice;
        return this;
    }

    public Mesa getTable() {
        return table;
    }

    public Contenedor setTable(Mesa table) {
        this.table = table;
        return this;
    }

    public static class CommandDetail implements Parcelable {
        private Comanda command;
        private Producto product;

        public CommandDetail() {

        }

        public CommandDetail(Comanda command, Producto product) {
            this.command = command;
            this.product = product;
        }

        protected CommandDetail(Parcel in) {
            command = in.readParcelable(Comanda.class.getClassLoader());
            product = in.readParcelable(Producto.class.getClassLoader());
        }

        public static final Creator<CommandDetail> CREATOR = new Creator<CommandDetail>() {
            @Override
            public CommandDetail createFromParcel(Parcel in) {
                return new CommandDetail(in);
            }

            @Override
            public CommandDetail[] newArray(int size) {
                return new CommandDetail[size];
            }
        };

        @Override
        public String toString() {
            return "CommandDetail{" +
                    "command=" + command.toString() +
                    ", productList=" + product.toString() +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(command, flags);
            dest.writeParcelable(product, flags);
        }

        public Comanda getCommand() {
            return command;
        }

        public CommandDetail setCommand(Comanda command) {
            this.command = command;
            return this;
        }

        public Producto getProduct() {
            return product;
        }

        public CommandDetail setProduct(Producto product) {
            this.product = product;
            return this;
        }
    }
}
