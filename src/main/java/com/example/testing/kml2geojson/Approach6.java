//package com.example.testing.kml2geojson;
//
//import org.geotools.data.DataStore;
//import org.geotools.data.DataStoreFinder;
//import org.geotools.data.simple.SimpleFeatureCollection;
//import org.geotools.data.simple.SimpleFeatureIterator;
//import org.geotools.geojson.feature.FeatureJSON;
//import org.opengis.feature.simple.SimpleFeature;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.HashMap;
//import java.util.Map;
//public class Approach6 {
//
//    public static void convertKMLtoGeoJSON(String kmlFilePath, String geoJsonFilePath) {
//        try {
//            File kmlFile = new File(kmlFilePath);
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("url", kmlFile.toURI().toURL());
//
//            //getting dataStore as null
//            DataStore dataStore = DataStoreFinder.getDataStore(map);
//            String typeName = dataStore.getTypeNames()[0];
//            SimpleFeatureCollection featureCollection = dataStore.getFeatureSource(typeName).getFeatures();
//
//            File geoJsonFile = new File(geoJsonFilePath);
//            FileOutputStream outputStream = new FileOutputStream(geoJsonFile);
//
//            FeatureJSON featureJSON = new FeatureJSON();
//            featureJSON.writeFeatureCollection(featureCollection, outputStream);
//
//            outputStream.close();
//            dataStore.dispose();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
