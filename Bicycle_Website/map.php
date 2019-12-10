<!DOCTYPE html>
<!-- Website template by freewebsitetemplates.com -->
<html>
<head>

	<style type="text/css">
      html, body, #basicMap {
          height: 600px;  /* The height is 400 pixels */
        width: 100%;
      }
    </style>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css"
   integrity="sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ=="
   crossorigin=""/>
   <script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"
   integrity="sha512-gZwIG9x3wUXg2hdXF6+rVkLF/0Vi9U8D2Ntg4Ga5I5BZpVkVxlJWbSQtXPSiUTtC0TjtGOmxa1AJPuV0CPthew=="
   crossorigin=""></script>
    <script >
    class Station {

        constructor(name,av_bikes,av_docks,lat,long,time,station_uri){
            this.name = name
            this.av_bikes = av_bikes
            this.av_docks = av_docks
            this.lat = lat
            this.long = long
            this.time = time
            this.station_uri = station_uri
        }
    }
    var mymap;
    var stations = []
    mapAlreadyInit = false;
    function init() {

    	if(document.getElementById('basicMap')){
        	if(!mapAlreadyInit){
                mymap = L.map('basicMap');
         	 	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        		maxZoom: 18,
        		attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
        			'<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        			'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        		id: 'mapbox.streets'
        		}).addTo(mymap);
                mapAlreadyInit = true;
            }
    		loadMarkers();
        }
    }

    function parseDate(time){
        var a = new Date(time * 1000);
        var year = a.getFullYear();
        var month = a.getMonth()+1;
        var date = a.getDate();
        var hour = a.getHours();
        var min = a.getMinutes();
        var sec = a.getSeconds();
        var formattedTime = date + '-' + month + '-' + year + '-T' + hour + ':' + min + ':' + sec ;
        return formattedTime;
    }
    function LoadStations() {

        stations = []
    	var xhttp = new XMLHttpRequest();
    	xhttp.onreadystatechange = function() {
    		if (this.readyState == 4 && this.status == 200) {
    			var obj = JSON.parse(this.responseText);
    			var results = obj.results;
    			var bindings = results.bindings;
    			var str = '';
                var count = 0
    			for (var i = 0; i < bindings.length; i++) {

    				var obj = bindings[i];
    				var lat_obj = obj['lat'];
    				var lon_obj = obj['lon'];

    				var lat_value = lat_obj.value;
    				var lon_value = lon_obj.value;

                    var name = obj['name'].value;
                    var av_bikes = obj['av_bikes'].value;
                    var av_docks = obj['av_docks'].value;
                    var unix_timestamp = obj['max_date'].value;

                    var a = new Date(unix_timestamp * 1000);
                    var year = a.getFullYear();
                    var month = a.getMonth()+1;
                    var date = a.getDate();
                    var hour = a.getHours();
                    var min = a.getMinutes();
                    var sec = a.getSeconds();
                    var formattedTime = date + '-' + month + '-' + year + '-T' + hour + ':' + min + ':' + sec ;
                    var station_uri = obj['station'].value;
                                
                    var station;
                    var station = new Station(name,av_bikes,av_docks,lat_value,lon_value,formattedTime,station_uri);

                    if(obj.hasOwnProperty('capacity')){
                         var capacity = obj['capacity'].value;
                         station.capacity = capacity;
                    }
                    if(obj.hasOwnProperty('address')){
                        var address = obj['address'].value;
                        station.address = address
                    }
                   
                    if(obj.hasOwnProperty('bank_card')){

                        var bank_card = obj['bank_card'].value;
                        station.bank_card = bank_card
                    }
    				if(obj.hasOwnProperty('last_time')){

                        station.time = obj['last_time'].value;

                    }
                    
                    stations.push(station)

    			}
                init();
    		}
    	};
    	var dynamicSelect = document.getElementById("dynamic_select");


    	var selected_city = dynamicSelect[dynamicSelect.selectedIndex].value;
        

    	xhttp.open("POST", "http://localhost:3030/test/query", true);
    	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        
        // change query ......


        /*var query = "query=PREFIX  wdt:  <http://www.wikidata.org/prop/direct/>\n" +
                "\n" +
                "SELECT  ?lat ?lon ?capacity ?name ?max_date ?availableBicycles ?availableDocks ?station\n" +
                " WHERE\n" +
                "   { ?station  <http://www.w3.org/2003/01/geo/wgs84_pos#long>  ?lon ;\n" +
                "               <http://www.w3.org/2003/01/geo/wgs84_pos#lat>  ?lat ;\n" +
                "               <http://www.w3.org/2000/01/rdf-schema#label>  ?name ;\n" +
                "               wdt:P1083             ?capacity ;\n" +
                "               wdt:P276              <"+ selected_city+"> ."+
                "               ?state <http://www.example.org/availableBicycles>  ?availableBicycles ;\n" +
                "                      <http://www.example.org/availableDocks>  ?availableDocks.\n" +
                "            {\n" +
                "    SELECT ?station (MAX(?date) AS ?max_date)  (SAMPLE(?state_1) as ?state)\n" +
                "WHERE { \n" +
                "  ?station <http://www.schema.org/State> ?state_1 .\n" +
                "      \n" +
                "?state_1  <http://www.example.org/time>  ?date .\n" +
                "}\n" +
                "group by ?station\n" +
                "      }\n" +
                "   }";*/

    	var query ="query=PREFIX wdt: <http://www.wikidata.org/prop/direct/> \n" +
                "SELECT ?station ?lat ?lon ?capacity ?bank_card ?name ?max_date ?av_bikes ?av_docks ?address ?last_time WHERE {\n" +
                " ?station <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?lon ; \n" +
                "       <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?lat ;\n" +
                "       <http://www.w3.org/2000/01/rdf-schema#label> ?name ; \n" +
                "       wdt:P276 <"+selected_city+"> .\n" +
                "       OPTIONAL { ?station wdt:P1083 ?capacity .} \n" +
                "       OPTIONAL { ?station wdt:P6375 ?address .} \n" +
                "       OPTIONAL { ?station <https://schema.org/PaymentCard> ?bank_card .} \n" +
                
                "  { \n" +
                "  SELECT ?station (MAX(?date) AS ?max_date) (MAX(?availableBicycles) as ?av_bikes) (MAX(?availableDocks) as ?av_docks) (MAX(?last_update) as ?last_time)  WHERE { \n" +
                "       ?station <http://www.schema.org/State> ?state .\n" +
                "       ?state <http://purl.org/iot/vocab/m3-lite#Timestamp> ?date ; \n" +
                "          <http://smart-ics.ee.surrey.ac.uk/ontology/m3-lite.owl#CountAvailableBicycles> ?availableBicycles ; \n" +
                "          <http://purl.org/iot/vocab/m3-lite#CountEmptyDockingPoints> ?availableDocks; \n" +
                "          optional { ?state wdt:P5017 ?last_update .} \n" +
                "     } group by ?station \n" +
                "  } \n" +
                "}";
    	xhttp.send(query);

    }

    function printStations(){
        stations.forEach(station => {
            console.log(station)
        })
    }
    function loadMarkers(){
        var counter = 0;
        var center_lat = 0;
        var center_lon = 0;

        stations.forEach(station => {

            var box_text = '<b>Bicycle station</b> <br/> <b>Station name</b>: '+station.name+'<br/><b>av_bikes</b>: '+station.av_bikes+'<br/> <b>av_docks</b>: '+station.av_docks+'<br/><b>last updated</b>: '+station.time
            if(station.capacity != null){
                box_text += '<br/><b>capacity</b>:'+station.capacity
            }
            if(station.address != null){
                box_text += '<br/><b>address</b>:'+station.address
            }
            if(station.bank_card != null){
                box_text += '<br/><b>payment by card:</b>:'+station.bank_card
            }

            var marker = L.marker([station.lat, station.long ]).addTo(mymap).bindPopup(box_text);
            

            center_lat += parseFloat(station.lat);
            center_lon += parseFloat(station.long);
            counter++;
        })

        center_lat = center_lat /counter;
        center_lon = center_lon / counter;

        mymap.setView([center_lat, center_lon], 13);
    }
    function loadCities(){
   
    var dynamicSelect = document.getElementById("dynamic_select");

   	var xhttp = new XMLHttpRequest();

    	xhttp.onreadystatechange = function() {
    		if (this.readyState == 4 && this.status == 200) {

    			var obj = JSON.parse(this.responseText);
    			var results = obj.results;
    			var bindings = results.bindings;
    			var str = '';

    			for (var i = 0; i < bindings.length; i++) {

    				var obj = bindings[i];
    				var name = obj['name'].value;
    				var uri = obj['city'].value;

    				dynamicSelect.innerHTML += "<option value=\""+uri+"\" >"+name+"</option>";

    			}
                LoadStations();
    		}
    	};

    	xhttp.open("POST", "http://localhost:3030/test/query", true);
    	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    	xhttp.send("query=select ?city ?name where {?city <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.wikidata.org/entity/Q515> . ?city <http://www.w3.org/2000/01/rdf-schema#label> ?name }");

    }

    function switchListener(showMap){
        
        var button_show = document.getElementById("btn_show");
        if(showMap == false){
            button_show.innerHTML = "SHOW MAP"
            showMap = false
            showList()
        }else{
            button_show.innerHTML = "SHOW LIST"
            showMap = true
            generateMap()
        }
    }
    function generateMap(){

        var test = JSON.stringify(stations)


        var form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", "map.php");

        //Move the submit function to another variable
        //so that it doesn't get overwritten.
        form._submit_function_ = form.submit;


        document.body.appendChild(form);
        form._submit_function_();


        // var list = document.getElementById("div_list_stations");
        // list.parentNode.removeChild(list);

        // var wrapper = document.getElementById("wrapper");
        // var div_map = document.createElement("div");
        // div_map.id = "basicMap";
        // wrapper.appendChild(div_map);
        // wrapper.appendChild(document.createElement("br"))
        // init()


    }
    function showList(){

        var test = JSON.stringify(stations)



        var form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", "map.php");

        //Move the submit function to another variable
        //so that it doesn't get overwritten.
        form._submit_function_ = form.submit;
        var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "test");
        hiddenField.setAttribute("value", test);

        form.appendChild(hiddenField);
        document.body.appendChild(form);
        form._submit_function_();



        

        /*var wrapper = document.getElementById("wrapper");

        var div_list = document.createElement("div");
        div_list.id = "div_list_stations"
        div_list.className = "list-cards";

        stations.forEach(station => {
            var div_rounded_shadowed = document.createElement("div");
            div_rounded_shadowed.className = "card rounded shadowed";

            var div_content = document.createElement("div");
            div_content.className = "card-content";

            var div_title = document.createElement("div");
            div_title.className = "card-title";
            div_title.innerHTML = "<h3>"+station.name+"</h3>";

            var div_description = document.createElement("div");
            div_description.className = "card-description";

            div_description.innerHTML += "<b>capacity:</b>"+station.capacity+"</br>"
            div_description.innerHTML += "<b>av_bikes:</b>"+station.av_bikes+"</br>"
            div_description.innerHTML += "<b>av_docks:</b>"+station.av_docks+"</br>"
            // div_description.innerHTML += "<b>latitude:</b>"+station.lat+"</br>"
            // div_description.innerHTML += "<b>longtitude:</b>"+station.long+"</br>"

            div_content.appendChild(div_title);
            div_content.appendChild(div_description);
            div_rounded_shadowed.appendChild(div_content);
            div_list.appendChild(div_rounded_shadowed);
        })

        wrapper.appendChild(div_list);*/
        

        
    }
    </script>
	<meta charset="UTF-8">
	<title>Map</title>
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link rel="stylesheet" href="css/combo.css" type="text/css">
    <link rel="stylesheet" href="css/style_cards.css" type="text/css">
