package com.application.photovault;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

public class Search extends AppCompatActivity {
    private static final String TAG = "Search";
    private int type = -1;
    private static ArrayList<Photo> searched = new ArrayList<>();
    private static ArrayList<Photo> sList = new ArrayList<>();
    ArrayList<String>databaseTags = new ArrayList<>();
    ArrayAdapter<String>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button cancel;
        Button search;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ArrayList<String>databaseTags = new ArrayList<>();
        AutoCompleteTextView searchText = findViewById(R.id.autoCompleteTextView);


        cancel = findViewById(R.id.cancelSearch);
        search = findViewById(R.id.searchItem);
        read();
        int j = 0;
        for (Photo p : searched)
        {
            for (Tag t : p.tags)
            {
                if(!databaseTags.contains(t.getData()))
                {
                    databaseTags.add(t.getData());
                }
                j++;
            }
        }
        searched.clear();
        sList.clear();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, databaseTags);
        searchText.setAdapter(adapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ArrayList<String> tags = new ArrayList<>();
                int i = 0;
                String and = "and";
                String or = "or";
                searched.clear();
                sList.clear();
                read();
                if(!searchText.getText().toString().equals(""))
                {
                    int count=0;
                    for (Photo p : searched)
                    {
                            if(searchText.getText().toString().toLowerCase().trim().contains(and))
                            {
                                String [] split = searchText.getText().toString().trim().toLowerCase().split(and);

                                for(Tag t: p.tags)
                                {

                                    if(t.getData().toLowerCase().trim().contains(split[0].trim().toLowerCase()))
                                    {
                                        count++;
                                    }
                                    if(t.getData().toLowerCase().trim().contains(split[1].trim().toLowerCase()))
                                    {
                                        count++;
                                    }
                                }
                                if(count>=2)
                                {
                                    sList.add(p);
                                    i++;
                                    break;
                                }
                                count = 0;
                            }
                        if(searchText.getText().toString().toLowerCase().trim().contains(or))
                        {
                            String [] split = searchText.getText().toString().trim().toLowerCase().split(or);
                            for(Tag t: p.tags)
                            {
                                if(t.getData().toLowerCase().trim().contains(split[0].trim().toLowerCase()))
                                {
                                    count++;
                                }
                                else if(t.getData().toLowerCase().trim().contains(split[1].trim().toLowerCase()))
                                {
                                    count++;
                                }
                            }
                            if(count>=1)
                            {
                                sList.add(p);
                                i++;
                                break;
                            }
                            count = 0;
                        }
                        else
                        {
                            for(Tag t: p.tags)
                            {
                                if(t.getData().toLowerCase().trim().contains(searchText.getText().toString().toLowerCase().trim()))
                                {
                                    sList.add(p);
                                    i++;
                                    break;
                                }
                            }
                        }
                    }
                    write();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), AlbumView.class);
                    HomeScreen.albumName="SearchRes";
                    startActivity(intent);




                    //}
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please write something to search", Toast.LENGTH_SHORT).show();
                }



            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void read() {
        String[] strings = {};
        ArrayList<String> masterList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = openFileInput("albums.albm");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            ArrayList<String> list = new ArrayList<>();
            String lineIn;

            while ((lineIn = bufferedReader.readLine()) != null) {
                list.add(lineIn);
            }

            for (String s : list) {
                try {
                    FileInputStream fileInputStream2 = openFileInput(s + ".list");

                    InputStreamReader inputStreamReader2 = new InputStreamReader(fileInputStream2);
                    BufferedReader bufferedReader2 = new BufferedReader(inputStreamReader2);

                    while ((lineIn = bufferedReader2.readLine()) != null) {
                        masterList.add(lineIn);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (String input: masterList) {
                if (input.substring(0,4).equals("TAG:")) {
                    searched.get(searched.size() - 1).addTag(input.substring(4));
                }
                else {
                    searched.add(new Photo(Uri.parse(input)));
                }
            }
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void write(){
        try {

            String str = "";
            new File(getFilesDir() + File.separator + "SearchRes.list").delete();
            FileOutputStream fileOutputStream = openFileOutput("SearchRes.list", MODE_PRIVATE);
            for (int i = 0; i < sList.size();i++) {
                ArrayList<String> tgs = new ArrayList<>();
                Photo u = sList.get(i);
                if (str.equals("")) {
                    str = u.getUri().toString();
                }
                else {
                    str = str + "\n" + u.getUri().toString();
                }
                for (Tag t : u.tags){
                    if (!tgs.contains(t.toString())) {
                        str = str + "\nTAG:" + t.toString();
                        tgs.add(t.toString());
                    }
                }
            }
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();


        } catch(ArrayIndexOutOfBoundsException | IOException e){
            e.printStackTrace();
        }
    }
}
