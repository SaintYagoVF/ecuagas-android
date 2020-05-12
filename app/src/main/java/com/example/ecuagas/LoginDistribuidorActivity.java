package com.example.ecuagas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class LoginDistribuidorActivity extends AppCompatActivity {


    Button btnLoginDistribuidor, btnRegistroDistribuidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_distribuidor);

        btnLoginDistribuidor=(Button)findViewById(R.id.btnLoginDistribuidor);
        btnRegistroDistribuidor=(Button)findViewById(R.id.btnRegistroDistribuidor);

        btnLoginDistribuidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRegistroDistribuidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginDistribuidorActivity.this, RegistroDistribuidorActivity.class);

                startActivity(intent);

            }
        });


        btnLoginDistribuidor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.getBackground().setAlpha(150);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    v.getBackground().setAlpha(255);
                }
                return false;
            }
        });

        btnRegistroDistribuidor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.getBackground().setAlpha(150);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    v.getBackground().setAlpha(255);
                }
                return false;
            }
        });
    }
}
