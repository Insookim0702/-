<?php
$con = Mysqli_connect('localhost','root','1234','TH');

if(Mysqli_connect_errno($con)){
  echo "실패";
}else{
  //echo "성공";
  //안드로이드 앱으로부터 아래 값들을 받음
  $stdnum = $_POST['stdnum'];
  $name = $_POST['userName'];
  $gender = $_POST['userGender'];
  $major = $_POST['userMajor'];
  $phone = $_POST['userphone'];
  $pww = $_POST['ch_pww'];
  //insert 쿼리문 실행
  $sql = "INSERT INTO rstudent VALUES(?,?,?,?,?,?)";
  //echo "INSERT INTO rstudent VALUES($stdnum,$name,$gender,$major,$phone,$pww)";
  //echo "\n";
  $statement = mysqli_prepare($con, $sql);
  mysqli_stmt_bind_param($statement, "ssssss", $stdnum, $name, $gender, $major, $phone, $pww);
  mysqli_stmt_execute($statement);
  $response = array();
  $response["success"] = true;
  //회원 가입 성공을 알려주기 위한 부분
  echo json_encode($response);
}
 ?>
