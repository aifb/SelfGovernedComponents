package application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;



@ApplicationPath("/")
public class App extends Application {


	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<Class<?>>();
		
		resources.add(wrapper.OpenTripPlannerRestApiPlannerResourceWrapper.class);
		
		return resources;
	}

}