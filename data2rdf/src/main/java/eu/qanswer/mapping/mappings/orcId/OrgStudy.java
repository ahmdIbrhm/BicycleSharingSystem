//package eu.qanswer.mapping.mappings.orcId;
//
//import eu.qanswer.mapping.configuration.AbstractConfigurationFile;
//import eu.qanswer.mapping.configuration.Mapping;
//import eu.qanswer.mapping.configuration.Type;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class OrgStudy extends AbstractConfigurationFile {
//    public OrgStudy()
//    {
//        format = "xml";
//        baseUrl = "http://orcid.org/";
//        file="orcid.xml";
//        key="profiles.record.activities-summary.educations.education-summary.organization.name";
//        iterator="profiles.record.activities-summary.educations";
//        mappings = new ArrayList<>(Arrays.asList(
//                new Mapping("(.?)education-summary(.*)organization.name","http://www.wikidata.org/prop/direct/P31","http://www.wikidata.org/entity/Q3918"),
//                new Mapping("(.?)education-summary(.*)disambiguated-organization-identifier","http://www.wikidata.org/prop/direct/P2427", Type.LITERAL),
////                new Mapping("(.?)education-summary(.*)organization.name","http://www.wikidata.org/prop/direct/P1813", Type.LITERAL),
//                new Mapping("(.?)education-summary(.*)organization.name","http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL)
//                ));
//    }
//}
