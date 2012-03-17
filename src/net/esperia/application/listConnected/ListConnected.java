package net.esperia.application.listConnected;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import net.esperia.application.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ListConnected extends ListActivity {
	private ServerStatusObject connectedObject;
	private final int MULTIPLICATEUR_IMAGE = 15;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new DownloadListTask().execute();
	}


	private class DownloadListTask extends AsyncTask<Void, Void, ServerStatusObject> {
		protected ServerStatusObject doInBackground(Void... urls) {
			String json = executeHttpGet();

			try {
				JSONObject jobj = new JSONObject(json);
				return new ServerStatusObject(jobj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(ServerStatusObject result) {
			//tv.setText(result);
			//mImageView.setImageBitmap(result);
			connectedObject = result;
			setListAdapter(new ConnectedArrayAdapter(ListConnected.this, result.getConnected()));
			int listSize = result.getConnected().length;
			
			setTitle("Connectés :" + listSize);
			
			for(int i = 0; i < listSize; i++)
				new DownloadAvatarTask().execute(i);
		}
	}
	
	private class DownloadAvatarTask extends AsyncTask<Integer, Void, Integer> {
		protected Integer doInBackground(Integer... pos) {
			connectedObject.setAvatar(pos[0], getFullAvatarBitmap(connectedObject.getLogin(pos[0])));
			return pos[0];
		}

		protected void onPostExecute(Integer pos) {
			View child = getListView().getChildAt(pos);
			ImageView im = (ImageView)child.findViewById(R.id.avatar);
			Bitmap avatar = connectedObject.getAvatar(pos);
			if(avatar != null)
				im.setImageBitmap(avatar);
		}
	}
	
	private String executeHttpGet() {
		BufferedReader in = null;
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI("http://www.esperia-rp.net/server_status_json.php"));
			HttpResponse response = client.execute(request);
			in = new BufferedReader
					(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				//sb.append(line + NL);
				sb.append(line);
			}
			in.close();
			String page = sb.toString();
			result = page;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	private String getAvatarUrl(String loginMinecraft) {
		return "http://s3-eu-west-1.amazonaws.com/esperia/Skin/"+loginMinecraft+".png";
	}
	
	private Bitmap getFullAvatarBitmap(String loginMinecraft)  {
		Bitmap downloaded;
		try {
			downloaded = getBitmap(getAvatarUrl(loginMinecraft));
			Matrix transform = new Matrix();
			transform.setScale(MULTIPLICATEUR_IMAGE, MULTIPLICATEUR_IMAGE);

			Bitmap visage = Bitmap.createBitmap(downloaded, 8, 8, 8, 8);
			Bitmap cheveux = Bitmap.createBitmap(downloaded, 40, 8, 8, 8);

			Bitmap complet = applyMask(visage, cheveux);
			
			
			return Bitmap.createBitmap(complet, 0, 0, 8, 8, transform, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}
		return null;
		
	}
	
	private Bitmap getBitmap(String strURL) throws IOException {
		URL url = new URL(strURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setConnectTimeout(1000);
		conn.setRequestMethod("GET");
		conn.connect();
		//TODO enlever ces 5 tentatives
		for (int i = 0; i < 5; i++) { //5 tentatives
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = conn.getInputStream();
				Bitmap bm = BitmapFactory.decodeStream(is, null, null);
				if (is != null) {
					is.close();
					is = null;
				}
				if (conn != null) {
					conn.disconnect();
					conn = null;
				}
				return bm;
			}
		}
		return null;
	}
	private static Bitmap applyMask(Bitmap tileBitmap, Bitmap maskBitmap)
	{
		Bitmap workingBitmap = tileBitmap.copy(Bitmap.Config.ARGB_8888, true); //IMPORTANT
		int width = tileBitmap.getWidth();
		int height = tileBitmap.getHeight();
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				int maskPixel = maskBitmap.getPixel(x, y);
				int tilePixel = tileBitmap.getPixel(x, y);

				//change this line for different color mixing types
				//int newPixel = Color.argb( Color.red(maskPixel), Color.red(tilePixel), Color.green(tilePixel), Color.blue(tilePixel));
				if(Color.alpha(maskPixel) < 128) {
					workingBitmap.setPixel(x, y, tilePixel);
				} else {
					workingBitmap.setPixel(x, y, maskPixel);
				}
				//workingBitmap.setPixel(x, y, newPixel); //IMPORTANT
			}
		}
		return workingBitmap;
	} 
}
