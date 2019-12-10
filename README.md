# BicycleSharingSystem

## Abstract

In this project we provide a service based on semantic web technologies where we can add open data from any source about bicycle sharing services in cities, convert the data into a unique model, provide visualization for this data.We conisder a fixed model for such services, we convert the provided data to RDF format using a pre-designed tool that accepts parameters about how it is structured which could be fairly diverse from source to source. The processs can be run in the background to dump the real-time data into the database (triple store) so that we will have an up-to date data visualization(website) and also we keep track about the station states where everything is available for statistical issues.The process could fail due to the fact that we could a huge diversity where it have some limitations described more precisely in the scope.

## System architecture

### Description
We talk here about the structure and the architecture of the system.Starting with a source of data we provide a mechanisim where we convert the data to RDF which is a data representation in Semantic Web for linked data (expressed as triples), for this matter we fix a specific model that covers the most important information about bicycle stations in our case. The model will be explained later but basically it expresses the stations as things having static and the dynamic information at each specific epoch of time. We use the fuseki triple store to store the generated data into RDF, and for data visualization we provide a leaf-let by open-street-map showing the information about the stations.The tool converting to RDF uses Jena model to create the triples as well as literals and URIs while parsing the introduced data.In the figure below is a bried representation about the flow starting from the source to the visualization on the website.
![alt text](https://github.com/ahmdIbrhm/BicycleSharingSystem/blob/master/system_arch.png "System Architecture")

### Semantic Usages

The technologies used in the implementation could be listed as follow:

* The setup if the triplestore fuseki in our case where it is running on the localhost for testing matters and is accepting queries from outside (website data visualization, testing using curl... etc) by using the end-point provided by the triplestore.

* Definition of a model to represent stations using protege tool to generate an OWL file containing the structure we desire.It uses properties and classes of other ontologies and for some URIs we use (http://example.org) domain name to express our entities.

* Implementation of a website where we can add new cities by specifiying the desired parameters (explained well in the how to use documenetation [documentation](https://github.com/ahmdIbrhm/BicycleSharingSystem/blob/master/documentation.md) ), also we allow the visualization of the stations of a selected city on a map. The data can be seen also as list where it contains embeded linked data using RDF-a that can be extracted using any disteller.

* The conversion tool is an extended tool where it exists before [Data_To_RDF](https://github.com/ahmdIbrhm/dataToRDF) and in this projects it adapts the usage of bicycle sharing open data and the model proposed in the next session.It uses Jena model to create triples while digging for the information in the loaded data.

### Bicyle Stations Model Definition

In order to dump data about the stations into a triple-store we need some sort of a fixed model so later then we can run queries to get insights about the data inorder to visualize it.And in the case of bicycle stations we have updates on the states where the data is not static.So one can say that a station must have a unique state related to the context of time when this station had those specific information related to this time.The proposed solution is the following:
* each station has a specific URI to be distinguished from other stations in the graph.
* each staiton is associated with the static information about it (name,city,coordinates ).
* the station has a lot of unique states which represents the dicussion before.
* the state is associated to a date-time and the dynamic information (# available bikes, # available docks).
* each city which the station is assoiciated to holds a configuration, which basically contain the information filled by the user so those parameters can be quieried later to get the data dynamically and dump them to the triple store.

The model is generated using protege and the OWL file is attached to this repo here [Bicycle OWL](https://github.com/ahmdIbrhm/BicycleSharingSystem/blob/master/bicycle.owl)
A clear demonstration is available in this picture below:
![alt text](https://github.com/ahmdIbrhm/BicycleSharingSystem/blob/master/bicycle.svg "Bicyle stations model representation")

### Queries for data visualization

* Query to get the available cities in the database:
`select ?city ?name where{ ?city <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.wikidata.org/entity/Q515> . ?city <http://www.w3.org/2000/01/rdf-schema#label> ?name }`
* Query to get the stations associated to the selected city with their current states and the information about the states:
`PREFIX wdt: <http://www.wikidata.org/prop/direct/>  
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
`


## Problems and limitations
In the current implementation there some limitations that are caused by the fact of the diversity and interoperability problems:

* one could load a dataset that would not match with the structure maybe he would miss some neccessary information needed.
* The parsing scenario would fail due to the structure of the data.
* Dumping so often the data would start making the triple store slow in performance especially if the number of indexed triples exceeds a certain limit where the triple store stop scaling.
* Allowing the exceptions in the strucure of the data would make the GUI of the input more complicated and not user-friendly.

## Evaluation

## Conclusion and future work

The current system is a start where it aims to be more generic than normal cases using a user-friendly interface to help the parsing scenario allowing exceptions and extra information but the open-data is so diverse where it is a huge challenge for such systems.The system could be extended to other kind of availabilities and the interoperability problems would start appearing more often.We could imagine a tool that can parse any kind of structure but this is quite impossible for the moment but maybe later it would not be that hard.

