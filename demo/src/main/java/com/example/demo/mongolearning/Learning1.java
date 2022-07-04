package com.example.demo.mongolearning;




import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DemoApplication;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import java.util.List;

@RestController 
public class Learning1 {
	
	public static final String query1= "{\"$match\":{'awards.wins':{'$gt':1}}},{'$limit':1}";
	
	public static final String query2= "{\"$match\":{'awards.wins':{'$gt':1}}},{'$limit':2}";
	
	public static void main(String[] args) {
		
		String uri = "mongodb://localhost:27017";
	    try (MongoClient mongoClient = MongoClients.create(uri)) {
	        MongoDatabase database = mongoClient.getDatabase("learning");
	        MongoCollection<Document> collection = database.getCollection("Movie");
	        Document doc = collection.find(new Document("title","The Birth of a Nation")).first();
	        System.out.println(doc);
	        System.out.println();
	        System.out.println(doc.toJson());
	    }
	}
	
	public  MongoClient getDataSource() {
		MongoClient mongoClient = MongoClients.create();
		return mongoClient;
	}
	
	
	@GetMapping("/data")
	public List<Document> testing1(@RequestParam String dbName,@RequestParam String cloName,@RequestParam Integer queryID) {
		MongoDatabase db = DemoApplication.mgClient.getDatabase(dbName);
		String aggPipe = null;
		if(Integer.valueOf(1).equals(queryID)) {
			aggPipe=query1;
		}else if(Integer.valueOf(2).equals(queryID)) {
			aggPipe=query2;
		}
		
		
		String strcCmd = "{ 'aggregate': '" + cloName + "', 'pipeline': [" + aggPipe + "], 'cursor': { } }";
		Document bsonCmd = Document.parse(strcCmd);
		System.out.println("bson command : "+bsonCmd);
		Document result = db.runCommand(bsonCmd);
		Document cursor = (Document) result.get("cursor");
		
		
		@SuppressWarnings("unchecked")
		List<Document> docs = (List<Document>) 
		cursor.get("firstBatch");
		
		
		System.out.println();
		docs.forEach(e -> System.out.println("Data : " + e.toString()));
		System.out.println();
		
		return docs;
		
		
	}
}
