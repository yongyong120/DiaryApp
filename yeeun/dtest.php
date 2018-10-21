<?php
    
    $conn = mysqli_connect("ec2-54-180-86-219.ap-northeast-2.compute.amazonaws.com", "root", '1234');
 
    if (mysqli_connect_errno($conn))  
    {  
       echo "Failed to connect to MySQL: " . mysqli_connect_error();  
    }  
    mysqli_set_charset($conn,"utf8");
 
    mysqli_select_db($conn, "d_test");
 
    $res = mysqli_query($conn,"select * from table1");  
 
    $result = array();  
       
    while($row = mysqli_fetch_array($res)){  
      array_push($result,  
        array('date' =>$row[0], 'contents'=>$row[1]
        ));  
    }  
       
    echo json_encode(array("result"=>$result));  
       
    mysqli_close($conn);  
    
?>
