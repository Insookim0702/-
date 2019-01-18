<?php
  $con = mysqli_connect('localhost','root', '1234','TH');
  //안드로이드 앱으로부터 받는 값들
  $stdnum = $_POST['stdnum'];
  $userPassword =$_POST['userPassword'];

  $sql = "SELECT stdnum FROM rstudent WHERE stdnum= ? AND pww = ?";
  $statement = mysqli_prepare($con, $sql);
  mysqli_stmt_bind_param($statement,"ss", $stdnum, $userPassword);
  mysqli_stmt_execute($statement);
  mysqli_stmt_store_result($statement);
  mysqli_stmt_bind_result($statement, $stdnum);

$response = array();
$response["success"] = false;
 //echo "select stdnum from rstudent where stdnum = $stdnum and pww =$userPassword";

while (mysqli_stmt_fetch($statement)) {
  // code...
  $response["success"] = true;
  $response["stdnum"] = $stdnum;
}

echo json_encode($response);

 ?>
