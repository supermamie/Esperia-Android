package net.esperia.application.listConnected;

import net.esperia.application.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConnectedArrayAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final String[] values;

	public ConnectedArrayAdapter(Context context, String[] values) {
		super(context, R.layout.connectedlistitem, values);
		this.context = context;
		this.values = values;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.connectedlistitem, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.textView1);
		ImageView imgView = (ImageView) rowView.findViewById(R.id.avatar);
		
		String login = values[position];
		
		textView.setText(login);

		Log.d("trucmuche","On affiche la vue n°"+position);
		if(ListConnected.connectedObject != null) {
			Bitmap avatar = ListConnected.connectedObject.getAvatar(position);
			if(avatar != null)
				imgView.setImageBitmap(avatar);
		}
		/*try {
			imageView.setImageBitmap(getFullAvatarBitmap(login));
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}*/
		/*if (s.equals("WindowsMobile")) {
			imageView.setImageResource(R.drawable.windowsmobile_logo);
		} else if (s.equals("iOS")) {
			imageView.setImageResource(R.drawable.ios_logo);
		} else if (s.equals("Blackberry")) {
			imageView.setImageResource(R.drawable.blackberry_logo);
		} else {
			imageView.setImageResource(R.drawable.android_logo);
		}*/

		return rowView;
	}
	
	
}
