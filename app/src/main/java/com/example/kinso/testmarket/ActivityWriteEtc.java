package com.example.kinso.testmarket;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * Created by kinso on 2019-01-17.
 */

public class ActivityWriteEtc extends AppCompatActivity{
    private TextView CategoryText;
    //레이아웃 id
    private LinearLayout Status_Layout;
    //버튼 id
    private ImageButton Btn_cam;

    //이미지뷰
    private ImageView camview1;
    private ImageView camview2;
    private ImageView camview;
    //라디오그룹
    private RadioGroup RG_Status;
    //데이터베이스로 가는 id:Data파일타입...
    private String DataCategory;                    //1 . 등록 제품 카테고리
    private String DataWriteNum;                    //2. 글 등록 번호 (Primary key)
    private String DataUserId;                  //3.  등록 유저ID
    private String Date;                         //4. 글 쓴 날짜
    private String Status;                      //5. 제품 상태
    private String DataTitle;                       //6. 제품 제목
    private String DataPrice;                       //7. 제품 가격
    private String DataDescription;                 //8. 제품 설명
    private String ImgName1;                  //9. 제품 이미지 이름 1
    private String ImgName2;                  //10. 제품 이미지 이름 2
    private String ImgName3;                  //11. 제품 이미지 이름 3
    private String DataEncoded_imageString1;        //12. 인코딩된 이미지1
    private String DataEncoded_imageString2;        //13. 인코딩된 이미지2
    private String DataEncoded_imageString3;        //14. 인코딩된 이미지3

    //이미지 처리
    private String image_name;
    private File emulfile;
    private File imagepath; //이미지 최종 경로
    private File f; //절대 경로
    private Uri imageUri;
    private String encoded_string;
    private AsyncWriteDecodeImg WriteImgDecodeAsync;

    //
    private String resultstatus;
    //JSON id
    JSONObject GetUserInfoJson;
    //에딧텍스트
    private EditText ED_WriteTitle;
    private EditText ED_Price;
    private EditText ED_WriteDescription;

    private Handler mHandler;
    private ProgressDialog mProgressDialog;

