package extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import module6.CityMarker;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	private CommonMarker lastSelected;

	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	
	public void setup() {
		// setting up PAppler
		size(900,700, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 750, 550);
		map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			m.setRadius(5);
			airportList.add(m);
			
			
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		

			//System.out.println(sl.getProperties());
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			routeList.add(sl);
			
		}
	
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		//map.addMarkers(routeList);
		
		//map.addMarkers(airportList);
		
		AirportMarker.routes = (List<SimpleLinesMarker>)(List<?>)routeList;
		
		
		/*
		for (int i = 0; i <10; i++) {
			
			AirportMarker am = (AirportMarker)airportList.get(i);
			am.getNumberOfRoutes();
			
			map.addMarkers(airportList.get(i));
			
		}
		
		*/

		
		
		AirportMarker.calculateRoutes();
		
		Object [] airportsArray = airportList.toArray();
		
		Arrays.sort(airportsArray);

		
		
		System.out.println("Total airports " + airportsArray.length);
		System.out.println("Calculations " + AirportMarker.calculations);

		
		for (int i = 0; i < 50; i++) {
			
		
			
			AirportMarker airportMarker = (AirportMarker)airportsArray[i];
			System.out.println(airportMarker.getName() + " " + airportMarker.getNumberOfRoutes());
		}
		
		

		
		Marker[] airpotsArray = Arrays.copyOfRange(airportsArray, 0, 51, Marker[].class);

		map.addMarkers(Arrays.asList(airpotsArray));
		
		
	
		
		
		
	}
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		
	}
	
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		
		selectMarkerIfHover(airportList);

		
		//loop();
	}
	
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 160, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Top 50 ", xbase+50, ybase+25);	
		text("Airports with most routes ", xbase+8, ybase+50);		
		
		text("Airport", xbase+55, ybase+78);	
		fill(11);
		ellipse(xbase + 45, ybase+80, 5, 5);



		
		
	}

}
