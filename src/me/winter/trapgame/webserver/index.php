<?php
if(!isset($_POST['action']))
	return;

if($_POST['action'] == 'update')
{
	$servers = "";
	
	if(file_exists("serverlist"))
		$servers = file_get_contents("serverlist");
	
	$data = "time=" . time() . "&ip=" . $_SERVER['REMOTE_ADDR'];
	$i = 0;
	
	foreach($_POST as $key => $value)
	{
		if($i > 0)
			$data.= "&" . $key . "=" . $value;
		$i++;
	}
	
	foreach(preg_split("/(\n)/", $servers) as $line)
	{
		$ip = preg_split("/=/", preg_split("/&/", $line)[1])[1];
		
		if(preg_match("/" . preg_quote($_SERVER['REMOTE_ADDR'], "/"). "/", $ip))
			$servers = str_replace($line, "", $servers);
	}
	
	$servers = preg_replace("/(\n){2,}/", "\n", $servers);
	
	$servers .= $data;
	
	file_put_contents("serverlist", $servers);
}
else if($_POST['action'] == 'query')
{
	if(!file_exists("serverlist"))
		return;
	
	$servers = file_get_contents("serverlist");
	
	foreach(preg_split("/(\n)/", $servers) as $line)
	{
		$time = preg_split("/=/", preg_split("/&/", $line)[0])[1];
		
		if(time() - $time > 10)
			$servers = str_replace($line, "", $servers);
	}
	
	$servers = preg_replace("/(\n){2,}/", "\n", $servers);
	
	file_put_contents("serverlist", $servers);
	echo $servers;
}
?>