package semweb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sys.JenaSystem;

public class DynamicParametersProvider {

	private static final String LOCAL_ENDPOINT = "http://localhost:3030/test";
	private static final String AHMAD_ENDPOINT = "http://192.168.43.132:3030/test";
	
	
	private String selectedService;
	public DynamicParametersProvider(String selectedService) {
		
		this.selectedService = selectedService;
		
	}
	
	public static void main(String[] args) {
		
		if(args.length == 2) {
			DynamicParametersProvider dpp = new DynamicParametersProvider(args[0]);
			//dpp.execSelectAndPrint(dpp.selectedService,"SELECT * WHERE { ?s ?p ?o} limit 10");
			ArrayList<DynamicCity> cities = dpp.getPramemeters();
			

			System.err.println("started !");
			try {
				for(DynamicCity city:cities) {
					
					String fileName = city.name+"DynamicData";
					URL website = new URL(city.url);
					ReadableByteChannel rbc = Channels.newChannel(website.openStream());
					FileOutputStream fos = new FileOutputStream(fileName);
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					
					
					String command = "java -jar "+args[1]+" -c \""+city.name+"\" -t \""+city.fileType+"\" -k \""+city.keyTag+"\" -i \""+city.iteratorTag+"\" -a \""+city.allMappings+"\" -f \""+fileName+"\"";
					
					ProcessBuilder builder = new ProcessBuilder(command);		    
					Process process = builder.start();
					
					//Process ps =Runtime.getRuntime().exec(command);
					
					/*InputStream is=ps.getInputStream();
			        byte b[]=new byte[is.available()];
			        is.read(b,0,b.length);
			        System.out.println(new String(b));*/
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.err.println("finished !");
		} else {
			System.out.println("Specify endpoint url !");
		}
		
	}
	public ArrayList<DynamicCity> getPramemeters() {
		
		ArrayList<DynamicCity> cities = new ArrayList<DynamicParametersProvider.DynamicCity>();
		String query = "SELECT ?url ?fileType ?keyTag ?iteratorTag ?allMappings ?name WHERE {"
				+ "?city <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.wikidata.org/entity/Q515> ."
				+ "?city <http://www.w3.org/2000/01/rdf-schema#label> ?name ."
				+ "?city <http://schema.org/Property> ?config ."
				+ "?config <http://www.wikidata.org/prop/direct/P2699> ?url ."
				+ "?config <http://www.example.org/keyTag> ?keyTag ."
				+ "?config <http://www.example.org/fileType> ?fileType ."
				+ "?config <http://www.example.org/iteratorTag> ?iteratorTag ."
				+ "?config <http://www.example.org/allMappings> ?allMappings"
				+ "} limit 10";
		System.out.println(query);
		
		Query query_1 = QueryFactory.create(query);
		
		QueryEngineHTTP q = new QueryEngineHTTP(this.selectedService, query_1);
		ResultSet results = q.execSelect();
		while(results.hasNext()) {
			QuerySolution qs = results.next();
			RDFNode url = qs.get("url");
			RDFNode fileType = qs.get("fileType");
			RDFNode keyTag = qs.get("keyTag");
			RDFNode iteratorTag = qs.get("iteratorTag");
			RDFNode allMappings = qs.get("allMappings");
			RDFNode name = qs.get("name");
			
			
			cities.add(new DynamicCity(url.toString(), fileType.toString(), keyTag.toString(),
					iteratorTag.toString(), allMappings.toString(),name.toString()));
			
		}
		return cities;
	}
	
	private class DynamicCity{
		private String url;
		private String fileType;
		private String keyTag;
		private String iteratorTag;
		private String allMappings;
		private String name;
		public DynamicCity(String url,String fileType,String ketTag,String iteratorTag,String allMappings,String name) {
			this.url = url;
			this.fileType = fileType;
			this.keyTag = ketTag;
			this.iteratorTag = iteratorTag;
			this.allMappings = allMappings; 
			this.name = name;
		}
		@Override
		public String toString() {
			return this.url+" "+this.fileType+" "+this.keyTag+" "+this.iteratorTag+" "+this.allMappings;
		}
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
