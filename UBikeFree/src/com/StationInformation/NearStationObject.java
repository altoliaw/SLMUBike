package com.StationInformation;

import com.google.android.gms.maps.model.Marker;

public class NearStationObject{
	public Marker obj_StationMarkers;
	public double db_Distance;
	public NearStationObject(Marker obj_StationMarkers,double db_Distance){
		this.obj_StationMarkers=obj_StationMarkers;
		this.db_Distance=db_Distance;
	}	
}
