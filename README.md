# BicycleSharingSystem

## Abstract

In this project we provide a service based on semantic web technologies where we can add open data from any source about bicycle sharing stations in different cities, convert the data into a unique model, and provide visualization for this data. We conisder a fixed model for such services, we convert the provided data (which could be fairly diverse from source to source) to RDF format. The process can be run in the background to dump the real-time data into the database (triple store) so that we will have an up-to date data visualization (website) and also keep track about the station states to perform statistical visualization.

## System architecture

### Description
We talk here about the structure and the architecture of the system. Starting with a source of data we provide a mechanisim where we convert the data to RDF which is a data representation in Semantic Web for linked data (expressed as triples). For this matter we fix a specific model that covers the most important information about bicycle stations in our case. The model will be explained later but basically it expresses the stations as objects having static and dynamic information at each specific epoch of time. We use the fuseki triple store to save the generated RDF data. For data visualization, we provide a website using the library [leaf-let](https://leafletjs.com/) offered by open-street-map which shows the location and some information about the stored stations. The tool converting to RDF uses Jena model to create the triples as well as literals and URIs while parsing the introduced data. In the figure below is a brief representation about the flow starting from the source to the visualization on the website.
![alt text](https://github.com/ahmdIbrhm/BicycleSharingSystem/blob/master/system_arch.png "System Architecture")

### Semantic Usages

The technologies used in the implementation could be listed as follow:

* The setup of the fuseki triplestore that, in our case, runs on the localhost for testing matters and is accepting queries from outside (website data visualization, testing using curl... etc) by using the end-point provided by the triplestore.

* Definition of an ontology model to represent stations. Using protege tool, we generated an OWL file containing the structure we desire. It uses properties and classes of other ontologies and for some URIs we used [example.org](http://example.org) domain name to express our entities.

* Implementation of a website where we can add new cities by specifiying the desired parameters (explained well in the how to use [documentation](https://github.com/ahmdIbrhm/BicycleSharingSystem/blob/master/documentation.md) ). Also we allow the visualization of the stations of a selected city on a map. The data can be seen also as list where it contains embeded linked data using RDF-a that can be extracted using any disteller.

* The conversion tool is an extended tool [Data_To_RDF](https://github.com/ahmdIbrhm/dataToRDF) and in this project it adapts the usage of bicycle sharing open data and the model proposed in the next session. It uses Jena model to create triples while digging for the information in the loaded data.

### Bicyle Stations Model Definition

In order to dump data about the stations into a triple-store we need some sort of a fixed model so later then we can run queries to get insights about the data and visualize it. In our model, each station should have many states, where each of these states have different times from one another. On the other hand, each state has some parameters that express the status of the station at a specific time. This concept was demonstrated as follows:
* Each station has a specific URI to be distinguished from other stations in the graph.
* Each staiton is associated with the static information about it (name,city,coordinates ).
* Each station has **some** states each represent the state of the station at a specific time.
* The state is associated to a date-time and the dynamic information (Available bikes, Available docks, ...).
* Each station is associated to a city. Each city holds a configuration, which basically contain the information filled by the user so those parameters can be queried later to get the data dynamically and dump them to the triple store.

The model is generated using protege and the OWL file is attached to this repo here [Bicycle OWL](https://github.com/ahmdIbrhm/BicycleSharingSystem/blob/master/bicycle.owl). Addition to that, we can view the documentation by accessing the [documentation folder](https://github.com/ahmdIbrhm/BicycleSharingSystem/tree/master/Ontology%20Documentation). 
A clear demonstration is available in this picture below:
![alt text](https://github.com/ahmdIbrhm/BicycleSharingSystem/blob/master/bicycle.svg "Bicyle stations model representation")

### Queries for data visualization

* Query to get the available cities in the database:  
`select ?city ?name where{ ?city <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.wikidata.org/entity/Q515> . ?city <http://www.w3.org/2000/01/rdf-schema#label> ?name }`

The next three queries are to get the same content but we chosed the fastest one (Query number 1.):

1. Query to get the stations associated to the selected city with their current states and the information about the states:
```PREFIX wdt: <http://www.wikidata.org/prop/direct/>  
SELECT ?station ?lat ?lon ?capacity ?bank_card ?name ?max_date ?av_bikes ?av_docks ?address ?last_time  
      WHERE {  ?station <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?lon ;
                        <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?lat ;
                        <http://www.w3.org/2000/01/rdf-schema#label> ?name ;
                        wdt:P276 <http://www.example.org/Lyon> .
                        OPTIONAL { ?station wdt:P1083 ?capacity .}
                        OPTIONAL { ?station wdt:P6375 ?address .}
                        OPTIONAL { ?station <https://schema.org/PaymentCard> ?bank_card .}
                        {    
                              SELECT ?station (MAX(?date) AS ?max_date) (MAX(?availableBicycles) as ?av_bikes) (MAX(?availableDocks) as ?av_docks) (MAX(?last_update) as ?last_time)  WHERE 
                              {         ?station <http://www.schema.org/State> ?state .
                                        ?state <http://purl.org/iot/vocab/m3-lite#Timestamp> ?date ;            <http://smart-ics.ee.surrey.ac.uk/ontology/m3-lite.owl#CountAvailableBicycles> ?availableBicycles ;            <http://purl.org/iot/vocab/m3-lite#CountEmptyDockingPoints> ?availableDocks;
                                        optional { ?state wdt:P5017 ?last_update .}
                               } group by ?station
                          }  
             }
```
2. This query was proposed using the SAMPLE method but apparently the one above is faster.
```PREFIX  wdt:  <http://www.wikidata.org/prop/direct/>  
      SELECT  ?lat ?lon ?capacity ?name ?max_date ?availableBicycles ?availableDocks ?station  WHERE    { 
      ?station  <http://www.w3.org/2003/01/geo/wgs84_pos#long>  ?lon ;
                <http://www.w3.org/2003/01/geo/wgs84_pos#lat>  ?lat ;
                 <http://www.w3.org/2000/01/rdf-schema#label>  ?name ;
                 wdt:P1083             ?capacity ;
                 wdt:P276              <http://www.example.org/Lyon> .
                 ?state <http://www.example.org/availableBicycles>  ?availableBicycle;
                 <http://www.example.org/availableDocks>  ?availableDocks.
                 {     
                 SELECT ?station (MAX(?date) AS ?max_date)  (SAMPLE(?state_1) as ?state) WHERE {
                              ?station <http://www.schema.org/State> ?state_1 .
                              ?state_1  <http://www.example.org/time>  ?date .
                    } group by ?station
                 }
       }
```
3. Using FILTERs:
```
PREFIX wdt: <http://www.wikidata.org/prop/direct/>
SELECT ?lat ?lon ?capacity ?name ?max_date ?availableBicycles ?availableDocks
  WHERE {
  ?station <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?lon ;
  <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?lat ;
  <http://www.w3.org/2000/01/rdf-schema#label> ?name ;
  <http://www.wikidata.org/prop/direct/P1083> ?capacity ;
  wdt:P276 <http://www.example.org/Saint_Etienne> ;
  {
        select ?station  ?availableBicycles ?availableDocks ?max_date where {
        ?station <http://www.schema.org/State> ?state .
        ?state <http://www.example.org/availableBicycles> ?availableBicycles;
        <http://www.example.org/availableDocks> ?availableDocks ;
        <http://www.example.org/time> ?max_date 
            {
                  select ?strState (max(?date) as ?max_date) where 
                  {
                    SELECT (replace(str(?state),"[0-9]*$","") as ?strState) ?date
                    WHERE
                    {
                    ?state <http://www.example.org/time> ?date .
                    }
                  }
                  group by ?strState
             }
        }
  }
group by ?station  ?availableBicycles ?availableDocks ?max_date
  }
}
```
* Query to get the states of the stations starting from a specified date:
```
PREFIX wdt: <http://www.wikidata.org/prop/direct/>
      select ?av_bikes ?av_docks ?time where {
      <http://www.example.org/Lyon/7008> <http://www.schema.org/State> ?state;
                                          wdt:P276 <http://www.example.org/Lyon> .
      ?state <http://purl.org/iot/vocab/m3-lite#Timestamp> ?time;
             <http://smart-ics.ee.surrey.ac.uk/ontology/m3-lite.owl#CountAvailableBicycles>  ?av_bikes ;
             <http://purl.org/iot/vocab/m3-lite#CountEmptyDockingPoints>  ?av_docks.
             FILTER (?time >= "1575158400")
    }
```
## Problems and limitations
In the current implementation there were some limitations that are caused by the fact of the diversity and interoperability problems:

* Sometimes, the complex structure of the input data would let the parsing scenario fail.
* Dumping so often to the triplestore would start making it slow in performance.
* Covering all possible file structures would make the GUI of the input more complicated and not user-friendly.
* The tool used to convert data to RDF is limited to bicycle stations only.

## Evaluation
### What is done well?
The things that are robustly implemented:
* **The ontology architecture and structure**  
We reused some properties that describes bicycle-sharing stations such as some properties from [M3-lite](http://purl.org/iot/vocab/m3-lite) ontology. We also used pre-defined properties from [Wikidata](https://www.wikidata.org/wiki/Wikidata:List_of_properties).   
Addition to that, the design of our ontology helps us to store several states for each station. We created a new class "State" that will be responsible for assigning some states to 1 station. Without this class, we couldn't store except only 1 state for each station. This class has 4 properties which describes the state of the station at a specific time.
* **The website**  
The website implemented uses semantic web technologies to query data and view them on a map. Addition to that, the page called map.php is implemented using HTML with integrated RDF-a. 
* **The tool used to convert JSON, XML, or CSV into RDF**  
The reason is that this tool is able to convert data about bicycle stations in any city, so it is not dedicated only to a specific city

### What is poorly done? 
The things that are poorly implemented:
* **The tool used to convert JSON, XML, or CSV into RDF**  
The reason is that this tool is not extendable to be able to convert data about train stations and car stations

### Who did what?

#### As a team
* Design of the ontology
* Queries to get specific data
#### Ahmad 
* Implemented the contexts of adding data to the triple store (Add city)
* Data conversion from any format to RDF 
* Created ontology file and description (Protege)
* Storing data in background
#### Ali
* Implemented the contexts of querying data from the triple store. 
* Data visualization (Map, Statistics)
* RDF-a integration in HTML



## Conclusion and future work

The current system is a start where it aims to be more generic than normal cases using a user-friendly interface to help the parsing scenario allowing exceptions and extra information but the open-data is so diverse where it is a huge challenge for such systems.The system could be extended to other kind of availabilities and the interoperability problems would start appearing more often.We could imagine a tool that can parse any kind of structure but this is quite impossible for the moment but maybe later it would not be that hard.

## References and sources
Map: [LeafLet](https://leafletjs.com/)  
Website template: [Free website templates](https://freewebsitetemplates.com/preview/astronomywebsitetemplate/index.html)  
Statistics' Graph: [ChartJS](https://www.chartjs.org/)   
Alerts: [Sweet Alert](https://sweetalert2.github.io/)  

