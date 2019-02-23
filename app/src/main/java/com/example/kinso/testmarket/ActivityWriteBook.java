package com.example.kinso.testmarket;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/*
 * Created by kinso on 2019-01-11.
 */

public class ActivityWriteBook extends AppCompatActivity {
    private TextView CategoryText;
    private String isbn;
    private String res;
    private AlertDialog dialog;
    private Uri imageUri;
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
    private Button Btn_Open_BookStatus_Layout, Btn_BookSubmit;
    //등록자 info id
    private TextView Tx_WriteNumber;
    private TextView Tx_UserCode;
    private TextView write_date_text;
    private EditText Edt_user_description;
    private EditText Edt_Subject;
    private EditText Edt_price;
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
    private String DataCategory;         //1. 등록 제품 카테고리
    private String DataWriteNum;         //2. 등록 번호 (Primary key)
    private String DataUserId;           //3. 등록 유저 ID
    private String Date;             //4. 등록 날짜
    private String DataBcBookTitle;      //5. 책이름
    private String DataBcBookImageUrl;   //6. 책이미지url
    private String DataBcAuthor;         //7. 책 저자.
    private String DataBcPublisher;      //8. 책 출판사
    private String DataBcPrice;          //9. 책 가격
    private String DataBcDiscountPrice;   //10. 책 할인가격
    private String DataUserPrice;        //11. 유저가 등록한 가격
    private String DataSubjectName;      //12. 수강과목 명
    private String DataDescription;      //13. 등록자 코멘트
    private String Status;   //15. 책 상태
    private String ImgName1;         //16. 제품 이미지1
    private String ImgName2;         //17. 제품 이미지2
    private String ImgName3;         //18. 제품 이미지3
    private String DataEncoded_imageString1; //19. 이미지 디코딩
    private String DataEncoded_imageString2; //20. 이미지 디코딩
    private String DataEncoded_imageString3; //21. 이미지 디코딩

