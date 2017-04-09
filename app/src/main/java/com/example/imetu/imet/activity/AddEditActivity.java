package com.example.imetu.imet.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.imetu.imet.R;
import com.example.imetu.imet.database.DBEngine;
import com.example.imetu.imet.image.AlbumStorageDirFactory;
import com.example.imetu.imet.image.BaseAlbumDirFactory;
import com.example.imetu.imet.image.CircleTransform;
import com.example.imetu.imet.image.FroyoAlbumDirFactory;
import com.example.imetu.imet.model.Address;
import com.example.imetu.imet.model.Member;
import com.example.imetu.imet.widget.ConfidentialUtil;
import com.example.imetu.imet.widget.ObservableScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xw.repo.BubbleSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.example.imetu.imet.R.id.menuTakePhoto;
import static com.example.imetu.imet.widget.Util.ADD_MEMBER;
import static com.example.imetu.imet.widget.Util.BODY_MEDIUM;
import static com.example.imetu.imet.widget.Util.BODY_PLUMP;
import static com.example.imetu.imet.widget.Util.BODY_THIN;
import static com.example.imetu.imet.widget.Util.BODY_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GENDER_FEMALE;
import static com.example.imetu.imet.widget.Util.GENDER_MALE;
import static com.example.imetu.imet.widget.Util.GENDER_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GLASSES_WITH;
import static com.example.imetu.imet.widget.Util.GLASSES_WITHOUT;
import static com.example.imetu.imet.widget.Util.HAIR_LONG;
import static com.example.imetu.imet.widget.Util.HAIR_MEDIUM;
import static com.example.imetu.imet.widget.Util.HAIR_SHORT;
import static com.example.imetu.imet.widget.Util.HAIR_UNDEFINED;

@RuntimePermissions
public class AddEditActivity extends AppCompatActivity {

    ObservableScrollView obsScrollView;
    ImageView ivPreview;
    TextInputLayout nameLayout;
    EditText etName;
    EditText etPhone;
    EditText etEmail;
    EditText etRelationship;
    EditText etEvent;
    EditText etLocation;
    EditText etYearMet;
    EditText etTopicDiscussed;
    EditText etOther;
    BubbleSeekBar seekbarHeight;
    int seekbarHeightProgress = 165; //default

    RadioGroup genderRadioGroup;
    Spinner spinnerBodyShape;

    Spinner spinnerHairLength;
    Switch switchGlasses;
    CheckBox checkbox_Permed;
    CheckBox checkbox_Dyed;

    ImageView menuTakePhoto;

    private DBEngine dbEngine;
    private Member member;
    private int type;
    private final int TAKE_PICTURE_REQUEST_CODE = 10;
    private static final int REQUEST_CAMERA = 111;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_GPS = 113;
    private String photoPath;
    public FileOutputStream out = null;

    Bitmap pictureBitmap;
    private Address myAddress;
    private final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
    private final String GEOCODING_API_KEY = ConfidentialUtil.GEOCODING_API_KEY;

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        String iMetUserId = mSettings.getString("iMetUserId", null);

        dbEngine = new DBEngine(iMetUserId);

