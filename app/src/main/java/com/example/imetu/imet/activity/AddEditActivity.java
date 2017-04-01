package com.example.imetu.imet.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.imetu.imet.R;
import com.example.imetu.imet.database.DBEngine;
import com.example.imetu.imet.image.ImageHelper;
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

import cz.msebera.android.httpclient.Header;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.example.imetu.imet.widget.Util.ADD_MEMBER;
import static com.example.imetu.imet.widget.Util.BODY_MEDIUM;
import static com.example.imetu.imet.widget.Util.BODY_PLUMP;
import static com.example.imetu.imet.widget.Util.BODY_THIN;
import static com.example.imetu.imet.widget.Util.BODY_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GENDER_FEMALE;
import static com.example.imetu.imet.widget.Util.GENDER_MALE;
import static com.example.imetu.imet.widget.Util.GENDER_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GLASSES_UNDEFINED;
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
    RadioGroup bodyShapeRadioGroup;
    RadioGroup hairLengthRadioGroup;
    RadioGroup glassesRadioGroup;
    CheckBox checkbox_Permed;
    CheckBox checkbox_Dyed;

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
            getSupportActionBar().setTitle("ADD NEW MEMBER");
            AddEditActivityPermissionsDispatcher.getGPSWithCheck(this);
        } else {
            member = dbEngine.selectOne(getIntent().getIntExtra("id", 0));
            getSupportActionBar().setTitle("EDIT MEMBER");
            setMemberValue();
        }
        if (savedInstanceState != null) {
            member.setImgPath(savedInstanceState.getString("ImgPath"));
            Glide.with(AddEditActivity.this).load(member.getImgPath()).into(ivPreview);
//            Picasso.with(AddEditActivity.this).load(member.getImgPath()).transform(new CircleTransform()).into(ivPreview);
        }


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
                bodyShapeRadioGroup.check(R.id.radio_Thin);
                break;
            case BODY_MEDIUM:
                bodyShapeRadioGroup.check(R.id.radio_Medium);
                break;
            case BODY_PLUMP:
                bodyShapeRadioGroup.check(R.id.radio_Plump);
                break;
            default:
                // do nothing
        }

        switch (member.getHairLength()) {
            case HAIR_SHORT:
                hairLengthRadioGroup.check(R.id.radio_ShortHair);
                break;
            case HAIR_MEDIUM:
                hairLengthRadioGroup.check(R.id.radio_MediumHair);
                break;
            case HAIR_LONG:
                hairLengthRadioGroup.check(R.id.radio_LongHair);
                break;
            default:
                // do nothing
        }

        checkbox_Permed.setChecked(member.isPermed());
        checkbox_Dyed.setChecked(member.isDyed());

        switch (member.getGlasses()) {
            case GLASSES_WITH:
                glassesRadioGroup.check(R.id.radio_WithGlasses);
                break;
            case GLASSES_WITHOUT:
                glassesRadioGroup.check(R.id.radio_WithoutGlasses);
                break;
            default:
                // do nothing
        }
        if (member.getImgPath() != null) {
            Glide.with(AddEditActivity.this).load(member.getImgPath()).into(ivPreview);
//            Picasso.with(AddEditActivity.this).load(member.getImgPath()).transform(new CircleTransform()).into(ivPreview);
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
            case R.id.menuTakePhoto:
                AddEditActivityPermissionsDispatcher.takePhotoWithCheck(this);
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

        switch (bodyShapeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_Thin:
                member.setBodyShape(BODY_THIN);
                break;
            case R.id.radio_Medium:
                member.setBodyShape(BODY_MEDIUM);
                break;
            case R.id.radio_Plump:
                member.setBodyShape(BODY_PLUMP);
                break;
            default:
                member.setBodyShape(BODY_UNDEFINED);
        }

        switch (hairLengthRadioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_ShortHair:
                member.setHairLength(HAIR_SHORT);
                break;
            case R.id.radio_MediumHair:
                member.setHairLength(HAIR_MEDIUM);
                break;
            case R.id.radio_LongHair:
                member.setHairLength(HAIR_LONG);
                break;
            default:
                member.setHairLength(HAIR_UNDEFINED);
        }

        member.setPermed(checkbox_Permed.isChecked() ? true : false);
        member.setDyed(checkbox_Dyed.isChecked() ? true : false);

        switch (glassesRadioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_WithGlasses:
                member.setGlasses(GLASSES_WITH);
                break;
            case R.id.radio_WithoutGlasses:
                member.setGlasses(GLASSES_WITHOUT);
                break;
            default:
                member.setGlasses(GLASSES_UNDEFINED);
        }

        dbEngine.editMember(member);

        setResult(RESULT_OK);
        finish();
    }

    private void setView() {
        ivPreview = (ImageView) findViewById(R.id.addEditImage);
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
        bodyShapeRadioGroup = (RadioGroup) findViewById(R.id.BodyShapeRadioGroup);
        hairLengthRadioGroup = (RadioGroup) findViewById(R.id.HairLengthRadioGroup);
        glassesRadioGroup = (RadioGroup) findViewById(R.id.GlassesRadioGroup);

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
                pictureBitmap = (Bitmap) data.getExtras().get("data");

                pictureBitmap = ImageHelper.getCroppedBitmap(pictureBitmap, 100);

                File photoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoPath = photoDir + "/IMG_" + System.currentTimeMillis() + ".jpg";
                AddEditActivityPermissionsDispatcher.savePhotoWithCheck(this, photoPath);
            }
        }
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
                    Glide.with(AddEditActivity.this).load(member.getImgPath()).into(ivPreview);
//                    Picasso.with(AddEditActivity.this).load(member.getImgPath()).transform(new CircleTransform()).into(ivPreview);
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

}
