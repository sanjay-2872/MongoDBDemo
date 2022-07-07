package com.example.demo.newdrivertesting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.example.demo.mongolearning.Learning1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class testing1 {
	public static final String query2= "{\"$match\":{'imdb.rating':{'$gte':0}}},{'$limit':1}";
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		Learning1 l1 = new Learning1();
		MongoTemplate mongoTemplate =  l1.mongoTemplate();
		String jsonCommand = "{ aggregate: 'Movie', pipeline: [ "+query2+" ], cursor: { batchSize:5000} }";
		//int i=0;
		List<Document> data = new ArrayList<>();

		BiConsumer<? super String, ? super Object> action
        = new MyBiConsumer();
		
		//Document resultDoc = 
				mongoTemplate.executeCommand(jsonCommand).forEach(action);;
		
//		log.info("Data2 : {}",resultDoc);
//		data.addAll(((List<Document>)((Document) resultDoc.get("cursor")).get("firstBatch")));
//		log.info("Data :: {}",data);;
	}


}




class MyBiConsumer implements BiConsumer<String, Object> {
	  

	@Override
	public void accept(String t, Object v) {
		// TODO Auto-generated method stub
        System.out.print("Key = " + t);
        System.out.print("\t Value = " + v);
        System.out.println();
	}
}
