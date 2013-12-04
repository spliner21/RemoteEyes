<?php

if(!file_exists("media"))
{
	mkdir("media");
}

if(isset($_POST) && !empty($_POST) && isset($_FILES) && !empty($_FILES)) {
	$uploadfile1 = "";
	$uploadfile2 = "";
	//if(strstr($_FILES['file1']['type'],"audio") || strstr($_FILES['file1']['type'],"video") || strstr($_FILES['file1']['type'],"image")) 	//sprawdzenie typów mime
	//{	
		$uploadfile1 = "media/".basename($_FILES['file1']['name']);		//œcie¿ka do pliku na serwerze
		move_uploaded_file($_FILES['file1']['tmp_name'], $uploadfile1);	//przeniesienie pliku
	//}
	if(!empty($_FILES['file2']))// && (strstr($_FILES['file2']['type'],"audio") || strstr($_FILES['file2']['type'],"video") || strstr($_FILES['file2']['type'],"image"))) 	//sprawdzenie typów mime
	{	
		$uploadfile2 = "media/".basename($_FILES['file2']['name']);		//œcie¿ka do pliku na serwerze
		move_uploaded_file($_FILES['file2']['tmp_name'], $uploadfile2);	//przeniesienie pliku
	}
	$gpsdata = $_POST["gpsdata"];
	$senderid = $_POST["senderid"];
	
	$con = mysql_connect('localhost', 'remoteeyes', 'test');
	mysql_select_db('remoteeyes',$con);
	
	mysql_query("INSERT INTO messages (ID, file1path, file2path, date, gpsdata, senderid) VALUES ('', '".$uploadfile1."', '".$uploadfile2."', '".date ("d.m.Y G:i:s")."', '".$gpsdata."', '".$senderid."');");
	$id = mysql_insert_id();
	mysql_close($con);
	
	echo $id;
}