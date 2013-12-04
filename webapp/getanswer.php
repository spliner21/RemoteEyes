<?php

if(isset($_GET['id']))
{
	$con = mysql_connect('localhost', 'remoteeyes', 'test');
	mysql_select_db('remoteeyes',$con);
	
	
	$result = mysql_query("SELECT 'answerpath' FROM 'messages' WHERE 'id' = ".$_GET['id']."");
	if($result) {
		$answer = mysql_result($result,0,"answerpath");
		
		mysql_close($con);
		return $_SERVER["SERVER_NAME"].dirname($_SERVER['PHP_SELF']).'/'.$answer;
	} else {
		mysql_close($con);
		return false;
	}

}