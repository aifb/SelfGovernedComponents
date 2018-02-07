package edu.kit.aifb.step.web;



import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.semanticweb.yars.jaxrs.trailingslash.RedirectMissingTrailingSlash;

import edu.kit.aifb.step.web.api.WebResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.Contact;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;



@SwaggerDefinition(
		info = @Info(
				description = "FLSVisitourCallAPI",
				version = "Alpha",
				title = "Part of STEP's API",
				termsOfService = "http://localhost",
				contact = @Contact(
						name = "kit", 
						email = "kit@kit.edu", 
						url = "http://google.de"
						),
				license = @License(
						name = "KIT", 
						url = "www.aifb.kit.edu/"
						)
				),
		consumes = {"application/json", "application/xml"},
		produces = {"application/json", "application/xml"},
		schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
		tags = {
				@Tag(name = "Private", description = "Tag used to denote operations as private")
		}, 
		externalDocs = @ExternalDocs(value = "KIT - AIFB - STEP", url = "http://www.aifb.kit.edu/web/STEP")
		)
@Api(value = "function", authorizations = {
		@Authorization(value="sampleoauth", scopes = {})
})
@Path("/visitour")
public class VirtualResourceServlet {


	@Context
	ServletContext _ctx;
	
	@Context
	UriInfo uriInfo;
	


	@GET
	public Response doGet(@Context UriInfo uriinfo) {
		
		return Response.ok().build();

	}
	
	
	@Path("/")
	public WebResource root(@HeaderParam("Accept") String accept) {
		
		return new SataliaWebContainer(new SataliaContainer( Utils.getBaseURI(uriInfo)), Utils.getBaseURI(uriInfo), accept);
	}
}
