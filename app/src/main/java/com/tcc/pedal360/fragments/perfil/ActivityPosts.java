package com.tcc.pedal360.fragments.perfil;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tcc.pedal360.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

public class ActivityPosts extends AppCompatActivity {

    ImageView imageView;
    ProgressBar progressBar;
    private Uri selectedUri;
    private static final int PICK_FILE = 1;
    UploadTask uploadTask;
    EditText etdesc;
    Button btnchoosefile, btnuploadfile;
    VideoView videoView;
    String url, name;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db1, db2, db3;
    PostMember postMember;
    MediaController mediaController;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        postMember = new PostMember();
        mediaController = new MediaController(this);
        
        progressBar = findViewById(R.id.pb_post);
        imageView   = findViewById(R.id.iv_post);
        videoView   = findViewById(R.id.vv_post);
        btnchoosefile = findViewById(R.id.btn_choosefile);
        btnuploadfile = findViewById(R.id.btn_send);
        etdesc = findViewById(R.id.et_desc_post);
        db1 = database.getReference("All images");
        db2 = database.getReference("All videos");
        db3 = database.getReference("All posts");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        db1 = database.getReference("All images").child(currentuid);
        db2 = database.getReference("All videos").child(currentuid);
        db3 = database.getReference("All posts");

        btnuploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dopost();
            }
        });

        btnchoosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE || requestCode == RESULT_OK || data != null || data.getData() != null){

            selectedUri = data.getData();
            if (selectedUri.toString().contains("image")){
                Picasso.get().load(selectedUri).into(imageView);
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                type = "iv";
            } else if (selectedUri.toString().contains("video")){
                videoView.setMediaController(mediaController);
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVideoURI(selectedUri);
                videoView.start();
                type = "vv";
            } else {
                Toast.makeText(this, "Nenhum arquivo selecionado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("user").document(currentuid);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()){

                    name = task.getResult().getString("name");
                    url = task.getResult().getString("url");


                } else {
                    Toast.makeText(ActivityPosts.this, "Erro", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    void Dopost() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        String desc = etdesc.getText().toString();

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat currentdata = new SimpleDateFormat("dd-MMMM-yyyy");
        final String savedata = currentdata.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("dd-MMMM-yyyy");
        final String savetime = currenttime.format(cdate.getTime());

        String time = savedata + ":" + savetime;

        if (TextUtils.isEmpty(desc) || selectedUri != null){

            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(selectedUri));
            uploadTask = reference.putFile(selectedUri);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        if (type.equals("iv")){

                            postMember.setDesc(desc);
                            postMember.setName(name);
                            postMember.setPostUri(downloadUri.toString());
                            postMember.setTime(time);
                            postMember.setUid(currentuid);
                            postMember.setUrl(url);
                            postMember.setType("iv");

                            String id = db1.push().getKey();
                            db1.child(id).setValue(postMember);

                            String id1 = db3.push().getKey();
                            db3.child(id1).setValue(postMember);
                            Toast.makeText(ActivityPosts.this, "Post enviado", LENGTH_SHORT).show();

                        } else if (type.equals("vv")){
                            postMember.setDesc(desc);
                            postMember.setName(name);
                            postMember.setPostUri(downloadUri.toString());
                            postMember.setTime(time);
                            postMember.setUid(currentuid);
                            postMember.setUrl(url);
                            postMember.setType("vv");

                            String id3 = db2.push().getKey();
                            db1.child(id3).setValue(postMember);

                            String id4 = db3.push().getKey();
                            db3.child(id4).setValue(postMember);

                            Toast.makeText(ActivityPosts.this, "Post enviado", LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivityPosts.this, "Erro", LENGTH_SHORT).show();
                        }

                    }
                }
            });

        } else {
            Toast.makeText(this, "Preencha todos os campos", LENGTH_SHORT).show();
        }

        }
    }
