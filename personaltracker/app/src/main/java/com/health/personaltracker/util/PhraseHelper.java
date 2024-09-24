package com.health.personaltracker.util;

import com.google.gson.Gson;
import com.health.personaltracker.model.Phrase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class PhraseHelper {

    public static String getTodayPhrase(InputStream XmlFileInputStream ) {

        //2 This reads your JSON file
        String jsonString = readTextFile(XmlFileInputStream);

        // create a gson object
        Gson gson = new Gson();
        // read your json file into an array
        Phrase[] questions = gson.fromJson(jsonString, Phrase[].class);
        // convert your array to a list using the Arrays utility class

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        String seed = String.format("%d%d%d", year, month, day);
        Random rand = new Random(Integer.parseInt(seed));
        int low = 1;
        int high = questions.length - 1;
        int result = rand.nextInt(high - low) + low;
        return "\"" + questions[result].getPhrase() + "\"";
    }

    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
}
