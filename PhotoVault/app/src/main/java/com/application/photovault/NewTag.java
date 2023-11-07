package com.application.photovault;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Aditi Patel
 * @author Aakaash Prakash Hemdev
 */

public class NewTag extends AppCompatActivity {
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private EditText tagData;
    private Button send, cancel;
    private int type = -1;

    public SlideShowView slideShowView = new SlideShowView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        tagData = (EditText) findViewById(R.id.data);
        send = (Button) findViewById(R.id.add);
        cancel = (Button) findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> tags = new ArrayList<>();

                for (Tag t: AlbumView.imgAdapter.uris.get(SlideShowView.index).tags) {
                    if (!(tags.contains(t.toString())))
                    {
                        tags.add(t.toString());
                    }
                }
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                if (!tagData.getText().toString().equals("")) {
                    if(radioButton.getText().toString().equals("Location"))
                    {

                        if (tags.stream().anyMatch(element -> element.contains("Location = ")))
                        {
                            Toast.makeText(getApplicationContext(), "This tag type already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("NewTag", "TagType" + radioButton.getText().toString());
                            Log.d("NewTag", "TagType" + tagData.getText().toString());
                            AlbumView.imgAdapter.uris.get(SlideShowView.index).addTag("Location = " + tagData.getText().toString()); slideShowView.gridView.setAdapter(SlideShowView.tagAdapter);
                            write();
                            slideShowView.gridView.setAdapter(SlideShowView.tagAdapter);
                            Toast.makeText(getApplicationContext(), "The Location tag is added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else if(radioButton.getText().toString().equals("Person"))
                    {
                        if (tags.contains("Person = " + tagData.getText().toString().trim())) {
                            Toast.makeText(getApplicationContext(), "This tag already exists", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            AlbumView.imgAdapter.uris.get(SlideShowView.index).addTag("Person = " + tagData.getText().toString());
                            write();
                            Toast.makeText(getApplicationContext(), "The Person tag is added", Toast.LENGTH_SHORT).show();
                            slideShowView.gridView.setAdapter(SlideShowView.tagAdapter);
                            finish();
                        }
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please write something to add selected tag", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void write(){
        try {
            ArrayList<Photo> uris = AlbumView.imgAdapter.getPhotos();

            String str = "";
            FileOutputStream fileOutputStream = openFileOutput(HomeScreen.albumName+".list", MODE_PRIVATE);
            for (Photo u : uris) {
                if (str.equals("")) {
                    str = u.getUri().toString();

                }
                else {
                    str = str + "\n" + u.getUri().toString();
                }
                for (Tag t : u.tags){
                    str = str + "\nTAG:" + t.toString();
                }
            }
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();


        } catch(ArrayIndexOutOfBoundsException | IOException e){
            e.printStackTrace();
        }
    }

}
