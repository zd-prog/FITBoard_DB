package by.ziziko.fitboard.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import by.ziziko.fitboard.JSONHelper;
import by.ziziko.fitboard.Entities.Post;
import by.ziziko.fitboard.R;

public class NewPostActivity extends AppCompatActivity {


    private static final int GALLERY_REQUEST = 100;
    private String login;
    private DatabaseReference mDb;
    private String PostKey = "Post";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mDb = FirebaseDatabase.getInstance().getReference(PostKey);

        if (getIntent().getExtras()!=null)
        {
            login = getIntent().getExtras().getString("login");
        }

        List<String> categories = JSONHelper.importFromJSON(getApplicationContext());
        if (categories == null)
            categories = new ArrayList<>();

        String[] cats = new String[categories.size()];

        for (int i = 0; i < categories.size(); i++)
        {
            cats[i] = categories.get(i);
        }

        Spinner spinner = findViewById(R.id.post_category);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, cats);
        spinner.setAdapter(adapter);

        Button save = findViewById(R.id.button_save_post);
        EditText title = findViewById(R.id.new_post_title);
        EditText text = findViewById(R.id.post_fulltext);
        ImageButton imageButton = findViewById(R.id.image_button);
        ImageView imageView = findViewById(R.id.post_image);

        save.setOnClickListener(view ->
        {
            if (!title.getText().toString().equals("") || !text.getText().toString().equals(""))
            {
                String selected = spinner.getSelectedItem().toString();
                Post post = new Post();
                post.setTitle(title.getText().toString());
                post.setCategory(selected);
                post.setAuthor(login);
                post.setDate(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()));
                post.setText(text.getText().toString());
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                if (drawable!=null)
                {
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    post.setImage(encodedImage);
                }
                mDb.push().setValue(post);

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("login", login);
                startActivity(intent);

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
            }
        });

        imageButton.setOnClickListener(view ->
        {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        ImageView imageView = findViewById(R.id.post_image);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                }
        }
    }
}