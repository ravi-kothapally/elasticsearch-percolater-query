package com.example.testing;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

@SpringBootApplication
public class TestingApplication {

	public static void main(String[] args) throws XMLStreamException, IOException, SAXException {
		SpringApplication.run(TestingApplication.class, args);
//		Approach8.convertKMLtoGeoJSON("/home/gaian/Desktop/laptop1/INCOIS/ss1.kml","/home/gaian/Desktop/laptop1/INCOIS/doc3/ss1.geojson");
	}

}
