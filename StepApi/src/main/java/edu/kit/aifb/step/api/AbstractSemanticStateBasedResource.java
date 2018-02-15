package edu.kit.aifb.step.api;

import org.openrdf.repository.RepositoryConnection;

public abstract class AbstractSemanticStateBasedResource implements SemanticStateBasedResource {

	private String baseUri;
	private RepositoryConnection connection;
	
	/**
	 * 
	 * @param objects: object_0 = base URI, object_1 = RepositoryConnection, ...
	 */
	public AbstractSemanticStateBasedResource(Object... objects ) {
		if (objects == null) return ;
		if (objects.length > 0) baseUri = objects[0].toString();
		if (objects.length > 1) connection = (RepositoryConnection) objects[1];
		
	}

}
