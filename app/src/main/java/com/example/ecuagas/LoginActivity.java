package com.example.ecuagas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button btnLoginUsuario, btnRegistroUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLoginUsuario=(Button)findViewById(R.id.btnLoginUsuario);
        btnRegistroUsuario=(Button)findViewById(R.id.btnRegistroUsuario);

        btnLoginUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRegistroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnLoginUsuario.setOnTouchListener(new View.OnTouchListener() {
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

        btnRegistroUsuario.setOnTouchListener(new View.OnTouchListener() {
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
