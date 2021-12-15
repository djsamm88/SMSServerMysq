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
	$data_inbox = $db->query("SELECT * FROM data_inbox");
	$out = array();
	while($data = $data_inbox->fetch_object())
	{
		array_push($out, $data);
	}
	echo json_encode($out);

}else if($_GET['action']=='simpan')
{
	if(!isset($_GET['pesan']) & !isset($_GET['nomor']) )
	{
		echo json_encode(array("error"=>"1","msg"=>"Variable nomor and pesan required for Sipan"));
		die();
	}

	$pesan = $_GET['pesan'];
	$nomor = $_GET['nomor'];
	$tanggal = date('Y-m-d H:i:s');

	
	$generated_id = $_GET['generated_id'];
	$generated_id = substr($generated_id, strpos($generated_id, "@") + 1);    



	$email = $_GET['email'];
	$db->query("INSERT INTO data_inbox SET status='sent', nomor='$nomor',pesan='$pesan',waktu='$tanggal',generated_id='$generated_id',email='$email'");

	$out = array('save'=>'1');
	echo json_encode($out);


}else if($_GET['action']=='delete')
{
	if(!isset($_GET['id']))
	{
		echo json_encode(array("error"=>"1","msg"=>"Variable id required for Delete"));
		die();
	}

	$id = $_GET['id'];
	$db->query("DELETE FROM data_inbox WHERE id='$id'");

	$out = array('id'=>$id);
	echo json_encode($out);


}