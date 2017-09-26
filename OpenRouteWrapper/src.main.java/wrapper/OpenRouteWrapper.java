package wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.namespace.RDFS;
import org.semanticweb.yars.nx.namespace.XSD;

@Path("/mapDir")
public class OpenRouteWrapper {
	@Context
	UriInfo uri;

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

		return "Hallo Welt";

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

		// String rdf = "<> <http://example.org/is> \"geo gps coding webservice\" ; "
		// + " a <http://example.org/webservice> . ";
		Node base = new Resource("http://localhost");
		graph.add(new Node[] { base, new Resource("http://example.org/predicate"),
				new Literal("the wrapper", XSD.STRING) });

		return Response.status(Response.Status.OK).entity(new GenericEntity<Iterable<Node[]>>(graph) {
		}).build();
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
	public Response doPost(@Context UriInfo uriinfo, Iterable<Node[]> input)
			throws UnsupportedOperationException, URISyntaxException, IOException {
		OpenRouteWrapper app = new OpenRouteWrapper();
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

	// Mehr ueber Maske von openMaps hier:
	// https://app.swaggerhub.com/apis/OpenRouteService/ors-api/4.3

	public List<Node[]> getDirections(Iterable<Node[]> input)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, URISyntaxException, UnsupportedOperationException, IOException {

		// Status: hier alle informationen über Java zugreifbar
		String origin = null;
		String destination = null;
		String profile = null;

		List<Node[]> list = new ArrayList<Node[]>();
		for (Node[] node1 : input) {
			list.add(node1);
		}

		for (Node[] node : list) {

			if (node[1].getLabel().toLowerCase().contains("destination")) {
				String subject = node[2].getLabel();
				String lat = null;
				String lng = null;
				for (Node[] node2 : list) {
					if (node2[0].getLabel().contains(subject) && node2[1].getLabel().contains("lat")) {
						lat = node2[2].getLabel();
					} else if (node2[0].getLabel().contains(subject) && node2[1].getLabel().contains("long")) {
						lng = node2[2].getLabel();
					}
				}
				destination = lat + "%2C" + lng;
			}
			if (node[1].getLabel().toLowerCase().contains("origin")) {
				String subject = node[2].getLabel();
				String lat = null;
				String lng = null;
				for (Node[] node2 : list) {
					if (node2[0].getLabel().contains(subject) && node2[1].getLabel().contains("lat")) {
						lat = node2[2].getLabel();
					} else if (node2[0].getLabel().contains(subject) && node2[1].getLabel().contains("long")) {
						lng = node2[2].getLabel();
					}
				}
				origin = lat + "%2C" + lng;
			}
			if (node[1].getLabel().toLowerCase().contains("profile")) {
				String subject = node[2].getLabel();
				String prof = null;
				for (Node[] node2 : list) {
					if (node2[0].getLabel().contains(subject) && node2[1].getLabel().contains("prof")) {
						prof = node2[2].getLabel();
					}
				}
				profile = prof;
			}

		}

		String key = "58d904a497c67e00015b45fc703fe6235a204ee270550ab4dadf8892";

		String httpCall = ("https://api.openrouteservice.org/directions?coordinates=" + origin
				+ URLEncoder.encode("|", "UTF-8") + destination + "&profile=" + profile + "&api_key=" + key);

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
		context.put("summary", "http://example.org/summary#");
		context.put("duration", "http://example.org/duration#");
		context.put("duration", "http://example.org/duration#");
		context.put("distance", "http://example.org/distance#");
		context.put("geometry_format", "http://example.org/geometry_format#");
		
		context.put("segments", "http://example.org/segments#");
		context.put("steps", "http://example.org/steps#");
		context.put("duration", "http://example.org/duration#");
		context.put("distance", "http://example.org/distance#");
		context.put("duration", "http://example.org/duration#");
		context.put("distance", "http://example.org/distance#");
		context.put("instruction", "http://example.org/instruction#");
		context.put("name", "http://example.org/name#");
		context.put("type", "http://example.org/type#");
		// context.put("way_points", "http://example.org/way_points#");
		context.put("coords", "http://example.org/coordinates#");
		context.put("origin", "http://example.org/origin#");
		context.put("destination", "http://example.org/destination#");
		context.put("lat", "http://example.org/lat#");
		context.put("long", "http://example.org/long#");
		context.put("info", "http://example.org/info#");
		context.put("service", "http://example.org/service#");
		context.put("query", "http://example.org/query#");
		context.put("profile", "http://example.org/profile#");

		JSONObject jsonStack = new JSONObject(data);
		JSONArray array = jsonStack.getJSONArray("routes").getJSONObject(0).getJSONArray("bbox");

		JSONObject pair1 = new JSONObject();
		JSONObject pair2 = new JSONObject();
		pair1.append("lat", array.get(0));
		pair1.append("long", array.get(1));
		pair2.append("lat", array.get(2));
		pair2.append("long", array.get(3));

		JSONObject coords = new JSONObject();
		coords.append("origin", pair1);
		coords.append("destination", pair2);
		jsonStack.append("coords", coords);

		// Add Context du the JSON Object
		jsonStack.append("@context", context);

		// Parse das JSON Object
		InputStream in = new ByteArrayInputStream(jsonStack.toString().getBytes(StandardCharsets.UTF_8));

		Model myModel = ModelFactory.createDefaultModel();
		myModel.read(in, "", "JSON-LD");

		List<Node[]> graph = new ArrayList<Node[]>();

		Node base = new Resource("#this");

		StmtIterator iterator = myModel.listStatements();
		while (iterator.hasNext()) {

			Statement smt = iterator.next();

			graph.add(parseSemanticWebNodesFromJenaStatement(smt));

		}

		System.out.println(jsonStack.toString());

		return graph;
	}

	/**
	 * converts a valid Jena Statement Object into a SemanticWeb Node[] triple.
	 * 
	 * @author sba
	 * 
	 * @param smt
	 * @return a Node[] triple
	 */
	public Node[] parseSemanticWebNodesFromJenaStatement(Statement smt) {

		Node subject;
		if (smt.getSubject().isURIResource()) {
			subject = new Resource(smt.getSubject().toString());
		} else if (smt.getSubject().isResource()) {
			subject = new BNode(smt.getSubject().toString());
		} else {
			throw new IllegalArgumentException("RDF subject node must be a resource (either an URI or a BlankNode).");
		}

		Resource predicate;
		if (smt.getPredicate().isURIResource()) {
			predicate = new Resource(smt.getPredicate().toString());
		} else {
			throw new IllegalArgumentException("RDF predicate node must be a URI.");
		}

		Node object;
		if (smt.getObject().isURIResource()) {
			object = new Resource(smt.getObject().toString());
		} else if (smt.getObject().isResource()) {
			object = new BNode(smt.getObject().toString());
		} else if (smt.getObject().isLiteral()) {

			org.apache.jena.rdf.model.Literal object_literal = smt.getObject().asLiteral();
			RDFDatatype data_type = object_literal.getDatatype();
			String value = object_literal.getValue().toString();

			object = new Literal(value, new Resource(data_type.getURI()));

		} else {
			throw new IllegalArgumentException(
					"RDF object node must be a resource (either an URI or a BlankNode) or a Literal.");
		}

		return new Node[] { subject, predicate, object };
	}

}
