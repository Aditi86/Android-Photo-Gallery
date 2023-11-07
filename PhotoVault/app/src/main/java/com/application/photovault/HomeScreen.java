package com.application.photovault;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Aditi Patel
 * @author Aakaash Prakash Hemdev
 */

public class HomeScreen extends AppCompatActivity {
    public GridView gridView;
    Button newButton, delete, rename, open, search;
    public static ArrayList<String> albums;
    public static String albumName;
    private static int index;
    public static Photo copy;
    public static boolean isCopy;

    /**
     * Sets data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        albums = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);



        gridView = (GridView) findViewById(R.id.gridView1);
        search = (Button) findViewById(R.id.search);
        newButton = (Button) findViewById(R.id.newButton);
        open = (Button) findViewById(R.id.open);
        delete = (Button) findViewById(R.id.delete);
        rename = (Button) findViewById(R.id.rename);
        open.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        rename.setVisibility(View.INVISIBLE);

        isCopy = false;

        for (String s:read()) {
            albums.add(s);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, albums);

        gridView.setAdapter(arrayAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                index = position;
                open.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                rename.setVisibility(View.VISIBLE);
            }
        });

        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index>=0 && index<albums.size()) {
                    getApplicationContext().deleteFile(new File(albums.get(index)+".list").getName());
                    albums.remove(index);
                    index = -1;

                    write();
                    albums.clear();
                    for (String s : read()) {
                        albums.add(s);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, albums);

                    gridView.setAdapter(arrayAdapter);

                    open.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                    rename.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "The album has been removed.", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getApplicationContext(),
                            "Failed to delete\nIndex = "+index+"\nSize = "+albums.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                albumName=albums.get(index);
                showAlbum();
            }
        });

        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch();
            }
        });

        newButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewAlbum();
            }
        });


        rename.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditAlbum();
            }
        });


    }

    /**
     * Starts NewAlbum
     */
    private void showNewAlbum(){
        Intent intent = new Intent(this, NewAlbum.class);
        startActivity(intent);
    }

    /**
     * Starts AlbumView
     */
    private void showAlbum(){
        Intent intent = new Intent(this, AlbumView.class);
        startActivity(intent);
    }

    /**
     * Starts Search
     */
    private void showSearch(){
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    /**
     * Starts EditAlbum
     */
    private void showEditAlbum(){
        Intent intent = new Intent(this, EditAlbum.class);
        startActivity(intent);
    }

    /**
     * Reads and stores list of Albums
     */
    public String[] read() {
        String[] strings = {};

        try {
            FileInputStream fileInputStream = openFileInput("albums.albm");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            ArrayList<String> list = new ArrayList<>();
            String lineIn;

            while ((lineIn = bufferedReader.readLine()) != null) {
                list.add(lineIn);
            }

            strings = new String[list.size()];

            for (int i = 0; i < list.size(); i++){
                strings[i] = list.get(i);
            }
            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();



            return strings;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    /**
     * Writes the list of album names adn returns it to "albums.albm"
     */
    public void write(){
        try {
            String str = "";
            if (albums.size() > 0) {
                str = albums.get(0);
            }

            FileOutputStream fileOutputStream = openFileOutput("albums.albm", MODE_PRIVATE);
            for (int i = 1; i < albums.size(); i++) {
                str = str.concat("\n" + albums.get(i));
            }

            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();


        } catch(ArrayIndexOutOfBoundsException | IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns index of the most recent album what was selected
     */
    public static int getIndex(){return index;
    }


}