package parsing;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import org.imt.atlantique.sss.upas.device.linky.controller.struct.DefaultData;
import outils.LogManager;
import java.io.InputStream;

/*
 * This class allow to parse and display the datas which passes through the serial port
 */
public class HistoricParser extends GenericParser {
	
	// ATTRIBUTES
	private BufferedReader buffRead;
	private InputStream input;
	public HashMap<String, DefaultData> dataHashMap;
	public DefaultData dfData;
	public HashMap<String,String[]> UnitsHashmap;
	
	// CONSTRUCTOR
	public HistoricParser(String buffer) {
		super();
		buffRead = new BufferedReader(new StringReader(buffer));
		this.dataHashMap = new HashMap<String, DefaultData>();
		
		//instantiate data HashMap
		//These four type of datas are the ones which we want to send through the network
		this.dataHashMap.put("ADCO",new DefaultData("","","",""));
		this.dataHashMap.put("ISOUSC",new DefaultData("","","",""));
		this.dataHashMap.put("BASE",new DefaultData("","","",""));
		this.dataHashMap.put("PAPP",new DefaultData("","","",""));
		
		//instantiate a units HashMap
		this.UnitsHashmap = new HashMap<String,String[]>();
		String[] ADCO = {"Adresse_Compteur","n°"};
		String[] ISOUSC = {"Intensite_souscrite","A"};
		String[] BASE = {"Consommation_totale","KWh"};
		String[] PAPP = {"Puissance_apparente","KVA"};
		this.UnitsHashmap.put("ADCO",ADCO);
		this.UnitsHashmap.put("ISOUSC",ISOUSC);
		this.UnitsHashmap.put("BASE",BASE);
		this.UnitsHashmap.put("PAPP",PAPP);
		
		
		
	}

	//METHODS
	/*
	 * This method allow to display the parsed datas 
	 */
	public void readDatas() {
		displayInfosEdf(this.dataHashMap);
	}
	
	/*
	 * This method allow to parse the datas and put them into a DefaultData scruture
	 */
	public void parse() {
		try {
			//fill the hashmap with datas from linky counter
			//instantiate boolean to test if the data serie is over or not
			boolean isOver = false;
			boolean isBegun = false;
			//instantiate line which contains the actual line in the buffer
			String line;
			//the while loop make a round of data
			while ((line = buffRead.readLine()) != null && !isOver) {

				String[] elements = line.split(" ");
				if (elements.length >= 2) {
					//if the datas are one of the four datas we want to send, we push them into the DefaultData
					if (elements[0].toString().equals("ADCO") || 
							elements[0].toString().equals("ISOUSC") || 
							elements[0].toString().equals("BASE") || 
							elements[0].toString().equals("PAPP")) {
					DefaultData data = new DefaultData(elements[0],(String)(this.UnitsHashmap.get(elements[0])[0]) , elements[1],(String)(this.UnitsHashmap.get(elements[0])[1]));
					this.dataHashMap.put(elements[0], data);
					}
				}
				//if we reach the last data of the round, the loop is over
				if (line.startsWith("MOTDETAT") ) {
					//dataHashMap.clear();
					isOver = true;
					
				}
			}
		} catch (Exception e) {
			System.out.println("Erreur au cours de la récupération des données du compteur EDF");
			LogManager.log("Erreur au cours de la récupération des données du compteur EDF");
			e.printStackTrace();
			
		}
	}

	/*
	 * This method allow to display the parsed datas on the console
	 */
	public void displayInfosEdf(HashMap<String, DefaultData> infosEdf)
    {
		System.out.println("--------------------------------------------------");
    	System.out.println("N° d’identification du compteur :"+infosEdf.get("ADCO"));
    	System.out.println("Option tarifaire :"+infosEdf.get("OPTARIF"));
    	System.out.println("Intensité souscrite :"+infosEdf.get("ISOUSC"));
    	System.out.println("Index option base  :"+infosEdf.get("BASE") + "Wh");
		//System.out.println("Index heures creuses  :"+infosEdf.get("HCHC") + "Wh");
		//System.out.println("Index heures pleines :"+infosEdf.get("HCHP")+ "Wh");
		System.out.println("Période tarifaire en cours :"+infosEdf.get("PTEC"));
		System.out.println("Intensité instantanée : :"+infosEdf.get("IINST"));
		//System.out.println("Intensité souscrite :"+infosEdf.get("HCHC") + "A");
		System.out.println("Intensité maximale :"+infosEdf.get("IMAX") + "A");
		System.out.println("Puissance apparente :"+infosEdf.get("PAPP") + "VA");
		System.out.println("Groupe horaire :"+infosEdf.get("HHPHC"));
		System.out.println("--------------------------------------------------");
    }
}
