package com.example.sugarandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

private AppBarConfiguration mAppBarConfiguration;
//private static int RESULT_LOAD_IMAGE = 1;
private  String selectImage;
ByteArrayOutputStream stream = new ByteArrayOutputStream();


@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);


    setSupportActionBar(toolbar);
    FloatingActionButton fab = findViewById(R.id.fab);


    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          selectImage(MainActivity.this);
        }
    });
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    mAppBarConfiguration = new AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
            .setDrawerLayout(drawer)
            .build();
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}

@Override
public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
            || super.onSupportNavigateUp();
}
private void selectImage(Context context) {

    final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Choose a mode");

    builder.setItems(options, new DialogInterface.OnClickListener() {

        //private static final int RESULT_LOAD_IMG = ;

        @Override
        public void onClick(DialogInterface dialog, int item) {

            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent,1);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        }
    });
    builder.show();
}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //ImageView imageView= findViewById(R.id.Gallery);
    if (resultCode != RESULT_CANCELED) {
       // setContentView(R.layout.imagelayout);
        switch (requestCode) {

            case 0:

                if (resultCode == RESULT_OK && data != null) {
                    Bitmap thumbnail = null;
                            thumbnail = (Bitmap) data.getExtras().get("data");
                            Intent i = new Intent(this, ImageActivity.class);
                            i.putExtra("name", thumbnail);
                            startActivity(i);
                        }



                break;
            case 1:

                   if (resultCode == RESULT_OK && data != null) {
                       Uri selectedImage = data.getData();


                       //  String[] filePathColumn = {MediaStore.Images.Media.DATA};
                       if (selectedImage != null)
                       {
                           try
                           {
                               Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                               bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
                               byte[] byteArray = stream.toByteArray();
                              // Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                               Intent i = new Intent(this, ImageActivity.class);
                               i.putExtra("ByteArray", byteArray);
                               startActivity(i);
                           }
                           catch (IOException e)
                           {
                               e.printStackTrace();
                           }
                       }
                   }
                      //Bit thumbnail = (Bitmap) data.getExtras().get("data");







                break;
        }

        }
    }
}




