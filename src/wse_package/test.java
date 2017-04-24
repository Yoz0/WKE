package wse_package;

import java.io.IOException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;

public class test {
	public static void main(String[] args) throws IOException {
		System.out.print("coucou");
		MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
		MongoClient mongoClient = new MongoClient(connectionString);
		
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = database.getCollection("test");

//		{
//			"name" : "MongoDB",
//			"type" : "database",
//			"count" : 1,
//			"versions": [ "v3.2", "v3.0", "v2.6" ],
//			"info" : { x : 203, y : 102 }
//		}
		Document doc = new Document("name", "MongoDB")
			.append("type", "database")
			.append("count", 1)
			.append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
			.append("info", new Document("x", 203).append("y", 102));
		collection.insertOne(doc);
	}
}
