package com.cinestar.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cinestar.R;
import com.cinestar.models.Post;
import com.cinestar.providers.AuthProviders;
import com.cinestar.providers.ImageProviders;
import com.cinestar.providers.PostProvider;
import com.cinestar.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost1;
    //ImageView mImageViewPost2;
    File mImageFile;
    Button mButtonPost;
    TextInputEditText mTextInputTitle;
    TextInputEditText mTextInputDescription;
    ImageView mImageViewAction;
    ImageView mImageViewTerror;
    ImageView mImageViewComedia;
    ImageView mImageViewAnimation;
    ImageView mTextViewAnime;
    ImageView mTextViewRomance;
    TextView mTextViewCategory;
    String mCategoria = "";
    String mTitle = "";
    String mDescription = "";

    ImageProviders mImageProvider;
    PostProvider mPostProvider;
    AuthProviders mAuthProviders;

    private final int Gallery_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mButtonPost = findViewById(R.id.btnPost);
        mImageViewPost1=findViewById(R.id.imageViewPost1);
        //mImageViewPost1=findViewById(R.id.imageViewPost2);
        mTextInputTitle=findViewById(R.id.TxtInputMovie);
        mTextInputDescription=findViewById(R.id.TxtInputDescription);
        mImageViewAction=findViewById(R.id.imageViewAction);
        mImageViewTerror=findViewById(R.id.imageViewTerror);
        mImageViewComedia=findViewById(R.id.imageViewComedia);
        mImageViewAnimation=findViewById(R.id.imageViewAnimation);
        mTextViewAnime=findViewById(R.id.imageViewAnime);
        mTextViewRomance=findViewById(R.id.imageViewRomance);
        mTextViewCategory=findViewById(R.id.textViewCategory);

        mImageProvider= new ImageProviders();
        mPostProvider = new PostProvider();
        mAuthProviders=new AuthProviders();

        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // saveImage(); antes
                clickPost();
            }
        });

        mImageViewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoria = "Accion";
                mTextViewCategory.setText(mCategoria);
            }
        });

        mImageViewTerror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoria = "Terror";
                mTextViewCategory.setText(mCategoria);
            }
        });

        mImageViewComedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoria = "Comedia";
                mTextViewCategory.setText(mCategoria);
            }
        });

        mImageViewAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoria = "Animacion";
                mTextViewCategory.setText(mCategoria);
            }
        });

        mTextViewAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoria = "Anime";
                mTextViewCategory.setText(mCategoria);
            }
        });

        mTextViewRomance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoria = "Romance";
                mTextViewCategory.setText(mCategoria);
            }
        });
    }

    private void saveImage() {
        mImageProvider.save(PostActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=uri.toString();
                            Post post=new Post();
                            post.setImage1(url);
                            post.setTitle(mTitle);
                            post.setDescription(mDescription);
                            post.setCategory(mCategoria);
                            post.setIdUser(mAuthProviders.getUid());

                            mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> tasksave) {
                                    if(tasksave.isSuccessful()){
                                        Toast.makeText(PostActivity.this, "La imagen se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(PostActivity.this, "No se pudo almacenar la imagen", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    //Toast.makeText(PostActivity.this, "La imagen se almaceno correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PostActivity.this, "Error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clickPost() {
        mTitle=mTextInputTitle.getText().toString();
        mDescription=mTextInputDescription.getText().toString();
        if(!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategoria.isEmpty()){
            if(mImageFile !=null){
                saveImage();
            }else {
                Toast.makeText(this, "Selecciona una imagen", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Completa los campos para publicar", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* VALIDACION DE IMAGEN CON GALERIA */
        if (requestCode == Gallery_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}