package edu.kit.aifb.step.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Variant;

import org.semanticweb.yars.jaxrs.JsonLdMessageBodyReaderWriter;
import org.semanticweb.yars.jaxrs.NxMessageBodyReaderWriter;
import org.semanticweb.yars.jaxrs.RdfXmlMessageBodyWriter;


public class Utils {


	public static List<Variant> getVariants() {
		List<Variant> variants = new ArrayList<Variant>();
		variants.add(new Variant(NxMessageBodyReaderWriter.TURTLE_MEDIATYPE, Locale.ENGLISH, null));	
		variants.add(new Variant(JsonLdMessageBodyReaderWriter.JSONLD_MEDIATYPE, Locale.ENGLISH, null));	
		variants.add(new Variant(NxMessageBodyReaderWriter.NTRIPLES_MEDIATYPE, Locale.ENGLISH, null));	
		variants.add(new Variant(RdfXmlMessageBodyWriter.RDF_XML_MEDIATYPE, Locale.ENGLISH, null));	
		return variants;
	}


	public static String getBaseURI(UriInfo uriInfo) {
		return uriInfo.getAbsolutePath().toString().endsWith("/") ? uriInfo.getAbsolutePath().toString() : uriInfo.getAbsolutePath() + "/" ;
	}

}
