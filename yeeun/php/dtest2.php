<?php
	echo "hello!!";
    $body = file_get_contents('php://input');
	echo $body;
	//json data를 array 형시긍로 바꿔ㅈ준다.
	$array = json_decode($body,true);
	$date = $array['date'];
	$contents = $array['contents'];
	$time = $array['time'];
	echo $date;
	echo $contents;

	 $conn = mysqli_connect("ec2-54-180-86-219.ap-northeast-2.compute.amazonaws.com", "root", '1234');
	   if (mysqli_connect_errno($conn))  
    {  
       echo "Failed to connect to MySQL: " . mysqli_connect_error();  
    }  
    mysqli_set_charset($conn,"utf8");
 
    mysqli_select_db($conn, "d_test");
 	
    $query = "INSERT INTO table1 (date,contents,time) VALUES ('$date','$contents','$time')";
	mysqli_query($conn,$query);
	mysqli_close($conn);
?>