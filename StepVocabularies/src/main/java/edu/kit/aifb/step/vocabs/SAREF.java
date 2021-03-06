package edu.kit.aifb.step.vocabs;


import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.semanticweb.yars.nx.Resource;



/**
 * @see http://ontology.tno.nl/saref/
 * 
 * @author sba
 *
 */
public class SAREF {
	
	public static final String NAMESPACE = "https://w3id.org/saref#";

    /** {@code hydra} **/
    public static final String PREFIX = "saref";

    // classes
	public static final Resource Actuator;

	
	// properties
	public static final Resource hasState;
	

	

	
    static {
        //ValueFactory factory = ValueFactoryImpl.getInstance();
        
        // Classes:
        Actuator = new Resource(SAREF.NAMESPACE + "Actuator");

        
        // Predicates:
    	hasState = new Resource(SAREF.NAMESPACE + "hasState");
   
    }

}
