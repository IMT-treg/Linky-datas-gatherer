package flo.teleinfo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.ws4d.java.service.parameter.ParameterAttribute;
import org.ws4d.java.service.parameter.ParameterValue;
import org.ws4d.java.structures.Iterator;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This class allow to connect to a mongo database and add document in it
 */
public class MongoManager {

	// ATTRIBUTS
	public MongoClient myMongoClient;
	public MongoDatabase myDB;
	public MongoCollection<Document> myCollection;
	
	//METHODS
	/*
	 * Method allowing to connect to an atlas database
	 * If the database or the collection doesn't exist, its creates it
	 */
	public void connection() {
		Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
		String connectionString = "mongodb+srv://pi:rasp@cluster0-kcrqr.mongodb.net/test?retryWrites=true&w=majority";
		this.myMongoClient = MongoClients.create(connectionString);
		this.myDB = this.myMongoClient.getDatabase("teleinfoDB");
		this.myCollection = this.myDB.getCollection("energy_info");

	}
	/*
	 * Method which insert a document into the database. Calling the insertRow function
	 */
	public void docToDatabase(ParameterValue parameterValue) {
		int i = 0;
		Document doc = new Document("_id", new ObjectId());
		HashMap<String,String> values = new HashMap<String, String>();
		insertRow(parameterValue, doc, i,values);
		for (Map.Entry me : values.entrySet()) {
	          if (me.getKey().toString().contains("value") || me.getKey().toString().contains("timesStamp")) {
	        	  doc.append(me.getKey().toString(), me.getValue().toString());
	          }
	        }
	        
		this.myCollection.insertOne(doc);
	}
	/*
	 * Method which insert a row in a document. The running is based on the IoTClient Class from the demoloIoTConnector Project
	 */
	public void insertRow(ParameterValue parameterValue, Document doc, int i,HashMap<String,String> hashMap) {
		for (Iterator attributeIterator = parameterValue.attributes(); attributeIterator.hasNext();) {
			ParameterAttribute attribut = (ParameterAttribute) attributeIterator.next();
			hashMap.put(attribut.getName().getLocalPart() + Integer.toString(i), attribut.getValue());
		}
		if (parameterValue.hasChildren()) {
			for (Iterator iterator = parameterValue.children(); iterator.hasNext();) {
				insertRow((ParameterValue) iterator.next(), doc, i,hashMap);
				i++;
			}
		}
		
		
			
		}
}