        setView();
        type = getIntent().getIntExtra("TYPE", ADD_MEMBER);
        if (type == ADD_MEMBER) {
            member = new Member();
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            String monthWithZero;
            if(month+1 < 10){
                monthWithZero = "0"+String.valueOf(month+1);
            } else {
                monthWithZero = String.valueOf(month+1);
            }
            int day = c.get(Calendar.DATE);
            String dayWithZero;
            if(day < 10){
                dayWithZero = "0"+String.valueOf(day);
            } else {
                dayWithZero = String.valueOf(day);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(year).append("-").append(monthWithZero).append("-").append(dayWithZero);
            etYearMet.setText(sb.toString());
            AddEditActivityPermissionsDispatcher.getGPSWithCheck(this);
        } else {
            member = dbEngine.selectOne(getIntent().getIntExtra("id", 0));
            setMemberValue();
        }
        if (savedInstanceState != null) {
            member.setImgPath(savedInstanceState.getString("ImgPath"));
            Glide.with(AddEditActivity.this).load(member.getImgPath()).transform(new CircleTransform(AddEditActivity.this)).into(ivPreview);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuTakePhoto = (ImageView) findViewById(R.id.menuTakePhoto);
        menuTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEditActivityPermissionsDispatcher.takePhotoWithCheck(AddEditActivity.this);
            }
        });

    }

    private void setMemberValue() {
        etName.setText(member.getName());
        etPhone.setText(member.getPhone());
        etEmail.setText(member.getEmail());
        etRelationship.setText(member.getRelationship());
        etEvent.setText(member.getEvent());
        etLocation.setText(member.getLocation());
        etYearMet.setText(member.getYearMet());
        etTopicDiscussed.setText(member.getTopicDiscussed());

        switch (member.getGender()) {
            case GENDER_MALE:
                genderRadioGroup.check(R.id.radio_Male);
                break;
            case GENDER_FEMALE:
                genderRadioGroup.check(R.id.radio_Female);
                break;
            default:
                // do nothing
        }

        seekbarHeight.setProgress(member.getHeight());

        switch (member.getBodyShape()) {
            case BODY_THIN:
                spinnerBodyShape.setSelection(1);
                break;
            case BODY_MEDIUM:
                spinnerBodyShape.setSelection(2);
                break;
            case BODY_PLUMP:
                spinnerBodyShape.setSelection(3);
                break;
            default:
                spinnerBodyShape.setSelection(0);
        }

        switch (member.getHairLength()) {
            case HAIR_SHORT:
                spinnerHairLength.setSelection(1);
                break;
            case HAIR_MEDIUM:
                spinnerHairLength.setSelection(2);
                break;
            case HAIR_LONG:
                spinnerHairLength.setSelection(3);
                break;
            default:
                spinnerHairLength.setSelection(0);
        }


        checkbox_Permed.setChecked(member.isPermed());
        checkbox_Dyed.setChecked(member.isDyed());

        switch (member.getGlasses()) {
            case GLASSES_WITH:
                switchGlasses.setChecked(true);
                break;
            case GLASSES_WITHOUT:
                switchGlasses.setChecked(false);
                break;
            default:
                switchGlasses.setChecked(false);
        }

        if (member.getImgPath() != null) {
            Glide.with(AddEditActivity.this).load(member.getImgPath()).transform(new CircleTransform(AddEditActivity.this)).into(ivPreview);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSave:
                saveClick();
                return true;
            case android.R.id.home:
                if(member.getImgPath() != null && !member.getImgPath().isEmpty()) {
                    supportFinishAfterTransition();
                }else{
                    finish();
                }
                return true;
            default:
                return false;
        }
    }

    //  save action
    public void saveClick() {
        if (type == ADD_MEMBER) {
            member.setId(ADD_MEMBER);
        }

        if(etName.length() == 0){
            nameLayout.setErrorEnabled(true);
            nameLayout.setError("Can't leave this empty");
        }else {

            member.setName(etName.getText().toString());
            member.setPhone(etPhone.getText().toString());
            member.setEmail(etEmail.getText().toString());
            member.setRelationship(etRelationship.getText().toString());
            member.setEvent(etEvent.getText().toString());
            member.setLocation(etLocation.getText().toString());
            member.setYearMet(etYearMet.getText().toString());
            member.setTopicDiscussed(etTopicDiscussed.getText().toString());
            member.setOther(etOther.getText().toString());

            switch (genderRadioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_Male:
                    member.setGender(GENDER_MALE);
                    break;
                case R.id.radio_Female:
                    member.setGender(GENDER_FEMALE);
                    break;
                default:
                    member.setGender(GENDER_UNDEFINED);
            }

            member.setHeight(seekbarHeightProgress);

            switch (spinnerBodyShape.getSelectedItem().toString()) {
                case "Thin":
                    member.setBodyShape(BODY_THIN);
                    break;
                case "Medium":
                    member.setBodyShape(BODY_MEDIUM);
                    break;
                case "Plump":
                    member.setBodyShape(BODY_PLUMP);
                    break;
                default:
                    member.setBodyShape(BODY_UNDEFINED);
            }

            switch (spinnerHairLength.getSelectedItem().toString()) {
                case "Short":
                    member.setHairLength(HAIR_SHORT);
                    break;
                case "Medium":
                    member.setHairLength(HAIR_MEDIUM);
                    break;
                case "Long":
                    member.setHairLength(HAIR_LONG);
                    break;
                default:
                    member.setHairLength(HAIR_UNDEFINED);
            }

            member.setPermed(checkbox_Permed.isChecked() ? true : false);
            member.setDyed(checkbox_Dyed.isChecked() ? true : false);

            if (switchGlasses.isChecked()) {
                member.setGlasses(GLASSES_WITH);
            } else {
                member.setGlasses(GLASSES_WITHOUT);
            }

            dbEngine.editMember(member);

            setResult(RESULT_OK);
            if(member.getImgPath() != null && !member.getImgPath().isEmpty()) {
                supportFinishAfterTransition();
            }else{
                finish();
            }
        }
    }

    private void setView() {
        ivPreview = (ImageView) findViewById(R.id.addEditImage);
        nameLayout = (TextInputLayout)findViewById(R.id.til_et_name);

        nameLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    nameLayout.setErrorEnabled(true);
                    nameLayout.setError("Can't leave this empty");
                }

                if (s.length() > 0) {
                    nameLayout.setError(null);
                    nameLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etRelationship = (EditText) findViewById(R.id.etRelationship);
        etEvent = (EditText) findViewById(R.id.etEvent);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etYearMet = (EditText) findViewById(R.id.etYearMet);
        etTopicDiscussed = (EditText) findViewById(R.id.etTopicDiscussed);
        etOther = (EditText) findViewById(R.id.etOther);

        genderRadioGroup = (RadioGroup) findViewById(R.id.GenderRadioGroup);
        spinnerBodyShape = (Spinner) findViewById(R.id.spinnerBodyShape);
        spinnerHairLength = (Spinner) findViewById(R.id.spinnerHairLength);
        switchGlasses = (Switch) findViewById(R.id.switchGlasses);

        checkbox_Permed = (CheckBox) findViewById(R.id.checkbox_Permed);
        checkbox_Dyed = (CheckBox) findViewById(R.id.checkbox_Dyed);

        seekbarHeight = (BubbleSeekBar) findViewById(R.id.seekbarHeight);
        seekbarHeight.correctOffsetWhenContainerOnScrolling();
        seekbarHeight.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                seekbarHeightProgress = progress;
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        obsScrollView = (ObservableScrollView) findViewById(R.id.activity_add_edit);

        obsScrollView.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                seekbarHeight.correctOffsetWhenContainerOnScrolling();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                setPic();
                File photoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoPath = photoDir + "/IMG_" + System.currentTimeMillis() + ".jpg";
                AddEditActivityPermissionsDispatcher.savePhotoWithCheck(this, photoPath);

//                Glide.with(AddEditActivity.this).load(mCurrentPhotoPath).transform(new CircleTransform(AddEditActivity.this)).into(ivPreview);
                Glide.with(AddEditActivity.this).load(photoPath).transform(new CircleTransform(AddEditActivity.this)).into(ivPreview);

                member.setImgPath(photoPath);
            }
        }
    }
    private void setPic() {

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

        Matrix matrix = new Matrix();
        if (orientation == 6) {
            matrix.postRotate(90);
        }
        else if (orientation == 3) {
            matrix.postRotate(180);
        }
        else if (orientation == 8) {
            matrix.postRotate(270);
        }

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */
		/* Get the size of the ImageView */
        int targetW = 400;
        int targetH = 400;

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        pictureBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        pictureBitmap = Bitmap.createBitmap(pictureBitmap, 0, 0, pictureBitmap.getWidth(), pictureBitmap.getHeight(), matrix, true);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("ImgPath", member.getImgPath());
        super.onSaveInstanceState(outState);
    }

    //  Camera permission
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;
        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }
        startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showRationaleForCamera(PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void OnCameraDenied() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void OnCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show();
    }

    //  Save photo permission
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void savePhoto(String photoPath) {
        try {
            out = new FileOutputStream(photoPath);
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    member.setImgPath(photoPath);
                    Glide.with(AddEditActivity.this).load(member.getImgPath()).transform(new CircleTransform(AddEditActivity.this)).into(ivPreview);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForStorage(PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void OnStorageDenied() {
        Toast.makeText(this, R.string.permission_storage_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void OnStorageNeverAskAgain() {
        Toast.makeText(this, R.string.permission_storage_neverask, Toast.LENGTH_SHORT).show();
    }

    //  GPS Permission
    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void getGPS() {
        LocationManager myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = myLocationManager.getLastKnownLocation("gps");
        if (myLocation != null) {
            getLocation(myLocation);
        }
    }

    @OnShowRationale({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void showRationaleForGPS(PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void onGPSDenied() {
        Toast.makeText(this, R.string.permission_gps_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void onGPSNeverAskAgain() {
        Toast.makeText(this, R.string.permission_gps_neverask, Toast.LENGTH_SHORT).show();
    }

    //  Use Geocoding api for location
    private void getLocation(Location location) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", GEOCODING_API_KEY);
        params.put("language", "zh-TW");
        params.put("latlng", location.getLatitude() + "," + location.getLongitude());
        client.get(GEOCODING_API_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    myAddress = new Address(response.getJSONArray("results").getJSONObject(0));
                    if (myAddress != null) {
                        member.setLocation(myAddress.getArea_level_1() + myAddress.getArea_level_3());
                        etLocation.setText(member.getLocation());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("IMet", "AddEditActivity, getLocation onFailure, statusCode: " + statusCode + "responseString: " + responseString);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddEditActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }


    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }
}
