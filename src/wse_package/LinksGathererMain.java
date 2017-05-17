package wse_package;

import java.io.IOException;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class LinksGathererMain {
	public static void main(String[] args) throws IOException {
		//connecting to database, retrieving collection
		MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
		MongoClient mongoClient = new MongoClient(connectionString);
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = database.getCollection("articles");
		LinksGathererOrchestrer zelda = new LinksGathererOrchestrer(collection);
		zelda.start();
	}


}
