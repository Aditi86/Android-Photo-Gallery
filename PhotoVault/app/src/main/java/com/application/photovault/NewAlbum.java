package com.application.photovault;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Aditi Patel
 * @author Aakaash Prakash Hemdev
 */

public class NewAlbum extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_album);

        final EditText albumName =  (EditText) findViewById(R.id.albumName);
        Button createButton =  (Button) findViewById(R.id.create),
                backButton = (Button) findViewById(R.id.cancel);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!albumName.getText().toString().isEmpty() && !HomeScreen.albums.contains(albumName.getText().toString())) {
                    try {
                        FileOutputStream fileOutputStream = openFileOutput(albumName.getText().toString()+".list", MODE_PRIVATE);
                        HomeScreen.albums.add(albumName.getText().toString());
                        write();
                        finish();
                        Toast.makeText(getApplicationContext(), "The album was successfully created.", Toast.LENGTH_SHORT).show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void write(){
        try {
            String str = "";
            if (HomeScreen.albums.size() > 0) {
                str = HomeScreen.albums.get(0);
            }

            FileOutputStream fileOutputStream = openFileOutput("albums.albm", MODE_PRIVATE);
            for (int i = 1; i < HomeScreen.albums.size(); i++) {
                str = str.concat("\n" + HomeScreen.albums.get(i));
            }

            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();

        } catch(ArrayIndexOutOfBoundsException | IOException e){
            e.printStackTrace();
        }
    }
}