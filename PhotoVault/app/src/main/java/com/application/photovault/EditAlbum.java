package com.application.photovault;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Aditi Patel
 * @author Aakaash Prakash Hemdev
 */

public class EditAlbum extends AppCompatActivity {

    /**
     * Sets Data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

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
                int index = HomeScreen.getIndex();
                if (!albumName.getText().toString().isEmpty() && !HomeScreen.albums.contains(albumName.getText().toString())) {

                    Toast.makeText(getApplicationContext(), "The album "+ HomeScreen.albums.get(index) + "have been renamed.", Toast.LENGTH_SHORT).show();
                    try {

                        File old = new File((getFilesDir() + File.separator + HomeScreen.albums.get(index)+".list"));
                        FileOutputStream fileOutputStream = openFileOutput(albumName.getText().toString()+".list", MODE_PRIVATE);

                        String path = old.toPath().toUri().toString();
                        FileReader fileInputStream = new FileReader(old);

                        String str = "";
                        int check;
                        while ((check = fileInputStream.read()) != -1)
                        {
                            str = str + ((char) check + "");
                        }

                        fileOutputStream.write(str.getBytes());

                        getApplicationContext().deleteFile(new File(HomeScreen.albums.get(index)+".list").getName());
                        HomeScreen.albums.remove(index);
                        HomeScreen.albums.add(index,albumName.getText().toString());
                        write();

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, HomeScreen.albums);
                        HomeScreen home = new HomeScreen();
                        home.gridView.setAdapter(arrayAdapter);
                        fileOutputStream.close();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Saves app data
     */
    public void write(){
// FILE PATH    /data/user/0/com.AJ_David.photos/files/albums.albm
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
