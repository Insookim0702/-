<?php
 header('Content-type : bitmap; charset=utf-8');
$con = Mysqli_connect('localhost','root','1234','TH');
if(Mysqli_connect_errno($con)){
  echo "실패";
}else{
  //echo "데이터베이스 접속 성공"
    //if(isset($_POST['encoded_string'])){
      $encoded_string = $_POST['encoded_string'];
      $image_name = $_POST['image_name'];
      $decoded_string = base64_decode($encoded_string);
      $path ='image/'.$image_name;
      $file = fopen($path, 'wb');
      $is_written = fwrite($file, $decoded_string);
      fclose($file);

      if($is_written >0){
            $sql = "INSERT INTO photos values(?,?,?);";
            $statement = mysqli_prepare($con,$sql);
            $var =2;
            mysqli_stmt_bind_param($statement,"iss",$var,$image_name, $path);
            mysqli_stmt_execute($statement);
            $response = array();
            $response["success"] =true;
            if($result){
              echo "success";
        }else{
            echo "false";
        }
        mysqli_close($con);
    }
    //회원 가입 성공을 알려주기 위한 부분
    echo json_encode($response);
  //}
}
?>
