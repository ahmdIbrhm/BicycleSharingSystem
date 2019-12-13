package eu.qanswer.mapping;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.opencsv.CSVReader;

import eu.qanswer.mapping.configuration.AbstractConfigurationFile;
import eu.qanswer.mapping.configuration.Mapping;
import eu.qanswer.mapping.configuration.Type;
import eu.qanswer.mapping.utility.Utility;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.util.Pair;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;

import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFWriter;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class Main {
    String output="";
    HashMap<String,String> mapOfIds=new HashMap<>();
    HashMap<String, Integer> mapAvailabileBicycles=new HashMap<>();
    HashMap<String, Integer> mapAvailableDocks=new HashMap<>();
    HashMap<String, String> mapLastUpdate=new HashMap<>();

    ArrayList<Mapping> arraylistOfMappings;

    @Parameter(names={"--typeOfFile", "-t"})
    private String typeOfFile;

    @Parameter(names = {"--key", "-k"})
    private String key;

    @Parameter(names = {"--iterator", "-i"})
    private String iterator;

    @Parameter(names = {"--arrayMappings", "-a"})
    private String arrayMappings;

    @Parameter(names = {"--inputFile", "-f"})
    private String inputFile;

    @Parameter(names = {"--cityName", "-c"})
    private String cityName;

    @Parameter(names = {"--url", "-u"})
    private String dynamicURL;

    @Parameter(names = {"--dynamic", "-d"})
    private String isWorkingDynamic;

    @Parameter(names = "--help", help = true)
    private boolean help = false;

    String outputFilePath="output.ttl";
    static String baseUrl;
    static String staticCity;
    public void run() throws Exception {
        staticCity=cityName;
        baseUrl="http://www.example.org/";
        if (help)
        {
            System.out.println("Help Yourself");
            return;
        }
        if(isWorkingDynamic!=null && isWorkingDynamic.equals("true"))
        {
            DynamicWork dynamicWork=new DynamicWork(DynamicWork.LOCAL_ENDPOINT);
            ArrayList<DynamicWork.DynamicCity> arrayListCities=dynamicWork.getPramemeters();
            for (DynamicWork.DynamicCity city: arrayListCities)
            {
                String fileName = "dynamic";
                URL website = new URL(city.getUrl());
                try
                {
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream(fileName);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                typeOfFile=city.getFileType();
                key=city.getKeyTag();
                iterator=city.getIteratorTag();
                arrayMappings=city.getAllMappings();
                dynamicURL=city.getUrl();
                cityName=city.getName();
                isWorkingDynamic="";
                inputFile=fileName;
                run();
            }
        }
        else if(typeOfFile==null || key==null || iterator==null ||arrayMappings==null)
        {
            System.out.println("Some variables are null!!!");
        }

        else if (typeOfFile.equals("") || key.equals("")|| arrayMappings.equals("")) {
            System.out.println("Specify the arguments correctly, some arguments are null");
            return;
        }
        else {
            try {
//                System.out.println(dynamicURL);
                File outputFile = new File(outputFilePath);
                outputFile.setExecutable(true);
                outputFile.setReadable(true);
                outputFile.setWritable(true);

                    StreamRDF writer = StreamRDFWriter.getWriterStream(new FileOutputStream(outputFile), RDFFormat.NTRIPLES);
                    arraylistOfMappings = new ArrayList<>();
                    String[] array = arrayMappings.split("&");
                    for (int i = 0; i < array.length; i++) {
                        String[] objectArray = array[i].split("~");
                        String tag = objectArray[0];
                        String property = objectArray[1];
                        String type = objectArray[2];
                        String objectUri = "";
                        if (objectArray.length == 4)
                            objectUri = objectArray[3];

                        if (objectUri.equals("") && !type.equals("")) {
                            if (type.equals("literal"))
                                arraylistOfMappings.add(new Mapping(tag, property, Type.LITERAL));
                            else if (type.equals("uri"))
                                arraylistOfMappings.add(new Mapping(tag, property, Type.URI));
                            else if (type.equals("uri_with_label"))
                                arraylistOfMappings.add(new Mapping(tag, property, Type.URI_WITH_LABEL));
                            else if (type.equals("integer"))
                                arraylistOfMappings.add(new Mapping(tag, property, Type.INTEGER));
                            else if (type.equals("decimal"))
                                arraylistOfMappings.add(new Mapping(tag, property, Type.DECIMAL));
                            else if (type.equals("date"))
                                arraylistOfMappings.add(new Mapping(tag, property, Type.DATE));
                            else if (type.equals("boolean"))
                            {
                                arraylistOfMappings.add(new Mapping(tag, property, Type.BOOLEAN));
                            }

                        } else if (type.equals("") && !objectUri.equals(""))
                            arraylistOfMappings.add(new Mapping(tag, property, objectUri));
                        else {
                            arraylistOfMappings.add(new Mapping(tag, property, objectUri));
                        }
                    }
                    AbstractConfigurationFile mappings = new AbstractConfigurationFile(typeOfFile, key, iterator, arraylistOfMappings);
                    if (typeOfFile.equals("json")) {
                        parseJson(mappings, writer);
                    } else if (typeOfFile.equals("xml")) {
                        parseXML(mappings, writer);
                    } else if (typeOfFile.equals("csv")) {
                        parseCSV(mappings, writer);
                    } else {
                        System.out.println("Error");
                    }
                    addSpecialTriplesGeneral(writer);
                    writer.finish();
                    uploadRDF(outputFile);
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
    }

    private void addSpecialTriplesGeneral(StreamRDF writer)
    {
        ArrayList<Triple> triples=new ArrayList<>();
        Node subjectCityName=Utility.createURI(baseUrl+cityName);
        Node predicateCityName=Utility.createURI("http://www.w3.org/2000/01/rdf-schema#label");
        Node objectCityName=Utility.createLiteral(cityName);
        Triple tripleCityName=new Triple(subjectCityName,predicateCityName,objectCityName);
        triples.add(tripleCityName);

        Node subjectCityType=Utility.createURI(baseUrl+cityName);
        Node predicateCityType=Utility.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        Node objectCityType=Utility.createURI("http://www.wikidata.org/entity/Q515");
        Triple tripleCityType=new Triple(subjectCityType,predicateCityType,objectCityType);
        triples.add(tripleCityType);

        Node subjectCityConfiguration=Utility.createURI(baseUrl+cityName);
        Node predicateCityConfiguration=Utility.createURI("http://schema.org/Property");
        Node objectCityConfiguration=Utility.createURI(baseUrl+cityName+"/dynamicConfiguration");
        Triple tripleCityConfiguration=new Triple(subjectCityConfiguration,predicateCityConfiguration,objectCityConfiguration);
        triples.add(tripleCityConfiguration);

        if(dynamicURL!=null) {
            output+="URL";
            ArrayList<Pair<String, String>> arrayListOfPredicatesObjects = new ArrayList<>();
            arrayListOfPredicatesObjects.add(new Pair<>("<http://www.wikidata.org/prop/direct/P2699>", dynamicURL));
            arrayListOfPredicatesObjects.add(new Pair<>(baseUrl + "keyTag", key));
            arrayListOfPredicatesObjects.add(new Pair<>(baseUrl + "fileType", typeOfFile));
            arrayListOfPredicatesObjects.add(new Pair<>(baseUrl + "iteratorTag", iterator));
            arrayListOfPredicatesObjects.add(new Pair<>(baseUrl + "allMappings", arrayMappings));

            for (Pair<String, String> pair : arrayListOfPredicatesObjects) {
                Node subjectConfigurationProperties = Utility.createURI(baseUrl + cityName + "/dynamicConfiguration");
                Node predicateConfigurationProperties = Utility.createURI(pair.getKey());
                Node objectConfigurationProperties;
                objectConfigurationProperties = Utility.createLiteral(pair.getValue());
                Triple tripleConfigurationProperties = new Triple(subjectConfigurationProperties, predicateConfigurationProperties, objectConfigurationProperties);
                triples.add(tripleConfigurationProperties);
            }

            for (String cityId : mapOfIds.keySet()) {
                long unixTimestamp = System.currentTimeMillis() / 1000L;
                Node subjectStateStation = Utility.createURI(mapOfIds.get(cityId));
                Node predicateStateStation = Utility.createURI("<http://www.schema.org/State>");
                Node objectStateStation = Utility.createURI(baseUrl + cityName + "/" + cityId + "/" + unixTimestamp);
                Triple tripleStateStation = new Triple(subjectStateStation, predicateStateStation, objectStateStation);
                triples.add(tripleStateStation);

                Node predicateAvailableBicycles = Utility.createURI("<http://smart-ics.ee.surrey.ac.uk/ontology/m3-lite.owl#CountAvailableBicycles>");
                Node objectAvailableBicycles = Utility.createLiteral(String.valueOf(mapAvailabileBicycles.get(cityId)),XSDDatatype.XSDinteger);
                Triple tripleAvailableBicycles = new Triple(objectStateStation, predicateAvailableBicycles, objectAvailableBicycles);
                triples.add(tripleAvailableBicycles);

                Node predicateAvailableDocks = Utility.createURI("<http://purl.org/iot/vocab/m3-lite#CountEmptyDockingPoints>");
                Node objectAvailableDocks = Utility.createLiteral(String.valueOf(mapAvailableDocks.get(cityId)),XSDDatatype.XSDinteger);
                Triple tripleAvailableDocks = new Triple(objectStateStation, predicateAvailableDocks, objectAvailableDocks);
                triples.add(tripleAvailableDocks);

                if(arrayMappings.contains("http://www.wikidata.org/prop/direct/P5017")) {
                    Node predicateLastUpdate = Utility.createURI("http://www.wikidata.org/prop/direct/P5017");
                    Node objectLastUpdate = Utility.createLiteral(String.valueOf(mapLastUpdate.get(cityId)), XSDDatatype.XSDdateTime);
                    Triple tripleLastUpdate = new Triple(objectStateStation, predicateLastUpdate, objectLastUpdate);
                    triples.add(tripleLastUpdate);
                }

                Node predicateTime = Utility.createURI("<http://purl.org/iot/vocab/m3-lite#Timestamp>");
                Node objectTime = Utility.createLiteral(String.valueOf(unixTimestamp));
                Triple tripleTime = new Triple(objectStateStation, predicateTime, objectTime);
                triples.add(tripleTime);
            }
        }
        for(Triple triple:triples)
        {
            writer.triple(triple);
        }
    }

    public void uploadRDF(File rdf) {
        try
        {
//            output+="Ahmad";
            List<String> cmdList = new ArrayList<>();
            // adding command and args to the list
            cmdList.add("sh");
            cmdList.add("uploadRDF.sh");
            cmdList.add(rdf.getPath());
            ProcessBuilder pb = new ProcessBuilder(cmdList);
            Process process=pb.start();
            if(process.waitFor()==0)
            {
                System.out.println("Terminated Well");
            }
//            Scanner input=new Scanner(process.getInputStream());
//            while (input.hasNext())
//            {
//                System.out.println(input.nextLine());
//            }
        }
        catch (Exception e) {
            System.out.println("Error in uploading... "+e);
        }
    }
    private int nbOfTimes=0;
    private void parseXML(AbstractConfigurationFile mappings, StreamRDF writer) {
        String iterator = mappings.getIterator();
        HashMap<String, String> article = new HashMap<>();
        ArrayList<String> path=new ArrayList<>();
        try
        {
            Stack<String> stack=new Stack<>();
            String lastElementOnStack="";
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(inputFile));
            while (eventReader.hasNext())
            {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType())
                {
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        stack.push(qName);

                        path.add(qName);
//                        System.out.println(splitByPoints(path));
//                        System.out.println(lastElementOnStack);
//                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
                        if(qName.equals(lastElementOnStack))
                        {
                            String arrayPath=path.get(path.size()-2);
                            if(arrayPath.length()>3)
                            {
                                String number= StringUtils.substringBetween(arrayPath, "[", "]");
                                if(NumberUtils.isNumber(number))
                                {
                                    int newNbOfTimes=Integer.parseInt(number)+1;
                                    String newArrayPath=(arrayPath.split("\\[(.*?)\\]"))[0];
                                    newArrayPath=newArrayPath+"["+newNbOfTimes+"]";
                                    path.remove(path.size()-2);
                                    path.add(path.size()-1,newArrayPath);
                                }
                                else
                                {
                                    arrayPath=arrayPath+"["+nbOfTimes+"]";
                                    path.remove(path.size()-2);
                                    path.add(path.size()-1,arrayPath);
                                    nbOfTimes++;
                                }
                            }
                        }
                        else
                            nbOfTimes=0;
//                        System.out.println(splitByPoints(path));
//                        System.out.println("==============================================");
                        String pathString;
                        Iterator attributes = event.asStartElement().getAttributes();
                        while(attributes.hasNext())
                        {
                            Attribute attribute = (Attribute) attributes.next();
                            path.add(attribute.getName().toString());
                            article.put(splitByPoints(path),attribute.getValue());
                            path.remove(path.size()-1);
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        String data=characters.getData();
                        if(!data.trim().equals(""))
                        {
                            article.put(splitByPoints(path), data.trim());
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        event.asEndElement();
                        if(stack.size()!=0)
                            lastElementOnStack=stack.pop();
                        path.remove(path.size()-1);
                        pathString=splitByPoints(path);
                        if(pathString.replaceAll("\\[(.*?)]", "").replace("$.", "").equals(iterator))
                        {
//                            System.out.println(article);
                            processMap(article, writer, mappings);
                            article = new HashMap<>();
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseJson(AbstractConfigurationFile mappings, StreamRDF writer){
        try {
            String iterator = mappings.getIterator();
            int counterNew = 0;
            int counterOld = -1;
            HashMap<String, String> article = new HashMap<>();
            InputStream inputstream = new FileInputStream(inputFile);
            JsonReader reader = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));
            String name = "";
            reader.setLenient(true);

            boolean continuing = true;
            while (continuing) {
                String s;
                JsonToken token = reader.peek();
                switch (token) {
                    case BEGIN_ARRAY:
                        reader.beginArray();
                        break;
                    case END_ARRAY:
                        reader.endArray();
                        break;
                    case BEGIN_OBJECT:
//                    System.out.println(reader.getPath());
//                    System.out.println(reader.getPath().replaceAll("\\[(.*?)]", "").replace("$.", ""));
//                        System.out.println(iterator);
//                        System.out.println("=================================================");
                        if (reader.getPath().replaceAll("\\[(.*?)]", "").replace("$.", "").equals(iterator)) {
                            String[] list = iterator.split("\\.");
                            String arrayString = list[list.length - 1];
                            Pattern pattern = Pattern.compile(arrayString + "\\[(.*?)]");
                            Matcher matcher = pattern.matcher(reader.getPath());
                            if (matcher.find()) {
                                counterNew = Integer.parseInt(matcher.group(1));
                                counterOld = -1;
                            }
                        }
                        reader.beginObject();
                        break;
                    case END_OBJECT:
                        reader.endObject();
                        if (reader.getPath().replaceAll("\\[(.*?)]", "").replace("$.", "").equals(iterator) && counterNew != counterOld) {
                            counterOld = counterNew;
                            processMap(article, writer, mappings);
                            article = new HashMap<>();
                        }
                        break;
                    case NAME:
                        name = reader.nextName();
                        break;
                    case STRING:
                    case NUMBER:
                        s = reader.nextString();

                        if (!name.equals("") && !s.trim().equals(""))
                            article.put(reader.getPath().replace("$.", ""), s);

                        break;
                    case BOOLEAN:
                        reader.nextBoolean();
                        break;
                    case NULL:
                        reader.nextNull();
                        break;
                    case END_DOCUMENT:
                        continuing = false;
                        break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void parseCSV(AbstractConfigurationFile mappings, StreamRDF writer) throws Exception
    {
        CSVReader reader = new CSVReader(new FileReader(inputFile),';');
        String [] nextLine;
        System.out.println("Reading header of the CSV file ...");
        String [] header = reader.readNext();
        for (int i=0; i<header.length; i++) {
            System.out.println(header[i]);
        }
        HashMap<String, String> article = new HashMap<>();
        System.out.println("Reading the CSV file ...");
        while ((nextLine = reader.readNext()) != null) {
            for (int i=0; i<nextLine.length; i++){
                article.put(header[i],nextLine[i]);
            }
            System.out.println(article);
            processMap(article, writer, mappings);
            break;

        }
    }

    public static void main(String[] argv) throws Exception {
        Main main = new Main();
            JCommander.newBuilder()
                    .addObject(main)
                    .build()
                    .parse(argv);
            main.run();

}
    private void processMap(HashMap<String, String> article, StreamRDF writer, AbstractConfigurationFile mappings) {
        mappings.mappings.add(new Mapping(arraylistOfMappings.get(0).getTag(),"<http://www.wikidata.org/prop/direct/P276>",baseUrl+cityName));
        mappings.mappings.add(new Mapping(arraylistOfMappings.get(0).getTag(),"http://www.w3.org/1999/02/22-rdf-syntax-ns#type","http://www.w3.org/2003/01/geo/wgs84_pos#SpatialThing"));
        for (int i=0;i<mappings.mappings.size();i++)
        {
            ArrayList<Triple> triples = getObjects(mappings.mappings.get(i), mappings, article);
            for (Triple triple : triples)
            {
                String [] subjectArray=triple.getSubject().toString().split("/");
                String stationId=subjectArray[subjectArray.length-1];
                mapOfIds.put(stationId,triple.getSubject().toString());
                if(triple.getPredicate().toString().contains("http://smart-ics.ee.surrey.ac.uk/ontology/m3-lite.owl#CountAvailableBicycles"))
                {
                    mapAvailabileBicycles.put(stationId,Integer.parseInt(triple.getObject().toString().replaceAll("\"","")));
                }
                else if(triple.getPredicate().toString().contains("http://purl.org/iot/vocab/m3-lite#CountEmptyDockingPoints"))
                {
                    mapAvailableDocks.put(stationId,Integer.parseInt(triple.getObject().toString().replaceAll("\"","")));
                }
                else if(triple.getPredicate().toString().contains("http://www.wikidata.org/prop/direct/P5017"))
                {
                    mapLastUpdate.put(stationId,triple.getObject().toString().replaceAll("\"",""));
                }
                else
                {
                    writer.triple(triple);
                }
            }
        }
    }

    private static void printList(List<Mapping> list)
    {
        for (Mapping mapping: list)
        {
            System.out.println(mapping.getTag());
            System.out.println("¦¦¦¦¦¦¦¦¦¦¦¦¦");
            System.out.println(mapping.getPropertyUri());
        }
    }

    private static String splitByPoints(List<String> array) {
        String result = "";
        if (array.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(".");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }
    public static Node getSubject(HashMap<String,String> article, AbstractConfigurationFile mappings, String key)
    {
        String keyWithoutBrackets = key.replaceAll("\\[(.*?)]", "");
        List<String> arrayMappingsKey = new ArrayList<>(Arrays.asList(mappings.getKey().split("\\.")));
        List<String> arrayKeyArticle = new ArrayList<>(Arrays.asList(keyWithoutBrackets.split("\\.")));
        String[] keySplitted = key.replace("$.", "").split("\\.");
        ArrayList<String> finalSubject = new ArrayList<>();
        for (int i = 0; i < arrayMappingsKey.size(); i++)
        {
            if (i < arrayKeyArticle.size())
            {
                if (arrayKeyArticle.get(i).equals(arrayMappingsKey.get(i)))
                {
                    finalSubject.add(keySplitted[i]);
                }
                else {
                    finalSubject.add(arrayMappingsKey.get(i));
                }
            }
            else {
                finalSubject.add(arrayMappingsKey.get(i));
            }
        }
        String uri=baseUrl+staticCity+"/" + article.get(splitByPoints(finalSubject));
        String [] array=uri.split(" ");
        String newUri=String.join("_",array);
        return (Utility.createURI(newUri));
    }
    private static Node getPredicate(Mapping mapping)
    {
        Utility utility = new Utility();
        return utility.createURI(mapping.getPropertyUri());
    }
    private static ArrayList<Triple> getObjects(Mapping mapping, AbstractConfigurationFile mappings, HashMap<String,String> article) {
        ArrayList<Triple> triples = new ArrayList<>();
        HashMap<String, Pattern> fast=new HashMap<>();
        Node subject, predicate, object;
        Utility utility = new Utility();
        for (String key : article.keySet())
        {
            Pattern p;
            if (mappings.format.equals("csv"))
            {
                p = Pattern.compile(mapping.getTag());
                System.out.println(p);
                System.out.println(key);
                //p = fast.get("^"+mapping.getTag()+"$");
            }
            else if (fast.containsKey(mapping.getTag()))
            {
                p = fast.get(mapping.getTag());
//                System.out.println("P: "+p);
            }
            else
                {
                p = Pattern.compile(mapping.getTag());
                fast.put(mapping.getTag(), p);
            }
            Matcher m = p.matcher(key);
            if (m.find())
            {
                System.out.println("Found");
//                System.out.println(key);
//                System.out.println(mapping.getTag());
//                System.out.println("==================================================");
                if (mapping.getType() == null)
                {
                    subject = getSubject(article, mappings, key);
                    predicate = getPredicate(mapping);
                    object = Utility.createURI(mapping.getObject());
                    triples.add(new Triple(subject, predicate, object));
                }
                else {
                    switch (mapping.getType())
                    {
                        case LITERAL:
                            if (mapping.getDatatype() == null)
                            {
                                subject = getSubject(article, mappings, key);
                                predicate = getPredicate(mapping);
                                object = Utility.createLiteral(article.get(key));
                                triples.add(new Triple(subject, predicate, object));
                            }
                            else {
                                subject = getSubject(article, mappings, key);
                                predicate = getPredicate(mapping);
                                object = Utility.createLiteral(article.get(key), mapping.getDatatype());
                                triples.add(new Triple(subject, predicate, object));
                            }
                            break;
                        case URI:
                            if (mapping.getBaseurl() != null) {
                                subject = getSubject(article, mappings, key);
                                predicate = getPredicate(mapping);
                                object = utility.createURI(mapping.getBaseurl() + article.get(key));
                                triples.add(new Triple(subject, predicate, object));
                            } else {
                                if (article.get(key).startsWith("http://")) {
                                    subject = getSubject(article, mappings, key);
                                    predicate = getPredicate(mapping);
                                    object = utility.createURI(article.get(key));
                                    triples.add(new Triple(subject, predicate, object));
                                } else {
                                    subject = getSubject(article, mappings, key);
                                    predicate = getPredicate(mapping);
                                    object = utility.createURI(baseUrl + article.get(key));
                                    triples.add(new Triple(subject, predicate, object));
                                }
                            }
                            break;
                        case URI_WITH_LABEL:
                            if (mapping.getBaseurl() != null)
                            {
                                subject = getSubject(article, mappings, key);
                                predicate = getPredicate(mapping);
                                object = utility.createURI(mapping.getBaseurl() + article.get(key));
                                triples.add(new Triple(subject, predicate, object));

                                subject = object;
                                predicate = utility.createURI("http://www.w3.org/2000/01/rdf-schema#label");
                                object = NodeFactory.createLiteral(article.get(key));
                                triples.add(new Triple(subject, predicate, object));
                            }
                            else {
                                if (article.get(key).startsWith("http://")) {
                                    subject = getSubject(article, mappings, key);
                                    predicate = getPredicate(mapping);
                                    object = utility.createURI(article.get(key));
                                    triples.add(new Triple(subject, predicate, object));

                                    subject = object;
                                    predicate = utility.createURI("http://www.w3.org/2000/01/rdf-schema#label");
                                    object = NodeFactory.createLiteral(article.get(key));
                                    triples.add(new Triple(subject, predicate, object));

                                } else {
                                    subject = getSubject(article, mappings, key);
                                    predicate = getPredicate(mapping);
                                    object = utility.createURI(baseUrl + article.get(key));
                                    triples.add(new Triple(subject, predicate, object));

                                }
                            }
                            break;

                        case INTEGER:
                            subject = getSubject(article, mappings, key);
                            predicate = getPredicate(mapping);
                            object =NodeFactory.createLiteral(article.get(key), XSDDatatype.XSDinteger);
                            triples.add(new Triple(subject, predicate, object));
                            break;
                        case DECIMAL:
                            subject = getSubject(article, mappings, key);
                            predicate = getPredicate(mapping);
                            object =NodeFactory.createLiteral(article.get(key), XSDDatatype.XSDdecimal);
                            triples.add(new Triple(subject, predicate, object));
                            break;
                        case BOOLEAN:
                            subject = getSubject(article, mappings, key);
                            predicate = getPredicate(mapping);
                            object =NodeFactory.createLiteral(article.get(key), XSDDatatype.XSDboolean);
                            triples.add(new Triple(subject, predicate, object));
                            break;
                        case DATE:
                            subject = getSubject(article, mappings, key);
                            predicate = getPredicate(mapping);
                            object = NodeFactory.createLiteral(article.get(key), XSDDatatype.XSDdateTime);
                            triples.add(new Triple(subject, predicate, object));
                            break;
                        case CLASS:
                            if(mapping.getObject()!=null)
                            {
                                subject = getSubject(article, mappings, key);
                                predicate = getPredicate(mapping);
                                object = utility.createURI(mapping.getObject());
                                triples.add(new Triple(subject, predicate, object));
                            }
                            break;
                        case CUSTOM:
                            triples.addAll(mapping.getCustomMapping().function(article, key));
                            break;
                    }
                }
            }
        }
        return triples;
    }
    public String printHashmap(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append('\n');
            }
        }
        return sb.toString();

    }
}

