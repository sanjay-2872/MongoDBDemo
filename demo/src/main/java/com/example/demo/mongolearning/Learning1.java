package com.example.demo.mongolearning;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.datasource.TenantDataSource;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class Learning1 {

	public static final String query1 = "{\"$match\":{'awards.wins':{'$gt':1}}},{'$limit':1}";

	public static final String query2 = "{\"$match\":{'awards.wins':{'$gt':1}}},{'$limit':2}";

	@Autowired
	TenantDataSource dataSource;

	@Autowired
	ObjectMapper mapper;

	@PostMapping("/data")
	public List<Map<String,Object>> testing1(@RequestBody Map<String, String> data) {

		MongoClient client = dataSource.getMongoClientConnection(data.get("clientID"));

		MongoDatabase db = client.getDatabase(data.get("dbName"));

		String aggPipe = data.get("query");

		log.info("Aggregator PipeLine Query: {}", aggPipe);

		//String input = "{'$match':{'expiryCreated' : {'$gte' : new ISODate({{DATE{-1}-{TIME{23:59:59Z}}}}),'$lte' : new ISODate('2023-06-16T23:59:59Z')}}},{ '$sort' : { 'expiryCreated' : 1}}";


		String datePattern = "\\{\\{DATE\\{([-+]?\\d+)\\}-(.*?)\\}\\}";
		String timePattern = "\\{TIME\\{(.*?)\\}\\}";

		// Compile regular expressions
		Pattern dateRegex = Pattern.compile(datePattern);
		Pattern timeRegex = Pattern.compile(timePattern);

		// Match date and time patterns
		Matcher dateMatcher = dateRegex.matcher(aggPipe);
		Matcher timeMatcher = timeRegex.matcher(aggPipe);

		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = null;
		LocalDate endDate = null;
		// Extract Date values
		String startDateFormat = null;
		String endDateFormat = null;
		if (dateMatcher.find()) {
			startDateFormat = dateMatcher.group(1);
			System.out.println("startDateFormat :: "+startDateFormat);
			startDate = getFormattedLocalDate(startDateFormat);
			if (dateMatcher.find()) {
				endDateFormat = dateMatcher.group(1);
				System.out.println("endDateFormat :: "+endDateFormat);
				endDate = getFormattedLocalDate(endDateFormat);
			}
		}

		// Extract time values
		String startTime = null;
		String endTime = null;
		while (timeMatcher.find()) {
			if (startTime == null) {
				startTime = timeMatcher.group(1);
			} else {
				endTime = timeMatcher.group(1);
			}
		}
		String fromReplaceOrgData = "{{DATE{"+startDateFormat+"}-{TIME{"+startTime+"}}}}";
		String fromReplaceFormatDate = "'"+startDate+"T"+startTime+"'";
		String toReplaceOrgDate = "{{DATE{"+endDateFormat+"}-{TIME{"+endTime+"}}}}";
		String toReplaceFormatDate = "'"+endDate+"T"+endTime+"'";
		String updatedAggPipe =  aggPipe.replace(fromReplaceOrgData,fromReplaceFormatDate).replace(toReplaceOrgDate,toReplaceFormatDate);
		log.info("Date - Format :: {} update :: {}", aggPipe,updatedAggPipe);

		String strcCmd = "{ 'aggregate': '" + data.get("cloName") + "', 'pipeline': [" + updatedAggPipe + "], 'cursor': {'batchSize': 2500000} }";
		log.info("CMD : {}",strcCmd);
		Document bsonCmd = Document.parse(strcCmd);
		System.out.println("bson command : " + bsonCmd);
		Document result = db.runCommand(bsonCmd);

		Document cursor = (Document) result.get("cursor");

		//log.info("cursor :: {}",cursor);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> docs = (List<Map<String,Object>>) cursor.get("firstBatch");

		System.out.println("Data Size :: "+ docs.size());
		//docs.forEach(e -> System.out.println("Data : " + e.toString()));
		System.out.println();
		return docs;

	}

	private static LocalDate getFormattedLocalDate(String startDateFormat) {
		LocalDate currentDate = LocalDate.now();
		if(Objects.nonNull(startDateFormat)){
			log.info("Date :: {}",startDateFormat.charAt(0));
			if(startDateFormat.charAt(0)=='-'){
				log.info("currentDate.minusDays(Integer.parseInt(startDateFormat)); :: {}",currentDate.minusDays(-Integer.parseInt(startDateFormat)));
				return currentDate.minusDays(-Integer.parseInt(startDateFormat));
			}else {
				log.info("currentDate.plusDays(Integer.parseInt(startDateFormat)); :: {}",currentDate.plusDays(Integer.parseInt(startDateFormat)));
				return currentDate.plusDays(Integer.parseInt(startDateFormat));
			}
		}
		return currentDate;
	}
}
//{
//		"dbName": "FINCARE",
//		"cloName": "iso_message",
//		"query": "{'$match':{'expiryCreated' : {'$gte' : new ISODate('2023-06-16T00:00:00.000Z'),'$lte' : new ISODate('2023-06-17T23:59:59.999Z')}}},{ '$sort' : { 'expiryCreated' : 1}}",
//		"clientID": "MONGO_LOCAL"
//		}