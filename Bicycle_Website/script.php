<?php 
function convert_multi_array($array) 
{
	  $out = implode("&",array_map(function($a) {return implode("~",$a);},$array));
	  return $out;
}
if (isset($_FILES['file']))
{
	$staticOptions=$_POST['staticOptions'];
	$array= json_decode($staticOptions,true);

	$inputFile=$_FILES["file"]["tmp_name"];

	$cityName=$array["cityName"];
	$typeOfFile=$array["typeOfFile"];
	$key=$array["key"];
	$iterator=$array["iterator"];
	$arrayMappings=$array["arrayMappings"];
	$stringMappings=convert_multi_array($arrayMappings);

	// echo $stringMappings;

	$dynamicOptions=$_POST['dynamicOptions'];
	$arrayDynamic=json_decode($dynamicOptions,true);

	$linkFileDynamic=$arrayDynamic["linkFile"];
	$typeOfFileDynamic=$arrayDynamic["typeOfFile"];
	$keyDynamic=$arrayDynamic["key"];
	$iteratorDynamic=$arrayDynamic["iterator"];
	$arrayMappingsDynamic=$arrayDynamic["arrayMappings"];
	$stringMappingsDynamic=convert_multi_array($arrayMappingsDynamic);

	// echo $stringMappingsDynamic;

	$fileNameDynamic="dynamic.json";
	file_put_contents($fileNameDynamic, file_get_contents($linkFileDynamic));



	$javaCommandStatic= 'java -jar eu.wdaqua.semanticscholar-1.0-SNAPSHOT.jar -c "' . $cityName . '" -t "' . $typeOfFile . '" -k "' . $key . '" -i "' . $iterator . '" -a "' . $stringMappings . '" -f "' . $inputFile . '" ';
	echo exec($javaCommandStatic,$outputStatic);
	sleep (1);
	$javaCommandDynamic= 'java -jar eu.wdaqua.semanticscholar-1.0-SNAPSHOT.jar -c "' . $cityName . '" -t "' . $typeOfFileDynamic . '" -k "' . $keyDynamic . '" -i "' . $iteratorDynamic . '" -a "' . $stringMappingsDynamic . '" -f "' . $fileNameDynamic . '" -u "' . $linkFileDynamic . '" ';
	echo exec($javaCommandDynamic,$outputDynamic);
}
else
{
	$allOptions=$_POST['allOptions'];
	$array= json_decode($allOptions,true);

	$cityName=$array["cityName"];
	$typeOfFileDynamic=$array["typeOfFile"];
	$keyDynamic=$array["key"];
	$iteratorDynamic=$array["iterator"];
	$arrayMappingsDynamic=$array["arrayMappings"];
	$stringMappingsDynamic=convert_multi_array($arrayMappingsDynamic);
	$linkFileDynamic=$array["linkFile"];
	// echo $stringMappingsDynamic;

	$fileNameDynamic="dynamic.json";
	file_put_contents($fileNameDynamic, file_get_contents($linkFileDynamic));

	// echo $linkFileDynamic;

	$javaCommandDynamic= 'java -jar eu.wdaqua.semanticscholar-1.0-SNAPSHOT.jar -c "' . $cityName . '" -t "' . $typeOfFileDynamic . '" -k "' . $keyDynamic . '" -i "' . $iteratorDynamic . '" -a "' . $stringMappingsDynamic . '" -f "' . $fileNameDynamic . '" -u "' . $linkFileDynamic . '" ';
	// echo $javaCommandDynamic;
	echo exec($javaCommandDynamic,$outputDynamic);
	// echo "Success";

}

?> 