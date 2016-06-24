<?php 

 $db_host = 'fdb5.biz.nf';
 $db_name = '2059292_andruy94';
$db_username = '2059292_andruy94';
$db_password = '1233War1234';
session_start();
$connect_to_db = mysql_connect($db_host, $db_username, $db_password)
      or die("Could not connect: " . mysql_error());
$qr_result = mysql_query(" USE ".$db_name)
      or die("Random error".mysql_error());
?>