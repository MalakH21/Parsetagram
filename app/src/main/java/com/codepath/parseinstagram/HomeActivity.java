package com.codepath.parseinstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.parseinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String imagePath = null;
    private EditText descriptionInput;
    private Button createButton;
    private Button refreshButton;
    private Button takephotoButton;
    private Button postBtn;
    private ImageView picView;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;
    public ImageView photoSave;
    private static File filesDir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home);
        photoSave = (ImageView) findViewById(R.id.photoSave);

        filesDir = getApplicationContext().getFilesDir();
        //descriptionInput = findViewById(R.id.description_et);
        //createButton = findViewById(R.id.create_btn);
        //refreshButton = findViewById(R.id.refresh_btn);


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        loadTopPosts();
        View view = findViewById(R.id.homeLayout);


        takephotoButton = findViewById(R.id.takephotoButton);
        takephotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {
                    onLaunchCamera(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //create Intent to take a picture and return control to the calling application
                // Create a File reference to access to future access
                /*try {
                    photoFile = getPhotoFileUri(photoFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                //photoFile = getPhotoFileUri(photoFileName);

                // wrap File object into a content provider
                // required for API >= 24
                // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                //Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "com.codepath.parseinstagram", photoFile);
                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                //if (intent.resolveActivity(getPackageManager()) != null) {
                // Start the image capture intent to take photo
                //    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                //}
//                dispatchTakePictureIntent();

            }
        });

        postBtn = findViewById(R.id.postBtn2);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(intent);

            }
        });
    }

    private void loadTopPosts() {

        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        ParseQuery.getQuery(Post.class).findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().toString());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    // Added to attempt to take photos using app

    public void onLaunchCamera(View view) throws IOException {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri();

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "com.codepath.parseinstagram", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Bitmap takenImage = BitmapFactory.decodeFile(photoFileName);
            photoSave.setImageBitmap(takenImage);

            // RESIZE BITMAP, see section below
            // Load the taken image into a preview
            ImageView ivPreview = (ImageView) findViewById(R.id.photoSave);
            ivPreview.setImageBitmap(takenImage);

            photoFile = persistImage(takenImage, "test");

            ParseFile parseFile = new ParseFile(photoFile);
            parseFile.saveInBackground();

            createPost("post222", parseFile, ParseUser.getCurrentUser(), ParseUser.getCurrentUser().getString("handle"));
        }else { // Result was a failure
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }





    }



    public File getPhotoFileUri() throws IOException {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        photoFileName = image.getAbsolutePath();








        // Return the file target for the photo based on filename
        //File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return image;
    }



    private static File persistImage(Bitmap bitmap, String name) {
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("SaveImage", "Saving image did not work");
        }




        return imageFile;
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user, String handle){
        Post post = new Post();
        post.setDescription(description);
        post.setImage(imageFile);
        post.setUser(user);
        post.setHandle(handle);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Log.d("PostImage", "Post image successful");
                } else {
                    Log.e("PostImage", "Post image failed" );

                }
            }
        });



    }
}