</head>
<body onload="loadCities()">
	<div id="header">
		<div class="wrapper clearfix">
			<div id="logo">
				<a href="index.html"><img src="images/logo.png" width="60" height="50"></a>
			</div>
			<ul id="navigation">
                <li >
                    <a href="index.html">Home</a>
                </li>
				<li >
                    <a href="blog.html">Add Cities</a>
                </li>
                <li class="selected">
                    <a href="map.php">Map</a>
                </li>
                <li>
                    <a href="about.html">Statistics</a>
                </li>
			</ul>
		</div>
	</div>
    <div id="contents">
        <div id="contact" class="wrapper clearfix">
            <div class="main">
                
            </div>
        </div>
    </div>
	<div id="footer">
		<div id="wrapper" class="wrapper clearfix">		
			<h3>Choose city:</h3>
			<select id="dynamic_select" onchange="LoadStations()" style="width:150px;height:30px;">

			</select>
			</br>
			
            

            <?php


                echo '<p style="font-size:160%;">Click on the markers viewed on the map to check bicycle stations information</p>';
                if(isset($_POST['test'])){
                    echo '</br>
                        <div id="sidebar">
                            <a id="btn_show" href="#" class="outlinedbtn" onclick="switchListener(true)">SHOW MAP</a>
                            <br/>
                            <br/>
                        </div>';
                }else{
                    echo '</br>
                        <div id="sidebar">
                            <a id="btn_show" href="#" class="outlinedbtn" onclick="switchListener(false)">SHOW LIST</a>
                            <br/>
                            <br/>
                        </div>';
                }
                        

                echo '<h2> Stations</h2>';
                if(isset($_POST['test'])){
                    $stations = $_POST['test'];
                    $stations_array = json_decode($stations,true);
                    echo '<div class="list-cards">';

                    foreach ($stations_array as $key => $value) {
                        echo '  <div class="card rounded shadowed">
                            <div class="card-content">
                            <div class="card-title">
                                <h3 about="'.$value['station_uri'].'" property="http://www.w3.org/2000/01/rdf-schema#label" >'.$value['name'].'</h3>
                            <div class="card-description">
                                <p about="'.$value['station_uri'].'" property="http://www.example.org/availableBicycles" content="'.$value['av_bikes'].'"><b>Available Bicycles: </b>'.$value['av_bikes'].'</p>
                                <p about="'.$value['station_uri'].'" property="http://www.example.org/availableDocks" content="'.$value['av_docks'].'" ><b>Available Docks: </b>'.$value['av_docks'].'</p>
                                ';
                        if(isset($value['capacity'])){
                            echo '<p about="'.$value['station_uri'].'" property="http://www.wikidata.org/prop/direct/P1083" content="'.$value['capacity'].'" ><b>Capacity: </b>'.$value['capacity'].'</p>
                            ';
                        }
                        if(isset($value['address'])){
                            echo '<p about="'.$value['station_uri'].'" property="http://www.wikidata.org/prop/direct/P6375" content="'.$value['address'].'" ><b>Address: </b>'.$value['address'].'</p>
                            ';
                        }
                        if(isset($value['bank_card'])){
                            echo '<p about="'.$value['station_uri'].'" property="https://schema.org/PaymentCard" content="'.$value['bank_card'].'" ><b>Pay by card: </b>'.$value['bank_card'].'</p>
                            ';
                        }
                        if(isset($value['time'])){
                            echo '<p about="'.$value['station_uri'].'" property="http://purl.org/iot/vocab/m3-lite#Timestamp" content="'.$value['time'].'" ><b>Last reported: </b>'.$value['time'].'</p>
                            ';
                        }


                        echo'</div>    
                            </div>
                            </div></div>';                
                    }
                    echo '</div>';
                }else{
                    echo '<div id="basicMap"></div>';
                }
            ?>
            <br/>

		</div>
        <div class="body">
            <div class="wrapper clearfix">
                <div id="links">
                    <div>
                        <h4>Social</h4>
                        <ul>
                            <li>
                                <a href="http://freewebsitetemplates.com/go/googleplus/" target="_blank">Google +</a>
                            </li>
                            <li>
                                <a href="http://freewebsitetemplates.com/go/facebook/" target="_blank">Facebook</a>
                            </li>
                            <li>
                                <a href="http://freewebsitetemplates.com/go/youtube/" target="_blank">Youtube</a>
                            </li>
                        </ul>
                    </div>
                    <div>
                        <h4>Heading placeholder</h4>
                        <ul>
                            <li>
                                <a href="index.html">Link Title 1</a>
                            </li>
                            <li>
                                <a href="index.html">Link Title 2</a>
                            </li>
                            <li>
                                <a href="index.html">Link Title 3</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div id="newsletter">
                    <h4>Newsletter</h4>
                    <p>
                        Sign up for Our Newsletter
                    </p>
                    <form action="index.html" method="post">
                        <input type="text" value="">
                        <input type="submit" value="Sign Up!">
                    </form>
                </div>
                <p class="footnote">
                    © Copyright © 2023.Company name all rights reserved
                </p>
            </div>
        </div>

		
	</div>
<!--     <div class="body">
        <div class="wrapper clearfix">
            
        </div>
    <div> -->
	

</body>
</html>