    private Handler mHandler;
    private ProgressDialog mProgressDialog;
    private int no = 1;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_write_book);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("중고도서 등록하기(2/3)");
        ab.setDisplayHomeAsUpEnabled(true);
        CategoryText = (TextView) findViewById(R.id.CategoryText);
        bookname = (TextView) findViewById(R.id.bookname);
        bookprice = (TextView) findViewById(R.id.bookprice);
        bookauthor = (TextView) findViewById(R.id.bookauthor);
        bookpublisher = (TextView) findViewById(R.id.bookpublisher);
        bookImage = (ImageView) findViewById(R.id.bookImage);
        Btn_Open_User_Info_Layout = (Button) findViewById(R.id.Btn_Open_User_Info_Layout);
        Btn_Open_BookStatus_Layout = (Button) findViewById(R.id.Btn_Open_BookStatus_Layout);
        Btn_BookSubmit = (Button) findViewById(R.id.Btn_BookSubmit);
        //1. 등록자 닉네임/등록일/등록번호/등록자코멘트
        Tx_WriteNumber = (TextView) findViewById(R.id.Tx_WriteNumber);
        Tx_UserCode = (TextView) findViewById(R.id.Tx_UserCode);
        write_date_text = (TextView) findViewById(R.id.write_date_text);
        Edt_user_description = (EditText) findViewById(R.id.Edt_user_description);
        Edt_Subject = (EditText) findViewById(R.id.Edt_Subject);
        Edt_price = (EditText) findViewById(R.id.Edt_price);
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
        Btn_Open_User_Info_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User_Info_Layout = (LinearLayout) findViewById(R.id.User_Info_Layout);
                User_Info_Layout.setVisibility(View.VISIBLE);
                Btn_Open_User_Info_Layout.setVisibility(View.GONE);
                Btn_Open_BookStatus_Layout.setVisibility(View.VISIBLE);
            }
        });
        Btn_Open_BookStatus_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookStatus_Layout = (LinearLayout) findViewById(R.id.BookStatus_Layout);
                BookStatus_Layout.setVisibility(View.VISIBLE);
                Btn_Open_BookStatus_Layout.setVisibility(View.GONE);
                Btn_BookSubmit.setVisibility(View.VISIBLE);

            }
        });

        //0. 카테고리 분류
        Intent getintent = getIntent();
        CategoryText.setText(getintent.getExtras().getString("category"));
        //1. 바코드 메소드
        if (getintent.getExtras().getString("category").equals("도서")) {
            DataCategory = "도서";
            Barcode_Layout = (LinearLayout) findViewById(R.id.barcode_layout);
            Barcode_Layout.setVisibility(View.VISIBLE);
            System.out.println("도서 카테고리가 픽되었습니다.");
            barcode_recognition();

        }
        dispatchTakePictureIntent();
        //2. 등록자 닉네임/등록일/등록번호
        try {
            ClassJsonInfoUser uji = new ClassJsonInfoUser();
            JSONObject getJson = uji.userjsoninfo();
            //Json 처리
            DataWriteNum = getJson.getString("writeno").toString();
            ClassSqlite classSqlite = new ClassSqlite(this);
            DataUserId = classSqlite.select();
            Date = getJson.getString("today");
            Tx_WriteNumber.setText("등록번호 : " + DataWriteNum);
            Tx_UserCode.setText("등록자 : " + DataUserId);
            write_date_text.setText("등록 날짜 : " + Date);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //데이터베이스에 Btn_BookSubmit
        Button Btn_BookSubmit = (Button) findViewById(R.id.Btn_BookSubmit);
        Btn_BookSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassAlertDialog classAlertDialog = new ClassAlertDialog(ActivityWriteBook.this);
                System.out.println("1. 카테고리 : " + DataCategory);
                System.out.println("2. 등록번호 : " + DataWriteNum);
                System.out.println("3. 등록 유저 ID : " + DataUserId);
                System.out.println("4. 등록 날짜 : " + Date);
                System.out.println("5. 책 이름 : " + DataBcBookTitle);
                System.out.println("6. 책 이미지URL : " + DataBcBookImageUrl);
                System.out.println("7. 책 저자 : " + DataBcAuthor);
                System.out.println("8. 책 출판사 : " + DataBcPublisher);
                System.out.println("9. (int)책 장상가 : " + DataBcPrice);
                System.out.println("10. (int)책 할인가 : " + DataBcDiscountPrice);
                DataUserPrice = Edt_price.getText().toString();
                System.out.println("11. (int)유저 등록가 : " + DataUserPrice);
                DataSubjectName = Edt_Subject.getText().toString();
                System.out.println("12. 수강과목명 : " + DataSubjectName);
                DataDescription = Edt_user_description.getText().toString();
                System.out.println("13. 등록 코멘트 : " + DataDescription);
                String p = "^[0-9]*$";
                boolean bool = Pattern.matches(p, DataUserPrice);
                if (DataUserPrice.length() == 0 || DataUserPrice.length() < 3) {
                    classAlertDialog.dialogmethod("가격", "가격을 입력해 주세요.(최소 3자리)");
                } else if (bool == false) {
                    classAlertDialog.dialogmethod("가격(원)입니다.", "숫자로 7자리까지 입력할 수 있습니다.");
                } else if (DataDescription.length() >= 300) {
                    classAlertDialog.dialogmethod("글 수 초과", "300자 이내로 적어주세요.");
                } else {
                    JsonInfoBookStatus bsji = new JsonInfoBookStatus();
                    Status = bsji.GetBookStatusJsonInfo(resultline, resultnote, resultcover, resultwritename, resultpage).toString();
                    System.out.println("14. 책 상태 : " + Status);
                    if (ImgName3 == null) {
                        ImgName3 = "0";
                        DataEncoded_imageString3 = "0";
                        if (ImgName2 == null) {
                            ImgName2 = "0";
                            DataEncoded_imageString2 = "0";
                            if (ImgName1 == null) {
                                ImgName1 = "0";
                                DataEncoded_imageString1 = "0";
                            }
                        }
                    }
                    System.out.println("15. 사진1 : " + ImgName1);
                    System.out.println("16. 사진2 : " + ImgName2);
                    System.out.println("17. 사진3 : " + ImgName3);
                    //1. 데이터베이스 통신
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("listener 성공!");
                            res = response;
                            Toast.makeText(ActivityWriteBook.this, response, Toast.LENGTH_LONG).show();
                            System.out.println("====================================");
                            Log.i("tagconvertstr", response);  //-> php에서 반환되는 log를 보여준다.
                            System.out.println("====================================");
                        }
                    };
                    //2. 화면 넘기기
                    RequestWrite bwr = new RequestWrite(
                            DataCategory,
                            DataWriteNum,
                            DataUserId,
                            Date,
                            DataBcBookTitle,
                            DataBcBookImageUrl,
                            DataBcAuthor,
                            DataBcPublisher,
                            DataBcPrice,
                            DataBcDiscountPrice,
                            DataUserPrice,
                            DataSubjectName,
                            DataDescription,
                            Status,
                            ImgName1,
                            ImgName2,
                            ImgName3,
                            DataEncoded_imageString1,
                            DataEncoded_imageString2,
                            DataEncoded_imageString3, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ActivityWriteBook.this);
                    queue.add(bwr);

                    mHandler = new Handler();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog = ProgressDialog.show(ActivityWriteBook.this, "데이터 입력 중 ..", "잠시만 기다려 주세요.", true);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                            mProgressDialog.dismiss();
                                        }
                                        Intent moveBookReadAct = new Intent(ActivityWriteBook.this, ActivityReadBook.class);
                                        moveBookReadAct.putExtra("DataWriteNum", DataWriteNum);
                                        moveBookReadAct.putExtra("ActionBarName", "등록 완료 (3/3)");
                                        ActivityWriteBook.this.startActivity(moveBookReadAct);
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 3000);
                        }
                    });//runOnUiThread

                }
            }
        });
    }

    private void barcode_recognition() {
        barcode_btn = (Button) findViewById(R.id.barcode_btn);
        barcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(ActivityWriteBook.this);
                integrator.setCaptureActivity(captureActivityVerticalOrientation.class);
                integrator.setOrientationLocked(false);//zxing을 세로화면으로 사용할 수 있게 하였다.
                integrator.initiateScan();
            }
        });
    }

    String mCurrentPhotoPath;

    //카메라에서 찍은 사진을 외부 저장소에 저장한다.
    private File createImageFile() throws IOException {
        //외부저장소 쓰기 권한을 얻어온다.
        PermissionRequester.Builder requester = new PermissionRequester.Builder(this);
        int result = requester.create().request(Manifest.permission.WRITE_EXTERNAL_STORAGE, 20000, new PermissionRequester.OnClickDenyButtonListener() {
            @Override
            public void onClick(Activity activity) {

            }
        });

        //사용자가 권한을 수락한 경우: Create an image file name
        if (result == PermissionRequester.ALREADY_GRANTED || result == PermissionRequester.REQUEST_PERMISSION) {
            //사진 파일의 이름을 만든다. Data는 java.util을 Import 한다.
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            Random random = new Random();
            String imageFileName = "JPEG_" + timeStamp.substring(2, 13) + random.nextInt(100);
            switch (no) {
                case 1:
                    ImgName1 = imageFileName;
                    no++;
                    break;
                case 2:
                    ImgName2 = imageFileName;
                    no++;
                    break;
                default:
                    ImgName3 = imageFileName;
            }
            /*
             * 사진파일이 저장될 장소를 구한다. 외장메모리에서 사진을 저장하는 폴더를 찾아서
             * 그곳에 Tran_Helper이라는 폴더를 만든다.
             */
            File pictureStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Tran_Helper");
            //pictureStorageDir 출력값 : /storage/emulated/0/Pictures/Tran_Helper

            if (!pictureStorageDir.exists()) {   //만약 장소가 존재하지 않으면 폴더를 새로 만든다.
                //mkdir은 폴더를 하나 만들고,
                //mkdirs는 경로상에 존재하는 모든 폴더를 만들어 준다.
                pictureStorageDir.mkdirs();
            }
            try {
                File image = File.createTempFile(imageFileName, ".jpg", pictureStorageDir);
                //image의 출력값 : /storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004150_1971008652.jpg
                mCurrentPhotoPath = image.getAbsolutePath();
                //mCurrentPhotoPath의 출력값 : /storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004506_744591108.jpg

                //찍힌 사진을 "갤러리"앱에 추가한다.
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f); //contentUri의 출력값 :  file:///storage/emulated/0/Pictures/Tran_Helper/JPEG_20190113_004506_744591108.jpg
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                return image;
            } catch (IOException e) {
                Toast.makeText(ActivityWriteBook.this, "파일을 저장하는 디렉토리에 저장할 수 없습니다.", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        }
        return null;
    }

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY = 2;

    //1번으로 실행된다.
    private void dispatchTakePictureIntent() {

        ImageButton cambtn = (ImageButton) findViewById(R.id.cambtn);
        cambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ActivityWriteBook.this);
                alert.setPositiveButton("사진찍기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isExistCameraApplication()) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {//Intent를 실행할 수 있는 앱이 있는 경우
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    Toast.makeText(ActivityWriteBook.this, "이미지 저장 못함", Toast.LENGTH_SHORT);
                                }
                                if (photoFile != null) {
                                    Uri photoURI = FileProvider.getUriForFile(ActivityWriteBook.this, "com.example.android.provider", photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                                }
                            } else {
                                Toast.makeText(ActivityWriteBook.this, "카메라 앱을 설치하세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                alert.setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ActivityWriteBook.this, "갤러리", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent moveGalleryIntent = new Intent(Intent.ACTION_PICK);
                        File photoFile = null;
                        if (moveGalleryIntent.resolveActivity(getPackageManager()) != null) {
                            try {
                                photoFile = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (photoFile != null) {
                                //갤러리 앱으로 이동
                                moveGalleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                moveGalleryIntent.setType("image/*"); //이미지 형태만 가져온다.
                                Uri photoUri = FileProvider.getUriForFile(ActivityWriteBook.this, "com.example.android.provider", photoFile);
                                moveGalleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(moveGalleryIntent, REQUEST_GALLERY);
                            }
                        } else {
                            Toast.makeText(ActivityWriteBook.this, "갤러리 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alert.setMessage("사진을 올려주세요.");
                alert.show();
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            //데이터를 받는 쪽
            Bundle bun = msg.getData();
            String receive = bun.getString("DATA");
            Bitmap bmp = bun.getParcelable("BitmapImage");
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(receive);
                DataBcBookTitle = jsonObj.getString("title");
                DataBcBookImageUrl = jsonObj.getString("image");
                DataBcPrice = jsonObj.getString("price");
                DataBcPublisher = jsonObj.getString("publisher");
                DataBcAuthor = jsonObj.getString("author");
                DataBcDiscountPrice = jsonObj.getString("discount");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            bookname.setText(DataBcBookTitle);
            bookpublisher.setText("출판사  : " + DataBcPublisher);
            bookauthor.setText("저자   : " + DataBcAuthor);
            bookprice.setText("정상 가격 : " + DataBcPrice + " (할인가 : " + DataBcDiscountPrice + ")");
            bookImage.setImageBitmap(bmp);
        }
    };

    public void callbackThread(Bitmap bm, String res) {
        Message msg = handler.obtainMessage();
        Bundle bun = new Bundle();
        bun.putString("DATA", res);
        bun.putParcelable("BitmapImage", bm);
        msg.setData(bun);
        handler.sendMessage(msg); //메인 스레드 신호 전달
    }

    int count = 0;
    Bitmap imageBitmap;
    File file;

    //바코드 스캔한 인식 값 받아오는 코드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camera_layout = (LinearLayout) findViewById(R.id.Camera_Layout);
        Layout_BookInfo = (LinearLayout) findViewById(R.id.Layout_BookInfo);
        camview = (ImageView) findViewById(R.id.camview);
        camview1 = (ImageView) findViewById(R.id.camview1);
        camview2 = (ImageView) findViewById(R.id.camview2);
        ClassImageEdit ie = new ClassImageEdit();

        if (resultCode == RESULT_OK && requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            isbn = result.getContents();
            if (isbn != null) {
                Layout_BookInfo.setVisibility(View.VISIBLE);
                ClassNaverApi th1 = new ClassNaverApi(isbn, this);
                th1.start();
                Intent write = new Intent(ActivityWriteBook.this, ActivityWriteBook.class);
                write.putExtra("ISBN", isbn);
                setResult(RESULT_OK, write);
                camera_layout.setVisibility(View.VISIBLE);
                Btn_Open_User_Info_Layout.setVisibility(View.VISIBLE);
                barcode_btn.setVisibility(View.GONE);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityWriteBook.this);
                dialog = builder.setTitle("책 바코드 검색")
                        .setMessage("바코드를 다시 인식시켜주세요.")
                        .setPositiveButton("OK", null)
                        .create();
                dialog.show();
            }

        }
        //사진찍기 버튼을 누른 후 잘 찍고 돌아왔다면,
        Bitmap bm;
        Intent intent = new Intent();
        if (resultCode == RESULT_OK &&(requestCode==REQUEST_TAKE_PHOTO || requestCode ==REQUEST_GALLERY)) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    imageUri = Uri.parse(mCurrentPhotoPath);
                    imageBitmap = sendPicture(imageUri, REQUEST_TAKE_PHOTO);
                    break;
                case REQUEST_GALLERY:
                    try {
                        imageBitmap = sendPicture(data.getData(), REQUEST_GALLERY);
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(ActivityWriteBook.this, "이미지 용량이 큽니다. ", Toast.LENGTH_SHORT).show();
                    }
                    setResult(RESULT_OK, intent);
                    break;

                default:
                    setResult(RESULT_CANCELED, intent);
                    break;
            }
            ie.saveExifFile(imageBitmap, mCurrentPhotoPath);
            imageBitmap.recycle();// 비트맵 메모리 반환
            imageUri = Uri.parse(mCurrentPhotoPath);
            file = new File(imageUri.getPath());

            try {
                InputStream ims = new FileInputStream(file);
                switch (count) {
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
                        break;
                    default:
                        Toast.makeText(ActivityWriteBook.this, "3장만 업로드할 수 있습니다.", Toast.LENGTH_SHORT);
                }
                AsyncWriteDecodeImg bwa = new AsyncWriteDecodeImg(imageUri.toString());
                try {
                    String encoded_string = bwa.execute().get();
                    if (DataEncoded_imageString1 == null) {
                        DataEncoded_imageString1 = encoded_string;
                    } else if (DataEncoded_imageString1 != null && DataEncoded_imageString2 == null) {
                        DataEncoded_imageString2 = encoded_string;
                    } else {
                        DataEncoded_imageString3 = encoded_string;
                    }
                    System.out.println("res >>>>>>>>>>>>>>>>>>>>>>>>>>>>" + encoded_string.substring(0, 9));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                System.out.println("파일을 찾지 못했습니다.");
                return;
            }


        }

    }

    private Bitmap sendPicture(Uri imgUri, int requestCode) {
        ClassImageEdit ie = new ClassImageEdit();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        String imagePath = "";
        if (requestCode == REQUEST_GALLERY) {
            imagePath = getRealPathFromURL(imgUri);
        } else {
            imagePath = imgUri.getPath();
        }
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = ie.exifOrientationToDegrees(exifOrientation);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        return ie.rotate(bitmap, exifDegree);
    }

    private String getRealPathFromURL(Uri imgUri) {
        int column_index = 0;
        String[] data = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imgUri, data, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    //데이터베이스에 넣을 이미지 크기 작게 하기(오류)
    private Bitmap resize(Bitmap bm) {
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 800) {
            bm = Bitmap.createScaledBitmap(bm, 400, 240, true);
        } else if (config.smallestScreenWidthDp >= 600) {
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        } else if (config.smallestScreenWidthDp >= 400) {
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        } else if (config.smallestScreenWidthDp >= 360) {
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        } else {
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
        }
        return bm;
    }

    /*
     * 카메라 어플이 핸드폰에 있는지 확인
     * return카메라 앱이 있으면 true, 없으면 false
    */
    private boolean isExistCameraApplication() {
        //Android의 모든 Application을 얻어온다.
        PackageManager packageManager = getPackageManager();
        //Camera Application
        Intent camintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //MediaStore.ACTION_IMAGE_CAPTURE을 처리할 수 있는 APP정보를 가져온다.
        List cameraApps = packageManager.queryIntentActivities(camintent, PackageManager.MATCH_DEFAULT_ONLY);
        //카메라 App이 적어도 한개 이상 있는지 리턴
        return cameraApps.size() > 0;
    }

    //액션바 뒤로가기 버튼 활성화
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