    private int no =1;
    String res;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_write_etc);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("중고거래 등록하기(2/3)");
        ab.setDisplayHomeAsUpEnabled(true);
        CategoryText = (TextView)findViewById(R.id.CategoryText);
        ED_WriteTitle = (EditText)findViewById(R.id.ED_WriteTitle);
        ED_Price = (EditText)findViewById(R.id.ED_Price);
        ED_WriteDescription =(EditText)findViewById(R.id.ED_WriteDescription);
        Status_Layout =(LinearLayout)findViewById(R.id.Status_Layout);


        //0. 카테고리
        Intent getintent = getIntent();
        DataCategory=getintent.getExtras().getString("category");
        CategoryText.setText(DataCategory);
        //1. 등록 번호 /등록자 닉네임/등록일
        ClassJsonInfoUser uji = new ClassJsonInfoUser();
        GetUserInfoJson=new JSONObject();
        try {
            GetUserInfoJson = uji.userjsoninfo();
            DataWriteNum=GetUserInfoJson.getString("writeno");
            ClassSqlite classSqlite = new ClassSqlite(this);
            DataUserId = classSqlite.select();
            Date = GetUserInfoJson.getString("today");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //2. 사진
        dispatchTakePictureIntent();

        //3. 물건 상태
        if(DataCategory.equals("삽니다.")||
                DataCategory.equals("일 할 사람 구해요.")||
                DataCategory.equals("방 비어요.")||
                DataCategory.equals("과외/클래스/스터디 모집")||
                DataCategory.equals("꿀팁")||
                DataCategory.equals("전시/공연/행사")){
            Status_Layout.setVisibility(View.INVISIBLE);
        }else{
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
        //데이터베이스에 Btn_Submit
        Button Btn_Submit = (Button)findViewById(R.id.Btn_Submit);
        Btn_Submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ClassAlertDialog classAlertDialog = new ClassAlertDialog(ActivityWriteEtc.this);

                //1. 카테고리 픽
                System.out.println("1. 카테고리 : "+DataCategory);
                //2. 등록 번호 /등록자 닉네임/등록일
                System.out.println("2. 등록 번호 : "+DataWriteNum);
                System.out.println("3. 등록자 ID : "+DataUserId);
                System.out.println("4. 등록 날짜 : "+Date);
                //5. 등록 글 정보 제목/가격/코멘트
                DataTitle= ED_WriteTitle.getText().toString();
                DataPrice= ED_Price.getText().toString();
                DataDescription = ED_WriteDescription.getText().toString();
                String p ="^[0-9]*$";
                boolean bool = Pattern.matches(p,DataPrice);
                if(DataTitle =="" ||DataTitle.length()<4){
                    classAlertDialog.dialogmethod("글 제목","글 제목을 입력해 주세요.(최소 4자)");
                }else if(DataTitle.length()>100){
                    classAlertDialog.dialogmethod("글 제목","제목은 100자 이내로 입력해주세요.");
                }else if(DataPrice.length()==0||DataPrice.length()<3){
                    classAlertDialog.dialogmethod("가격","가격을 입력해 주세요.(최소 3자리)");
                }else if(bool==false){
                    classAlertDialog.dialogmethod("가격(원)입니다.","숫자로 7자리까지 입력할 수 있습니다.");
                }else if(DataDescription.length()>=300){
                    classAlertDialog.dialogmethod("글 수 초과", "300자 이내로 적어주세요.");
                }else{
                    System.out.println("5. 등록 제목 : "+DataTitle);
                    System.out.println("6. 등록 가격 : "+DataPrice);
                    System.out.println("7. 등록 설명 : "+DataDescription);
                    //System.out.println("***************************DataEncoded_imageString1의 값은 : "+DataEncoded_imageString1);
                    //4. 물건 상태
                    Status= resultstatus;
                    //5. 이미지
                    if(ImgName3==null){
                        ImgName3 = "0";
                        DataEncoded_imageString3 ="0";
                        if(ImgName2==null){
                            ImgName2 = "0";
                            DataEncoded_imageString2 ="0";
                            if(ImgName1==null){
                                ImgName1 = "0";
                                DataEncoded_imageString1 ="0";
                            }
                        }
                    }
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("listener 접속 성공");
                            res =response;
                            Toast.makeText(ActivityWriteEtc.this, response, Toast.LENGTH_LONG).show();
                            System.out.println("====================================");
                            Log.i("tagconvertstr", response);  //-> php에서 반환되는 log를 보여준다.
                        }
                    };//Response.Listener 완료.
                    RequestWrite bwr = new RequestWrite(
                            DataCategory,
                            DataWriteNum,
                            DataUserId,
                            Date,
                            DataTitle,
                            "",
                            "",
                            "",
                            "",
                            "",
                            DataPrice,
                            "",
                            DataDescription,
                            Status,
                            ImgName1, ImgName2, ImgName3,
                            DataEncoded_imageString1,
                            DataEncoded_imageString2,
                            DataEncoded_imageString3, responseListener);
                    /*RequestWriteEtc etcWriteRequest = new RequestWriteEtc(DataWriteNum, 
                                                                          Date,
                                                                          DataUserId, 
                                                                          DataCategory, 
                                                                          DataTitle, 
                                                                          DataPrice,  
                                                                          DataDescription, 
                                                                          Status,
                                                                          ImgName1, ImgName2, ImgName3, 
                                                                          DataEncoded_imageString1, DataEncoded_imageString2, DataEncoded_imageString3, 
                                                                          responseListener);*/
                    RequestQueue queue = Volley.newRequestQueue(ActivityWriteEtc.this);
                    queue.add(bwr);

                    mHandler = new Handler();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog = ProgressDialog.show(ActivityWriteEtc.this,"데이터 저장 중...", "잠시만 기다려 주세요.",true);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        if(mProgressDialog!=null&&mProgressDialog.isShowing()){
                                            mProgressDialog.dismiss();
                                        }
                                        Intent moveReadActivity = new Intent(ActivityWriteEtc.this,ActivityReadEtc.class);
                                        moveReadActivity.putExtra("DataWriteNum",DataWriteNum);
                                        moveReadActivity.putExtra("ActionBarName","등록 완료 (3/3)");
                                        ActivityWriteEtc.this.startActivity(moveReadActivity);
                                        finish();
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            },3000);
                        }
                    });
                }
            }//buttonclicklistener
        });
    }//메인 끝

    String mCurrentPhotoPath;
    Map map = new HashMap<>();

    //카메라에서 찍은 사진을 외부 저장소에 저장한다.
    private File createImageFile() throws IOException{
        //외부저장소 쓰기 권한을 얻어온다.
        PermissionRequester.Builder requester = new PermissionRequester.Builder(this);
        int result = requester.create().request(Manifest.permission.WRITE_EXTERNAL_STORAGE, 20000, new PermissionRequester.OnClickDenyButtonListener() {
            @Override
            public void onClick(Activity activity) {

            }
        });

        if(result == PermissionRequester.ALREADY_GRANTED||result == PermissionRequester.REQUEST_PERMISSION) { //사용자가 권한을 수락한 경우: Create an image file name
            //사진 파일의 이름을 만든다. Data는 java.util을 Import 한다.
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            Random random = new Random();
            image_name = "JPEG_"+timeStamp.substring(2,14) +random.nextInt(100);
            /*
            * 사진파일이 저장될 장소를 구한다. 외장메모리에서 사진을 저장하는 폴더를 찾아서
            * 그곳에 Tran_Helper이라는 폴더를 만든다.
            * */
            emulfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Tran_Helper");
            //pictureStorageDir 출력값 : /storage/emulated/0/Pictures/Tran_Helper
            //만약 장소가 존재하지 않으면 폴더를 새로 만든다.
            if(!emulfile.exists()){                                                                     //mkdir은 폴더를 하나 만들고,
                emulfile.mkdirs();                                                                      //mkdirs는 경로상에 존재하는 모든 폴더를 만들어 준다.
            }
            try{
                switch (no){
                    case 1:  ImgName1 = image_name;no++;break;
                    case 2:  ImgName2 = image_name; no++;break;
                    default: ImgName3  = image_name;
                }
                imagepath = File.createTempFile(image_name, ".jpg", emulfile);//imagepath의 출력값 : /storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004150_1971008652.jpg

                //ImageView에 보여주기 위해 사진파일의 절대 경로를 얻어온다.
                mCurrentPhotoPath =imagepath.getAbsolutePath();         //mCurrentPhotoPath의 출력값 : /storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004506_744591108.jpg
                //찍힌 사진을 "갤러리"앱에 추가한다.
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                //contentUri의 출력값 :  file:///storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004506_744591108.jpg
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                return imagepath;
            }catch (IOException e){
                Toast.makeText(ActivityWriteEtc.this, "파일을 저장하는 디렉토리에 저장할 수 없습니다.",Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        }
        return null;
    }

    static final int REQUEST_TAKE_PHOTO =1;
    static final int REQUEST_GALLERY =2;
    //1번으로 실행된다.
    private void dispatchTakePictureIntent(){
        Btn_cam =(ImageButton)findViewById(R.id.Btn_cam);
        Btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ActivityWriteEtc.this);

                alert.setPositiveButton("사진찍기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(isExistCameraApplication()){
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File photoFile = null;
                            if(takePictureIntent.resolveActivity(getPackageManager())!=null){
                                try{
                                    photoFile = createImageFile();
                                } catch (IOException e) {
                                    System.out.println("이미지를 저장할 파일을 만들지 못했습니다.");
                                    e.printStackTrace();
                                }
                                if(photoFile !=null){
                                    System.out.println("사진이 저장될 파일이 성공적으로 만들어짐.");
                                    Uri photoUri = FileProvider.getUriForFile(ActivityWriteEtc.this, "com.example.android.provider",photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                                }
                            }
                        }else{
                            Toast.makeText(ActivityWriteEtc.this, "카메라앱을 설치하세요",Toast.LENGTH_SHORT);
                        }
                    }
                });

                alert.setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ActivityWriteEtc.this, "갤러리", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent moveGalleryIntent = new Intent(Intent.ACTION_PICK);
                        File photoFile = null;
                        if (moveGalleryIntent.resolveActivity(getPackageManager()) != null) {
                            try {
                                photoFile = createImageFile();
                            } catch (IOException e) {
                                System.out.println("이미지를 저장할 파일을 만들지 못했습니다.");
                                e.printStackTrace();
                            }
                            if (photoFile != null) {
                                moveGalleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                moveGalleryIntent.setType("image/*");
                                System.out.println("사진이 저장될 파일이 성공적으로 만들어짐.");
                                Uri photoUri = FileProvider.getUriForFile(ActivityWriteEtc.this, "com.example.android.provider", photoFile);
                                moveGalleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(moveGalleryIntent, REQUEST_GALLERY);
                            }
                        }else{
                            Toast.makeText(ActivityWriteEtc.this, "갤러리 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.setMessage("사진을 올려주세요.");
                alert.show();
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
    Bitmap imageBitmap;
    File file;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ClassImageEdit ie = new ClassImageEdit();
        Intent intent = new Intent();
        camview = (ImageView)findViewById(R.id.camview);
        camview1 = (ImageView)findViewById(R.id.camview1);
        camview2 = (ImageView)findViewById(R.id.camview2);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_TAKE_PHOTO:
                    imageUri = Uri.parse(mCurrentPhotoPath);      //mCurrentPhotoPath의 출력값 : /storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004506_744591108.jpg
                    System.out.println("imageUri!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + imageUri);         ///storage/emulated/0/Pictures/Tran_Helper/JPEG_19021712224121956044468.jpg
                    System.out.println("mCurrentPhotoPath!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + mCurrentPhotoPath);///storage/emulated/0/Pictures/Tran_Helper/JPEG_190217122108721980331987.jpg
                    imageBitmap = sendPicture(imageUri, REQUEST_TAKE_PHOTO);
                    break;

                case REQUEST_GALLERY:
                    try{
                        System.out.println("ReQUEST_ GALLERY 에서 mCurrentPhotoPath >>>>>>>>>>>>" + mCurrentPhotoPath);
                        imageBitmap = sendPicture(data.getData(),REQUEST_GALLERY );
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
            ie.saveExifFile(imageBitmap, mCurrentPhotoPath);//3. 회전시킨 이미지 파일 저장.
            imageBitmap.recycle();
            imageUri = Uri.parse(mCurrentPhotoPath);
            file = new File(imageUri.getPath());
            try{
                InputStream ims = new FileInputStream(file);
                switch (count){
                    case 0:
                        camview.setImageBitmap(BitmapFactory.decodeStream(ims));
                        System.out.println(" 첫번째 이미지 파일을 전송하였습니다.");
                        count++;
                        break;
                    case 1:
                        camview1.setImageBitmap(BitmapFactory.decodeStream(ims));
                        System.out.println("두번째 이미지 파일을 전송하였습니다.");
                        count++;
                        break;
                    case 2:
                        camview2.setImageBitmap(BitmapFactory.decodeStream(ims));
                        System.out.println("세번째 이미지 파일을 전송하였습니다.");
                        count++;
                        break;
                    default:
                        Toast.makeText(ActivityWriteEtc.this, "3장만 업로드할 수 있습니다.",Toast.LENGTH_SHORT);
                }
            }catch(FileNotFoundException e){
                System.out.println("파일을 찾지 못했습니다.");
                return ;
            }
            WriteImgDecodeAsync= new AsyncWriteDecodeImg(imageUri.toString());
            try {
                encoded_string = WriteImgDecodeAsync.execute().get();
                if(DataEncoded_imageString1 ==null){
                    DataEncoded_imageString1 =encoded_string;
                }else if(DataEncoded_imageString1 !=null &&DataEncoded_imageString2==null){
                    DataEncoded_imageString2 =encoded_string;
                }else{
                    DataEncoded_imageString3 =encoded_string;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    private Bitmap sendPicture(Uri imgUri, int requestCode){
        ClassImageEdit ie = new ClassImageEdit();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        String imagePath ="";
        if(requestCode==REQUEST_GALLERY){
            imagePath = getRealPathFromURL(imgUri);
        }else{
            imagePath = imgUri.getPath();
        }
        ExifInterface exif = null;
        try{
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = ie.exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options); //경로를 통해 비트맵으로 전환
        return ie.rotate(bitmap,exifDegree);
    }
    private String getRealPathFromURL(Uri contentUri){
        int column_index =0;
        String[] data = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, data,null,null,null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
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


}
