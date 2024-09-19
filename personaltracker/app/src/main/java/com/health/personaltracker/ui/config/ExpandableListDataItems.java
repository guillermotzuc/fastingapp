package com.health.personaltracker.ui.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataItems {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableDetailList = new HashMap<String, List<String>>();

        // As we are populating List of fruits, vegetables and nuts, using them here
        // We can modify them as per our choice.
        // And also choice of fruits/vegetables/nuts can be changed
        List<String> bibliografia = new ArrayList<String>();
        bibliografia.add("'Manual de estoicismo' by Epicteto");
        bibliografia.add("Lecciones de epicureísmo: El arte de la felicidad");
        bibliografia.add("'Sobre la constancia del sabio' by Séneca");
        bibliografia.add("'Sobre la felicidad' by Séneca.");
        bibliografia.add("'De la brevedad de la vida' by Séneca.");
        bibliografia.add("'De la ira' by Séneca.");

        List<String> contacto = new ArrayList<String>();
        contacto.add("email: guillermo.tzuc@gmail.com");

        expandableDetailList.put("Bibliografia", bibliografia);
        expandableDetailList.put("Contacto", contacto);
        return expandableDetailList;
    }
}

