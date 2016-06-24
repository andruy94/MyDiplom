<!DOCTYPE HTML>
<?php 
$a=1;
ec

// получение таблицы PIC|Points 
//мы тут можем получить всё что нам надо: User_id,Pic_id
$sql = "SELECT TPoints.Points,TPictures.FileName FROM `TUsers`,`TPoints`,`TPictures` WHERE TPoints.User_id=1 AND TUsers.Login=\'Admin\' AND TPictures.Pics_id=TPoints.Pic_id";
$sql = "SELECT TPictures.Pics_id,TPictures.FileName,TUsers.id,TPoints.Points FROM TPictures,TPoints,TUsers WHERE TUsers.Login=\"Admin\" AND \n"
    . "TPictures.Pics_id=TPoints.Pic_id";
//Всьавка очка
//но как мы получим User_id,Pic_id,???
$sql = "INSERT INTO `TPoints` (`P_id`, `Points`, `Pic_id`, `User_id`) VALUES (NULL, \'0\', \'1\', \'2\')";
//Регистраия юзера
$sql = "INSERT INTO `TUsers` (`id`, `Login`, `Password`, `Email`, `Gold`) VALUES (NULL, \'User0\', \'user\', \'user@user.com\', \'0\')";
//редактирование Points, как получить Points id???
$sql = "UPDATE `TPoints` SET `Points` = \'66\' WHERE `TPoints`.`P_id` = 1";

?>
<?php
//первым делом сделаем протокл синхронизации GSP(Game Sync Protocol) работает при включение игры или принудительно (очень тяжелый)
//input $login,$password, $Pics-id=array(),$Points=array
<php?
	include 'connect_to_bd.php';
	
	$mysql="SELECT TUsers.id From TUsers WHERE TUsers.Login=".$login." TUsers.Password=".$password." ;";
    $qr_result = mysql_query($mysql) or die('что-то пошло не так друг:('.mysql_error());
	
	//получаем $TUsers-id иначе сворачиваемся и посылаем код ошибки
	// $TUsers-id// наш псевдо токен:)
	while($data = mysql_fetch_array($qr_result)){// просто пока рисуем
		for ($i=0;$i<count($data);$i++)
			echo $data[$i]
		}
	}
	
	
	for($i=0;$i<$Points;$i++ ){// обновим всё это
		$sql = "UPDATE TPoints SET Points =".$Points[i]." WHERE TPoints.Pic_id=".$Pics-id." AND TPoints.U_id=".$TUsers-id." ;"; 
		
	}//вообще тут можно сделать изи синхро протокол SGSP(Small Game Protocol)

	$sql = "SELECT TPoints.Points,TPictures.FileName,Pictures.URL FROM TPoints,TPictures WHERE TPoints.User_id=".$TUsers-id." AND TPictures.Pics_id=TPoints.Pic_id";
	while(query что-то там){
	$Pictures->filename
	...//врятли тут будем проверять на валидность фотки
	$Pictures->URl=взять строку из query
	}
	echo json_encode($Pictures)

php?> 
	
