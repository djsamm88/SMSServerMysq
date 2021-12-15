<?php
header('Content-Type: application/json'); 
$db = new mysqli("localhost","root","","sms_gateway_local");


if(!isset($_GET['action']))
{
	echo json_encode(array("error"=>"1","msg"=>"Variable action required"));
	die();
}

if($_GET['action']=='list')
{
	$data_outbox = $db->query("SELECT * FROM data_outbox");
	$out = array();
	while($data = $data_outbox->fetch_object())
	{
		array_push($out, $data);
	}
	echo json_encode($out);
}
else if($_GET['action']=='list_terbaru')
{
	$data_outbox = $db->query("SELECT * FROM data_outbox WHERE status='new'");
	$out = array();
	while($data = $data_outbox->fetch_object())
	{
		array_push($out, $data);
	}
	echo json_encode($out);

}else if($_GET['action']=='update')
{
	if(!isset($_GET['id']))
	{
		echo json_encode(array("error"=>"1","msg"=>"Variable id required for Update"));
		die();
	}
	$id = $_GET['id'];
	$db->query("UPDATE data_outbox SET status='sent' WHERE id='$id'");

	$out = array('id'=>$id);
	echo json_encode($out);


}else if($_GET['action']=='delete')
{
	if(!isset($_GET['id']))
	{
		echo json_encode(array("error"=>"1","msg"=>"Variable id required for Delete"));
		die();
	}

	$id = $_GET['id'];
	$db->query("DELETE FROM data_outbox WHERE id='$id'");

	$out = array('id'=>$id);
	echo json_encode($out);


}