package com.example.demo.mongotesting;

import java.util.List;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoPractice {

	
	public static void main(String[] args) {
		// Replace the uri string with your MongoDB deployment's connection string
		String[] completeUri = "mongodb://localhost:27017//learning//learning2".split("//");
		String uri = completeUri[0] + "//" + completeUri[1];
		String databaseName = completeUri[2];
		String collectionName = completeUri[3];

		try (MongoClient mongoClient = MongoClients.create(uri)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			try {

				// Aggregation pipeline stages
				String match = "{ '$match':{ 'author': 'Leo Tolstoy' } }";
				String sort = "{ '$sort':{ 'title': 1} },{'$project':{'_id':0}}";
				
				String Data = "{\"$group\":{\"_id\":{\"exp\":'$exp','gender':'$gender'},count:{'$count':{}},'sum':{\"$sum\":\"$exp\"}}},{'$project':{\"Exp\":\"$_id.exp\",\"Gender\":\"$_id.gender\",\"_id\":0,\"count\":\"$count\",\"SumOfExp\":\"$sum\"}},{'$sort':{'Exp':-1}}";

				// Build pipeline as a Bson
				String pipe = match + ", " + sort;
				String strcCmd = "{ 'aggregate': '" + collectionName + "', 'pipeline': [" + Data + "], 'cursor': { } }";
				Document bsonCmd = Document.parse(strcCmd);

				// Execute the native query
				Document result = database.runCommand(bsonCmd);
				System.out.println("\nResult :"+result+"\n");
				// Get the output
				Document cursor = (Document) result.get("cursor");
				
System.out.println("\nCursor :"+cursor+"\n");
				
				@SuppressWarnings("unchecked")
				List<Document> docs = (List<Document>) cursor.get("firstBatch");
				System.out.println();
				docs.forEach(e -> System.out.println("Data : " + e.toString()));

			} catch (MongoException me) {
				System.err.println("An error occurred: " + me);
			}
		}
	}

}
