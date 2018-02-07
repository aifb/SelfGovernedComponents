package edu.kit.aifb.step.web.api;

import java.net.URISyntaxException;
import java.rmi.RemoteException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.cli.MissingArgumentException;
import org.semanticweb.yars.nx.Node;


public interface WebResource {
	
	
	/**
	 * 
	 * returns basic descriptions on the wrapper root resource
	 * 
	 * @return 200, 404, or error
	 */
	@GET
	@Produces({MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, "application/n-triples", "application/n-quads", "text/turtle", "application/ld+json", "application/rdf+xml"})
	public Response doGet();
	
	
	
	/**
	 * 
	 * create a new child instance
	 * 
	 * @return 201 or error
	 * @throws MissingArgumentException 
	 * @throws RemoteException 
	 */
	@POST
	@Consumes({MediaType.TEXT_HTML, "application/n-triples", "application/n-quads", "text/turtle", "application/ld+json", "application/rdf+xml"})
	public Response doPost(Iterable<Node[]> input);
	
	

	/**
	 * 
	 * update this instance
	 * 
	 * @return 200, 406 or error
	 */
	@PUT
	@Consumes({MediaType.TEXT_HTML, "application/n-triples", "application/n-quads", "text/turtle", "application/ld+json", "application/rdf+xml"})
	public Response doPut(Iterable<Node[]> input);
	
	
	/**
	 * 
	 * delete this resource
	 * 
	 * @return
	 */
	@DELETE
	public Response doDelete();
	
	
	@OPTIONS
	@Produces({MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, "application/n-triples", "application/n-quads", "text/turtle", "application/ld+json", "application/rdf+xml"})
	public Response doOptions();
	

	@HEAD
	@Produces({MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, "application/n-triples", "application/n-quads", "text/turtle", "application/ld+json", "application/rdf+xml"})
	public Response doHead();	
	
	
	
	@Path("{child}")
	public WebResource retrieve(@PathParam("child") String child);

}
