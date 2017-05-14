package wse_package;

import static com.mongodb.client.model.Filters.regex;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class QuerySearchEngine {
	/**
	 * Queries the search engines to find best articles that match the searchString.
	 * @param searchString
	 * @return the ordered list of articles (as documents) that suits best to the searchString.
	 */
	static ArrayList<Document> search(String searchString){
		//connecting to database, retrieving collection
		MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
		MongoClient mongoClient = new MongoClient(connectionString);
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = database.getCollection("articles");
		
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
		
		mongoClient.close();
		return ret;
	}
}