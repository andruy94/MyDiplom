<?php
function update($Pics_id,$Points,$Users_id){// список id_пикчей,Очков, id user'a
	for($i=0;$i<count($Pics_id);$i++){
		$mysql = "UPDATE TPoints1 SET Points =".
			$Points[$i]." WHERE TPoints1.Pic_id=".$Pics_id[$i]." AND TPoints1.User_id=".$Users_id." ;";
		Logger("Log.txt",$mysql,"update",$User_id);//проверка на ошибку
		mysql_query($mysql) or die(json_encode(mysql_error()));//лучше писать ошибку в ЛОГ
	}
}
//---------------
function newUser($login,$password){
	$mysql="INSERT INTO TUsers (id, Login, Password, Email, Gold) VALUES (NULL,'".
		$login."','" .$password."','". $login."@test.com', '0');";
	Logger("Log.txt",$mysql,"NewUser",getId($login,$password));
	mysql_query($mysql) or die(json_encode("error"));
		$mysql="SELECT * FROM TPictures;";
		$qr_result=mysql_query($mysql) or die('dead'.mysql_error());//die=return
		while($data = mysql_fetch_array($qr_result)){//получим все айди пикчей
			$Pic_id= $data["Pics_id"];
			$User_id=getId($login,$password);
			$mysql="INSERT INTO TPoints1 (P_id, Points, Pic_id, User_id) VALUES (NULL, '0', '".
			$Pic_id."', '".$User_id."')";
			Logger("Log.txt",$mysql,"newUser",$User_id);
			mysql_query($mysql) or die('sad'.mysql_error());// обновим усё;
		}
}
//---------------
function getId($login,$password){//return id,если что не так -1
	$mysql="SELECT TUsers.id From TUsers WHERE TUsers.Login='".
		$login."' AND TUsers.Password='".$password."' ;";
    $qr_result = mysql_query($mysql) or die('не правильный запрос');
	//получаем $TUsers-id иначе сворачиваемся и посылаем код ошибки
	// $TUsers-id// наш псевдо токен:)
	$User_id=-1;
	while($data = mysql_fetch_array($qr_result)){
			$User_id= $data[0];
	}
	return $User_id;
}

function NewPic($Url,$Answer,$Name,$Hint){//@адресс картинки,@Ответ,@Имя,@Подсказка 
	//INSERT PIC
	//SELECT все TUser.id и Pic_id
	//INSERT Points n/ 1..N (User,id)
}

function Logger($file,$data,$func_name,$User_id){//@имя файла,@данные для записи,@имя функции или что делалось, 
	$current = file_get_contents($file);
	$current.=$User_id.'|';
	$current.=$data.'|';
	$current.=$func_name.'|';
	$current.='\n';
	// Пишем содержимое обратно в файл
	file_put_contents($file, $current);
}
?>