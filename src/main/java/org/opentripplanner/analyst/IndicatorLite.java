package org.opentripplanner.analyst;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.opentripplanner.analyst.pointset.Attribute;
import org.opentripplanner.analyst.pointset.Category;

/**
 * A TimeSurface is evaluated at all the points in a PointSet to yield an Indicator.
 *
 * These are represented as a constrained format of GeoJSON.
 * They provide cumulative distributions for access to the opportunities in the PointSet
 * with respect to travel time from a particular origin.
 *
 * Indicators these are one of the three main web analyst resources:
 * Pointsets
 * Indicators
 * TimeSurfaces
 *
 * note: A TimeSurface is a lot like a Coverage. A Pointset is a lot like a FeatureCollection.
 *
 * An Indicator is structured exactly like a pointset.
 * In fact, it could be a subclass of Pointset with an extra field in each Attribute.
 *
 * Is it a one-to-many indicator, or many to many? Attributes.quantiles is an array, so
 * it's many-to-many.
 */

public class IndicatorLite {

	private static final long serialVersionUID = -6723127825189535112L;
    
	/*
	 * The time to reach each target, for each origin.
	 */
	public Map<String,Histogram> histograms;
	
    public IndicatorLite (SampleSet samples, TimeSurface surface) {
    	histograms = new HashMap<String,Histogram>();
    	
        PointSet targets = samples.pset;
        // Evaluate the surface at all points in the pointset
        int[] times = samples.eval(surface);
        for (Entry<String, int[]> cat : targets.categories.entrySet()) {
        	String catId = cat.getKey();
        	int[] mags = cat.getValue();
        	
        	this.histograms.put(catId, new Histogram(times, mags));
        }
    }
    
    /**
     * Each origin will yield CSV with columns category,min,q25,q50,q75,max
     * Another column for the origin ID would allow this to extend to many-to-many.
     */
    void toCsv() {
    	
    	

    }
    
	public void writeJson(OutputStream output) {
		// TODO Auto-generated method stub
		
	}
    


}