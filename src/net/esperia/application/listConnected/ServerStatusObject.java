package net.esperia.application.listConnected;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class ServerStatusObject {
	private boolean online;
	private int connectedNumber;
	private String[] connected;
	private List<Bitmap> avatars;

	public ServerStatusObject() throws JSONException {
		this(new JSONObject(ServerStatusObject.executeHttpGet()));
	}
	
	public ServerStatusObject(JSONObject jobj) {
		try {
			this.avatars = new ArrayList<Bitmap>();
			String status = jobj.getString("status");
			this.online = (status != null && status.equalsIgnoreCase("online"));

			this.connectedNumber = jobj.getInt("connected");

			//this.connected = new ArrayList<String>();

			JSONArray liste = jobj.getJSONArray("players");
			this.connected = new String[liste.length()];
			for(int i = 0; i < liste.length(); i++){
				//this.connected.add(liste.getString(i));
				this.connected[i] = liste.getString(i);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	public String getLogin(int i) {
		if(this.connected != null && this.connected.length > i)
			return connected[i];
		else
			return "";
	}

	public Bitmap getAvatar(int i) {
		if(this.avatars != null && this.avatars.size() > i)
			return avatars.get(i);
		else
			return null;
	}

	public boolean isOnline() {
		return online;
	}


	public void setOnline(boolean online) {
		this.online = online;
	}


	public int getConnectedNumber() {
		return connectedNumber;
	}


	public void setConnectedNumber(int connectedNumber) {
		this.connectedNumber = connectedNumber;
	}


	public String[] getConnected() {
		return connected;
	}


	public void setConnected(String[] connected) {
		this.connected = connected;
	}


	public List<Bitmap> getAvatars() {
		return avatars;
	}


	public void setAvatars(List<Bitmap> avatars) {
		this.avatars = avatars;
	}

	public void setAvatar(int pos, Bitmap avatar) {
		if(this.avatars == null)
			this.avatars = new ArrayList<Bitmap>();
		this.avatars.add(pos, avatar);
	}
	private static String executeHttpGet() {
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


}
