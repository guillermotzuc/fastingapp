package com.health.personaltracker.ui.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataItems {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableDetailList = new HashMap<String, List<String>>();

        List<String> bibliografia = new ArrayList<>();
        bibliografia.add("'Manual de estoicismo' by Epicteto");
        bibliografia.add("Lecciones de epicureísmo: El arte de la felicidad");
        bibliografia.add("'Sobre la constancia del sabio' by Séneca");
        bibliografia.add("'Sobre la felicidad' by Séneca.");
        bibliografia.add("'De la brevedad de la vida' by Séneca.");
        bibliografia.add("'De la ira' by Séneca.");
        bibliografia.add("'Enquiridion' Epicteto.");

        List<String> contacto = new ArrayList<>();
        contacto.add("email: guillermo.tzuc@gmail.com");

        expandableDetailList.put("Bibliografia", bibliografia);
        expandableDetailList.put("Contacto", contacto);
        return expandableDetailList;
    }
}

