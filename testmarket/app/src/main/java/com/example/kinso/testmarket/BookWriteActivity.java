package com.example.kinso.testmarket;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
import java.util.List;

/*
 * Created by kinso on 2019-01-11.
 */

public class BookWriteActivity extends AppCompatActivity{
    private TextView CategoryText;
    private String isbn;
    private String res;
    private AlertDialog dialog;
    //레이아웃 id
    private LinearLayout User_Info_Layout;
    private LinearLayout BookStatus_Layout;
    private LinearLayout Barcode_Layout;
    private LinearLayout Write_Info_Layout;
    private LinearLayout Camera_Layout;
    private LinearLayout Status_Layout;
    //바코드 인식 책 정보 id
    private TextView bookname;
    private TextView bookprice;
    private TextView bookpublisher;
    private TextView bookauthor;
    private ImageView bookImage;
    //찍은 사진 id
    private ImageView camview;
    private ImageView camview1;
    private ImageView camview2;
    private LinearLayout camera_layout;
    private LinearLayout Layout_BookInfo;

    private Button barcode_btn;
    private Button Btn_Open_User_Info_Layout;
    private Button Btn_Open_BookStatus_Layout;
    //등록자 info id
    private TextView write_no_text;
    private TextView user_nick_text;
    private TextView write_date_text;
    private EditText Edt_user_description;
    private EditText Edt_Subject;
    //책 상태 id
    private RadioGroup cover_rg;
    private RadioGroup line_rg;
    private RadioGroup note_rg;
    private RadioGroup writename_rg;
    private RadioGroup page_rg;
    private String resultline;
    private String resultnote;
    private String resultcover;
    private String resultpage;
    private String resultwritename;
    //물건 상태 id
    private RadioGroup RG_Status;
    private String resultstatus;
    //JSON id

