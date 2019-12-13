//package eu.qanswer.mapping.mappings.informa;
//
//import eu.qanswer.mapping.utility.Parser;
//
//import org.apache.jena.graph.NodeFactory;
//import org.apache.jena.graph.Triple;
//import org.apache.jena.query.QueryExecution;
//import org.apache.jena.query.QueryExecutionFactory;
//import org.apache.jena.query.QuerySolution;
//import org.apache.jena.query.ResultSet;
//import org.apache.jena.rdf.model.Model;
//import org.apache.jena.rdf.model.ModelFactory;
//import org.apache.jena.riot.RDFFormat;
//import org.apache.jena.riot.lang.PipedRDFIterator;
//import org.apache.jena.riot.system.StreamRDF;
//import org.apache.jena.riot.system.StreamRDFWriter;
//import org.rdfhdt.hdt.hdt.HDTManager;
//import org.rdfhdt.hdtjena.HDTGraph;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class Substitute {
//
//    private static Logger logger = LoggerFactory.getLogger(Substitute.class);
//
//    static final String OUTPUT_PATH = "/Users/Dennis/IdeaProjects/semanticscholar/";
//
//    public static void main(String[] argv) throws IOException {
//
//        //use meshID's to make links to wikidata illnesses
//        StreamRDF writer = StreamRDFWriter.getWriterStream(new FileOutputStream(OUTPUT_PATH + "links_wikidata_diseases.n3"), RDFFormat.NTRIPLES);
//        String hdtFile = "/Users/Dennis/IdeaProjects/semanticscholar/mesh.hdt";
//
//        HDTGraph graph = new HDTGraph(HDTManager.mapIndexedHDT(hdtFile, null));
//        Model model = ModelFactory.createModelForGraph(graph);
//
//        Parser parser = new Parser();
//        PipedRDFIterator<Triple> iterator = parser.parse("/Users/Dennis/IdeaProjects/semanticscholar/newclin.ttl");
//        while (iterator.hasNext()) {
//            Triple t = iterator.next();
//            //System.out.println(t.getObject().toString());
//            if (t.getPredicate().toString().equals("http://www.wikidata.org/prop/direct/P486")) {
//                String query = "Select ?wikidata where {" +
//                        " <http://id.nlm.nih.gov/mesh/" + t.getObject().toString().replace("\"", "") + "> <http://newclin.eu/materialized> ?o ." +
//                        " ?wikidata <http://www.w3.org/2002/07/owl#sameAs> ?o ." +
//                        " } ";
//                //System.out.println(query);
//                QueryExecution qe = QueryExecutionFactory.create(query, model);
//                ResultSet result = qe.execSelect();
//                if (result.hasNext() == false) {
//                    System.out.println("No label found for " + t.getObject().toString());
//                } else {
//                    while (result.hasNext()) {
//                        Triple triple = new Triple(t.getSubject(), NodeFactory.createURI("http://www.newclean.eu/wikidataDisese"), NodeFactory.createURI(result.next().get("?wikidata").toString()));
//                        //System.out.println(triple.getSubject() + "--" + triple.getPredicate() + "--" + triple.getObject());
//                        writer.triple(triple);
//                    }
//                }
//            }
//        }
//        System.out.println("Finished");
//
//        writer.finish();
//        writer = StreamRDFWriter.getWriterStream(new FileOutputStream(OUTPUT_PATH + "links_wikidata_countries.n3"), RDFFormat.NTRIPLES);
//        hdtFile = "/Users/Dennis/Downloads/datasets/open/wikidata/hdt_index/index_big.hdt";
//        graph = new HDTGraph(HDTManager.mapIndexedHDT(hdtFile, null));
//        model = ModelFactory.createModelForGraph(graph);
//
//        System.out.println("Finished");
//
//        parser = new Parser();
//        iterator = parser.parse("/Users/Dennis/IdeaProjects/semanticscholar/newclin.ttl");
//        while (iterator.hasNext()) {
//            Triple t = iterator.next();
//            if (t.getPredicate().toString().equals("http://www.wikidata.org/prop/direct/P17")) {
//                String query = "Select ?s where {" +
//                        " OPTIONAL { ?s <http://www.wikidata.org/prop/direct/P31> <http://www.wikidata.org/entity/Q6256> ." +
//                        " ?s <http://www.w3.org/2000/01/rdf-schema#label> \"" + t.getObject().toString().replace("https://citeline.eu.qanswer.mapping.mappings.informa.com/trials/details/", "").replace("_", " ") + "\"@en  } ." +
//                        " OPTIONAL { ?s <http://www.wikidata.org/prop/direct/P31> <http://www.wikidata.org/entity/Q6256> ." +
//                        " ?s <http://www.w3.org/2004/02/skos/core#altLabel> \"" + t.getObject().toString().replace("https://citeline.eu.qanswer.mapping.mappings.informa.com/trials/details/", "").replace("_", " ") + "\"@en } ." +
//                        " } ";
//                System.out.println(query);
//                QueryExecution qe = QueryExecutionFactory.create(query, model);
//                ResultSet result = qe.execSelect();
//                if (result.hasNext()) {
//                    QuerySolution var2 = result.next();
//                    if (var2.get("?s") != null) {
//                        String var = var2.get("?s").toString();
//
//                        Triple triple = new Triple(t.getSubject(), NodeFactory.createURI("http://www.newclean.eu/wikidataCountry"), NodeFactory.createURI(var));
//                        //System.out.println(triple.getSubject()+"--"+triple.getPredicate()+"--"+triple.getObject());
//                        writer.triple(triple);
//                    } else {
//                        System.out.println("No label found for " + t.getObject().toString());
//                    }
//                } else {
//                    System.out.println("No label found for " + t.getObject().toString());
//                }
//            }
//
//        }
//        writer.finish();
//
//        //geoCoder();
//
//    }
//
//
////    public static void geoCoder() {
////        try {
////            StreamRDF writer = StreamRDFWriter.getWriterStream(new FileOutputStream(OUTPUT_PATH + "links_wikidata_locations.n3"), RDFFormat.NTRIPLES);
////            Parser rdfParser = new Parser();
////            PipedRDFIterator<Triple> iterator = rdfParser.parse("/Users/Dennis/IdeaProjects/semanticscholar/newclin.ttl");
////            while (iterator.hasNext()) {
////                Triple triple = iterator.next();
////                //System.out.println(t.getObject().toString());
////                if (triple.getPredicate().toString().equals("http://www.wikidata.org/prop/direct/P625") && !triple.getSubject().toString().startsWith("http://www.wikidata.org/entity/")) {
////                    System.out.println(triple.getObject());
////                    String[] object = triple.getObject().toString().replace("\"Point(", "").replace(")^^<http://www.opengis.net/ont/geosparql#wktLiteral>\"", "").split(" ");
////                    String lat = object[0];
////                    String lon = object[1];
////                    System.out.println(lat+"---"+lon);
////
////                    String nominatimService = "http://qanswer-svc4.univ-st-etienne.fr";
////                    URL url = new URL(nominatimService + "/reverse.php?format=json&lat="+lat+"&lon="+lon+"&namedetails=1");
////                    System.out.println(triple.getSubject());
////                    System.out.println(nominatimService + "/reverse.php?format=json&lat="+lat+"&lon="+lon+"&namedetails=1");
////                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
////                    if (con.getResponseCode() == 200) {
////                        InputStream in = new BufferedInputStream(con.getInputStream());
////                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
////                        StringBuilder sb = new StringBuilder();
////                        String line;
////                        while ((line = bufferedReader.readLine()) != null) {
////                            sb.append(line);
////                        }
////                        String requestResult = sb.toString();
////                        System.out.println(requestResult);
////                        in.close();
////                        bufferedReader.close();
////                        con.disconnect();
////                        //System.out.println(requestResult);
////                        JSONParser parser = new JSONParser();
////                        JSONObject o = (JSONObject) parser.parse(requestResult);
////                        if (o != null && o.containsKey("address")) {
////                            JSONObject address = (JSONObject) o.get("address");
////                            //For the types check https://github.com/openstreetmap/Nominatim/blob/master/sql/functions.sql#L828
////                            String[] types = {"continent", "sea", "country", "state", "region", "county", "city", "island", "town", "village", "hamlet", "municipality", "district", "unincorporated_area", "borough", "suburb", "croft", "subdivision", "isolated_dwelling", "road", "place"};
////                            for (String t : types) {
////                                if (address != null && address.containsKey(t)) {
////                                    JSONObject road = (JSONObject) address.get(t);
////                                    if (road != null && road.containsKey("osm_type") && road.get("osm_type") != null && road.containsKey("osm_id") && road.get("osm_id") != null) {
////                                        String type_materilaized = road.get("osm_type").toString();
////                                        String id_materialized = road.get("osm_id").toString();
////                                        if (type_materilaized.equals("W")) {
////                                            Triple newtriple = new Triple(triple.getSubject(), NodeFactory.createURI("http://www.newclean.eu/location"), NodeFactory.createURI("http://linkedgeodata.org/triplify/way" + id_materialized));
////                                            writer.triple(newtriple);
////                                        } else if (type_materilaized.equals("R")) {
////                                            Triple newtriple = new Triple(triple.getSubject(), NodeFactory.createURI("http://www.newclean.eu/location"), NodeFactory.createURI("http://linkedgeodata.org/triplify/relation" + id_materialized));
////                                            writer.triple(newtriple);
////                                        } else if (type_materilaized.equals("N")) {
////                                            Triple newtriple = new Triple(triple.getSubject(), NodeFactory.createURI("http://www.newclean.eu/location"), NodeFactory.createURI("http://linkedgeodata.org/triplify/node" + id_materialized));
////                                            writer.triple(newtriple);
////                                        } else {
////                                            logger.info("Not take into account " + type_materilaized);
////                                        }
////                                    }
////                                }
////                            }
////                        }
////                    }
////                }
////            }
////            writer.finish();
////
////
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        } catch (MalformedURLException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
////    }
//}
