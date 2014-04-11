package org.taipeitech.csie1623.UBIkeData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ChengFu, Lin
 *
 */
public class UBikeDataGetter {
	
	private JSONObject uBikeJsonObject;
	private JSONArray uBikeStationJsonData;
	private int numOfUBikeStations;
	
	public static final String UBIKE_STATIONS_DB_URL = "http://210.69.61.60:8080/you/gwjs_cityhall.json";
	public static final String UBIKE_STATION_DATA_ARRAY_KEY = "retVal";
	public static final String UBIKE_STATION_NAME_KEY = "sna";
	public static final String UBIKE_STATION_DATA_ID_KEY = "iid";
	public static final String UBIKE_STATION_TOTAL_BIKES_KEY = "tot";
	public static final String UBIKE_STATION_CURRENT_BIKES_KEY = "sbi";
	public static final String UBIKE_STATION_LOCATED_AREA_KEY = "sarea";
	public static final String UBIKE_STATION_UPDATED_DATE_KEY = "mday";
	public static final String UBIKE_STATION_LATITUDE_KEY = "lat";
	public static final String UBIKE_STATION_LONGITUDE_KEY = "lng";
	
	private static final byte MAX_LENGTH_OF_TIMESTRING = 14;
	private static final byte NUM_OF_TIMESTRING_PARTS = 6;
	private static final byte LENGTH_OF_YEAR_TEXT = 4;
	private static final byte LENGTH_OF_MONTH_TEXT = 2;
	private static final byte LENGTH_OF_DAY_TEXT = 2;
	private static final byte LENGTH_OF_HOUR_TEXT = 2;
	private static final byte LENGTH_OF_MINUTE_TEXT = 2;
	private static final byte LENGTH_OF_SECOND_TEXT = 2;

	private static final byte TIMESTRING_YEAR = 0;
	private static final byte TIMESTRING_MONTH = 1;
	private static final byte TIMESTRING_DAY = 2;
	private static final byte TIMESTRING_HOUR = 3;
	private static final byte TIMESTRING_MINUTE = 4;
	private static final byte TIMESTRING_SECOND = 5;
	

	/**
	 * 
	 */
	public UBikeDataGetter() {
		
		uBikeStationJsonData = new JSONArray();
		uBikeJsonObject = new JSONObject();
		
		setUBikeJsonDataFromURL();
		setNumOfUBikeStations();
	}
	
	/**
	 * 
	 */
	private void setNumOfUBikeStations() {
		
		numOfUBikeStations = uBikeStationJsonData.length();
	}
	
	/**
	 * @return
	 */
	public JSONObject getUBikeJsonObject() {
		
		return uBikeJsonObject;
	}
	
	/**
	 * @return
	 */
	public JSONArray getUBikeStationJsonData() {
		
		return uBikeStationJsonData;
	}
	
	/**
	 * 
	 */
	public void setUBikeJsonDataFromURL() {
		
		try {
			
			String uBikeFeed = readUBikeFeed();
			uBikeJsonObject = new JSONObject(uBikeFeed);
			uBikeStationJsonData = uBikeJsonObject.getJSONArray(UBIKE_STATION_DATA_ARRAY_KEY);
		}
		catch(JSONException jsonException) {
			jsonException.printStackTrace();
		}
	}
	
