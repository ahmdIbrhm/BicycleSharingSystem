//package eu.qanswer.mapping.mappings.semanticscholar;
//
//import eu.qanswer.mapping.configuration.AbstractConfigurationFile;
//import eu.qanswer.mapping.configuration.Mapping;
//import eu.qanswer.mapping.configuration.Type;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class Journal extends AbstractConfigurationFile {
//    public Journal() {
//        format = "json";
//        file = "sample-S2-records";
//        baseUrl = "http://www.semanticscholar.org/journal/";
//        key = "publications.journalName";
//        iterator = "publications";
//        mappings = new ArrayList<Mapping>(Arrays.asList(
//                new Mapping("journalName", "http://www.wikidata.org/prop/direct/P31", "http://www.wikidata.org/entity/Q737498"),
//
//                new Mapping("journalName", "http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL)));
//
//    }
//}
