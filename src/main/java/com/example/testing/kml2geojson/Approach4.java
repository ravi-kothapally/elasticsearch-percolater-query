//package com.example.testing.kml2geojson;
//
//import org.geotools.data.DataUtilities;
//import org.geotools.data.simple.SimpleFeatureCollection;
//import org.geotools.data.simple.SimpleFeatureIterator;
//import org.geotools.geojson.feature.FeatureJSON;
//import org.geotools.kml.KML;
//import org.opengis.feature.simple.SimpleFeature;
//import org.w3c.dom.Document;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//public class Approach4 {
//    public void convertKMLtoGeoJSON(String kmlFilePath, String geoJsonFilePath) {
//        try {
//            File kmlFile = new File(kmlFilePath);
//
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document document = dBuilder.parse(kmlFile);
////           failed here-- 'KML()' has private access in 'org.geotools.kml.KML'
//
//
//            KML kml = new KML();
//            SimpleFeature feature = (SimpleFeature) kml.read(document).iterator().next();
//            SimpleFeatureCollection featureCollection = DataUtilities.collection(feature);
//
//            File geoJsonFile = new File(geoJsonFilePath);
//            FileOutputStream outputStream = new FileOutputStream(geoJsonFile);
//
//            FeatureJSON featureJSON = new FeatureJSON();
//            featureJSON.writeFeatureCollection(featureCollection, outputStream);
//
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
