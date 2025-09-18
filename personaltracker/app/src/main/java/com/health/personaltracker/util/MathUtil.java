package com.health.personaltracker.util;

import java.util.Collections;
import java.util.List;

public class MathUtil {

    public static double getMedianHours(List<Integer> list) {
        if (list == null || list.isEmpty()) return 0;

        // Ordenar la lista
        Collections.sort(list);

        int n = list.size();
        if (n % 2 == 1) {
            // Si es impar, mediana = valor del medio
            return list.get(n / 2);
        } else {
            // Si es par, mediana = promedio de los dos valores centrales
            return (list.get(n / 2 - 1) + list.get(n / 2)) / 2.0;
        }
    }
}
