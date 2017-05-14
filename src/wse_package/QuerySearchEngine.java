package wse_package;

import java.util.ArrayList;
import java.util.Comparator;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

public class QuerySearchEngine {
	/**
	 * Queries the search engines to find best articles that match the searchString.
	 * @param searchString
	 * @return the ordered list of articles (as documents) that suits best to the searchString.
	 */
	static ArrayList<Document> search(MongoCollection<Document> collection, String searchString){
		//preparing search filters
		String[] listSearchTerms = searchString.split(" ");
		ArrayList<Bson> filters = new ArrayList<Bson>();
		for(String s : listSearchTerms){
			filters.add(Filters.regex("name", s, "i"));
		}
		Bson filter_and = Filters.and(filters);
		//Bson filter_or = Filters.or(filters);
		
		//querying
		ArrayList<Document> ret = new ArrayList<Document>();
		MongoCursor<Document> cursor = collection.find(filter_and).iterator();
		try {
		    while (cursor.hasNext()) {
		        ret.add(cursor.next());
		    }
		} finally {
		    cursor.close();
		}
		
		//sorting
		ret.sort(new pagerankComparator());
		
		return ret;
	}
	
	private static class pagerankComparator implements Comparator<Document>{
		@Override
		public int compare(Document arg0, Document arg1) {
			Object a = arg0.get("pagerank");
			Object b = arg1.get("pagerank");
			if(a != null && b != null){
				if( (double) a == (double) b ){
					return 0;
				}
				else if( (double) a < (double) b ){
					return 1;
				}
				else{
					return -1;
				}
			}
			return 0;
		}
	}
}
