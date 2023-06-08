//package com.example.testing.kml2geojson;
//
//import org.geotools.data.DataUtilities;
//import org.geotools.data.simple.SimpleFeatureCollection;
//import org.geotools.feature.FeatureCollection;
//import org.geotools.feature.SimpleFeature;
//import org.geotools.geojson.feature.FeatureJSON;
//import org.geotools.kml.KMLConfiguration;
//import org.geotools.xsd.PullParser;
//import org.opengis.feature.simple.SimpleFeatureType;
//import org.xml.sax.SAXException;
//
//import javax.xml.stream.XMLStreamException;
//import java.io.*;
//import java.util.ArrayList;
//
//public class Approach7 {
//    public static void convertKMLtoGeoJSON(String kmlFilePath, String geoJsonFilePath) throws IOException, XMLStreamException, SAXException {
//        FileInputStream reader = new FileInputStream(kmlFilePath);
//        PullParser parser = new PullParser(new KMLConfiguration(), reader, SimpleFeature.class);
//
//        FeatureJSON featureJSON = new FeatureJSON();
//        FileWriter fileWriter = new FileWriter(geoJsonFilePath);
//        BufferedWriter writer = new BufferedWriter(fileWriter);
//        ArrayList<SimpleFeature> features = new ArrayList<>();
//        SimpleFeature simpleFeature = (SimpleFeature) parser.parse();// failed here simpleFeature is coming as null
//        while (simpleFeature != null) {
//            features.add(simpleFeature);
//            simpleFeature = (SimpleFeature) parser.parse();
//        }
//
//        SimpleFeatureCollection featureCollectionUnreprojected = DataUtilities.collection((FeatureCollection<SimpleFeatureType, org.opengis.feature.simple.SimpleFeature>) features);
//
//        featureJSON.writeFeatureCollection(featureCollectionUnreprojected, writer);
//
//    }
//}
//
