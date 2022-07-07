package com.example.demo.mongolearning;





import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.BsonArrayCodec;
import org.bson.codecs.DecoderContext;
import org.bson.conversions.Bson;
import org.bson.json.JsonObject;
import org.bson.json.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.datasource.TenantDataSource;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class Learning1 {
	
	public static final String query1= "{\"$match\":{'awards.wins':{'$gt':1}}},{'$limit':1}";
	
	public static final String query2= "{\"$match\":{'imdb.rating':{'$gte':0}}}";
	static int i=0;
	
	@Autowired
	TenantDataSource dataSource;
	
	
	@GetMapping("/data")
	public List<Map<String,Object>> testing1(@RequestParam String dbName,@RequestParam String cloName,@RequestParam Integer queryID,@RequestParam String clientID) {
		
		MongoClient client = dataSource.getMongoClientConnection(clientID);
		
		
		StringBuilder output= new StringBuilder("[");
		
		log.info("Client : {}",client);
		
		MongoDatabase db = client.getDatabase(dbName);
		
		String aggPipe = null;
		if(Integer.valueOf(1).equals(queryID)) {
			aggPipe=query1;
		}else if(Integer.valueOf(2).equals(queryID)) {
			aggPipe=query2;
		}
		
		
		
		
		
		String strcCmd = "{ 'aggregate': '" + cloName + "', 'pipeline': [" + aggPipe + "], 'cursor': {batchSize:501 } }";
		Document bsonCmd = Document.parse(strcCmd);
		System.out.println("bson command : "+bsonCmd);
		Document result = db.runCommand(bsonCmd);
		log.info("Result : {}",result);
		Document cursor = (Document) result.get("cursor");
		
		log.info("Cursor : ["+cursor+"]");
		
		@SuppressWarnings("unchecked")
		List<Document> docs = (List<Document>) 
		cursor.get("firstBatch");
		
		log.info("fir Size {}",docs.size());
		
		
		
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> doc = (List<Map<String, Object>>) this.toObject(docs);
		//System.out.println();
//		docs.forEach(e -> {System.out.println("Data : " + e.toJson());output.append(e.toJson());});
//		output.append("]");
		log.info("Size : {}",docs.size());
		
		return doc;
		
		
	}
	
	
	public Object toObject(List<Document> docs) {
		return docs;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/data2")
	public List<Document> getdata() throws Exception {
		MongoTemplate mongoTemplate =  mongoTemplate();
		String jsonCommand = "{ aggregate: 'Movie', pipeline: [ "+query2+" ], cursor: { batchSize:5000} }";
		//int i=0;
		List<Document> data = new ArrayList<>();

		Document resultDoc = mongoTemplate.executeCommand(jsonCommand);
		
		log.info("Data2 : {}",resultDoc);
		data.addAll(((List<Document>)((Document) resultDoc.get("cursor")).get("firstBatch")));
		return data;
	}
	
	
	 public MongoClient mongo() {
	        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/learning");
	        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
	          .applyConnectionString(connectionString)
	          .build();
	        
	        return MongoClients.create(mongoClientSettings);
	    }
	 
	 public MongoTemplate mongoTemplate() throws Exception {
	        return new MongoTemplate(this.mongo(), "learning");
	    }
	 
	 
	 @GetMapping("/data3")
	 public void data2() {
		 String json = "["+query2+"]";

		    List<BsonDocument> pipeline = new BsonArrayCodec().decode(new JsonReader(json), DecoderContext.builder().build())
		            .stream().map(BsonValue::asDocument)
		            .collect(Collectors.toList());
		 
		 //BasicDBObject query = BasicDBObject.parse(json);
		 
//
		    MongoClient client = dataSource.getMongoClientConnection("MONGO");
		    
		   MongoCollection<JsonObject> coll=  client.getDatabase("learning").getCollection("Movie").withDocumentClass(JsonObject.class);
		    //.aggregate().into(new ArrayList<>());
		   
		   log.info(coll.toString());
		   
		   

		    
		    @SuppressWarnings("unchecked")
			AggregateIterable<JsonObject> dumps = coll.aggregate(pipeline);
		    log.info("Data:: {}",dumps.toString());
		    dumps.forEach(e -> {log.info(e.toString());i++;});
		    log.info("size:{}",i);
		 
	 }


	 
	 
	
}
