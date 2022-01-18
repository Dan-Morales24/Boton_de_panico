package com.example.botondepanico.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.botondepanico.Interfaces.UpdateData;
import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import com.bumptech.glide.Glide;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.botondepanico.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;


public class MainActivity extends AppCompatActivity implements UpdateData {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    ImageView Avatar;
    DrawerLayout drawer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    TextView txt_name;
    TextView txt_email;
    Bitmap thumb_bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  setTheme(R.style.Theme_BotonDePanico_NoActionBar);
        super.onCreate(savedInstanceState);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {



            user.getIdToken(true).addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                  //  Toast.makeText(this,"Id: "+task.getResult().getToken(),Toast.LENGTH_SHORT).show();

                }


                 }).addOnFailureListener( e-> {


                if(e.getMessage().contains("account has been disabled")) {

                    Toast.makeText(this,"Usuario bloqueado",Toast.LENGTH_SHORT).show();
                    close_sesion_autentication();
                     }

                else{

                    Toast.makeText(this,"Conexion a internet lenta",Toast.LENGTH_SHORT).show();


                }

            });




        }




        init();
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        storageReference = FirebaseStorage.getInstance().getReference();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

       drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);
        txt_name = headerView.findViewById(R.id.NameNavigation);
        txt_email = headerView.findViewById(R.id.EmailNavigation);
        Avatar = headerView.findViewById(R.id.imageAvatar);

        Glide
                .with(MainActivity.this)
                .load(Data_Reference.currentClient.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.mipmap.ic_launcher_round)
                .into(Avatar);


        txt_name.setText(Data_Reference.builderWelcomeMessage());
        txt_email.setText(Data_Reference.currentClient.getPhone());

        Menu mMenu = navigationView.getMenu();

       MenuItem LogOut =mMenu.findItem(R.id.nav_login_out);
       LogOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
          //     Toast.makeText(MainActivity.this, "Cerrar Sesion ", Toast.LENGTH_SHORT).show();
               Close_sesion();
           return false;
           }
       });

        Avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Dexter.withActivity(MainActivity.this)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                               )
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    CropImage.startPickImageActivity(MainActivity.this);
                                //    Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    // show alert dialog navigating to Settings
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).
                        withErrorListener(new PermissionRequestErrorListener() {
                            @Override
                            public void onError(DexterError error) {
                                Toast.makeText(getApplicationContext(), "Error occurred!"+ error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onSameThread()
                        .check();




            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){


            Uri imageUri = CropImage.getPickImageResultUri(this,data);
            //Trim Image
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(MainActivity.this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){


            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){

                progressDialog = ProgressDialog.show(MainActivity.this, "Espera un poquito..",
                        "Estoy verificando tus datos.", true);

                Uri resultUri =result.getUri();
                File uri = new File(resultUri.getPath());

                //Compress Image
                try {

                    thumb_bitmap = new Compressor(this)
                            .setQuality(90)
                            .compressToBitmap(uri);
                }catch (IOException e){
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
                final byte [] thumb_byte = byteArrayOutputStream.toByteArray();

                String unique_name = FirebaseAuth.getInstance().getCurrentUser().getUid();
                StorageReference avatarFolder = storageReference.child("Avatars/" +unique_name);
                UploadTask uploadTask = avatarFolder.putBytes(thumb_byte);

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if (!task.isSuccessful()){
                           throw Objects.requireNonNull(task.getException());
                       }

                        return avatarFolder.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String unique_name = FirebaseAuth.getInstance().getCurrentUser().getUid();
                       Uri downloaduri = task.getResult();

                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("Avatar", downloaduri.toString());
                        FirebaseDatabase.getInstance()
                                //RIDER
                                .getReference(Data_Reference.Client_Info_reference)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .updateChildren(updateData)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.cancel();
                                        Toast.makeText(MainActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        editor = sharedPreferences.edit();
                                        editor.putString("Avatar", downloaduri.toString());
                                        editor.commit();

                                        Data_Reference.currentClient.setAvatar(downloaduri.toString());

                                        Glide
                                                .with(MainActivity.this)
                                                .load(thumb_byte)
                                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                .error(R.mipmap.ic_launcher_round)
                                                .into(Avatar);

                                        progressDialog.cancel();
                                        Toast.makeText(MainActivity.this, "Imagen actualizada.", Toast.LENGTH_SHORT).show();

                                      }
                                  });
                                 }
                             });
                            }
                        }
                    }

    private void init(){

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Data_Reference.Client_Info_reference);
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile_User");

    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Necesitamos permiso");
        builder.setMessage("Esta aplicación necesita permiso para usar la galeria. Puede otorgarse en la configuración de la aplicación.");
        builder.setPositiveButton("ir a configuraciones", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private void close_sesion_autentication(){

        FirebaseAuth.getInstance().signOut();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);

    }





    private void Close_sesion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Alerta")
                .setMessage("Estas seguro que quieres cerrar sesion")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Cerrar Sesión", (DialogInterface, i) -> {
                    FirebaseAuth.getInstance().signOut();
                    editor.clear();
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);
                }).setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
        //    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this,android.R.color.holo_red_dark));
        //    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(MainActivity.this,R.color.bluehigh));


        });

        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void updateDataDrawer() {

        txt_name.setText(Data_Reference.builderWelcomeMessage());
        txt_email.setText(Data_Reference.currentClient.getPhone());


    }



}