package org.izv.proyecto.view.print;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.izv.proyecto.R;
import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Producto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class PrintDocument extends PrintDocumentAdapter {
    Context context;
    private static String NIF = "10511415N";
    private static String NOMBRE = "Jones Garcia, Francisco";
    private static String DIRECCION = "C/ Alameda, 8   Granada";
    private static String TELEFONO = "658012345";
    private int pageHeight;
    private int pageWidth;
    private PdfDocument myPdfDocument;
    private int totalpages = 2;
    private int startingFrom = 0;
    private int rest = 0;
    private double precioTotal = 0;
    private boolean end = false;
    List<Producto> productos;
    List<Comanda> comandas;

    public PrintDocument(Context context, List<Producto> productos, List<Comanda> comandas) {
        this.context = context;
        this.productos = productos;
        this.comandas = comandas;
        Log.v("xyz", "numero de comandas:  " + comandas.size());
        Log.v("xyz", "paginas extra; " + Math.ceil(comandas.size() / 24));
        this.totalpages = 1 + (comandas.size() + 24 - 1) / 24; // Ecuacion to guapa para calcular el numero de paginas extra en funcion de el numero de tuplas
        this.rest = comandas.size() % 24;

        if (rest == 0) { // Una pagina extra si las paginas quedan justas a la cantidad de comandas
            totalpages++;
        }
        Log.v("xyz", "numpaginas: " + totalpages + " resto: " + rest);
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        myPdfDocument = new PrintedPdfDocument(context, newAttributes);

        pageHeight = newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
        pageWidth = newAttributes.getMediaSize().getWidthMils() / 1000 * 72;

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        if (totalpages > 0) {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalpages);

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        for (int i = 0; i < totalpages; i++) {
            if (pageInRange(pages, i)) {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                        pageHeight, i).create();

                PdfDocument.Page page =
                        myPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                drawPage(page, i);
                myPdfDocument.finishPage(page);
            }
        }

        try {
            myPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }

        callback.onWriteFinished(pages);
    }

    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int i = 0; i < pageRanges.length; i++) {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }

    private void drawPage(PdfDocument.Page page, int pagenumber) {
        Date currentTime = Calendar.getInstance().getTime();
        pagenumber++;

        PdfDocument.PageInfo pageInfo = page.getInfo();

        Canvas canvas = page.getCanvas();

        int topMargin = 35;
        int leftMargin = 35;
        int rightMaximun = 575;

        Paint paint = new Paint();

        if (pagenumber == 1) {   // Pagina 1, header
            // MARCO TITULO
            paint.setColor(Color.BLACK);
            canvas.drawRect(leftMargin, topMargin, rightMaximun, topMargin + 150, paint);

            paint.setColor(Color.WHITE);
            canvas.drawRect(leftMargin + 2, topMargin + 2, rightMaximun - 2, topMargin + 148, paint);

            paint.setColor(Color.BLACK);
            canvas.drawRect(leftMargin + 4, topMargin + 4, rightMaximun - 4, topMargin + 146, paint);

            paint.setColor(Color.WHITE);

            // TITULO 'Android bar'
            paint.setTextSize(80);
            canvas.drawText(
                    "A",
                    leftMargin + 120,
                    topMargin + 80,
                    paint);

            paint.setTextSize(50);
            canvas.drawText(
                    "ndroid",
                    leftMargin + 170,
                    topMargin + 80,
                    paint);

            paint.setTextSize(70);
            canvas.drawText(
                    "B",
                    leftMargin + 210,
                    topMargin + 135,
                    paint);

            paint.setTextSize(40);
            canvas.drawText(
                    "ar",
                    leftMargin + 250,
                    topMargin + 115,
                    paint);

            paint.setColor(Color.BLACK);

            Drawable icon = context.getResources().getDrawable(R.drawable.androidlogo);
            icon.setBounds(leftMargin + 340, topMargin + 20, leftMargin + 470, topMargin + 140);
            icon.draw(canvas);

            // NIF
            paint.setTextSize(24);
            canvas.drawText(
                    "NIF " + NIF,
                    leftMargin + 180,
                    topMargin + 200,
                    paint);

            // Nombre
            paint.setTextSize(24);
            canvas.drawText(
                    NOMBRE,
                    leftMargin + 140,
                    topMargin + 250,
                    paint);

            // Direccion
            paint.setTextSize(24);
            canvas.drawText(
                    DIRECCION,
                    leftMargin + 140,
                    topMargin + 300,
                    paint);

            // Telefono
            paint.setTextSize(24);
            canvas.drawText(
                    "I.V.A. INCLUIDO - Tlf: " + TELEFONO,
                    leftMargin + 100,
                    topMargin + 350,
                    paint);

            // Fecha
            paint.setTextSize(24);
            canvas.drawText(
                    currentTime.toString(),
                    leftMargin + 70,
                    topMargin + 400,
                    paint);

            // Linea final divisora
            canvas.drawRect(leftMargin, topMargin + 440, rightMaximun, topMargin + 454, paint);

            paint.setColor(Color.WHITE);
            canvas.drawRect(leftMargin + 2, topMargin + 442, rightMaximun - 2, topMargin + 452, paint);

            paint.setColor(Color.BLACK);
            canvas.drawRect(leftMargin + 4, topMargin + 444, rightMaximun - 4, topMargin + 450, paint);

        } else if (pagenumber > 1) { // Pagina 2+, body

            // Donde termina el bucle de imprimir comanda
            int endAt = startingFrom + 24;

            // Casos en los que PRECIO TOTAL aparecera en la pagina
            if (endAt > comandas.size()) {    // Si el bucle termina antes del numero total de comandas
                endAt = startingFrom + rest;
                end = true;
            }
            if (endAt == startingFrom) { // Si las comandas ocupan la pagina entera
                end = true;
            }

            for (int i = startingFrom; i < endAt; i++) { // Bucle de imprimir comanda
                // Numero de veces pedido el producto en la comanda
                paint.setTextSize(24);
                canvas.drawText(
                        comandas.get(i).getUnidades() + "",
                        leftMargin,
                        topMargin,
                        paint);

                // Nombre producto
                paint.setTextSize(24);
                Producto p = new Producto();
                for (Producto product : productos) {
                    if (comandas.get(i).getIdproducto() == product.getId()) {
                        p = product;
                    }
                }
                canvas.drawText(
                        p.getNombre(),
                        leftMargin + 100,
                        topMargin,
                        paint);

                // Precio comanda
                paint.setTextSize(24);
                canvas.drawText(
                        comandas.get(i).getPrecio() + "€",
                        rightMaximun - 100,
                        topMargin,
                        paint);
                precioTotal += comandas.get(i).getPrecio();

                topMargin += 32; // La siguiente linea de comanda empieza x mas abajo
            }
            startingFrom += 24; // La siguiente pagina empieza por la x siguiente comanda

            if (end) {
                precioTotal = Math.ceil(precioTotal * 100) / 100;
                paint.setTextSize(24);
                canvas.drawText(
                        "TOTAL: " + precioTotal + "€",
                        rightMaximun - 200,
                        topMargin,
                        paint);
            }
        }
    }

    public File createPdf() { // METODO PARA CREAR Y ENVIAR PDF DE LA FACTURA
        int totalpages = 1 + (comandas.size() + 24 - 1) / 24;
        int rest = comandas.size() % 24;
        int pageWidth = 600;
        int pageHeight = 900;
        int startingFrom = 0;
        double precioTotal = 0;
        boolean end = false;

        if (rest == 0) { // Una pagina extra si las paginas quedan justas a la cantidad de comandas
            totalpages++;
        }

        // Create a new document
        PdfDocument document = new PdfDocument();

        // Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

        // Start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        // aaaa
        Date currentTime = Calendar.getInstance().getTime();

        int topMargin = 35;
        int leftMargin = 35;
        int rightMaximun = 575;

        // MARCO TITULO
        paint.setColor(Color.BLACK);
        canvas.drawRect(leftMargin, topMargin, rightMaximun, topMargin + 150, paint);

        paint.setColor(Color.WHITE);
        canvas.drawRect(leftMargin + 2, topMargin + 2, rightMaximun - 2, topMargin + 148, paint);

        paint.setColor(Color.BLACK);
        canvas.drawRect(leftMargin + 4, topMargin + 4, rightMaximun - 4, topMargin + 146, paint);

        paint.setColor(Color.WHITE);

        // TITULO 'Android bar'
        paint.setTextSize(80);
        canvas.drawText(
                "A",
                leftMargin + 120,
                topMargin + 80,
                paint);

        paint.setTextSize(50);
        canvas.drawText(
                "ndroid",
                leftMargin + 170,
                topMargin + 80,
                paint);

        paint.setTextSize(70);
        canvas.drawText(
                "B",
                leftMargin + 210,
                topMargin + 135,
                paint);

        paint.setTextSize(40);
        canvas.drawText(
                "ar",
                leftMargin + 250,
                topMargin + 115,
                paint);

        paint.setColor(Color.BLACK);

        Drawable icon = context.getResources().getDrawable(R.drawable.androidlogo);
        icon.setBounds(leftMargin + 340, topMargin + 20, leftMargin + 470, topMargin + 140);
        icon.draw(canvas);

        // NIF
        paint.setTextSize(24);
        canvas.drawText(
                "NIF " + NIF,
                leftMargin + 180,
                topMargin + 200,
                paint);

        // Nombre
        paint.setTextSize(24);
        canvas.drawText(
                NOMBRE,
                leftMargin + 140,
                topMargin + 250,
                paint);

        // Direccion
        paint.setTextSize(24);
        canvas.drawText(
                DIRECCION,
                leftMargin + 140,
                topMargin + 300,
                paint);

        // Telefono
        paint.setTextSize(24);
        canvas.drawText(
                "I.V.A. INCLUIDO - Tlf: " + TELEFONO,
                leftMargin + 100,
                topMargin + 350,
                paint);

        // Fecha
        paint.setTextSize(24);
        canvas.drawText(
                currentTime.toString(),
                leftMargin + 70,
                topMargin + 400,
                paint);

        // Linea final divisora
        canvas.drawRect(leftMargin, topMargin + 440, rightMaximun, topMargin + 454, paint);

        paint.setColor(Color.WHITE);
        canvas.drawRect(leftMargin + 2, topMargin + 442, rightMaximun - 2, topMargin + 452, paint);

        paint.setColor(Color.BLACK);
        canvas.drawRect(leftMargin + 4, topMargin + 444, rightMaximun - 4, topMargin + 450, paint);
        // aaa

        // Finish the page
        document.finishPage(page);

        // Create other pages
        for (int i = 1; i < totalpages; i++) {

            pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i + 1).create();
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();
            paint = new Paint();

            // Donde termina el bucle de imprimir comanda
            int endAt = startingFrom + 24;

            // Casos en los que PRECIO TOTAL aparecera en la pagina
            if (endAt > comandas.size()) {    // Si el bucle termina antes del numero total de comandas
                endAt = startingFrom + rest;
                end = true;
            }
            if (endAt == startingFrom) { // Si las comandas ocupan la pagina entera
                end = true;
            }

            for (int j = startingFrom; j < endAt; j++) { // Bucle de imprimir comanda

                // Numero de veces pedido el producto en la comanda
                paint.setTextSize(24);
                canvas.drawText(
                        comandas.get(j).getUnidades() + "",
                        leftMargin,
                        topMargin,
                        paint);

                // Nombre producto
                paint.setTextSize(24);
                Producto p = new Producto();
                for (Producto product : productos) {
                    if (comandas.get(j).getIdproducto() == product.getId()) {
                        p = product;
                    }
                }
                canvas.drawText(
                        p.getNombre(),
                        leftMargin + 100,
                        topMargin,
                        paint);

                // Precio comanda
                paint.setTextSize(24);
                canvas.drawText(
                        comandas.get(j).getPrecio() + "€",
                        rightMaximun - 100,
                        topMargin,
                        paint);
                precioTotal += comandas.get(j).getPrecio();

                topMargin += 32; // La siguiente linea de comanda empieza x mas abajo
            }
            startingFrom += 24; // La siguiente pagina empieza por la x siguiente comanda

            if (end) {
                precioTotal = Math.ceil(precioTotal * 100) / 100;
                paint.setTextSize(24);
                canvas.drawText(
                        "TOTAL: " + precioTotal + "€",
                        rightMaximun - 200,
                        topMargin,
                        paint);
            }

            document.finishPage(page);
            topMargin = 35;
            leftMargin = 35;
        }
        // Write the document content
        File filePath = new File(context.getExternalFilesDir(null), "Factura.pdf");
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(context, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // Close the document
        document.close();
        return filePath;
    }
}
