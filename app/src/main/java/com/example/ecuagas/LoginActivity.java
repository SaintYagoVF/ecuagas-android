package com.example.ecuagas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btnLoginUsuario, btnRegistroUsuario;

    EditText txtCorreoUsuario, txtClaveUsuario;

    //FIRESTORE
    private static final String TAG="LoginActivity";
    private static final String KEY_EMAIL="email";
    private static final String KEY_CLAVE="clave";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Rol = "rolKey";
    public static final String Id = "idKey";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLoginUsuario=(Button)findViewById(R.id.btnLoginUsuario);
        btnRegistroUsuario=(Button)findViewById(R.id.btnRegistroUsuario);

        txtCorreoUsuario=(EditText)findViewById(R.id.txtLoginEmail);
        txtClaveUsuario=(EditText)findViewById(R.id.txtloginClave);

        //SharedPreferences

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btnLoginUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUsuario();



            }
        });

        btnRegistroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegistroUsuarioActivity.class);

                startActivity(intent);

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


        txtClaveUsuario.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txtClaveUsuario.getRight() - txtClaveUsuario.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here




                        txtClaveUsuario.setTransformationMethod(HideReturnsTransformationMethod.getInstance());



                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void loginUsuario(){


        String email=txtCorreoUsuario.getText().toString();
        final String clave=txtClaveUsuario.getText().toString();



        db.collection("Usuario").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){

                            //Map<String,Object> usuario=documentSnapshot.getData();

                        String clave_firebase=documentSnapshot.getString(KEY_CLAVE);

                        if(clave_firebase.equals(clave)){


                            try {


                                SharedPreferences.Editor editor3 = sharedpreferences.edit();

                                editor3.putString(Rol,"Usuario");

                                editor3.commit();



                            } catch (Exception e) {


                                e.printStackTrace();
                            }

                            try {


                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString(Id,txtCorreoUsuario.getText().toString());

                                editor.commit();



                            } catch (Exception e) {


                                e.printStackTrace();
                            }


                            Toast.makeText(LoginActivity.this,"¡Bienvenido a Ecuagas!",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);



                        }else{
                            Toast.makeText(LoginActivity.this,"La clave es incorrecta",Toast.LENGTH_LONG).show();
                        }





                        }
                        else{

                            Toast.makeText(LoginActivity.this,"No existe un usuario con el correo ingresado.",Toast.LENGTH_LONG).show();

                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this,"Error al obtener código",Toast.LENGTH_LONG).show();
                        Log.d(TAG,e.toString());
                    }
                });


    }
}
