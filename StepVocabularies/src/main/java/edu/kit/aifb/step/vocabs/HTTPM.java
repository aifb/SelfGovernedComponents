package edu.kit.aifb.step.vocabs;


import org.semanticweb.yars.nx.Resource;


/**
 * @see http://www.hydra-cg.com/spec/latest/core/ 
 * 
 * @author sba
 *
 */
public class HTTPM {
	
	public static final String NAMESPACE = "http://www.w3.org/2011/http-methods#";

    /** {@code hydra} **/
    public static final String PREFIX = "httpm";

    // Entities
	public static final Resource GET;
	public static final Resource CONNECT;
	public static final Resource DELETE;
	public static final Resource HEAD;
	public static final Resource PATCH;
	public static final Resource POST;
	public static final Resource PUT;
	public static final Resource OPTIONS;
	public static final Resource TRACE;
	
	
    static {
        
        // Entities:
        GET = new Resource(HTTPM.NAMESPACE + "GET");
        CONNECT = new Resource(HTTPM.NAMESPACE + "CONNECT");
        DELETE = new Resource(HTTPM.NAMESPACE + "DELETE");
        HEAD = new Resource(HTTPM.NAMESPACE + "HEAD");
        PATCH = new Resource(HTTPM.NAMESPACE + "PATCH");
        POST = new Resource(HTTPM.NAMESPACE + "POST");
        PUT = new Resource(HTTPM.NAMESPACE + "PUT");
        OPTIONS = new Resource(HTTPM.NAMESPACE + "OPTIONS");
        TRACE = new Resource(HTTPM.NAMESPACE + "TRACE");

    }

}
