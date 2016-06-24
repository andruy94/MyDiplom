<html>
<?php
error_reporting(0);// нам не нужны Warning!
class PictureDscrs{
	function __construct($id,$Url,$Answer,$Name,$Points,$Hint) {//конструктор для заполнения класса
		$this->id =$id;
		$this->Url=$Url;
		$this->Answer=$Answer;
		$this->Name=$Name;
		$this->Points=$Points;
        $this->Hint=$Hint;
		
	}
	public $id=array();
	public $Url;
	public $Answer;
	public $Name;
	public $Points;
	public $Hint;
	public $NonUser;//флаг указывающий на неправильный логи/пароль но при Г+ такого не должно быть вроде
}
if ($_SERVER['REQUEST_METHOD'] == 'POST') { //
/*
*POST-полное обновление с возвратом
*PUT-копееченое обновление
Дроид обновит в onActivityRsult()
*/
	$data = json_decode(file_get_contents("php://input")); 
	include "connect_to_bd.php";
	$login=$data->{"login"};// пашет=>можно всё что есть брать
	$password=$data->{"password"};      
	$User_id=getId($login,$password);
	if($User_id==-1) die;//по хорошему надо отдавать ответ Андроиду типо не тот логин или пароль, но по факту такого не будет
    Logger("Log.txt",$data->{"login"}.'_'.$data->{"password"},"POST",$User_id)//@имя файла,@данные для записи,@имя функции или что делалось, User_id
	update($data->{"Pics_id"},$data->{"Points"},$User_id);//синхронизуем
	$mysql = "SELECT TPoints1.Pic_id,TPoints1.Points,TPictures.FileName,TPictures.Url,TPictures.Hint,TPictures.Answer FROM TPoints1,TPictures WHERE TPoints1.User_id=".
	$User_id." AND TPictures.Pics_id=TPoints1.Pic_id;";
        $qr_result = mysql_query($mysql) or die('что-то пошло не так друг:('.mysql_error());
	//получаем $TUsers-id иначе сворачиваемся и посылаем код ошибки
	// $TUsers-id// наш псевдо токен:)
    $id;
	$Url;
	$Answer;
	$Name;
	$Points;
	$Hint;
	$i=0;
	while($data = mysql_fetch_array($qr_result)){// просто пока рисуем                 
		$id[$i]=$data["Pic_id"];
		$Url[$i]=$data["Url"];
		$Answer[$i]=$data["Answer"];
		if($id[$i]==$Pics_id[$i % count($Pics_id)]){// просто упорядочиваем всё по ID(на server точно будет ASC by id,Но на клиенте не факт)
			$Name[$i]=$data["FileName"];
		}
		else{
			$Name[$i]="null";
		}
		$Points[$i]=$data["Points"];
		$Hint[$i]=$data["Hint"];
        $i++;
        }
        $Json=new PictureDscrs($id,$Url,$Answer,$Name,$Points,$Hint);
        echo json_encode($Json);//отдаём наши пикчи
   //это потом 
    }else if ($_SERVER['REQUEST_METHOD'] == 'PUT'){
		include "connect_to_bd.php";
		$login=$data->{"login"};// пашет=>можно всё что есть брать
		$password=$data->{"password"};
		$mysql='SELECT TUsers.id From TUsers WHERE TUsers.Login="'.$login.'" AND TUsers.Password="'.$password.'" ;';
           $qr_result = mysql_query($mysql) or die('что-то пошло не так друг:('.mysql_error());
		//получаем $TUsers-id иначе сворачиваемся и посылаем код ошибки
		// $TUsers-id// наш псевдо токен:)
		$User_id=-1;
		while($data = mysql_fetch_array($qr_result)){
			$User_id= $data[0];
		}
		update($data->{"Pics_id"},$data->{"Points"},$User_id);//синхронизуем
	}
	//Можно упаковать в файл все функции и легко с ними работать в последствие
//-----------------------------------------------------
function update($Pics_id,$Points,$Users_id){// список id_пикчей,Очков, id user'a
	for($i=0;$i<count($Pics_id);$i++){
		$mysql = "UPDATE TPoints SET Points =".
			$Points[$i]." WHERE TPoints.Pic_id=".$Pics_id[$i]." AND TPoints.U_id=".$Users_id." ;";
		Logger("log.txt",$mysql,"update",User_id);//проверка на ошибку
		mysql_query($mysql) or die(mysql_error());//лучше писать ошибку в ЛОГ
	}
}
//---------------
function newUser($login,$password){// может быть не нужно!!!
	$mysql="INSERT INTO TUsers (id, Login, Password, Email, Gold) VALUES (NULL,'".
		$login."','" .$password."','" $login."@test.com', '0');"
	Logger("Log.txt",$mysql,"NewUser",)
	mysql_query($mysql) or die('Такой пользователь уже есть');
		$mysql="SELECT TPoints.Pic_id FROM TPoints;"
		$qr_result=mysql_query($mysql) or die('что-то пошло не так друг:('.mysql_error());//die=return
		$Pics_id;
		while($data=mysql_fetch_array($qr_result)){//получим все айди пикчей
			$Pics_id= $data["Pic_id"];
			$mysql="INSERT valuse bla-bla INTO TPoints"//нужен токен не готово!!!
			$qr_result=mysql_query($mysql) or die('что-то пошло не так друг:('.mysql_error());// обновим усё;
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
//---------------может и не надо
function NewPic($Url,$Answer,$Name,$Hint){//@адресс картинки,@Ответ,@Имя,@Подсказка 
	//INSERT PIC
	//SELECT все TUser.id и Pic_id
	//INSERT Points n/ 1..N (User,id)
}
//---------------
function Logger($file,$data,$func_name,$User_id){//@имя файла,@данные для записи,@имя функции или что делалось, 
	$current = file_get_contents($file);
	$current.=$User_id;
	$current.=$data.'|';
	$current.=$func_name.'|';
	$current.='<br/>';
	// Пишем содержимое обратно в файл
	file_put_contents($file, $current);
}
?> 
</html>