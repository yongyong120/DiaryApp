<?php
    $text = "hello2";
	$date = $_POST['date'];
	$contents = $_POST['contents'];
	if(empty($date)){
           $errMSG = "전송되지 않았습니다.ㅠㅠ";
           $test = $errMSG;
    }
    else {echo "전송 성공";
	$test = "sucess";}
	echo $date;
	echo $contents;
	echo $text;

	$conn = mysqli_connect("ec2-54-180-86-219.ap-northeast-2.compute.amazonaws.com", "root", '1234');
 
    if (mysqli_connect_errno($conn))  
    {  
       echo "Failed to connect to MySQL: " . mysqli_connect_error();  
    }  
    mysqli_set_charset($conn,"utf8");
 
    mysqli_select_db($conn, "d_test");

 
	$query = "INSERT INTO table1 (date,contents) VALUES ('$date','$text')";
	mysqli_query($conn,$query);
	mysqli_close($conn);

?>