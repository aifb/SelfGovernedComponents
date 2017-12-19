package edu.kit.aifb.step.vocabs;


import org.semanticweb.yars.nx.Resource;


public class MEXCORE {



	public static final String NAMESPACE = "http://mex.aksw.org/mex-core#";

	/** {@code dbo} **/
	public static final String PREFIX = "mex-core";

	public static final Resource memory;

	public static final Resource cpu;


	static {
		
		// Properties:
		memory = new Resource(MEXCORE.NAMESPACE + "memory");
		cpu = new Resource(MEXCORE.NAMESPACE + "cpu");
		

	}

}
