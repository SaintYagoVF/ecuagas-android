package com.example.ecuagas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroUsuarioActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

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
    private static final String KEY_UBICACION="ubicacion";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Shared
    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Rol = "rolKey";
    public static final String Id = "idKey";
    SharedPreferences sharedpreferences;

    //GPS


    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private double Latitud=0.0;
    private double Longitud=0.0;
    private LocationManager locationManager;

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


        //SharedPreferences

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);



        //GPS


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone


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
       final String email=txtEmail.getText().toString();
        String cedula=txtCedula.getText().toString();
        String clave=txtClave.getText().toString();

        GeoPoint ubicacion = new GeoPoint(Latitud,Longitud);



      final  Map<String,Object> usuario=new HashMap<>();

        usuario.put(KEY_NOMBRE,nombre);
        usuario.put(KEY_EMAIL,email);
        usuario.put(KEY_CEDULA,cedula);
        usuario.put(KEY_CLAVE,clave);
        usuario.put(KEY_UBICACION,ubicacion);

        //db.document("Usuarios/"+email);



        db.collection("Usuario").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){

                            Toast.makeText(RegistroUsuarioActivity.this,"Este usuario ya se ha registrado anteriormente",Toast.LENGTH_LONG).show();


                        }
                        else{



                            db.collection("Usuario").document(email).set(usuario)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegistroUsuarioActivity.this,"¡Se ha registrado con éxito!",Toast.LENGTH_LONG).show();

                                            try {


                                                SharedPreferences.Editor editor3 = sharedpreferences.edit();

                                                editor3.putString(Rol,"Usuario");

                                                editor3.commit();



                                            } catch (Exception e) {


                                                e.printStackTrace();
                                            }

                                            try {


                                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                                editor.putString(Id,txtEmail.getText().toString());

                                                editor.commit();



                                            } catch (Exception e) {


                                                e.printStackTrace();
                                            }

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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroUsuarioActivity.this,"Error al obtener código",Toast.LENGTH_LONG).show();
                    }
                });




    }


    //GPS



    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());

        Log.i(TAG, "Latitud: " + location.getLatitude());
        Log.i(TAG, "Longitud: " + location.getLongitude());
        // mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        // mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        Latitud=location.getLatitude();
        Longitud=location.getLongitude();

        // You can now create a LatLng Object for use with maps
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
       final AlertDialog.Builder dialog
                = new AlertDialog
                .Builder(RegistroUsuarioActivity.this);
       // final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setTitle("Habilitar GPS")
                .setMessage("El GPS se encuentra deshabilitado.\nPor favor, habilítalo para " +
                        "usar esta aplicación")
                .setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
