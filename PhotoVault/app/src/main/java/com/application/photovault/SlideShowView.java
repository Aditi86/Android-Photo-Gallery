package com.application.photovault;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author Aditi Patel
 * @author Aakaash Prakash Hemdev
 */

public class SlideShowView extends AppCompatActivity {
    public static int index;
    private int tagIndex = -1;
    public  Button add;
    private TextView textView2;
    private Button prev, next, delete;
    public ImageView imgView;
    public  GridView gridView;
    public static ArrayAdapter tagAdapter;


    @Override
    protected void onResume() {
        super.onResume();

        if (AlbumView.imgAdapter.uris.get(index).tags.size() == 0) {
            delete.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
        } else {

            delete.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show_view);

        index = AlbumView.index;

        imgView = (ImageView) findViewById(R.id.imageView);
        gridView = (GridView) findViewById(R.id.gridView);

        tagAdapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1, AlbumView.imgAdapter.uris.get(index).tags);

        Log.d("SLideSHow",  " "+AlbumView.imgAdapter.uris.get(index).tags);
        gridView.setAdapter(tagAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tagIndex = i;
            }
        });

        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        add = (Button) findViewById(R.id.Add_Tag);
        delete = (Button) findViewById(R.id.Del_Tag);
        textView2 = findViewById(R.id.textView2);

        if (AlbumView.imgAdapter.uris.get(index).tags.size() == 0) {
            delete.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
        }
        else {
            delete.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
        }

        if (AlbumView.imgAdapter.getCount() == 1) {

            prev.setVisibility(View.INVISIBLE);
            next.setVisibility(View.INVISIBLE);
        }
        else if (index == 0) {
            prev.setVisibility(View.INVISIBLE);
        }
        else if (index == AlbumView.imgAdapter.getCount() - 1) {
            next.setVisibility(View.INVISIBLE);
        }

        try {
            InputStream pictureInputStream = getContentResolver().openInputStream(AlbumView.imgAdapter.uris.get(index).getUri());
            Bitmap currPic = BitmapFactory.decodeStream(pictureInputStream);
            imgView.setImageBitmap(currPic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index > 0) {
                    index--;
                    if (AlbumView.imgAdapter.uris.get(index).tags.size() == 0) {
                        delete.setVisibility(View.INVISIBLE);
                        textView2.setVisibility(View.INVISIBLE);
                    } else {
                        delete.setVisibility(View.VISIBLE);
                        textView2.setVisibility(View.VISIBLE);
                    }

                    if (next.getVisibility() == View.INVISIBLE) {
                        next.setVisibility(View.VISIBLE);
                    }
                    if (index == 0) {
                        prev.setVisibility(View.INVISIBLE);
                    }
                    try {
                        InputStream pictureInputStream = getContentResolver().openInputStream(AlbumView.imgAdapter.uris.get(index).getUri());
                        Bitmap currPic = BitmapFactory.decodeStream(pictureInputStream);
                        imgView.setImageBitmap(currPic);

                        tagAdapter = new ArrayAdapter<Tag>(getApplicationContext(), android.R.layout.simple_list_item_1, AlbumView.imgAdapter.uris.get(index).tags);
                        Log.d("SLideSHow",  " Tag2: "+AlbumView.imgAdapter.uris.get(index).tags);

                        gridView.setAdapter(tagAdapter);
                    } catch (FileNotFoundException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index < AlbumView.imgAdapter.getCount() - 1) {
                    index++;
                    if (AlbumView.imgAdapter.uris.get(index).tags.size() == 0) {
                        delete.setVisibility(View.INVISIBLE);
                        textView2.setVisibility(View.INVISIBLE);

                    } else {
                        delete.setVisibility(View.VISIBLE);
                        textView2.setVisibility(View.VISIBLE);
                    }

                    if (prev.getVisibility() == View.INVISIBLE) {
                        prev.setVisibility(View.VISIBLE);
                    }
                    if (index == AlbumView.imgAdapter.getCount() - 1) {
                        next.setVisibility(View.INVISIBLE);
                    }
                    try {
                        InputStream pictureInputStream = getContentResolver().openInputStream(AlbumView.imgAdapter.uris.get(index).getUri());
                        Bitmap currPic = BitmapFactory.decodeStream(pictureInputStream);
                        imgView.setImageBitmap(currPic);

                        tagAdapter = new ArrayAdapter<Tag>(getApplicationContext(), android.R.layout.simple_list_item_1, AlbumView.imgAdapter.uris.get(index).tags);
                        Log.d("SLideSHow",  " Tag3: "+AlbumView.imgAdapter.uris.get(index).tags);

                        gridView.setAdapter(tagAdapter);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), NewTag.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tagIndex != -1) {
                    AlbumView.imgAdapter.uris.get(index).tags.remove(tagIndex);
                    tagAdapter = new ArrayAdapter<Tag>(getApplicationContext(), android.R.layout.simple_list_item_1, AlbumView.imgAdapter.uris.get(index).tags);
                    Log.d("SLideSHow",  " Tag4: "+AlbumView.imgAdapter.uris.get(index).tags);
                    write();

                    gridView.setAdapter(tagAdapter);
                    Toast.makeText(getApplicationContext(), "The tag is deleted", Toast.LENGTH_SHORT).show();
                }

                if (AlbumView.imgAdapter.uris.get(index).tags.size() == 0) {
                    delete.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void write(){
        try {
            ArrayList<Photo> uris = AlbumView.imgAdapter.getPhotos();
            ArrayList<Tag> tags = new ArrayList<>();

            String str = "";
            FileOutputStream fileOutputStream = openFileOutput(HomeScreen.albumName+".list", MODE_PRIVATE);
            for (Photo u : uris) {
                if (str.equals("")) {
                    str = u.toString();

                } else {
                    str = str + "\n" + u.toString();
                }
            }

            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();

        } catch(ArrayIndexOutOfBoundsException | IOException e){
            e.printStackTrace();
        }
    }

}