	public String readUBikeFeed() {
		
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(UBIKE_STATIONS_DB_URL);
		
		try {
		      HttpResponse response = client.execute(httpGet);
		      StatusLine statusLine = response.getStatusLine();
		      int statusCode = statusLine.getStatusCode();
		      if (statusCode == 200) {
		        HttpEntity entity = response.getEntity();
		        InputStream content = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		        String line;
		        while ((line = reader.readLine()) != null) {
		          builder.append(line);
		        }
		      } else {
		        //Log.e(ParseJSON.class.toString(), "Failed to download file");
		      }
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
		
		return builder.toString();
	}
	
	/**
	 * @return
	 */
	public int getNumOfUBikeStations() {
		
		return numOfUBikeStations;
	}
	
	/**
	 * @param index
	 * @return
	 */
	public String getUBikeStationName(int index) {
		
		String name = null;
		JSONObject findingStation = new JSONObject();
		try {
			findingStation = uBikeStationJsonData.getJSONObject(index);
			name = findingStation.getString(UBIKE_STATION_NAME_KEY);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return name; 
	}
	
	/**
	 * @param index
	 * @return
	 */
	public String getUBikeStationDataID(int index) {
	
		String id = null;
		JSONObject findingStation = new JSONObject();
		try {
			findingStation = uBikeStationJsonData.getJSONObject(index);
			id = findingStation.getString(UBIKE_STATION_DATA_ID_KEY);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * @param index
	 * @return
	 */
	public String getUBikeStationTotalNumOfBikes(int index) {
		
		String numOfBikes = null;
		JSONObject findingStation = new JSONObject();
		
		
		try {
			findingStation = uBikeStationJsonData.getJSONObject(index);
			numOfBikes = findingStation.getString(UBIKE_STATION_TOTAL_BIKES_KEY);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return numOfBikes;
	}
	
	/**
	 * @param index
	 * @return
	 */
	public String getUBikeStationCurrentNumOfBikes(int index) {
		
		String numOfBikes = null;
		JSONObject findingStation = new JSONObject();
		
		try {
			findingStation = uBikeStationJsonData.getJSONObject(index);
			numOfBikes = findingStation.getString(UBIKE_STATION_CURRENT_BIKES_KEY);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numOfBikes;
	}
	
	/**
	 * @param index
	 * @return
	 */
	public String getUBikeStationLocateArea(int index) {
		
		JSONObject findingStation;
		String locatedArea = null;
		try {
			findingStation = uBikeStationJsonData.getJSONObject(index);
			locatedArea = findingStation.getString(UBIKE_STATION_LOCATED_AREA_KEY);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return locatedArea;
	}
	
	/**
	 * @param index
	 * @return
	 */
	public String getUBikeStationUpdateTimeString(int index) {
		
		String defaultTimeString = null;
		String formattedTimeString = null;
		
		JSONObject findingStation;
		try {
			findingStation = uBikeStationJsonData.getJSONObject(index);
			defaultTimeString = findingStation.getString(UBIKE_STATION_UPDATED_DATE_KEY);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		formattedTimeString = getFormattedTimeString(defaultTimeString);
		return formattedTimeString;
	}
	
	/**
	 * @param timeString
	 * @return
	 */
	private String getFormattedTimeString(String timeString) {
		
		String formattedTimeString = "";
		
		if(timeString.length() == MAX_LENGTH_OF_TIMESTRING) {
		
			for(int beginIndex = 0, endIndex = 0, loopIndex = 0; loopIndex < NUM_OF_TIMESTRING_PARTS; ++loopIndex) {
				
				switch(loopIndex) {
				
				case TIMESTRING_YEAR:
					endIndex = LENGTH_OF_YEAR_TEXT;
					formattedTimeString += timeString.substring(beginIndex, endIndex);
					break;
					
				case TIMESTRING_MONTH:
					endIndex += LENGTH_OF_MONTH_TEXT;
					formattedTimeString += "/" + timeString.substring(beginIndex, endIndex);
					break;
					
				case TIMESTRING_DAY:
					endIndex += LENGTH_OF_DAY_TEXT;
					formattedTimeString += "/" + timeString.substring(beginIndex, endIndex);
					break;
					
				case TIMESTRING_HOUR:
					endIndex += LENGTH_OF_HOUR_TEXT;
					formattedTimeString += " " + timeString.substring(beginIndex, endIndex);
					break;
					
				case TIMESTRING_MINUTE:
					endIndex += LENGTH_OF_MINUTE_TEXT;
					formattedTimeString += ":" + timeString.substring(beginIndex, endIndex);
					break;
					
				case TIMESTRING_SECOND:
					endIndex += LENGTH_OF_SECOND_TEXT;
					formattedTimeString += ":" + timeString.substring(beginIndex, endIndex);
					break;
				}//end of switch
				
				beginIndex = endIndex;
			}//end for loop
		}//end if
		
		return formattedTimeString;
	}
	
	/**
	 * @return
	 */
	public HashMap<String, JSONObject> getUBikeJsonObjectsHashMapWithKeyStationName() {
		
		HashMap<String, JSONObject> result = new HashMap<String, JSONObject>();
		
		for(int index = 0; index < uBikeStationJsonData.length(); ++index) {
			
			try {
				JSONObject object = uBikeStationJsonData.getJSONObject(index);
				result.put(object.getString(UBIKE_STATION_NAME_KEY), object);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//end for loop
		
		return result;
	}
	
	/**
	 * @return
	 */
	public ArrayList<String> getUBikeStationNames() {
		
		ArrayList<String> names = new ArrayList<String>();
		
    	for(int index = 0; index < numOfUBikeStations; ++index) {
    		names.add(getUBikeStationName(index));
    	}//end for loop
    	
    	return names;
	}
	
	/**
	 * @return
	 */
	public HashMap<String, String> getUBikeCurrentOfTotalBikesWithKeyStationName() {
		
		HashMap<String, String> mapForCurrentOfTotalBikes = new HashMap<String, String>();
		
		for(int index = 0; index < numOfUBikeStations; ++index) {
			
			try {
				JSONObject object = uBikeStationJsonData.getJSONObject(index);
				
				String currentBikes = object.getString(UBIKE_STATION_CURRENT_BIKES_KEY);
				String totalBikes = object.getString(UBIKE_STATION_TOTAL_BIKES_KEY);
				
				mapForCurrentOfTotalBikes.put(object.getString(UBIKE_STATION_NAME_KEY), (currentBikes + "/" + totalBikes) );
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//end for loop
		
		return mapForCurrentOfTotalBikes;
	}
	
	/**
	 * @return
	 */
	public HashMap<String, HashMap<String, String> > getUBikeStationLatitudeAndLogitudeWithKeyStationName() {
		
		HashMap<String, HashMap<String, String> > map = new HashMap<String, HashMap<String, String> >();
		
		for(int index = 0; index < numOfUBikeStations; ++index) {
			
			HashMap<String, String> locatMap = new HashMap<String, String>();
			
			try {
				JSONObject object = uBikeStationJsonData.getJSONObject(index);
				
				//get latitude and longitude
				locatMap.put(UBIKE_STATION_LATITUDE_KEY, object.getString(UBIKE_STATION_LATITUDE_KEY));
				locatMap.put(UBIKE_STATION_LONGITUDE_KEY, object.getString(UBIKE_STATION_LONGITUDE_KEY));
				
				map.put(object.getString(UBIKE_STATION_NAME_KEY), locatMap);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//end for loop
		
		return map;
	}
	
	
	
	/**
	 * @param columnName
	 * @return
	 */
	public HashMap<String, String> getUBikeColumnDataWithKeyStationName(String columnName) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		for(int index = 0; index < numOfUBikeStations; ++index) {
			
			try {
				JSONObject object = uBikeStationJsonData.getJSONObject(index);
				map.put(object.getString(UBIKE_STATION_NAME_KEY), object.getString(columnName));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//end for loop
		
		return map;
	}
}
