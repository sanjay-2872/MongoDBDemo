package com.example.demo.mongolearning;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.datasource.TenantDataSource;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.List;

@RestController
public class Learning1 {

	public static final String query1 = "{\"$match\":{'awards.wins':{'$gt':1}}},{'$limit':1}";

	public static final String query2 = "{\"$match\":{'awards.wins':{'$gt':1}}},{'$limit':2}";

	@Autowired
	TenantDataSource dataSource;

	@GetMapping("/data")
	public List<Document> testing1(@RequestParam String dbName, @RequestParam String cloName,
			@RequestParam Integer queryID, @RequestParam String clientID) {

		MongoClient client = dataSource.getMongoClientConnection(clientID);

		MongoDatabase db = client.getDatabase(dbName);

		String aggPipe = null;
		if (Integer.valueOf(1).equals(queryID)) {
			aggPipe = query1;
		} else if (Integer.valueOf(2).equals(queryID)) {
			aggPipe = query2;
		}

		String strcCmd = "{ 'aggregate': '" + cloName + "', 'pipeline': [" + aggPipe + "], 'cursor': { } }";
		Document bsonCmd = Document.parse(strcCmd);
		System.out.println("bson command : " + bsonCmd);
		Document result = db.runCommand(bsonCmd);
		Document cursor = (Document) result.get("cursor");

		@SuppressWarnings("unchecked")
		List<Document> docs = (List<Document>) cursor.get("firstBatch");

		System.out.println();
		docs.forEach(e -> System.out.println("Data : " + e.toString()));
		System.out.println();

		return docs;

	}
}
