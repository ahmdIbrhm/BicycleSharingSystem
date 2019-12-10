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

* Definition of a model to represent stations using protege tool to generate an OWL file containing the structure we desire.It uses properties and classes of other ontologies and for some URIs we use (www.example.org) domain name to express our entities.

* Implementation of a website where we can add new cities by specifiying the desired parameters (explained well in the documenetation ), also we allow the visualization of the stations of a selected city on a map. The data can be seen also as list where it contains embeded linked data using RDF-a that can be extracted using any disteller.

### Bicyle Stations Model Definition

In order to dump data about the stations into a triple-store we need some sort of a fixed model so later then we can run queries to get insights about the data inorder to visualize it.And in the case of bicycle stations we have updates on the states where the data is not static.So one can say that a station must have a unique state related to the context of time when this station had those specific information related to this time.The proposed solution is the following:
* each station has a specific URI to be distinguished from other stations in the graph.
* each staiton is associated with the static information about it (name,city,coordinates ).
* the station has a lot of unique states which represents the dicussion before.
* the state is associated to a date-time and the dynamic information (# available bikes, # available docks).

The model is generated using protege and the OWL file is attached to this repo here [Bicycle OWL](https://github.com/ahmdIbrhm/BicycleSharingSystem/blob/master/bicycle.owl)
A clear demonstration is available in this picture below:
![alt text](https://github.com/ahmdIbrhm/BicycleSharingSystem/blob/master/bicycle.svg "Bicyle stations model representation")



