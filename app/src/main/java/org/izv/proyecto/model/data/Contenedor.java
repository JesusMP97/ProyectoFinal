package org.izv.proyecto.model.data;

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

    public static class CommandDetail {
        private Comanda command;
        private Producto product;

        public CommandDetail() {

        }

        public CommandDetail(Comanda command, Producto product) {
            this.command = command;
            this.product = product;
        }

        @Override
        public String toString() {
            return "CommandDetail{" +
                    "command=" + command.toString() +
                    ", productList=" + product.toString() +
                    '}';
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
