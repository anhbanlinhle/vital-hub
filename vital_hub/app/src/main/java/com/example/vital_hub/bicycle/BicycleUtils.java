package com.example.vital_hub.bicycle;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class BicycleUtils {
    private static final double CALORIES_BURNED_PER_KM = 0.05;
    private static final double EARTH_RADIUS = 6371;
    public static class CyclingResults {
        public double distances;
        public double calories;
    }
    public static CyclingResults calculateRouteInfo(ArrayList<LatLng> route) {
        CyclingResults results = new CyclingResults();

        if (route == null || route.size() < 2) {
            return results;
        }

        double distances = 0.0;

        for (int i = 1; i < route.size(); i++) {
            LatLng startLatLng = route.get(i - 1);
            LatLng endLatLng = route.get(i);
            double distance = calculateDistance(startLatLng, endLatLng);
            distances += distance;
        }

        double calories = distances * CALORIES_BURNED_PER_KM;

        results.distances = distances;
        results.calories = calories;

        return results;
    }

    private static double calculateDistance(LatLng startLatLng, LatLng endLatLng) {
        double dLat = Math.toRadians(endLatLng.latitude - startLatLng.latitude);
        double dLng = Math.toRadians(endLatLng.longitude - startLatLng.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLatLng.latitude)) * Math.cos(Math.toRadians(endLatLng.latitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
