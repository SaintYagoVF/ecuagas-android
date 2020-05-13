package com.example.ecuagas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroUsuarioActivity extends AppCompatActivity {

    //FIREBASE AUTH
    private static final int MY_REQUEST_CODE = 7117; //cualquier numero

    List<AuthUI.IdpConfig> providers;

    EditText txtNombres, txtEmail, txtCedula, txtClave;

    Button btnAceptar, btnRegistrarFacebook;
    private static final String TAG="RegistroUsuarioActivity";
    private static final String KEY_NOMBRE="nombre";
    private static final String KEY_EMAIL="email";
    private static final String KEY_CEDULA="cedula";
    private static final String KEY_CLAVE="clave";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        txtNombres=(EditText)findViewById(R.id.txtUsuarioRegistro);
        txtEmail=(EditText)findViewById(R.id.txtEmailRegistro);
        txtCedula=(EditText)findViewById(R.id.txtCedulaRegistro);
        txtClave=(EditText)findViewById(R.id.txtClaveRegistro);

        btnAceptar=(Button)findViewById(R.id.btnDistribuidorAceptar);
        btnRegistrarFacebook=(Button)findViewById(R.id.btnRegistroFacebook);



        //init provider

        providers= Arrays.asList(

                new AuthUI.IdpConfig.FacebookBuilder().build(),  //facebook
                new AuthUI.IdpConfig.GoogleBuilder().build()  //google
        );


        btnRegistrarFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingInOptions();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroUsuario();
            }
        });

        btnRegistrarFacebook.setOnTouchListener(new View.OnTouchListener() {
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

        btnAceptar.setOnTouchListener(new View.OnTouchListener() {
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


        txtClave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txtClave.getRight() - txtClave.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here




                        txtClave.setTransformationMethod(HideReturnsTransformationMethod.getInstance());



                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void showSingInOptions(){
        startActivityForResult(

                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.myTheme)
                        .build(),MY_REQUEST_CODE
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==MY_REQUEST_CODE){

            IdpResponse response=IdpResponse.fromResultIntent(data);
            if(resultCode==RESULT_OK){
                //get user
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                //show email on toast
                //Toast.makeText(this,""+user.getEmail(),Toast.LENGTH_LONG).show();
                txtNombres.setText(user.getDisplayName());
                txtEmail.setText(user.getEmail());

                //set button signout
               // btn_sign_out.setEnabled(true);
            }else{
                Toast.makeText(this,response.getError().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void registroUsuario(){

        String nombre=txtNombres.getText().toString();
        String email=txtEmail.getText().toString();
        String cedula=txtCedula.getText().toString();
        String clave=txtClave.getText().toString();

        Map<String,Object> usuario=new HashMap<>();

        usuario.put(KEY_NOMBRE,nombre);
        usuario.put(KEY_EMAIL,email);
        usuario.put(KEY_CEDULA,cedula);
        usuario.put(KEY_CLAVE,clave);

        //db.document("Usuarios/"+email);



        db.collection("Usuario").document(email).set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistroUsuarioActivity.this,"¡Se ha registrado con éxito!",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RegistroUsuarioActivity.this, MainActivity.class);

                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(RegistroUsuarioActivity.this,"Error al registrar usuario",Toast.LENGTH_LONG).show();
                        Log.d(TAG,e.toString());

                    }
                });


    }
}
