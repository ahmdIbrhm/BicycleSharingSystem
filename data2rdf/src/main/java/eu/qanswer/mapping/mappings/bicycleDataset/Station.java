//package eu.qanswer.mapping.mappings.bicycleDataset;
//
//import eu.qanswer.mapping.Main;
//import eu.qanswer.mapping.configuration.AbstractConfigurationFile;
//import eu.qanswer.mapping.configuration.Mapping;
//import eu.qanswer.mapping.configuration.Type;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class Station extends AbstractConfigurationFile {
//    public Station()
//    {
//        format = "json";
//        file="/home/ahmad/data2rdf/saintEtienne.json";
//        baseUrl = "http://example.org/";
//        key = "data.stations.station_id";
//        iterator = "data.stations";
//        mappings = new ArrayList<>(Arrays.asList(
//                new Mapping("(.?).lat","http://www.w3.org/2003/01/geo/wgs84_pos#lat",Type.LITERAL),
//                new Mapping("(.?).lon","http://www.w3.org/2003/01/geo/wgs84_pos#long",Type.LITERAL),
//                new Mapping("(.?).name","http://www.w3.org/2000/01/rdf-schema#label",Type.LITERAL),
//                new Mapping("(.?).capacity","http://www.wikidata.org/prop/direct/P1083", Type.LITERAL),
//                new Mapping("(.?).station_id","http://www.w3.org/1999/02/22-rdf-syntax-ns#type","http://www.w3.org/2003/01/geo/wgs84_pos#SpatialThing")
//                ));
//    }
//}
