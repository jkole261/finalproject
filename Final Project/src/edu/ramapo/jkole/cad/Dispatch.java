package edu.ramapo.jkole.cad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.LatLng;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Dispatch {
	static double calllat;
	static double calllng;
	static String cadid;
	
	public static void recUnits(String callid) {
		//Recommend units based on avail and nature
		try {
			cadid = callid;
			List<Apparatus> units = getUnitsWithType(callid);
			DispatchSeq seq = new DispatchSeq(callid);
			List<AppList> nUni = UnitSelection(units, seq);
			suggest(nUni);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void recUnits(String callid, String type) {
		//Recommend units based on avail and nature
		try {
			cadid = callid;
			List<Apparatus> units = getUnitsWithType(callid);
			DispatchSeq seq = new DispatchSeq(callid, type);
			List<AppList> nUni = UnitSelection(units, seq);
			suggest(nUni);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void suggest(List<AppList> nUni) {
		// TODO Auto-generated method stub
		new ApparatusDispatch(cadid, nUni);
	}
	private static List<AppList> UnitSelection(List<Apparatus> units, DispatchSeq seq) {
		List<AppList> app = new ArrayList<AppList>();
		String[][] s = seq.getApp(); 
		for(int i = 0; i < s.length; i++){
			if(!(s[i][0] == null)){
				if(!(s[i][0].equalsIgnoreCase("*"))){
					app.add(new AppList(s[i][1], selectUnit(units, s[i][1])));
					s[i][0] = ((Integer.parseInt(s[i][0]))-1)+"";
					if((Integer.parseInt(s[i][0])) == 0){
						s[i][0] = null;
						s[i][1] = null;
						i = -1;
					}
				}
				else{
					System.out.println("NOTIFICATION: "+s[i][1]);
					app.add(new AppList(s[i][0], new Apparatus(s[i][1])));
				}
			}
		}
		return app;
	}

	private static Apparatus selectUnit(List<Apparatus> units, String string) {
		//get from list. if found remove from list
		Apparatus temp = new Apparatus();
		boolean found = true;
		int i = 0;
		while(found){
			try{
				if(string.equalsIgnoreCase("Engines") || 
						string.equalsIgnoreCase("engine")){
					if(units.get(i).isEngine() && !(units.get(i).isLadder())){
						temp = units.get(i);
						units.remove(i);
						found = false;
					}
				}
				else if(string.equalsIgnoreCase("truck") || 
						string.equalsIgnoreCase("trucks")){
					if(units.get(i).isLadder()){
						temp = units.get(i);
						units.remove(i);
						found = false;
					}
				}
				else if(string.equalsIgnoreCase("Rescue") ||
						string.equalsIgnoreCase("rescues")){
					if(units.get(i).isRescue()){
						temp = units.get(i);
						units.remove(i);
						found = false;
					}
				}
				else if(string.equalsIgnoreCase("brush")){
					if(units.get(i).isBrush()){
						temp = units.get(i);
						units.remove(i);
						found = false;
					}
				}
				else if(string.equalsIgnoreCase("bls")){
					if(units.get(i).isTbls()){
						temp = units.get(i);
						units.remove(i);
						found = false;
					}
				}
				else if(string.equalsIgnoreCase("PD")){
					temp = new Apparatus("PD");
					found = false;
				}
				else if(string.equalsIgnoreCase("special service") ||
						string.equalsIgnoreCase("special services")){
					if(units.get(i).isRescue() || units.get(i).isLadder()){
						temp = units.get(i);
						units.remove(i);
						found = false;
					}
				}
				else if(string.equalsIgnoreCase("Battalion Chief") || 
						string.equalsIgnoreCase("Battalion Chiefs")){
					if(units.get(i).isSupv()){
						temp = units.get(i);
						units.remove(i);
						found = false;
					}
				}
				else{
					System.out.println("NOT CHECKED/found");
					found = false;
				}
				i++;
			}
			catch(IndexOutOfBoundsException e){
				temp = new Apparatus();
				found = false;
			}
		}
		return temp;
	}

	static List<Apparatus> getUnitsWithType(String callid) throws IOException {
		// TODO Auto-generated method stub
		try {
			calllat = getCallLat(callid);
			calllng = getCallLng(callid);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Apparatus> apparatus = getApparatus(getStations());
		return apparatus;
	}


	private static List<Apparatus> getApparatus(List<Station> stations) {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		List<Apparatus> applist = new ArrayList<Apparatus>();
		for(int i = 0; i < stations.size(); i++){
			LatLng latlng = geocode(stations.get(i).getAddress()+", "
					+stations.get(i).getMunicipality()+", "
					+stations.get(i).getState());
			double dist = getDist(latlng.getLat().doubleValue(), latlng.getLng().doubleValue());
			ArrayList<Apparatus> app = getAppFrom(stations.get(i));	
			for(int k = 0; k < app.size(); k++){
				HashMap<String, Object> temp = new HashMap<String, Object>();
				temp.put("app", app.get(k));
				temp.put("dist", dist);
				list.add(temp);
			}
		}
		list = sortList(list);
		for(HashMap<String, Object> item : list){
			applist.add((Apparatus)item.get("app"));
		}
	
		return applist;
	}

	private static List<HashMap<String, Object>> sortList(List<HashMap<String, Object>> list) {
		 Collections.sort(list, new Comparator<HashMap<String, Object>>() {
			@Override
			public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
				return Double.compare(Double.parseDouble(o1.get("dist").toString()), 
						Double.parseDouble(o2.get("dist").toString()));
			}
         });
		return list;
	}

	private static double getDist(double lat, double lng) {
		double earthRadius = 3958.75;
	    double latDiff = Math.toRadians(lat-calllat);
	    double lngDiff = Math.toRadians(lng-calllng);
	    double a = Math.sin(latDiff / 2) * Math.sin(latDiff /2) +
	    Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(calllat)) *
	    Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double distance = earthRadius * c;

	    return new Float(distance).floatValue();
	}

	private static LatLng geocode(String string) {
		LatLng geom = null;
		try {
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(string).setLanguage("en").getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			geom = geocoderResponse.getResults().get(0).getGeometry().getLocation();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return geom;
	}

	private static ArrayList<Apparatus> getAppFrom(Station station) {
		// TODO Auto-generated method stub
		ArrayList<Apparatus> list = new ArrayList<Apparatus>();
		DBCursor curs = Database.getCol("Apparatus", "info").find(
				new BasicDBObject("UnitCounLoc", station.getCountycode())
				.append("UnitMuniLoc", station.getMuniccode())
				.append("UnitDistLoc", station.getDistrict())
				.append("Status.avail", true));
		while(curs.hasNext()){
			curs.next();
			list.add(new Apparatus((BasicDBObject)curs.curr()));
		}
		return list;
	}

	private static List<Station> getStations() {
		// TODO Auto-generated method stub
		List<Station> temp = new ArrayList<Station>();
		DBCursor curs = Database.getCol("departmentlocs", "addresses").find();
		while(curs.hasNext()){
			curs.next();
			temp.add(new Station(curs.curr()));
		}
		return temp;
	}

	private static double getCallLng(String callid) throws IOException {
		final Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(Database.get("addr", "cadid", callid, "Calls", "basicInfo")).setLanguage("en").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		return geocoderResponse.getResults().get(0).getGeometry().getLocation().getLng().doubleValue();
	}

	private static double getCallLat(String callid) throws IOException {
		final Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(Database.get("addr", "cadid", callid, "Calls", "basicInfo")).setLanguage("en").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		return geocoderResponse.getResults().get(0).getGeometry().getLocation().getLat().doubleValue();
	}

	public static List<AppList> recUnitsList(String cadid, List<AppList> list) {
		List<Apparatus> units;
		try {
			units = getUnitsWithType(cadid);
	//		DispatchSeq seq = new DispatchSeq(cadid);
			List<AppList> nUni = UnitSelection(units);
			return nUni;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	private static List<AppList> UnitSelection(List<Apparatus> units) {
		// TODO Auto-generated method stub
		return null;
	}
}