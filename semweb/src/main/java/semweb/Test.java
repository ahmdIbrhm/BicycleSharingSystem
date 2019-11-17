package semweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.XSD;

public class Test {
	private String fileName;
	private static final String OUTPUT = "out";
	private static final String DB_PEDIA_ENDPOINT = "http://dbpedia.org/sparql";
	private static final String LOCAL_ENDPOINT = "http://localhost:3030/test";
	private static final String WIKIDATA_ENDPOINT = "https://query.wikidata.org/bigdata/namespace/wdq/sparql";
	public Test(String fileName) {
		this.fileName = fileName;
	}
	
	
	public static void main(String[] args) {

		// create an empty Model
		 Test test = new Test("/home/alyhdr/Desktop/sncf_data/stops.txt");
		 //test.writeRDFModel(Test.OUTPUT);
		 
		 //we can put any endpoint as first parameter..
		 
		 String stations_north_of_saint_etienne = "";
		 
		test.execSelectAndPrint(Test.LOCAL_ENDPOINT,"SELECT * WHERE { ?s ?p ?o} limit 10");
		 
		//test.uploadRDF(new File("/home/alyhdr/Desktop/sncf_data/stops.ttl"), "http://localhost:3030/test");
		 //test.writeRDFModel("/home/alyhdr/Desktop/sncf_data/stops.ttl");
	}
	public void writeRDFModel(String outFile) {
		Model model = ModelFactory.createDefaultModel();

		// list the statements in the Model
		 StmtIterator iter = model.listStatements();
		 
		 ArrayList<String[]> list = readCSVFile();
		 String nsGeo = "http://www.w3.org/2003/01/geo/wgs84_pos#";
		 String nsEx = "http://www.example.com/";
		 model.setNsPrefix("geo", nsGeo);
		 model.setNsPrefix("ex", nsEx);
		 
		 for(String[] elts:list) {
			 for(int i=0;i<elts.length ;i++) {
				 String stop_id = elts[0];
				 String stop_name = elts[1];
				 
				 //remove the quotations from the string
				 
				 stop_name = stop_name.substring(1, stop_name.length()-1);
				 
				 //to make right URIs..
				 stop_id = stop_id.replace(" ", "_");
				 
				 double lat = 0;
				 double log = 0;
				 if(!(elts[3].equals("") || elts[4].equals(""))) {
					 lat = Double.parseDouble(elts[3]);
					 log = Double.parseDouble(elts[4]);
					 	 
				 }
				 
				 String uri = nsEx+stop_id;
				 Resource resource = model.createResource(uri);
				 resource.addProperty(RDFS.label, stop_name,"fr");
				 resource.addProperty(RDF.type, nsGeo+"SpatialThing");
				 
				 resource.addLiteral(model.createProperty(nsGeo+"lat"),lat);
				 resource.addLiteral(model.createProperty(nsGeo+"long"),log);
				 
				 
				 
			 }
		 }
		 if(outFile.equals(OUTPUT)) {
			 model.write(System.out,"TURTLE");
		 }else {
			 try {
				model.write(new FileOutputStream(new File(outFile)),"TURTLE");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		 }
	}
	public ArrayList<String[]> readCSVFile() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		try {
			Scanner input = new Scanner(new File(fileName));
			input.nextLine();
			while(input.hasNext()) {
				String line = input.nextLine();
				String elts[] = line.split(",");
				list.add(elts);
			}
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public void printModel(StmtIterator iter) {
		 // print out the predicate, subject and object of each statement
		 while (iter.hasNext()) {
		     Statement stmt      = iter.nextStatement();  // get next statement
		     Resource  subject   = stmt.getSubject();     // get the subject
		     Property  predicate = stmt.getPredicate();   // get the predicate
		     RDFNode   object    = stmt.getObject();      // get the object

		     System.out.print(subject.toString());
		     System.out.print(" " + predicate.toString() + " ");
		     if (object instanceof Resource) {
		        System.out.print(object.toString());
		     } else {
		         // object is a literal
		         System.out.print(" \"" + object.toString() + "\"");
		     }

		     System.out.println(" .");
		 }		
	}
	public  void uploadRDF(File rdf, String serviceURI) {

		Model m = ModelFactory.createDefaultModel();
		try (FileInputStream in = new FileInputStream(rdf)) {
			m.read(in, null, "TURTLE");
		}catch (Exception e){
			e.printStackTrace();
		}

		// upload the resulting model
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP(serviceURI);
		accessor.putModel(m);
	}

	public  void execSelectAndPrint(String serviceURI, String query) {
		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI,
				query);
		ResultSet results = q.execSelect();

		ResultSetFormatter.out(System.out, results);

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("x");
			System.out.println(x);
		}
	}
	
}
