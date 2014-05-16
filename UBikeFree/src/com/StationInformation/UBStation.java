package com.StationInformation;

import org.json.JSONObject;
import org.json.JSONException;

public class UBStation
{
    private String id_;
    private String name_;
    private String bikes_;
    private String emptySlots_;
    private float latitude_;
    private float lngitude_;
    private String area_;
    
    public UBStation(JSONObject jsta) throws JSONException {
        id_ = jsta.getString("iid");
        name_ = jsta.getString("sna");
        bikes_ = jsta.getString("sbi");
        emptySlots_ = jsta.getString("bemp");
        latitude_ = Float.parseFloat(jsta.getString("lat"));
        lngitude_ = Float.parseFloat(jsta.getString("lng"));
        area_ = jsta.getString("sarea");//取得UBike站點所在行政區(ex: 信義區)
    }

    public String getId()
    {
        return id_;
    }

    public String getName()
    {
        return name_;
    }

    public String getBikes()
    {
        return bikes_;
    }

    public String getEmptySlots()
    {
        return emptySlots_;
    }

    public float getLat()
    {
        return latitude_;
    }

    public float getLng()
    {
        return lngitude_;
    }
    
    public String getArea() {
    	return area_;
    }
}

