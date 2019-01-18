package com.example.kinso.testmarket;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.LinkAddress;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kinso on 2019-01-17.
 */

public class EtcWriteActivity extends AppCompatActivity{
    private TextView CategoryText;
    //레이아웃 id
    private LinearLayout Layout_Write_Info;
    private LinearLayout Camera_Layout;
    private LinearLayout Status_Layout;
    //버튼 id
    private Button Btn_Open_Camera_Layout;
    private ImageButton Btn_cam;
    private Button Btn_Open_Status_Layout;

    //이미지뷰
    private ImageView camview1;
    private ImageView camview2;
    private ImageView camview;
    //라디오그룹
    private RadioGroup RG_Status;
    //데이터베이스로 가는 id:Data파일타입...
    private String DataCategory; // 등록 제품 카테고리
    private String DataWriteNum; //글 등록 번호 (Primary key)
    private String DataJsonUserId; // 등록 유저ID
    private String DataDate;       //글 쓴 날짜
    private String DataJSonWriteInfo; //글 정보
    private String DataStatus;   //제품 상태
    private String DataUrl_img1; //제품 이미지1
    private String DataUrl_img2; //제품 이미지2
    private String DataUrl_img3; //제품 이미지3
    //이미지 처리
    private String encoded_string;
    private String image_name;
    private Bitmap bitmap;
    private File file;
    private File imagepath; //이미지 최종 경로
    private File f; //절대 경로
    private Uri imageUri;
    //
    private String resultstatus;
    //JSON id
    JSONObject GetWriteInfoJson;
    JSONObject GetUserInfoJson;
    //에딧텍스트
    private EditText ED_WriteTitle;
    private EditText ED_Price;
    private EditText ED_WriteDescription;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_write_etc);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("중고거래 등록하기(2/2)");
        ab.setDisplayHomeAsUpEnabled(true);
        CategoryText = (TextView)findViewById(R.id.CategoryText);
        ED_WriteTitle = (EditText)findViewById(R.id.ED_WriteTitle);
        ED_Price = (EditText)findViewById(R.id.ED_Price);
        ED_WriteDescription =(EditText)findViewById(R.id.ED_WriteDescription);
        Layout_Write_Info = (LinearLayout)findViewById(R.id.write_info_layout);
        Btn_Open_Status_Layout=(Button)findViewById(R.id.Btn_Open_Status_Layout);
        Btn_Open_Camera_Layout =(Button)findViewById(R.id.Btn_Open_Camera_Layout);
        Status_Layout =(LinearLayout)findViewById(R.id.Status_Layout);
        RG_Status = (RadioGroup)findViewById(R.id.RG_Status);



        Btn_Open_Camera_Layout = (Button)findViewById(R.id.Btn_Open_Camera_Layout);
        Btn_Open_Camera_Layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Camera_Layout= (LinearLayout)findViewById(R.id.Camera_Layout);
                Camera_Layout.setVisibility(View.VISIBLE);
                Btn_Open_Camera_Layout.setVisibility(View.INVISIBLE);
            }
        });
        //0. 카테고리
        Intent getintent = getIntent();
        CategoryText.setText(getintent.getExtras().getString("category"));
        DataCategory=getintent.getExtras().getString("category");

        dispatchTakePictureIntent();

        //물건 상태
        if(DataCategory.equals("삽니다.")||DataCategory.equals("일 할 사람 구해요.")||DataCategory.equals("방 비어요.")||DataCategory.equals("과외/클래스/스터디 모집")||
                DataCategory.equals("꿀팁")||DataCategory.equals("전시/공연/행사")){
            Status_Layout.setVisibility(View.INVISIBLE);
        }else{
            Btn_Open_Status_Layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Status_Layout = (LinearLayout) findViewById(R.id.Status_Layout);
                    Status_Layout.setVisibility(View.VISIBLE);
                    RG_Status =(RadioGroup)findViewById(R.id.RG_Status);
                    int statusID = RG_Status.getCheckedRadioButtonId();
                    resultstatus = ((RadioButton)findViewById(statusID)).getText().toString();
                    RG_Status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            resultstatus=((RadioButton)findViewById(i)).getText().toString();
                        }
                    });

                }
            });
            System.out.println("===================================" + resultstatus);


        }
        //데이터베이스에 Btn_BookSubmit
        Button Btn_Submit = (Button)findViewById(R.id.Btn_Submit);
        Btn_Submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //1. 카테고리 픽
                System.out.println("1. 카테고리 : "+DataCategory);

                //2. 등록 번호 /등록자 닉네임/등록일
                UserJsonInfo uji = new UserJsonInfo();
                GetUserInfoJson=new JSONObject();

                try {
                    GetUserInfoJson = uji.userjsoninfo();
                    DataWriteNum=GetUserInfoJson.getString("writeno");
                    DataJsonUserId = GetUserInfoJson.getString("userid");
                    DataDate = GetUserInfoJson.getString("today");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("2. 등록 번호 : "+DataWriteNum);
                System.out.println("3. 등록 번호 : "+DataJsonUserId);
                System.out.println("4. 등록 번호 : "+DataDate);
                //5. 등록 글 정보 제목/가격/코멘트
                try {
                    GetWriteInfoJson=new JSONObject();
                    GetWriteInfoJson.put("title",ED_WriteTitle.getText());
                    GetWriteInfoJson.put("price",ED_Price.getText());
                    GetWriteInfoJson.put("userdescription",ED_WriteDescription.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataJSonWriteInfo=GetWriteInfoJson.toString();
                System.out.println("3. 글 정보 : "+DataJSonWriteInfo);
                //4. 물건 상태
                DataStatus= resultstatus;
                System.out.println("4. 물건 상태 : " + DataStatus);
                //5. 이미지Json
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("listener 접속 성공");
                        Toast.makeText(EtcWriteActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.i("tagconvertstr", response);  //-> php에서 반환되는 log를 보여준다.
                        System.out.println("====================================");
                        Log.i("tagconvertstr", response);
                        System.out.println("====================================");
                    }
                };//Response.Listener 완료.

                //Volley 라이브러리를 써서 실제 서버와 통신을 구현하는 부분
                //파라미터 입력하는 순서대로 데이터베이스에 저장된다.
                EtcWriteRequest etcWriteRequest = new EtcWriteRequest(encoded_string, image_name, responseListener);
                RequestQueue queue = Volley.newRequestQueue(EtcWriteActivity.this);
                queue.add(etcWriteRequest);
            }
        });


    }
    //메인 끝
    String mCurrentPhotoPath;
    //카메라에서 찍은 사진을 외부 저장소에 저장한다.
    private File createImageFile() throws IOException{
        //외부저장소 쓰기 권한을 얻어온다.
        PermissionRequester.Builder requester = new PermissionRequester.Builder(this);
        int result = requester.create().request(Manifest.permission.WRITE_EXTERNAL_STORAGE, 20000, new PermissionRequester.OnClickDenyButtonListener() {
            @Override
            public void onClick(Activity activity) {

            }
        });

        //사용자가 권한을 수락한 경우: Create an image file name
        if(result == PermissionRequester.ALREADY_GRANTED||result == PermissionRequester.REQUEST_PERMISSION) {
            //사진 파일의 이름을 만든다. Data는 java.util을 Import 한다.
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            image_name = "JPEG_" + timeStamp + "_";
            /*
            * 사진파일이 저장될 장소를 구한다. 외장메모리에서 사진을 저장하는 폴더를 찾아서
            * 그곳에 Tran_Helper이라는 폴더를 만든다.
            * */
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Tran_Helper");
            //pictureStorageDir 출력값 : /storage/emulated/0/Pictures/Tran_Helper
            //만약 장소가 존재하지 않으면 폴더를 새로 만든다.
            if(!file.exists()){
                //mkdir은 폴더를 하나 만들고,
                //mkdirs는 경로상에 존재하는 모든 폴더를 만들어 준다.
                file.mkdirs();
            }
            try{
                imagepath = File.createTempFile(image_name, ".jpg", file);
                //image의 출력값 : /storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004150_1971008652.jpg
                //ImageView에 보여주기 위해 사진파일의 절대 경로를 얻어온다.
                mCurrentPhotoPath =imagepath.getAbsolutePath();
                //mCurrentPhotoPath의 출력값 : /storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004506_744591108.jpg

                //찍힌 사진을 "갤러리"앱에 추가한다.
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                //contentUri의 출력값 :  file:///storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004506_744591108.jpg
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                return imagepath;
            }catch (IOException e){
                Toast.makeText(EtcWriteActivity.this, "파일을 저장하는 디렉토리에 저장할 수 없습니다.",Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        }
        return null;
    }

    static final int REQUEST_TAKE_PHOTO =1;
    static final int REQUEST_GALLERY =2;
    //1번으로 실행된다.
    private void dispatchTakePictureIntent(){
        camview = (ImageView)findViewById(R.id.camview);
        camview1 = (ImageView)findViewById(R.id.camview1);
        camview2 = (ImageView)findViewById(R.id.camview2);
        Btn_cam =(ImageButton)findViewById(R.id.Btn_cam);
        Btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //2번. Camera Application이 있을 경우
                if(isExistCameraApplication()){
                    //Camera Application을 실행한다.
                    Intent takePictureIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //찍은 사진을 보관할 파일 객체를 만들어서 보낸다.
                    if(takePictureIntent.resolveActivity(getPackageManager())!=null){
                        //Create the File where the photo should go
                        File photoFile = null;
                        try{
                            photoFile = createImageFile();// 사진 저장 주소
                            //photoFile의 출력값 : /storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_005129_1104249842.jpg
                        }catch (IOException ex){
                            //Error occurred while createing the File
                            System.out.println("이미지를 저장할 파일을 만들지 못했습니다.");
                            Toast.makeText(EtcWriteActivity.this, "이미지 저장 못함",Toast.LENGTH_SHORT);
                        }
                        //Continue only if the File was successfully created
                        if(photoFile !=null){
                            System.out.println("dispatchTakePictureIntent(): 파일이 성공적으로 만들어졌다.");
                            Uri photoURI = FileProvider.getUriForFile(EtcWriteActivity.this, "com.example.android.provider",photoFile);
                            System.out.println("dispatchTakePictureIntent(): 사진이 저장될 주소 : "+photoURI);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);//EXTRA_OUTPUT을 통해서 저장할 파일을 지정해줘야 한다.
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                            System.out.println("dispatchTakePictureIntent(): 사진 찍기가 완료되었다. ->onActivityResult로 사진이 보내진다.");
                        }
                    }
                }else{
                    Toast.makeText(EtcWriteActivity.this,
                            "카메라 앱을 설치하세요.", Toast.LENGTH_SHORT);
                }
            }
        });
    }
    /*
     * 카메라 어플이 핸드폰에 있는지 확인
     * return카메라 앱이 있으면 true, 없으면 false
    */
    private boolean isExistCameraApplication(){
        //Android의 모든 Application을 얻어온다.
        PackageManager packageManager = getPackageManager();
        //Camera Application
        Intent camintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //MediaStore.ACTION_IMAGE_CAPTURE을 처리할 수 있는 APP정보를 가져온다.
        List cameraApps = packageManager.queryIntentActivities(camintent, PackageManager.MATCH_DEFAULT_ONLY);
        //카메라 App이 적어도 한개 이상 있는지 리턴
        return cameraApps.size() >0;
    }
    int count =0;
    //바코드 스캔한 인식 값 받아오는 코드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //사진찍기 버튼을 누른 후 잘 찍고 돌아왔다면,
        Bitmap bm;
        Intent intent = new Intent();
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_TAKE_PHOTO:
                    System.out.println("onActivityResult() : 시작");
                    imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());
                    ExifInterface exif = null;
                    try{
                        exif = new ExifInterface(mCurrentPhotoPath);
                        System.out.println("mCurrentPhotoPath : "+mCurrentPhotoPath);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    int exifOrientation;
                    int exifDegree;
                    if(exif!=null){
                        exifOrientation =exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        exifDegree = exifOrientationToDegrees(exifOrientation);
                    }else{
                        exifDegree =0;
                    }
                    try{
                        InputStream ims = new FileInputStream(file);
                        switch (count){
                            case 0:
                                camview.setImageBitmap(rotate(BitmapFactory.decodeStream(ims),exifDegree));
                                System.out.println(" 첫번째 이미지 파일을 전송하였습니다.");
                                count++;
                                break;
                            case 1:
                                camview1.setImageBitmap(rotate(BitmapFactory.decodeStream(ims),exifDegree));
                                System.out.println("두번째 이미지 파일을 전송하였습니다.");
                                count++;
                                break;
                            case 2:
                                camview2.setImageBitmap(rotate(BitmapFactory.decodeStream(ims),exifDegree));
                                System.out.println("세번째 이미지 파일을 전송하였습니다.");
                                break;
                            default:
                                Toast.makeText(EtcWriteActivity.this, "3장만 업로드할 수 있습니다.",Toast.LENGTH_SHORT);
                        }
                    }catch(FileNotFoundException e){
                        System.out.println("파일을 찾지 못했습니다.");
                        return ;
                    }
                    new Encode_image().execute();
                    //new Encode_image().execute();
                    break;
                case REQUEST_GALLERY:
                    try{
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        bm=resize(bm);
                        intent.putExtra("bitmap", bm);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    catch (OutOfMemoryError e){
                        Toast.makeText(getApplicationContext(), "이미지 용량이 크다.", Toast.LENGTH_SHORT);
                    }
                    setResult(RESULT_OK, intent);
                    break;
                default:
                    setResult(RESULT_CANCELED, intent);
                    break;

            }

        }
    }

    /*private void makeRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.10:80/HTphp/connection.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("====================================");
                Log.i("tagconvertstr", response);
                System.out.println("====================================");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("encoded_string", encoded_string);
                //System.out.println("데이터베이스에 저장되는 인코딩된 스트링 : "+encoded_string);
                map.put("image_name", image_name);
                System.out.println("데이터베이스에 저장되는 이미지 이름 : "+image_name);

                return map;

            }
        };
        requestQueue.add(request);
    }*/
    //데이터베이스에 넣을 이미지 크기 작게 하기(오류)
    private Bitmap resize(Bitmap bm){
        Configuration config = getResources().getConfiguration();
        if(config.smallestScreenWidthDp >=800){
            bm = Bitmap.createScaledBitmap(bm, 400, 240, true);
        }else if(config.smallestScreenWidthDp>=600){
            bm = Bitmap.createScaledBitmap(bm,300,180,true);
        }else if(config.smallestScreenWidthDp>=400){
            bm = Bitmap.createScaledBitmap(bm,200,120,true);
        }else if(config.smallestScreenWidthDp>=360){
            bm = Bitmap.createScaledBitmap(bm,180,108,true);
        }else{
            bm = Bitmap.createScaledBitmap(bm, 160,96,true);
        }
        return bm;
    }
    //이미지 사진 회전
    //상수를 받아 각도로 변환시켜주는 메소드
    private int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }
    //비트맵을 각도대로 회전시켜 결과를 반환해주는 메소드
    private Bitmap rotate(Bitmap bitmap, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix, true);
    }
    //액션바 뒤로가기 버튼 활성화
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class Encode_image extends  AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids){
            bitmap = BitmapFactory.decodeFile(imageUri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array,0);
            return null;
        }
    }
}
