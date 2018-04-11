package com.zmobile.percentcalc;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageArrayAdapter extends ArrayAdapter<ListItem> {
	

	private ArrayList<ListItem> items;
	Context c;


	public ImageArrayAdapter(Context context, int textViewResourceId, ArrayList<ListItem> items) {		
		super(context, textViewResourceId, items);
		this.c = context;
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_item, null);
		}
		
		ListItem item = items.get(position);
		
		if (item != null) {
			TextView title = (TextView) v.findViewById(R.id.title);
			//TextView text = (TextView) v.findViewById(R.id.text);
			//ImageView img = (ImageView) v.findViewById(R.id.img);
			
			if (title != null) {
				title.setText(item.title);
			}
			/*
			if(text != null) {
				text.setText("text: " + item.text );
			}
			
			if(img != null) {
				
				int id = c.getResources().getIdentifier(item.img, "id", c.getPackageName());
				img.setImageResource(id);
			}*/
		}
		return v;
	}
}