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

import javax.ws.rs.Consumes;
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
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.json.JSONObject;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.RDFS;
import org.semanticweb.yars.nx.namespace.XSD;


/**
 * 
 * @author sba
 *
 */
@Path("/wrapper")
public class JSON2JSONLD {
	@Context UriInfo uri;
	
	private String host = "aifb-ls3-vm1.aifb.kit.edu";
	private String port = "8090";

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

		return "the OpenTourPlanner wrapper for Linked Data";

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
		
		
		try {
			
			graph = parseInput();
			
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
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		if ((counter++ % 2) == 0) {
			return Response.status(Response.Status.OK).entity(new GenericEntity<Iterable<Node[]>>( calls.get("1") ) { }).build();
		} else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
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
	@Consumes({"application/ld+json", "text/turtle"})
	public Response doPost(@Context UriInfo uriinfo, Iterable<Node[]> input) throws UnsupportedOperationException, URISyntaxException, IOException {
		
		JSON2JSONLD app = new JSON2JSONLD();
		List<Node[]> graph = null;
		
		//return Response.status(Response.Status.CREATED).entity(new GenericEntity<Iterable<Node[]>>(graph) { }).header("Location", uri.getBaseUri() + "opt/" + counter).build();
		return Response.status(Response.Status.CREATED).entity(new GenericEntity<Iterable<Node[]>>(graph) { }).header("Location", uri.getBaseUri() + "opt/1").build();
	}

	
	
	public List<Node[]> parseInput()
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, URISyntaxException, UnsupportedOperationException, IOException {

		// JAX RS Client
		HttpClient client = HttpClientBuilder.create().build();

		// client macht seinen Call
		HttpGet req = new HttpGet();
		req.setURI(new URI("http://127.0.0.1:8050/~/in-cse"));
		req.setHeader("Accept", "application/json");
		req.setHeader("X-M2M-Origin","admin:admin");
		

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
		context.put("rn", "http://example.org/rn");
		context.put("ty", "http://example.org/ty");
		context.put("ri", "http://example.org/ri");
		context.put("ct", "http://example.org/ct");
		context.put("lt", "http://example.org/lt");
		context.put("acpi", "http://example.org/acpi");
		context.put("cst", "http://example.org/cst");
		context.put("csi", "http://example.org/csi");
		context.put("srt", "http://example.org/srt");
		context.put("poa", "http://example.org/poa");

		JSONObject jsonStack = new JSONObject(data);

		// Add Context du the JSON Object
		jsonStack.append("@context", context);

		// Parse das JSON Object
		InputStream in = new ByteArrayInputStream(jsonStack.toString().getBytes(StandardCharsets.UTF_8));

		Model myModel = ModelFactory.createDefaultModel();
		myModel.read(in, "", "JSON-LD");

		List<Node[]> graph = new ArrayList<Node[]>();

		
		
		StmtIterator iterator = myModel.listStatements();
		while (iterator.hasNext()) {
			
			Statement smt = iterator.next();
			
			graph.add( parseSemanticWebNodesFromJenaStatement(smt));
			
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
			throw new IllegalArgumentException("RDF object node must be a resource (either an URI or a BlankNode) or a Literal.");
		}
		
		
		
		
		return new Node[] { subject, predicate, object };
	}

}
