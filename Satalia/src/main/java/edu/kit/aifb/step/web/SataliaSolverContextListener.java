package edu.kit.aifb.step.web;

import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;



public class SataliaSolverContextListener implements ServletContextListener {

	final static Logger _log = Logger.getLogger(SataliaSolverContextListener.class.getName());

	ServletContext _ctx;
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		_ctx = sce.getServletContext();

		// Register Servlet
		ServletRegistration sr = _ctx.addServlet("STEP Wrapper",
				org.glassfish.jersey.servlet.ServletContainer.class);
		sr.addMapping("/*");
		sr.setInitParameter(org.glassfish.jersey.server.ServerProperties.PROVIDER_PACKAGES,
				this.getClass().getPackage().getName() 
				+ ", " 
				+ "io.swagger.jaxrs.listing");
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
