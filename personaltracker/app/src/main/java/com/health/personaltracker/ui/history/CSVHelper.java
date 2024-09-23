package com.health.personaltracker.ui.history;

import android.content.Context;
import android.net.Uri;

import com.health.personaltracker.model.Fasting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {

    public static void createCSVFile(Context context, List<Fasting> fastingList) {

        File externalCacheDir = context.getExternalCacheDir(); // Obtiene el directorio de caché externo
        // Verificar si el almacenamiento externo está disponible
        //if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        if (externalCacheDir != null) {
            // Crear un directorio (si no existe) para guardar el archivo CSV
            File directory = new File(externalCacheDir, "MyCSVFiles");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Crear el archivo CSV
            File csvFile = new File(directory, "data.csv");
            try {
                FileWriter writer = new FileWriter(csvFile);

                // Escribir encabezados
                writer.append("ID,Inicio,Fin,Horas\n");

                for (Fasting fasting : fastingList) {
                    String row = String.join(",",
                            String.valueOf(fasting.getUid()),
                            fasting.start_datetime,
                            fasting.end_datetime,
                            String.valueOf(fasting.hours),
                            "\n");
                    // Escribir datos
                    writer.append(row);
                }

                // Cerrar el archivo
                writer.flush();
                writer.close();

                System.out.println("Archivo CSV creado exitosamente!");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("El almacenamiento externo no está disponible o no es accesible.");
        }
    }
}
