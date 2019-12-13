//package eu.qanswer.mapping.mappings.semanticscholar;
//
//import eu.qanswer.mapping.configuration.AbstractConfigurationFile;
//import eu.qanswer.mapping.configuration.Mapping;
//import eu.qanswer.mapping.configuration.Type;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class Author extends AbstractConfigurationFile {
//    public Author()
//    {
//        format = "json";
//        file="sample-S2-records";
//        baseUrl = "http://www.semanticscholar.org/author/";
//        key = "publications.authors.ids[1]";
//        iterator = "publications.authors";
//        mappings = new ArrayList<Mapping>(Arrays.asList(
//                new Mapping("authors(.*)name","http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),
//                new Mapping("authors(.*)name","http://www.wikidata.org/prop/direct/P2561", Type.LITERAL)));
//
//    }
//}
