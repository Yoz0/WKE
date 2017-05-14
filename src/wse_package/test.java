package wse_package;

import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.regex;

import java.io.IOException;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class test {
	public static void main(String[] args) throws IOException {
		MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
		MongoClient mongoClient = new MongoClient(connectionString);
		
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = database.getCollection("articles");

		//delete all documents
		collection.deleteMany(exists("_id"));
		
		//insert sample documents
		Document doc = new Document("name", "Coucou_oriental")
			.append("list_links", Arrays.asList("Animal", "Famille_(biologie)", "Cuculidae"))
			.append("pagerank", 1.00);
		collection.insertOne(doc);
		Document doc2 = new Document("name", "Animal")
				.append("list_links", Arrays.asList("Protozoaire"))
				.append("pagerank", 1.00);
		collection.insertOne(doc2);
		Document doc3 = new Document("name", "Cuculidae")
				.append("list_links", Arrays.asList("Neomorphinae"))
				.append("pagerank", 1.00);
		collection.insertOne(doc3);
		
		//find some documents
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
		    while (cursor.hasNext()) {
		        System.out.println(cursor.next().toJson());
		    }
		} finally {
		    cursor.close();
		}
		System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
		MongoCursor<Document> cursor2 = collection.find(regex("name", "coucou", "i")).iterator();
		try {
		    while (cursor2.hasNext()) {
		        System.out.println(cursor2.next().toJson());
		    }
		} finally {
		    cursor2.close();
		}
		
		mongoClient.close();
	}
}
