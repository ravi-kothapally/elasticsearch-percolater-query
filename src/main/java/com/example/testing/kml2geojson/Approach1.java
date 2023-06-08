//package com.example.testing.kml2geojson;
//
//import org.geotools.data.simple.SimpleFeatureIterator;
//import org.geotools.feature.FeatureCollection;
//import org.geotools.geojson.feature.FeatureJSON;
//import org.geotools.xml.Parser;
////import org.geotools.xsd.Parser;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//
//public class Approach1 {
//
//    public static void convertKMLtoGeoJSON(String kmlFilePath, String geoJsonFilePath) {
//        try {
//            File kmlFile = new File(kmlFilePath);
//            FileInputStream inputStream = new FileInputStream(kmlFile);
//
//            Parser parser;
//            //failing here approach-1
//            parser = new Parser(null);
//            SimpleFeatureIterator features = (SimpleFeatureIterator) parser.parse(inputStream);
//
//            File geoJsonFile = new File(geoJsonFilePath);
//            FileOutputStream outputStream = new FileOutputStream(geoJsonFile);
//
//            FeatureJSON featureJSON = new FeatureJSON();
//            featureJSON.writeFeatureCollection((FeatureCollection) features, outputStream);
//
//            inputStream.close();
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}
