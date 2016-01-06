package extension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * A class to represent AirportMarkers on a world map.
 * 
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 *         MOOC team
 *
 */
public class AirportMarker extends CommonMarker implements Comparable<AirportMarker> {

	public static int calculations = 0;
	public static List<SimpleLinesMarker> routes;
	public static Map<String, Integer> routesPerAirport = new HashMap<String, Integer>();


	private int routesNo = -1;

	public AirportMarker(Feature city) {
		super(((PointFeature) city).getLocation(), city.getProperties());

	}

	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.fill(11);
		pg.ellipse(x, y, 5, 5);
		
		


	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		// show rectangle with title

		// show routes

		// String name = getCity() + " " + getCountry() + " ";
		// String pop = "Pop: " + getPopulation() + " Million";

		String name = getName() + " " + getCode();
		String location = getCity() + "," + getCountry();
		int routes = getNumberOfRoutes();

		pg.pushStyle();

		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y, Math.max(pg.textWidth(name), 250), 60);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x + 15, y);
		pg.text(location, x + 15, y + 10);
		pg.text("", x + 15, y + 20);
		pg.text("Number of routes: " + routes, x + 15, y + 30);

		pg.popStyle();
		
		pg.fill(0);
		pg.textSize(12);
	}

	@Override
	public String toString() {

		return getName() + "- " + getCode();
	}

	@Override
	public int compareTo(AirportMarker o) {
		// TODO Auto-generated method stub

		int localRoutesNo = getNumberOfRoutes();
		int otherRoutesNo = o.getNumberOfRoutes();

		if (localRoutesNo < otherRoutesNo)
			return 1;

		else if (localRoutesNo > otherRoutesNo)
			return -1;

		else
			return 0;

	}

	public String getName() {
		return getStringProperty("name").replace("\"", "");
	}

	public String getCity() {
		return getStringProperty("city").replace("\"", "");
	}

	public String getCountry() {
		return getStringProperty("country").replace("\"", "");
	}

	public String getCode() {
		return getStringProperty("code").replace("\"", "");
	}

	public String getAltitude() {
		return getStringProperty("altitude").replace("\"", "");
	}

	public String getId() {
		return getStringProperty("id").replace("\"", "");
	}

	public int getNumberOfRoutes2() {

		if (this.routesNo != -1)
			return this.routesNo;

		calculations++;
		String id = getId();
		int routesNo = 0;

		for (SimpleLinesMarker route : routes) {

			String routeSourceId = (String) route.getProperty("source");

			if (id.equals(routeSourceId)) {
				routesNo++;

			}

		}

		this.routesNo = routesNo;

		return routesNo;
	}

	public static void calculateRoutes() {

		for (SimpleLinesMarker route : routes) {

			String routeSourceId = (String) route.getProperty("source");

			Integer routesNo = routesPerAirport.get(routeSourceId);

			if (routesNo == null)
				routesNo = 0;

			routesNo++;

			routesPerAirport.put(routeSourceId, routesNo);

		}

	}
	
	

	public int getNumberOfRoutes() {

		String id = getId();

		Integer routesNo = routesPerAirport.get(id);

		if (routesNo == null) {
			return 0;
		}

		else {
			return routesNo;
		}

	}

}
