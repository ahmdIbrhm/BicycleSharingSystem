//package eu.qanswer.mapping.mappings.orcId;
//
//import eu.qanswer.mapping.configuration.AbstractConfigurationFile;
//import eu.qanswer.mapping.configuration.Mapping;
//import eu.qanswer.mapping.configuration.Type;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class OrgWork extends AbstractConfigurationFile {
//    public OrgWork()
//    {
//        format = "xml";
//        file = "orcid.xml";
//        baseUrl = "http://orcid.org/";
//        key = "profiles.record.activities-summary.employments.employment-summary.organization.name";
//        iterator="profiles.record.activities-summary.employments";
//        mappings = new ArrayList<>(Arrays.asList(
//                new Mapping("(.?)employment-summary(.*)organization.name","http://www.wikidata.org/prop/direct/P31","http://www.wikidata.org/entity/Q43229"),
//                new Mapping("(.?)employment-summary(.*)organization.name","http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),
//                new Mapping("(.?)employment-summary(.*)disambiguated-organization-identifier","http://www.wikidata.org/prop/direct/P2427", Type.LITERAL)
//                ));
//    }
//}
