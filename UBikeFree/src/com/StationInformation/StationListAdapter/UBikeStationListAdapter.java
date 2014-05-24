package com.StationInformation.StationListAdapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.StationInformation.UBStation;
import com.UBikeFree.R;

public class UBikeStationListAdapter extends ArrayAdapter<UBStation> implements Filterable{

	private List<UBStation> stationList;
	private List<UBStation> allStations;
	private Context context;
	
	private UBikeStationListFilter stationFilter;
	private static String currentGroup = "";

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
		
		//set separator visible or not
		
		determineItemSeapratorVisible(holder, position);
		
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

			//reset recorded group value
			currentGroup = "";
			
			FilterResults results = new FilterResults();
			//implement filter logic here
			if(constraint == null || constraint.length() == 0) {
				//No filter implemented, we return all the list
				clear();
				stationList = new ArrayList<UBStation>(allStations);
				notifyDataSetChanged();
				results.values = stationList;
				results.count = stationList.size();
			}//end if
			else {
				//we perform filtering operation
				List<UBStation> nStationList = new ArrayList<UBStation>();
				
				for(UBStation station : stationList) {
					if(station.getName().contains(constraint.toString())
							|| station.getAddress().contains(constraint))
						nStationList.add(station);
				}
				
				results.values = nStationList;
				results.count = nStationList.size();
			}//end else
			
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			
			//inform the adapter about the new list filtered
			if(results.count == 0)
				notifyDataSetInvalidated();
			else {
				
				stationList = (List<UBStation>)results.values;
				notifyDataSetChanged();
				clear();
			}
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
	
	private void determineItemSeapratorVisible(StationHolder viewHolder, int position) {
		
		if(viewHolder != null) {
			
			//first item of the list view
			if(position == 0) {
				//visible
				viewHolder.stationSeparator.setVisibility(View.VISIBLE);
				currentGroup = stationList.get(position).getArea();
			}
			else {
				//if this list item is not the first item of the current group
				if(currentGroup.compareTo(stationList.get(position).getArea()) == 0) {
					//invisible
					viewHolder.stationSeparator.setVisibility(View.GONE);
				}
				else {
					//visible
					viewHolder.stationSeparator.setVisibility(View.VISIBLE);
					//record new group
					currentGroup = stationList.get(position).getArea();
				}
			}
		}//end if
	}
	
}
