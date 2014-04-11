package com.smsReceiver.smsreceiver;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;
import android.util.Log;

import com.smsutils.MD5;
import com.smsutils.UpdateFailedException;

public class UpdateDatabase {
	String url="";
	String secret="";
	
	public UpdateDatabase() {
	}
	
	public UpdateDatabase(String url, String secret) {
		this.url = url;
		this.secret = secret;
	}
	
	public void setNetworkKey(String networkKey) {
		this.secret = networkKey;
	}
	
	public void setDomainName(String domainName) {
		this.url = domainName;
	}
	
	public void update(String number, String data) throws UpdateFailedException {		
		String hash = MD5.hash(number+data+this.secret);
		
		//String uri = this.url+this.path+"?number="+number+"&data="+data+"&signature="+hash;
		String uri = new Uri.Builder()
			.scheme("http")
			.encodedAuthority(this.url)
			.appendQueryParameter("number", number)
			.appendQueryParameter("data", data)
			.appendQueryParameter("signature", hash)
			.build().toString();
		
		Log.d("uri",uri);
		try {
			/*
			url = new URL(uri);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			
			urlConnection.disconnect();
			*/
		    HttpClient httpclient = new DefaultHttpClient();
		    httpclient.execute(new HttpGet(uri)).getEntity().getContent().close();
		} catch (Exception e) {
			throw  new UpdateFailedException(e);
		}

	}

}
