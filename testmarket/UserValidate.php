<?php
$con = Mysqli_connect('localhost','root','1234','TH');
if(Mysqli_connect_errno($con)){
  echo "실패";
}
else{
//  echo "성공성공";
  $stdnum =$_POST['stdnum'];
  $statement = mysqli_prepare($con, "SELECT stdnum FROM rstudent WHERE stdnum =?");
  mysqli_stmt_bind_param($statement, "s", $stdnum);
  mysqli_stmt_execute($statement);
  mysqli_stmt_store_result($statement); //결과를 클라이언트에 저장함
  mysqli_stmt_bind_result($statement, $stdnum); //결과를 $stdnum에 바인딩함
  $response = array();
  $response["success"] =true;
  //mysqli_stmt_fetch() : 준비된 문장에서 연결된 변수에 결과를 가져온다.
  while(mysqli_stmt_fetch($statement)){
    $response["success"] = false; //회원가입 불가를 나타냄
    $response["stdnum"] = $stdnum; //비교
  }
  //데이터베이스 작업이 성공 혹은 실패한 것을 알려줌
  echo json_encode($response);
}

 ?>
