package wse_package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class LinksGathererWorker extends Thread {

	private Vector<String> linklist;
	private int numero_de_thread;
	private MongoCollection mme_collection;
	
	
	public static String get(String url) throws IOException{
		 
		String source ="";
		URL oracle = new URL(url);
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(
		new InputStreamReader(
		yc.getInputStream()));
		String inputLine;
		 
		while ((inputLine = in.readLine()) != null)
		source +=inputLine;
		in.close();
		return source;
		}
	
	LinksGathererWorker(Vector<String> linklist, int num, MongoCollection<Document> collection) {
		this.linklist = linklist;
		this.numero_de_thread = num ;
		this.mme_collection = collection;
	}
	
	@Override
	public void run() {
		while (!linklist.isEmpty()){
			String article_name = linklist.remove(0);
			article_name = article_name.replaceAll("&", "%26");
			article_name = article_name.replaceAll("\\+", "%2B");
			String url = "https://fr.wikipedia.org/w/api.php?action=parse&page="+article_name+"&prop=links&format=json";
			String mrJson = null;
			try{
				mrJson = get(url);
			}
			catch (IOException e) {
				System.out.println("error with get request :"+url);
				break;
			}
			JSONObject obj = null;
			try {
				obj = new JSONObject(mrJson);
			} 
			catch (org.json.JSONException e) {
				System.out.println("error with json object creation");
				break;
			}

			java.util.Set<java.lang.String> keys = obj.keySet();
			if (keys.contains("error")){
				System.out.println("page (" + article_name + ") manquante");
			}
			else{
				JSONObject mrParse = null;
				try {
					mrParse = obj.getJSONObject("parse");
				} 
				catch (org.json.JSONException e) {
					System.out.println("error with mrParse object creation");
					break;
				}
				JSONArray mrLiens = null;
				try {
					mrLiens = mrParse.getJSONArray("links");
				} catch (org.json.JSONException e) {
					System.out.println("error while parsing links");
					break;
				}
				ArrayList<String> leavingLinks = new ArrayList<String>();
				JSONObject lien;
				for (int i = 0; i < mrLiens.length(); i++){
		            lien = mrLiens.getJSONObject(i);
		            int mmeValeur = lien.getInt("ns");
		            if (mmeValeur == 0){
		            	leavingLinks.add(lien.getString("*"));
		            }
		        }
				Document mrBdd = new Document().append("name", article_name).append("list_links", leavingLinks)
						.append("pagerank",1.00);
				mme_collection.insertOne(mrBdd);
				
			}
		}
	}
}
