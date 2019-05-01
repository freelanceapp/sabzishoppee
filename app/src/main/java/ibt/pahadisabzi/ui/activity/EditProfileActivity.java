package ibt.pahadisabzi.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import ibt.pahadisabzi.R;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.model.User;
import ibt.pahadisabzi.model.login_responce.LoginModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.AppPreference;
import ibt.pahadisabzi.utils.BaseActivity;
import ibt.pahadisabzi.utils.ConnectionDirector;
import ibt.pahadisabzi.utils.MyStringRandomGen;
import ibt.pahadisabzi.utils.PermissionUtility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static ibt.pahadisabzi.ui.activity.HomeActivity.iv_ShowUserImage;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_fullname, et_email_address;
    private TextView et_address, et_dob,et_email, et_mobile;
    private RadioGroup rb_gender;
    private CircleImageView ci_profile;
    private ImageView btn_camera, btn_editprofile_back;
    private ImageView btn_calender;
    private Button btnUpdate;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private static final String IMAGE_DIRECTORY = "/sabzishoppe";
    private File file;
    MyStringRandomGen myStringRandomGen;
    Bitmap decodedByte;
    RadioGroup rgGendar;
    String strGender = "";
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String profileImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        init();
    }

    private void init() {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        et_fullname = findViewById(R.id.et_fullname);
        et_email = findViewById(R.id.et_email_address);
        et_address = findViewById(R.id.et_address);
        et_mobile = findViewById(R.id.et_mobile);
        et_dob = findViewById(R.id.et_dob);
        btn_calender = findViewById(R.id.btn_calender);
        ci_profile = findViewById(R.id.ci_profile);
        btn_camera = findViewById(R.id.btn_camera);
        btnUpdate = findViewById(R.id.btnUpdate);
        rgGendar = findViewById(R.id.rgGender1);
        btn_editprofile_back = findViewById(R.id.btn_editprofile_back);
        myStringRandomGen = new MyStringRandomGen();
        btn_camera.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btn_editprofile_back.setOnClickListener(this);
        signUpApi();
        setDateTimeField();
        rgGendar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    //   Toast.makeText(mContext, rb.getText(), Toast.LENGTH_SHORT).show();
                    strGender = rb.getText().toString();
                }
            }
        });

        btn_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
    }


    private void setRadioFunction(int radioA, int radioB, int radioC, int radioD) {
        ((RadioButton) findViewById(radioA)).setChecked(true);
        ((RadioButton) findViewById(radioB)).setChecked(false);
        ((RadioButton) findViewById(radioC)).setChecked(false);
        ((RadioButton) findViewById(radioD)).setChecked(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera:
                selectImage();
                break;
            case R.id.btnUpdate :
                api();
                break;
            case R.id.btn_editprofile_back :
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;

            case PermissionUtility.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();

                } else {
                    //code for deny
                }
                break;
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = PermissionUtility.checkPermission(EditProfileActivity.this);
                boolean result1 = PermissionUtility.checkPermission1(EditProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result1)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ci_profile.setImageBitmap(thumbnail);
        saveImage(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ci_profile.setImageBitmap(bm);
        saveImage(bm);
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        Log.e("File", wallpaperDirectory.getPath());
        Log.e("wallpaperDirectory", "..." + wallpaperDirectory.exists());
        Log.e("wallpaperDirectory", "===" + wallpaperDirectory.mkdirs());
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            file = new File(wallpaperDirectory, myStringRandomGen.generateRandomString() + "_sabzeProfile.jpg");
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(mContext, new String[]{file.getPath()},
                    new String[]{"image/*"}, null);
            fo.close();
            Log.e("TAG", "File Saved::--->" + file.getAbsolutePath());

            return file.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public void api() {
        String strUserId = AppPreference.getStringPreference(mContext, Constant.User_Id);
        String strName = et_fullname.getText().toString();
        String strEmail = et_email.getText().toString();
        String strDob = et_dob.getText().toString();
        Log.e("strUserId", "..." + strUserId);
            if (file == null) {
                if (profileImage.equals("")) {
                    Toast.makeText(mContext, "Select Image", Toast.LENGTH_SHORT).show();
                } else {
                    String base64String = profileImage;
                    String base64Image = base64String.split(",")[1];
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    saveImage(decodedByte);
                    if (cd.isNetWorkAvailable()) {
                        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("user_profile_picture", file.getName(), mFile);
                        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), strUserId);
                        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), strGender);
                        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), strName);
                        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), strDob);
                        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), strEmail);
                        RetrofitService.getUpdateProfile(new Dialog(mContext), retrofitApiClient.profileimage(id, gender, name, dob, email, fileToUpload), new WebResponse() {
                            @Override
                            public void onResponseSuccess(Response<?> result) {
                                LoginModel loginModal = (LoginModel) result.body();
                                assert loginModal != null;
                                Gson gson = new GsonBuilder().setLenient().create();
                                String data = gson.toJson(loginModal);
                                AppPreference.setStringPreference(mContext, Constant.User_Data, data);
                                User.setUser(loginModal);
                                if (User.getUser().getUser().getUserProfilePicture() == null) {
                                    iv_ShowUserImage.setImageResource(R.drawable.ic_user);
                                } else {
                                    Glide.with(mContext).load(loginModal.getUser().getUserProfilePicture()).error(R.drawable.ic_user).into(iv_ShowUserImage);
                                }
                                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onResponseFailed(String error) {
                                Alerts.show(mContext, error);
                            }
                        });
                    } else {
                        cd.show(mContext);
                    }
                }
            } else {
                if (cd.isNetWorkAvailable()) {
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("user_profile_picture", file.getName(), mFile);
                    RequestBody id = RequestBody.create(MediaType.parse("text/plain"), strUserId);
                    RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), strGender);
                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"), strName);
                    RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), strDob);
                    RequestBody email = RequestBody.create(MediaType.parse("text/plain"), strEmail);
                    RetrofitService.getUpdateProfile(new Dialog(mContext), retrofitApiClient.profileimage(id, gender, name, dob, email, fileToUpload), new WebResponse() {
                        @Override
                        public void onResponseSuccess(Response<?> result) {
                            LoginModel loginModal = (LoginModel) result.body();
                            assert loginModal != null;
                            Gson gson = new GsonBuilder().setLenient().create();
                            String data = gson.toJson(loginModal);
                            AppPreference.setStringPreference(mContext, Constant.User_Data, data);
                            User.setUser(loginModal);
                           /* if (User.getUser().getUser().getUserProfilePicture() == null) {
                                iv_ShowUserImage.setImageResource(R.drawable.ic_user);
                            } else {
                                Glide.with(mContext).load(loginModal.getUser().getUserProfilePicture()).error(R.drawable.ic_user).into(iv_ShowUserImage);
                                String base64String = loginModal.getUser().getUserProfilePicture();
                                String base64Image = base64String.split(",")[1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                saveImage(decodedByte);
                            }*/
                            Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onResponseFailed(String error) {
                            Alerts.show(mContext, error);
                        }
                    });
                } else {
                    cd.show(mContext);
                }

        }
    }



    private void signUpApi() {

        String userId = AppPreference.getStringPreference(mContext , Constant.User_Id);
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getProfile(new Dialog(mContext), retrofitApiClient.getprofile(userId), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    LoginModel responseBody = (LoginModel) result.body();

                    if (!responseBody.getError())
                    {
                        et_fullname.setText(responseBody.getUser().getUserName());
                        et_email.setText(responseBody.getUser().getUserEmail());
                        et_mobile.setText(responseBody.getUser().getUserContact());
                        et_dob.setText(responseBody.getUser().getUserDateOfBirth());
                        //tv_address.setText(responseBody.getUser().get());

                        if (responseBody.getUser().getUserGender().equalsIgnoreCase("Male")) {
                            setRadioFunction(R.id.rb_male, R.id.rb_female, R.id.rb_other, R.id.rb_other);
                        } else if (responseBody.getUser().getUserGender().equalsIgnoreCase("Female")) {
                            setRadioFunction(R.id.rb_female, R.id.rb_male, R.id.rb_other, R.id.rb_other);
                        } else if (responseBody.getUser().getUserGender().equalsIgnoreCase("Other")) {
                            setRadioFunction(R.id.rb_other, R.id.rb_female, R.id.rb_male, R.id.rb_male);
                        } else {
                            ((RadioButton) findViewById(R.id.rb_male)).setChecked(false);
                            ((RadioButton) findViewById(R.id.rb_female)).setChecked(false);
                            ((RadioButton) findViewById(R.id.rb_male)).setChecked(false); }

                        profileImage = responseBody.getUser().getUserProfilePicture();
                        if (!responseBody.getUser().getUserProfilePicture().isEmpty()) {
                            String base64String = responseBody.getUser().getUserProfilePicture();
                            String base64Image = base64String.split(",")[1];

                            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            ci_profile.setImageBitmap(decodedByte);

                            //saveImage(decodedByte);


                        }else {
                            ci_profile.setImageResource(R.drawable.ic_user);
                        }

                        //Glide.with(mContext).load(decodedByte).error(R.drawable.profile_img).fitCenter().into(ci_profile);
                    }else {
                        Alerts.show(mContext , responseBody.getMessage());
                    }

                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext, error);
                }
            });
        }else {
            cd.show(mContext);
        }

    }

    private void setDateTimeField() {
        et_dob.setOnClickListener(this);
        String date = "1-1-2000";
        String parts[] = date.split("-");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        fromDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_dob.setText(dateFormatter.format(newDate.getTime()));
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        //fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

    }

    private boolean isValidEmailId(String email){
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
