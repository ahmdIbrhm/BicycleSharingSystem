<html>
<?php
	
	$stations = $_POST['test'];

	$stations_array = json_decode($stations,true);

	/*foreach ($stations_array as $key => $value) {
		foreach ($value as $key_station => $mappings) {
			echo $key_station.' '.$mappings.'<br/>';
		}
		echo '<hr/>';
	}*/

?>

<head>
	
</head>
<body>
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
                    <a href="map.html">Map</a>
                </li>
                <li>
                    <a href="about.html">Statistics</a>
                </li>
                
                <li>
                    <a href="contact.html">Contact Us</a>
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
			<p style="font-size:160%;">Click on the markers viewed on the map to check bicycle stations information</p>
			</br>
			<div id="sidebar">
                <a id="btn_show" href="#" class="outlinedbtn" onclick="switchListener()">SHOW LIST</a>
                <br/>
                <br/>
            </div>
            <h2> Stations</h2>
            <div class="list-cards">

			  <div class="card rounded shadowed">
			    <div class="card-content">
			      <div class="card-title">
			        Card title
			      </div>
			      <div class="card-description">
			        Card description
			      </div>
			    </div>
			  </div>
			 </div>
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