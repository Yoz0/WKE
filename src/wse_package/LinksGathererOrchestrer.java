package wse_package;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.mongodb.client.MongoCollection;



public class LinksGathererOrchestrer extends Thread {
	static final int NB_WORKERS = 64;
	List<LinksGathererWorker> workers;
	Vector<String> article_names;
	int nombre_articles;
	private MongoCollection collection;
	
	public Vector<String> readArticleNames(String file_name){
		Vector<String> result = new Vector<String>();
		try{
		InputStream flux=new FileInputStream(file_name); 
		InputStreamReader lecture=new InputStreamReader(flux);
		BufferedReader buff=new BufferedReader(lecture);
		String ligne;
		while ((ligne=buff.readLine())!=null){
			result.add(ligne);
		}
		buff.close(); 
		}		
		catch (Exception e){
		System.out.println(e.toString());
		}
		return result;
	}
	
	public LinksGathererOrchestrer(MongoCollection collection){
		LinksGathererWorker w;
		this.collection = collection;
		this.workers = new ArrayList<LinksGathererWorker>();
		this.article_names = readArticleNames("wikipedia-page-id-title.raw");
		this.nombre_articles = article_names.size();
		for(int i=0; i<NB_WORKERS; i++){
			w = new LinksGathererWorker(article_names, i, this.collection);
			workers.add(w);
		}
	}
	
	public void killWorkers(){
		for (LinksGathererWorker w : workers){
			w.interrupt();
		}
	}
	
	@Override
	public void run(){
		for (LinksGathererWorker w: workers){
			w.start();
		}
		while(true){
			System.out.println(article_names.size() + "/" + nombre_articles);
			if (article_names.size() < 100){
				break;
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (LinksGathererWorker w: workers){
			try{
				w.join();
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
