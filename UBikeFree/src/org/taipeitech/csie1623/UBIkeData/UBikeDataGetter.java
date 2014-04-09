package org.taipeitech.csie1623.UBIkeData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * @author ChengFu, Lin
 *
 */
public class UBikeDataGetter {
	
	private InputStream uBikeJsonDataInputStream;
	private Reader uBikeJsonDataReader;
	private String uBikeJsonDataString;
	private JSONObject uBikeJsonObject;
	private JSONArray uBikeStationJsonData;
	private final int numOfUBikeStations;
	
	public static final String UBIKE_STATIONS_DB_URL = "http://210.69.61.60:8080/you/gwjs_cityhall.json";
	public static final String UBIKE_STATION_DATA_ARRAY_KEY = "retVal";
	public static final String UBIKE_STATION_NAME_KEY = "sna";
	public static final String UBIKE_STATION_DATA_ID_KEY = "iid";
	public static final String UBIKE_STATION_TOTAL_BIKES_KEY = "tot";
	public static final String UBIKE_STATION_CURRENT_BIKES_KEY = "sbi";
	public static final String UBIKE_STATION_LOCATED_AREA_KEY = "sarea";
	public static final String UBIKE_STATION_UPDATED_DATE_KEY = "mday";
	
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

		uBikeJsonDataString = "";
		setUBikeJsonDataFromURL();

		try {
			setUBikeJSONObject();
			setUBikeStationJsonData();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		numOfUBikeStations = uBikeStationJsonData.length();
	}
	
	/**
	 * 
	 */
	private void setUBikeJsonDataFromURL() {
		
		try {
			//get input stream from URL
			uBikeJsonDataInputStream = new URL(UBIKE_STATIONS_DB_URL).openStream();
			uBikeJsonDataReader = new BufferedReader(new InputStreamReader(uBikeJsonDataInputStream));
			
			char buffer[] = new char[256];
			while(uBikeJsonDataReader.ready()) {
				uBikeJsonDataReader.read(buffer);
				uBikeJsonDataString.concat(new String(buffer));
			}
			
			uBikeJsonDataInputStream.close();
			uBikeJsonDataReader.close();
		}
		catch(MalformedURLException malFormedURLException) {
			malFormedURLException.printStackTrace();
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}//end try-catch blocks
	}
	
	/**
	 * @throws JSONException 
	 * 
	 */
	private void setUBikeJSONObject() throws JSONException {
		
		uBikeJsonObject = new JSONObject(new JSONTokener(uBikeJsonDataString));
	}
	
	/**
	 * @throws JSONException 
	 * 
	 */
	private void setUBikeStationJsonData() throws JSONException {
		
		uBikeStationJsonData = uBikeJsonObject.getJSONArray(UBIKE_STATION_DATA_ARRAY_KEY);
	}
	
	public int getNumOfUBikeStations() {
		
		return numOfUBikeStations;
	}
	
	/**
	 * @param index
	 * @return
	 * @throws JSONException 
	 */
	public String getUBikeStationName(int index) throws JSONException {
		
		JSONObject findingStation = uBikeStationJsonData.getJSONObject(index);
		return findingStation.getString(UBIKE_STATION_NAME_KEY);
	}
	
	/**
	 * @param index
	 * @return
	 * @throws JSONException 
	 */
	public String getUBikeStationDataID(int index) throws JSONException {
	
		JSONObject findingStation = uBikeStationJsonData.getJSONObject(index);
		return findingStation.getString(UBIKE_STATION_DATA_ID_KEY);
	}
	
	/**
	 * @param index
	 * @return
	 * @throws JSONException 
	 */
	public String getUBikeStationTotalNumOfBikes(int index) throws JSONException {
		
		JSONObject findingStation = uBikeStationJsonData.getJSONObject(index);
		return findingStation.getString(UBIKE_STATION_TOTAL_BIKES_KEY);
	}
	
	/**
	 * @param index
	 * @return
	 * @throws JSONException 
	 */
	public String getUBikeStationCurrentNumOfBikes(int index) throws JSONException {
		
		JSONObject findingStation = uBikeStationJsonData.getJSONObject(index);
		return findingStation.getString(UBIKE_STATION_CURRENT_BIKES_KEY);
	}
	
	/**
	 * @param index
	 * @return
	 * @throws JSONException 
	 */
	public String getUBikeStationLocateArea(int index) throws JSONException {
		
		JSONObject findingStation = uBikeStationJsonData.getJSONObject(index);
		return findingStation.getString(UBIKE_STATION_LOCATED_AREA_KEY);
	}
	
	/**
	 * @param index
	 * @return
	 * @throws JSONException 
	 */
	public String getUBikeStationUpdateTimeString(int index) throws JSONException {
		
		String defaultTimeString;
		String formattedTimeString;
		
		JSONObject findingStation = uBikeStationJsonData.getJSONObject(index);
		defaultTimeString = findingStation.getString(UBIKE_STATION_UPDATED_DATE_KEY);
		
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
	 * @throws JSONException 
	 */
	public HashMap<String, JSONObject> getUBikeJsonObjectsHashMapWithKeyStationName() throws JSONException {
		
		HashMap<String, JSONObject> result = new HashMap<String, JSONObject>();
		
		for(int index = 0; index < uBikeStationJsonData.length(); ++index) {
			
			JSONObject object = uBikeStationJsonData.getJSONObject(index);
			result.put(object.getString(UBIKE_STATION_NAME_KEY), object);
		}//end for loop
		
		return result;
	}
}
