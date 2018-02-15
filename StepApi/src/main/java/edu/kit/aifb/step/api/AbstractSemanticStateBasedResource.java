package edu.kit.aifb.step.api;

import org.openrdf.repository.RepositoryConnection;

public abstract class AbstractSemanticStateBasedResource implements SemanticStateBasedResource {

	protected String baseUri;
	protected RepositoryConnection connection;
	protected Object store;
	
	/**
	 * 
	 * @param objects: 1 = base URI, 2 = RepositoryConnection, 3 = null or BinaryStore
	 */
	public AbstractSemanticStateBasedResource( String baseUri, RepositoryConnection connection, Object store ) {
		this.baseUri = baseUri;
		this.connection = connection;
		this.store = store;
	}

}
