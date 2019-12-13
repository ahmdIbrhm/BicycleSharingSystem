//package eu.qanswer.mapping.mappings.informa;
//
//import eu.qanswer.mapping.configuration.AbstractConfigurationFile;
//import eu.qanswer.mapping.configuration.Mapping;
//import eu.qanswer.mapping.configuration.Type;
//import eu.qanswer.mapping.utility.Utility;
//import eu.qanswer.mapping.mappings.orcId.CustomMapping;
//import org.apache.jena.graph.Node;
//import org.apache.jena.graph.NodeFactory;
//import org.apache.jena.graph.Triple;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//
//public class Trial extends AbstractConfigurationFile {
//    //the url used for all instances
//    private Trial trial=this;
//    public Trial(){
//        format = "json";
////<<<<<<< HEAD
////        file = "/Users/Dennis/PycharmProjects/TrialTrove/crawl_trials";
////        baseUrl = "https://citeline.informa.com/trials/details/";
////        key = "trialId";
////        mappings = new ArrayList<Mapping>(Arrays.asList(
////                new Mapping("trialId", "http://www.w3.org/2000/01/rdf-schema#type", "http://www.wikidata.org/entity/Q30612"),
////                new Mapping("trialId", "http://purl.org/dc/terms/identifier", Type.LITERAL),
////                new Mapping("trialTitle", "http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),
////                new Mapping("trialStatus","http://www.newclean.eu/status", Type.URI_WITH_LABEL),
////                new Mapping("(.?).meshId","http://www.newclean.eu/meshId", "https://meshb.nlm.nih.gov/#/record/ui?ui=", Type.URI),
////                new Mapping("(.?).meshId","http://www.wikidata.org/prop/direct/P486", Type.LITERAL),
////                new Mapping("trialCountries(.?)","http://www.wikidata.org/prop/direct/P17", Type.URI),
////                new Mapping("trialRegions(.?)","http://www.newclean.eu/region", Type.CUSTOM),
////                new Mapping("(.?).trialDiseases\\[[0-9]\\].name","http://www.newclean.eu/disease", Type.LITERAL),
////                new Mapping("(.?).icd9Id","http://www.wikidata.org/prop/direct/P493", Type.URI),
////                new Mapping("trialObjective","http://www.w3.org/2000/01/rdf-schema#description", Type.LITERAL),
////                new Mapping("(.?).actualTrialStartDate","http://www.wikidata.org/prop/direct/P580", Type.DATE),
////                new Mapping("(.?).minAge($)","http://www.newclean.eu/age", Type.CUSTOM),
////                new Mapping("(.?).maxAge($)","http://www.newclean.eu/age", Type.CUSTOM),
////                new Mapping("(.?).gender","http://www.newclean.eu/gender","http://www.newclean.eu/gender/", Type.CUSTOM),
////                new Mapping("trialPhase","http://www.wikidata.org/prop/direct/P6099","http://www.newclean.eu/trialPhase/", Type.CUSTOM),
////                new Mapping("(.?).Sponsors\\[[0-9]\\].name","http://www.wikidata.org/prop/direct/P859","http://www.newclean.eu/sponsor/", Type.URI_WITH_LABEL),
////                new Mapping("(.?).Sponsors\\[[0-9]\\].type","http://www.newclean.eu/sponsorType","http://www.newclean.eu/sponsor/", Type.URI_WITH_LABEL),
////                new Mapping("trialExclusionCriteria","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
////                new Mapping("trialInclusionCriteria","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
////                new Mapping("trialStartDate","http://www.wikidata.org/prop/direct/P580", Type.LITERAL,XSDdateTime),
////                new Mapping("trialPrimaryCompletionDate","http://www.wikidata.org/prop/direct/P582", Type.LITERAL,XSDdateTime),
////                new Mapping("trialObjective","http://purl.org/dc/terms/description", Type.LITERAL,XSDstring),
////                new Mapping("trialPrimaryEndPoint","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
////                new Mapping("trialOtherEndPoint","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
////                new Mapping("trialStudyDesign","http://www.newclean.eu/trialStudyDesign", Type.LITERAL,XSDstring),
////                new Mapping("trialStudyKeywords(.?)","http://www.wikidata.org/prop/direct/P921", Type.LITERAL,XSDstring),
////                new Mapping("trialPatientPopulation","http://www.newclean.eu/patientPopulation", Type.LITERAL,XSDstring),
////                new Mapping("trialTreatmentPlan","http://www.newclean.eu/patientPopulation", Type.LITERAL,XSDstring),
////                new Mapping("trialTargetAccrual","http://www.newclean.eu/tragetNumberPatients", Type.CUSTOM),
////                new Mapping("trialActualAccrual","http://www.newclean.eu/currentNumberPatients", Type.CUSTOM),
////                new Mapping("trialInvestigators(.?)","http://www.wikidata.org/prop/direct/P1840", "https://citeline.eu.qanswer.mapping.informa.com/investigators/details/", Type.URI),
////                //new Mapping("(.*)trialSupportingUrls","http://www.wikidata.org/prop/direct/P1840", "" ,Type.URI),
////                new Mapping("trialSource","http://www.newclean.eu/source", Type.LITERAL)
////    ));
////=======
//        file = "C:\\Users\\My pc\\Desktop\\dumps\\trial.json";
//        baseUrl = "https://citeline.eu.qanswer.mapping.mappings.informa.com/trials/details/";
//        key = "trialId";
//        mappings = new ArrayList<>(Arrays.asList(
////                new Mapping("trialId", "http://www.w3.org/2000/01/rdf-schema#type", "http://www.wikidata.org/entity/Q30612"),
////                new Mapping("trialId", "http://purl.org/dc/terms/identifier", Type.LITERAL),
////                new Mapping("trialTitle", "http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),
////                new Mapping("trialStatus","http://www.newclean.eu/status", Type.URI_WITH_LABEL),
////                new Mapping("(.?).meshId","http://www.newclean.eu/meshId", "https://meshb.nlm.nih.gov/#/record/ui?ui=", Type.URI),
////                new Mapping("(.?).meshId","http://www.wikidata.org/prop/direct/P486", Type.LITERAL),
////                new Mapping("trialCountries(.?)","http://www.wikidata.org/prop/direct/P17", Type.URI),
////                new Mapping("trialRegions(.?)","http://www.newclean.eu/region",conditionsRegion,Type.CONDITIONAL),
////                new Mapping("(.?).trialDiseases\\[[0-9]\\].name","http://www.newclean.eu/disease", Type.LITERAL),
////                new Mapping("(.?).icd9Id","http://www.wikidata.org/prop/direct/P493", Type.URI),
////                new Mapping("trialObjective","http://www.w3.org/2000/01/rdf-schema#description", Type.LITERAL),
////                new Mapping("(.?).actualTrialStartDate","http://www.wikidata.org/prop/direct/P580", Type.DATE),
////                new Mapping("(.?).minAge($)","http://www.newclean.eu/age", Type.CUSTOM),
////                new Mapping("(.?).maxAge($)","http://www.newclean.eu/age", Type.CUSTOM),
//                  new Mapping("(.?).gender","http://www.newclean.eu/gender", new ClassGender(),Type.CUSTOM),
//                  new Mapping("trialPhase","http://www.wikidata.org/prop/direct/P6099", new ClassPhase(),Type.CUSTOM),
////                new Mapping("(.?).Sponsors\\[[0-9]\\].name","http://www.wikidata.org/prop/direct/P859","http://www.newclean.eu/sponsor/", Type.URI_WITH_LABEL),
////                new Mapping("(.?).Sponsors\\[[0-9]\\].type","http://www.newclean.eu/sponsorType","http://www.newclean.eu/sponsor/", Type.URI_WITH_LABEL),
////                new Mapping("trialExclusionCriteria","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
////                new Mapping("trialInclusionCriteria","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
////                new Mapping("trialStartDate","http://www.wikidata.org/prop/direct/P580", Type.LITERAL,XSDdateTime),
////                new Mapping("trialPrimaryCompletionDate","http://www.wikidata.org/prop/direct/P582", Type.LITERAL,XSDdateTime),
////                new Mapping("trialObjective","http://purl.org/dc/terms/description", Type.LITERAL,XSDstring),
////                new Mapping("trialPrimaryEndPoint","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
////                new Mapping("trialOtherEndPoint","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
////                new Mapping("trialStudyDesign","http://www.newclean.eu/trialStudyDesign", Type.LITERAL,XSDstring),
////                new Mapping("trialStudyKeywords(.?)","http://www.wikidata.org/prop/direct/P921", Type.LITERAL,XSDstring),
////                new Mapping("trialPatientPopulation","http://www.newclean.eu/patientPopulation", Type.LITERAL,XSDstring),
////                new Mapping("trialTreatmentPlan","http://www.newclean.eu/patientPopulation", Type.LITERAL,XSDstring),
////                new Mapping("trialTargetAccrual","http://www.newclean.eu/tragetNumberPatients", Type.LITERAL, XSDinteger),
////                new Mapping("trialActualAccrual","http://www.newclean.eu/currentNumberPatients", Type.LITERAL, XSDinteger),
////                new Mapping("trialInvestigators(.?)","http://www.wikidata.org/prop/direct/P1840", "https://citeline.eu.qanswer.mapping.informa.com/investigators/details/", Type.URI),
////                new Mapping("(.*)trialSupportingUrls","http://www.wikidata.org/prop/direct/P1840", "" ,Type.URI),
//                new Mapping("trialSource", "http://www.newclean.eu/source", Type.LITERAL)
//        ));
//    }
//    private class ClassGender extends CustomMapping
//    {
//        @Override
//        public ArrayList<Triple> function(HashMap<String,String> article, String tag) {
//            Utility utility = new Utility();
//            ArrayList<Triple> triples=new ArrayList<>();
//            Node predicate = utility.createURI(getMapping().getPropertyUri());
//            Node subject = NodeFactory.createURI(trial.baseUrl + article.get(trial.getKey()));
//            Node object;
//            if (getMapping().getTag().contains("gender"))
//            {
//                switch (article.get(tag)) {
//                    case "Both":
//                        object = NodeFactory.createURI("http://www.wikidata.org/entity/Q467");
//                        triples.add(new Triple(subject, predicate, object));
//
//                        object = NodeFactory.createURI("http://www.wikidata.org/entity/Q8441");
//                        triples.add(new Triple(subject, predicate, object));
//                        break;
//                    case "Male":
//                        object = NodeFactory.createURI("http://www.wikidata.org/entity/Q8441");
//                        triples.add(new Triple(subject, predicate, object));
//                        break;
//                    case "Female":
//                        object = NodeFactory.createURI("http://www.wikidata.org/entity/Q467");
//                        triples.add(new Triple(subject, predicate, object));
//                        break;
//                }
//            }
//            return triples;
//        }
//    }
//
//    private class ClassPhase extends CustomMapping
//    {
//        @Override
//        public ArrayList<Triple> function(HashMap<String, String> article, String key) {
//            ArrayList<Triple> triples=new ArrayList<>();
//            Utility utility = new Utility();
//            Node predicate = utility.createURI(getMapping().getPropertyUri());
//            Node subject = NodeFactory.createURI(trial.baseUrl + article.get(trial.getKey()));
//            Node object;
//            if (getMapping().getTag().contains("trialPhase")) {
//                if (article.get(key).equals("I") || article.get(key).contains("I/")) {
//                    object = NodeFactory.createURI("http://www.wikidata.org/entity/Q42824069");
//                    triples.add(new Triple(subject, predicate, object));
//                }
//                if (article.get(key).equals("II") || article.get(key).contains("II/") || article.get(key).contains("/II")) {
//                    object = NodeFactory.createURI("http://www.wikidata.org/entity/Q42824440");
//                    triples.add(new Triple(subject, predicate, object));
//                }
//                if (article.get(key).contains("III") || article.get(key).contains("III/") || article.get(key).contains("/III")) {
//                    object = NodeFactory.createURI("http://www.wikidata.org/entity/Q42824827");
//                    triples.add(new Triple(subject, predicate, object));
//                }
//                if (article.get(key).contains("IV") || article.get(key).contains("/IV")) {
//                    object = NodeFactory.createURI("http://www.wikidata.org/entity/Q42825046");
//                    triples.add(new Triple(subject, predicate, object));
//                }
//            }
//            return triples;
//        }
//    }
//}
//
