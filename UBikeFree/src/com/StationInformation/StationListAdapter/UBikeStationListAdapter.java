package com.StationInformation.StationListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
//import android.widget.Toast;

import com.StationInformation.UBStation;
import com.UBikeFree.R;

public class UBikeStationListAdapter extends ArrayAdapter<UBStation> implements Filterable{

	private List<UBStation> stationList;
	private List<UBStation> allStations;
	private Context context;
	
	private UBikeStationListFilter stationFilter;

	public UBikeStationListAdapter(Context context,	List<UBStation> objects) {
		super(context, R.layout.list_item, objects);
		
		this.context = context;
		this.stationList = objects;
		this.allStations = new ArrayList<UBStation>(objects);
	}
	
	@Override
	public int getCount() {
		return stationList.size();
	}
	
	@Override
	public UBStation getItem(int position) {
		return stationList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		StationHolder holder = new StationHolder();//View Holder Pattern
				
		//verify the convertView is not null
		if(convertView == null) {
			//a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item, parent, false);
		
			//fill the layout with right values
			TextView nameTextView = (TextView)v.findViewById(R.id.ubstation_name);
			TextView numTextView = (TextView)v.findViewById(R.id.ubstation_numofbikes);
			TextView separatorView = (TextView)v.findViewById(R.id.ubstation_separator);
			
			holder.stationNameView = nameTextView;
			holder.stationNumOfBikesView = numTextView;
			holder.stationSeparator = separatorView;
			
			v.setTag(holder);
		}//end if
		else
			holder = (StationHolder)v.getTag();
		
		UBStation station = stationList.get(position);
		holder.stationNameView.setText(station.getName());
		holder.stationNumOfBikesView.setText("�i�ɡG" + station.getBikes() +
											 "/�i�١G" + station.getEmptySlots());
		holder.stationSeparator.setText(station.getArea());
		
		determineSeparatorVisible(holder, position);

		return v;
	}
	
	private static class StationHolder {
		public TextView stationSeparator;
		public TextView stationNameView;
		public TextView stationNumOfBikesView;
	}
	
	
	private class UBikeStationListFilter extends Filter {
		
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			
			FilterResults results = new FilterResults();
			//implement filter logic here
			if(constraint == null || constraint.length() == 0) {
				
				//No filter implemented, we return all the list
				results.values = allStations;
				results.count = allStations.size();
			}//end if
			else {
				//we perform filtering operation
				List<UBStation> nStationList = new ArrayList<UBStation>();
				
				for(UBStation station : allStations) {
					//filter by area, name and address
					if(station.getArea().contains(constraint.toString())
							|| station.getName().contains(constraint.toString())
							|| station.getAddress().contains(constraint))
						nStationList.add(station);
				}
				
				results.values = nStationList;
				results.count = nStationList.size();
				Log.e("filtered values", nStationList.toString());
			}//end else
			
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			
			//inform the adapter about the new list filtered
			clear();
			stationList = (List<UBStation>) results.values;
			Log.e("publish results", results.values.toString());
			notifyDataSetChanged();
		}
	}
	
	@Override
	public Filter getFilter() {
		
		if(stationFilter == null)
			stationFilter = new UBikeStationListFilter();
		
		return stationFilter;
	}
	
	public void swapStationItems(List<UBStation> stations) {
		
		this.allStations.clear();
		this.stationList.clear();
		this.allStations = stations;
		this.stationList = this.allStations;
		notifyDataSetChanged();
	}
	
	private void determineSeparatorVisible(StationHolder viewHolder, int position) {
		
		//set separator visible or not
		if(position == 0) {
			viewHolder.stationSeparator.setVisibility(View.VISIBLE);
		}
		else {
			if(stationList.get(position-1).getArea()
					.compareTo(stationList.get(position).getArea()) != 0) {
				viewHolder.stationSeparator.setVisibility(View.VISIBLE);
			}
			else {
				viewHolder.stationSeparator.setVisibility(View.GONE);
			}
		}
	}
}
