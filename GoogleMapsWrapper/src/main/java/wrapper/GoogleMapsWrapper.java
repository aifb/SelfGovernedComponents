package wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.json.JSONObject;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.namespace.RDFS;
import org.semanticweb.yars.nx.namespace.XSD;

@Path("/mapDir")
public class GoogleMapsWrapper {
	@Context UriInfo uri;

	static HashMap<String, Iterable<Node[]>> calls = new HashMap<String, Iterable<Node[]>>();
	static int counter = 0;

	/**
	 * 
	 * returns basic descriptions on the wrapper root resource
	 * 
	 * @param collection
	 * @param uriinfo
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String doGet() {

		return "Hello World, I am the GoogleMapsWrapper!";

	}
	
	/**
	 * 
	 * returns basic descriptions on the wrapper root resource
	 * 
	 * @param collection
	 * @param uriinfo
	 * @return
	 */
	@GET
	public Response doGetRdf() {
		List<Node[]> graph = new ArrayList<Node[]>();

		//		String rdf = "<> <http://example.org/is> \"geo gps coding webservice\" ; "
		//				+ " a <http://example.org/webservice> . ";
		Node base = new Resource( uri.getBaseUri() + "mapDir/" );
		graph.add( new Node[] {base, new Resource("http://example.org/predicate"), new Literal("the GoogleMapsWrapper", XSD.STRING)} );


		return Response.status(Response.Status.OK).entity(new GenericEntity<Iterable<Node[]>>( graph ) { }).build();
	}

	
	
	
	
	
	
	/**
	 * 
	 * 
	 * @param callId
	 * @return
	 */
	@GET
	@Path("{callId}")
	public Response getRouteById( @PathParam("callId") String callId) {
		
		if (!calls.containsKey(callId)) 
			return Response.status(404).build();
		
		return Response.status(Response.Status.OK).entity(new GenericEntity<Iterable<Node[]>>( calls.get(callId) ) { }).build();
	}
	
	
	
	
	
	
	
	/**
	 * creates a new call
	 * 
	 * @param collection
	 * @param uriinfo
	 * @param input
	 * @return
	 * @throws URISyntaxException
	 */
	@POST
	public Response doPost(@Context UriInfo uriinfo, Iterable<Node[]> input) throws UnsupportedOperationException, URISyntaxException, IOException {
		GoogleMapsWrapper app = new GoogleMapsWrapper();
		List<Node[]> graph = null;
		try {
			graph = app.getDirections(input);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		counter++;
		calls.put(String.valueOf(counter), graph);
		
		return Response.status(Response.Status.OK).entity(new GenericEntity<Iterable<Node[]>>(graph) {
		}).build();
	}

	public List<Node[]> getDirections(Iterable<Node[]> input)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, URISyntaxException, UnsupportedOperationException, IOException {

		// Status: hier alle informationen über Java zugreifbar
		String origin = null;
		String destination = null;
		
		List<Node[]> list = new ArrayList<Node[]>();
		for (Node[] node1 : input) {
			list.add(node1);
		}
		
		for (Node[] node : list) {
			
			if (node[1].getLabel().toLowerCase().contains("destination")) {
				String subject = node[2].getLabel();
				String lat = null;
				String lng = null;
				for(Node[] node2 : list ) {
					if(node2[0].getLabel().contains(subject)&&node2[1].getLabel().contains("lat")) {
						lat = node2[2].getLabel();
					}else if(node2[0].getLabel().contains(subject)&&node2[1].getLabel().contains("long")) {
						lng = node2[2].getLabel();
					}
				}
				destination = lat + ",+" + lng;
			}
			if (node[1].getLabel().toLowerCase().contains("origin")) {
				String subject = node[2].getLabel();
				String lat = null;
				String lng = null;
				for(Node[] node2 : list ) {
					if(node2[0].getLabel().contains(subject)&&node2[1].getLabel().contains("lat")) {
						lat = node2[2].getLabel();
					}else if(node2[0].getLabel().contains(subject)&&node2[1].getLabel().contains("long")) {
						lng = node2[2].getLabel();
					}
				}
				origin = lat + ",+" + lng;
			}
			
		}

		String key = "AIzaSyCSx9yznKEXKIaTTnG0DsK0WyZATIh1SFg";

		String httpCall = ("https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination="
				+ destination + "&mode=transit&key=" + key);

		System.out.println(httpCall);

		// JAX RS Client
		HttpClient client = HttpClientBuilder.create().build();

		// client macht seinen Call
		HttpGet req = new HttpGet();
		req.setURI(new URI(httpCall));
		req.setHeader("Accept", "application/json");

		HttpResponse response = client.execute(req);

		// responses werden gelesen
		String data = "";
		InputStream body = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(body));
		String line = "";
		while ((line = br.readLine()) != null) {
			data += line + "\n";
		}
		br.close();

		JSONObject context = new JSONObject();
		context.put("routes", "http://example.org/routes#");
		context.put("legs", "http://example.org/legs#");
		context.put("distance", "http://example.org/distance#");
		context.put("duration", "http://example.org/duration#");
		context.put("text", "http://example.org/text#");
		context.put("value", "http://example.org/value#");
		context.put("end_address", "http://example.org/end_address#");
		context.put("end_location", "http://example.org/end_location#");
		context.put("lat", "http://example.org/lat#");
		context.put("lng", "http://example.org/lng#");
		context.put("start_address", "http://example.org/start_address#");
		context.put("start_location", "http://example.org/start_location#");
		context.put("steps", "http://example.org/steps#");
		context.put("html_instructions", "http://example.org/html_instructions#");

		JSONObject jsonStack = new JSONObject(data);

		if(counter % 2 == 0) {
			context.remove("http://example.org/html_instructions#");
		}else {
			context.remove("http://example.org/end_location#");
		}

		// Add Context du the JSON Object
		jsonStack.append("@context", context);
		
		if(counter % 2 == 0) {
			jsonStack.remove("http://example.org/html_instructions#");
		}else {
			jsonStack.remove("http://example.org/end_location#");
		}

		// Parse das JSON Object
		InputStream in = new ByteArrayInputStream(jsonStack.toString().getBytes(StandardCharsets.UTF_8));

		Model myModel = ModelFactory.createDefaultModel();
		myModel.read(in, "", "JSON-LD");

		List<Node[]> graph = new ArrayList<Node[]>();

		StmtIterator iterator = myModel.listStatements();
		while (iterator.hasNext()) {
			Statement smt = iterator.next();
			graph.add(new Node[] { new Literal(smt.getSubject().toString()), new Literal(smt.getPredicate().toString()), new Literal(smt.getObject().toString()) });
		}
		System.out.println(jsonStack.toString());

		return graph;
	}

}
