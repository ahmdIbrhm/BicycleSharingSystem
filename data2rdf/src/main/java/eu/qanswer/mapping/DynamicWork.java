package eu.qanswer.mapping;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class DynamicWork {

    public static final String LOCAL_ENDPOINT = "http://localhost:3030/test";
    public static final String AHMAD_ENDPOINT = "http://192.168.43.132:3030/test";


    private String selectedService;
    public DynamicWork(String selectedService) {

        this.selectedService = selectedService;

    }


    public ArrayList<DynamicCity> getPramemeters() {
        ArrayList<DynamicCity> cities = new ArrayList<DynamicWork.DynamicCity>();
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
        System.out.println(cities.size());
        return cities;
    }

    public class DynamicCity{
        private String url;
        private String fileType;
        private String keyTag;
        private String iteratorTag;
        private String allMappings;
        private String name;

        public String getUrl() {
            return url;
        }

        public String getFileType() {
            return fileType;
        }

        public String getKeyTag() {
            return keyTag;
        }

        public String getIteratorTag() {
            return iteratorTag;
        }

        public String getAllMappings() {
            return allMappings;
        }

        public String getName() {
            return name;
        }

        public DynamicCity(String url, String fileType, String ketTag, String iteratorTag, String allMappings, String name) {
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

