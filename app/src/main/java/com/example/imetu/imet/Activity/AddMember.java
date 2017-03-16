package com.example.imetu.imet.Activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.imetu.imet.DB.DBEngine;
import com.example.imetu.imet.Model.Member;
import com.example.imetu.imet.R;
import org.parceler.Parcels;



public class AddMember extends AppCompatActivity {
    private EditText etName;
    private EditText etEvent;
    private Button btnSave;
    private Member member;
    private DBEngine dbEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        //  setview
        setView();
    }

    private void setView() {
        dbEngine = new DBEngine();
        etName = (EditText)findViewById(R.id.etName);
        etEvent = (EditText)findViewById(R.id.etEvent);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setName(etName.getText().toString());
                member.setEvent(etEvent.getText().toString());
                dbEngine.editMember(member);
                Intent intent = new Intent();
                intent.putExtra("member", Parcels.wrap(member));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        member = new Member();
    }

    public void TakePhoto(View view) {
        Intent intent = new Intent(this, TakePhoto.class);
        startActivity(intent);

    }


}
