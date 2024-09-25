package com.health.personaltracker.ui.info;

import android.content.Context;

import com.health.personaltracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataItems {

    public static HashMap<String, List<String>> getData(Context context) {

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
        contacto.add("Contacto:" + context.getString(R.string.contact_mail));
        contacto.add("App Version:" + context.getString(R.string.app_version));

        expandableDetailList.put("Bibliografía", bibliografia);
        expandableDetailList.put("Información", contacto);
        return expandableDetailList;
    }
}