    //데이터베이스로 가는 id:Data파일타입...
    private String DataCategory; // 등록 제품 카테고리
    private String DataWriteNum; //등록 번호 (Primary key)
    private String DataJsonUserId; //등록 유저 ID
    private String DataDate      ; //등록 날짜
    private String DataDescription; // 등록자 코멘트
    private String DataSubjectName; // 수강과목 명
    private String DataJsonBarcodeInfo; //도서 카테고리, 도서 테이블에 들어가는 인식된 책 정보
    private String DataJsonBookStatus;  //책 상태
    private String DataUrl_img1; //제품 이미지1
    private String DataUrl_img2; //제품 이미지2
    private String DataUrl_img3; //제품 이미지3
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_write_book);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("중고거래 등록하기(2/2)");
        ab.setDisplayHomeAsUpEnabled(true);
        CategoryText = (TextView)findViewById(R.id.CategoryText);
        bookname = (TextView)findViewById(R.id.bookname);
        bookprice = (TextView)findViewById(R.id.bookprice);
        bookauthor = (TextView)findViewById(R.id.bookauthor);
        bookpublisher = (TextView)findViewById(R.id.bookpublisher);
        bookImage = (ImageView) findViewById(R.id.bookImage);
        Btn_Open_User_Info_Layout=(Button)findViewById(R.id.Btn_Open_User_Info_Layout);
        Btn_Open_BookStatus_Layout=(Button)findViewById(R.id.Btn_Open_BookStatus_Layout);
        //1. 등록자 닉네임/등록일/등록번호/등록자코멘트
        write_no_text = (TextView)findViewById(R.id.write_no_text);
        user_nick_text= (TextView)findViewById(R.id.user_nick_text);
        write_date_text= (TextView)findViewById(R.id.write_date_text);
        Edt_user_description= (EditText)findViewById(R.id.Edt_user_description);
        Edt_Subject =(EditText)findViewById(R.id.Edt_Subject);
        //2. 책상태 : 밑줄/필기/겉표지/이름기입.페이지 훼손
        cover_rg = (RadioGroup) findViewById(R.id.cover_rg);
        line_rg = (RadioGroup) findViewById(R.id.line_rg);
        note_rg = (RadioGroup) findViewById(R.id.note_rg);
        page_rg = (RadioGroup) findViewById(R.id.page_rg);
        writename_rg = (RadioGroup) findViewById(R.id.write_rg);

        int lineID = line_rg.getCheckedRadioButtonId();
        int coverID = cover_rg.getCheckedRadioButtonId();
        int noteID = note_rg.getCheckedRadioButtonId();
        int pageID = page_rg.getCheckedRadioButtonId();
        int writenameID = writename_rg.getCheckedRadioButtonId();
        resultline = ((RadioButton) findViewById(lineID)).getText().toString();
        resultnote = ((RadioButton) findViewById(coverID)).getText().toString();
        resultcover = ((RadioButton) findViewById(noteID)).getText().toString();
        resultpage = ((RadioButton) findViewById(pageID)).getText().toString();
        resultwritename = ((RadioButton) findViewById(writenameID)).getText().toString();
        line_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                resultline = ((RadioButton) findViewById(i)).getText().toString();
            }
        });
        note_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                resultnote = ((RadioButton) findViewById(i)).getText().toString();
            }
        });
        cover_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                resultcover = ((RadioButton) findViewById(i)).getText().toString();
            }
        });
        page_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                resultpage = ((RadioButton) findViewById(i)).getText().toString();
            }
        });
        writename_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                resultwritename = ((RadioButton) findViewById(i)).getText().toString();
            }
        });

        //다음 버튼
        Btn_Open_User_Info_Layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                User_Info_Layout = (LinearLayout)findViewById(R.id.User_Info_Layout);
                User_Info_Layout.setVisibility(View.VISIBLE);
                Btn_Open_User_Info_Layout.setVisibility(View.INVISIBLE);
            }
        });
        Btn_Open_BookStatus_Layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    BookStatus_Layout = (LinearLayout)findViewById(R.id.BookStatus_Layout);
                    BookStatus_Layout.setVisibility(View.VISIBLE);
                    Btn_Open_BookStatus_Layout.setVisibility(View.INVISIBLE);
            }
        });

        //0. 카테고리 분류
        Intent getintent = getIntent();
        CategoryText.setText(getintent.getExtras().getString("category"));
        //1. 바코드 메소드
        if(getintent.getExtras().getString("category").equals("도서")){
            DataCategory="도서";
            Barcode_Layout=(LinearLayout)findViewById(R.id.barcode_layout);
            Barcode_Layout.setVisibility(View.VISIBLE);
            System.out.println("도서 카테고리가 픽되었습니다.");
            barcode_recognition();

        }
        dispatchTakePictureIntent();
        //2. 등록자 닉네임/등록일/등록번호
        try {
            UserJsonInfo uji = new UserJsonInfo();
            //Json 처리
            DataWriteNum = uji.userjsoninfo().getString("writeno").toString();
            DataJsonUserId = uji.userjsoninfo().getString("userid");
            DataDate = uji.userjsoninfo().getString("today");
            write_no_text.setText("등록번호 : "+DataWriteNum);
            user_nick_text.setText("등록자 : "+DataJsonUserId);
            write_date_text.setText("등록 날짜 : " + DataDate);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //데이터베이스에 Btn_BookSubmit
        Button Btn_BookSubmit = (Button)findViewById(R.id.Btn_BookSubmit);
        Btn_BookSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //0. 카테고리 픽
                System.out.println("1. 카테고리 : "+DataCategory);
                //1. 등록번호
                System.out.println("2. 등록번호 : " + DataWriteNum);
                //2. 등록자 닉네임/등록일/등록자코멘트
                System.out.println("3. 등록 유저 ID : "+DataJsonUserId);
                System.out.println("4. 등록 날짜 : "+DataDate);
                //5. 바코드 인식 책 정보 title/price/author/publisher/imageUrl
                    System.out.println("5. 바코드 인식 도서 정보 : "+DataJsonBarcodeInfo);
                //6. 책상태 : 밑줄/필기/겉표지/이름기입.페이지 훼손
                    BookStatusJsonInfo bsji = new BookStatusJsonInfo();
                    DataJsonBookStatus = bsji.GetBookStatusJsonInfo(resultline, resultnote, resultcover, resultwritename, resultpage).toString();
                    System.out.println("6. 책 상태 : "+DataJsonBookStatus);
                //7. 등록자 코멘트
                //8. 사용되는 수업
                DataSubjectName = Edt_Subject.getText().toString();
                System.out.println("7. 수강과목명 : " + DataSubjectName);
                //9. 사용자 멘트
                DataDescription = Edt_user_description.getText().toString();
                System.out.println("8. 등록 코멘트 : " + DataDescription);
                // 이미지Json
            }
        });

    }

    private void barcode_recognition(){
        barcode_btn = (Button)findViewById(R.id.barcode_btn);
        barcode_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                IntentIntegrator integrator = new IntentIntegrator(BookWriteActivity.this);
                integrator.setCaptureActivity(captureActivityVerticalOrientation.class);
                integrator.setOrientationLocked(false);//zxing을 세로화면으로 사용할 수 있게 하였다.
                integrator.initiateScan();
            }
        });
    }

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
            String imageFileName = "JPEG_" + timeStamp + "_";
            /*
            * 사진파일이 저장될 장소를 구한다. 외장메모리에서 사진을 저장하는 폴더를 찾아서
            * 그곳에 Tran_Helper이라는 폴더를 만든다.
            * */
            //원래 이거 : File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File pictureStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Tran_Helper");
            //pictureStorageDir 출력값 : /storage/emulated/0/Pictures/Tran_Helper
            //만약 장소가 존재하지 않으면 폴더를 새로 만든다.
            if(!pictureStorageDir.exists()){
                //mkdir은 폴더를 하나 만들고,
                //mkdirs는 경로상에 존재하는 모든 폴더를 만들어 준다.
                pictureStorageDir.mkdirs();
            }
            try{
                File image = File.createTempFile(imageFileName, ".jpg", pictureStorageDir);
                //image의 출력값 : /storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004150_1971008652.jpg
                //ImageView에 보여주기 위해 사진파일의 절대 경로를 얻어온다.
                mCurrentPhotoPath =image.getAbsolutePath();
                //mCurrentPhotoPath의 출력값 : /storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004506_744591108.jpg

                //찍힌 사진을 "갤러리"앱에 추가한다.
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                //contentUri의 출력값 :  file:///storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004506_744591108.jpg
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                return image;
            }catch (IOException e){
                Toast.makeText(BookWriteActivity.this, "파일을 저장하는 디렉토리에 저장할 수 없습니다.",Toast.LENGTH_SHORT);
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
        ImageButton cambtn =(ImageButton)findViewById(R.id.cambtn);
        cambtn.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(BookWriteActivity.this, "이미지 저장 못함",Toast.LENGTH_SHORT);
                        }
                        //Continue only if the File was successfully created
                        if(photoFile !=null){
                            System.out.println("dispatchTakePictureIntent(): 파일이 성공적으로 만들어졌다.");
                            Uri photoURI = FileProvider.getUriForFile(BookWriteActivity.this, "com.example.android.provider",photoFile);
                            System.out.println("dispatchTakePictureIntent(): 사진이 저장될 주소 : "+photoURI);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);//EXTRA_OUTPUT을 통해서 저장할 파일을 지정해줘야 한다.
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                            System.out.println("dispatchTakePictureIntent(): 사진 찍기가 완료되었다. ->onActivityResult로 사진이 보내진다.");
                        }
                    }
                }else{
                    Toast.makeText(BookWriteActivity.this,
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
    Handler handler = new Handler(){
        public void handleMessage(Message msg){

            //데이터를 받는 쪽
            Bundle bun = msg.getData();
            String receive = bun.getString("DATA");
            Bitmap bmp = bun.getParcelable("BitmapImage");
            JSONObject jsonObj = null;
            String title="";
            String price="";
            String publisher="";
            String author="";
            String discount="";

            try {
                jsonObj = new JSONObject(receive);
                title = jsonObj.getString("title");
                price = jsonObj.getString("price");
                publisher = jsonObj.getString("publisher");
                author = jsonObj.getString("author");
                discount = jsonObj.getString("discount");
                DataJsonBarcodeInfo =receive;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            bookname.setText(title);
            bookpublisher.setText("  출판사  : "+publisher);
            bookauthor.setText("   저자   : "+author);
            bookprice.setText("정상 가격 : "+price + "(할인가 : "+ discount+")");
            bookImage.setImageBitmap(bmp);
        }
    };
    public void callbackThread(Bitmap bm,String res){
        Message msg = handler.obtainMessage();
        Bundle bun = new Bundle();
        bun.putString("DATA", res);
        bun.putParcelable("BitmapImage", bm);
        msg.setData(bun);
        handler.sendMessage(msg); //메인 스레드 신호 전달
    }
    int count =0;
    //바코드 스캔한 인식 값 받아오는 코드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        camera_layout = (LinearLayout)findViewById(R.id.Camera_Layout);
        Layout_BookInfo =(LinearLayout)findViewById(R.id.Layout_BookInfo);
        if(requestCode == IntentIntegrator.REQUEST_CODE){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            isbn = result.getContents();
            //Toast.makeText(this,"출력 ISBN: " + result.getContents(), Toast.LENGTH_SHORT).show();
            //Log.i("TAG", ">>> result.getContents() :" + result.getContents());
            if(isbn !=null){
                Layout_BookInfo.setVisibility(View.VISIBLE);
                NaverApi th1 = new NaverApi(isbn, this);
                th1.start();
                Intent write = new Intent(BookWriteActivity.this, BookWriteActivity.class);
                write.putExtra("ISBN", isbn);
                setResult(RESULT_OK,write);

                camera_layout.setVisibility(View.VISIBLE);
                barcode_btn.setVisibility(View.GONE);
            }else{
                AlertDialog.Builder builder= new AlertDialog.Builder(BookWriteActivity.this);
                dialog = builder.setTitle("책 바코드 검색")
                        .setMessage("바코드를 다시 인식시켜주세요.")
                        .setPositiveButton("OK",null)
                        .create();
                dialog.show();

            }

        }
        //사진찍기 버튼을 누른 후 잘 찍고 돌아왔다면,
        Bitmap bm;
        Intent intent = new Intent();
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_TAKE_PHOTO:
                    System.out.println("onActivityResult() : 시작");
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
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
                                Toast.makeText(BookWriteActivity.this, "3장만 업로드할 수 있습니다.",Toast.LENGTH_SHORT);
                        }
                    }catch(FileNotFoundException e){
                        System.out.println("파일을 찾지 못했습니다.");
                        return ;
                    }
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


}
