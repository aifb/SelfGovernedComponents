package edu.kit.aifb.step.api;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.http.MethodNotSupportedException;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;


/**
 *  
 * @author sba
 *
 */
public interface SemanticStateBasedResource {
	
	public Iterable<Node[]> read() throws RemoteException;
	public Iterable<Node[]> readDescription();
	
	public boolean update(Iterable<Node[]> nodes) throws RemoteException;;
	
	public String create(Iterable<Node[]> nodes) throws RemoteException, MissingArgumentException;
	
	public boolean delete() throws RemoteException, MethodNotSupportedException;
	
	
	/**
	 * 
	 * @return all instances
	 */
	public List<Resource> contains() throws RemoteException;
	 

	/**
	 * 
	 * @return one instances
	 */
	public SemanticStateBasedResource retrieve(String id) throws RemoteException;

}
