package com.example.exampleproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.example.exampleproject.ImageOneByOne.ImageAdapter;
import com.example.exampleproject.ImageOneByOne.ImageClass;
import com.example.exampleproject.databinding.ActivityCaptureAndGalleryBinding;
import com.example.exampleproject.databinding.AlertImageBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.result.BitmapPhoto;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.result.WhenDoneListener;
import io.fotoapparat.view.CameraView;

public class CaptureAndGalleryActivity extends AppCompatActivity {

    ActivityCaptureAndGalleryBinding binding;
    AlertDialog alertDialog;
    AlertImageBinding imageBinding;
    List<ImageClass> list;
    ImageAdapter adapter;
    List<String> imageList;
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCaptureAndGalleryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        list = new ArrayList<>();

        imageList=new ArrayList<>();
        adapter = new ImageAdapter(getApplicationContext(), list);
        binding.recyclerImage.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        binding.recyclerImage.setLayoutManager(manager);

        imageBinding = AlertImageBinding.inflate(getLayoutInflater(),null,false);
        AlertDialog.Builder builder1=new AlertDialog.Builder(this);
        builder1.setView(imageBinding.getRoot());
        alertDialog =builder1.create();

        binding.list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sizeList", String.valueOf(imageList.size()));
                for (int i=0;i<imageList.size();i++){
                    Log.d("sizeList", imageList.get(i)+"");
                }
            }
        });
        new DownloadImage().execute("http://185.151.4.167/SangHRMS/Data/image/2021/12/27/22/IMG_1640623866464.jpg");

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.show();

                imageBinding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                imageBinding.pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CaptureAndGalleryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
//                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("application/pdf");
                            startActivityForResult(intent,3);
                        }

                    }});

                imageBinding.capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CaptureAndGalleryActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                        } else {
                            addCaptureImage();
                            if (list.size() > 0) {
                                Log.d("sizeList", String.valueOf(list.size()));
                            }
                        }

                    }});

                imageBinding.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                    }
                });

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                if(data!=null) {
                    ImageClass photoClass = new ImageClass(data.getData());
                    try {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        Bitmap bitmap  = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        File f=new File(Tools.savePhoto(CaptureAndGalleryActivity.this,bitmap));
                        imageList.add(Uri.fromFile(f)+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    list.add(photoClass);
                    adapter.notifyDataSetChanged();
                }

            }
            if(requestCode==3){


                Uri uri = data.getData();


                String fileName="";
                UUID uuid = UUID.randomUUID();
                Log.d("UUIDUUID","UUID=" + uuid.toString() );
                if (uri != null ) {
                    Cursor returnCursor =
                            getContentResolver().query(uri, null, null, null, null);
                    if (returnCursor != null) {
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        if (nameIndex >= 0 && sizeIndex >= 0) {
                            Log.d("File Name : ", returnCursor.getString(nameIndex) + "");
                            Log.d("File Size : ", Long.toString(returnCursor.getLong(sizeIndex)));
//                    Boolean isValidFile = checkFormat(returnCursor.getString(nameIndex));
                            Log.d("getFileName",returnCursor.getString(nameIndex));
                            fileName=returnCursor.getString(nameIndex);
//                    if(!isValidFile){
//                    return returnCursor.getString(nameIndex);
//                    }
                        }
                    }
                }

                String path = getFilePathFromURI(CaptureAndGalleryActivity.this,uri,fileName);
                Log.d("ioooo",path);




                uploadPDF(path,fileName);








//                assert data != null;
//                Uri uri=data.getData();
//                String uriString = uri.toString();
//
//                File myFile = new File(uriString);
//
//
//                final String path = myFile.getAbsolutePath();
//                Log.d("displayName",path);
//
//                String displayName = null;
//
//                if (uriString.startsWith("content://")) {
//                    Cursor cursor = null;
//                    try {
//                        cursor = getContentResolver().query(uri, null, null, null, null);
//                        if (cursor != null && cursor.moveToFirst()) {
//                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
////                            tvFile.setVisibility ( View.VISIBLE );
////                            tvFile.setText ( displayName );
//
//                            Log.d("displayName",displayName);
//
//                            final String finalDisplayName = displayName;
//
//
//                            File dir = Environment.getExternalStorageDirectory();
//                            File yourFile = new File(dir, path+ finalDisplayName );
//
//                            Toast.makeText (CaptureAndGalleryActivity.this,"hiiiii"+yourFile,Toast.LENGTH_SHORT ).show ();
//                            Log.d("finalDisplayName",yourFile+"");
//                            //Toast.makeText ( getActivity (),"hii"+displayName,Toast.LENGTH_LONG ).show ();
//                        }
//                    } finally {
//                        cursor.close();
//                    }
//                }
            }
        }


    }

    private void uploadPDF(String path, String fileName) {
        List<File> fileList = new ArrayList<>();
        File file1 = new File(path);
        fileList.add(file1);


        JSONObject jsonObjectMain = new JSONObject();
        try {
            jsonObjectMain.put("iTransId", 23);
            jsonObjectMain.put("iEmployee", 1);
            jsonObjectMain.put("iLeaveType", 3);
            jsonObjectMain.put("sReason", "iii");
            jsonObjectMain.put("sFromDate", "2021-01-01");
            jsonObjectMain.put("sToDate", "2021-02-01");
            jsonObjectMain.put("fNoOfDays", 3);
            jsonObjectMain.put("sTelephone", "9999");
            jsonObjectMain.put("sMob", "9999");
            jsonObjectMain.put("sEmail", "nehamanedath@gmail.com");
            jsonObjectMain.put("sFileName", fileName);
            jsonObjectMain.put("iUser", "1");

            Log.d("jsonObjectMainn", jsonObjectMain.toString());

            Log.d("filefile", fileList + "");
            uploadToAPI(jsonObjectMain, fileList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void uploadToAPI(JSONObject jsonObjectMain, List<File> file) {
        if(Tools.isConnected(this)){

            alertDialog.show();
            try {
                AndroidNetworking.upload("http://185.151.4.167/SangHRMS/SangHRMS/PostLeaveApp")
                        .addMultipartParameter("json_content",jsonObjectMain.toString())
                        .setContentType("multipart/form-data")
                        .addMultipartFileList("file",file)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("responsePostt", response);

                                try {
                                    int i=Integer.parseInt(response);
                                    Toast.makeText(CaptureAndGalleryActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    finish();
                                }catch (Exception e){
                                    alertDialog.dismiss();
                                    Toast.makeText(CaptureAndGalleryActivity.this, response, Toast.LENGTH_SHORT).show();
//                                    String fnName=new Object() {}.getClass().getEnclosingClass().getSimpleName()+"."+ Objects.requireNonNull(new Object() {}.getClass().getEnclosingMethod()).getName();
//                                    Tools.logWrite(fnName,e,LeaveApplicationActivity.this);
                                }
                            }
                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(CaptureAndGalleryActivity.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                                Log.d("responsePost_R_P", anError.getErrorDetail() + anError.getErrorBody() + anError.toString());
                                alertDialog.dismiss();
                            }
                        });
            }catch (Exception e){
                alertDialog.dismiss();
//                String fnName=new Object() {}.getClass().getName()+"."+ Objects.requireNonNull(new Object() {}.getClass().getEnclosingMethod()).getName();
//                Tools.logWrite(fnName,e,LeaveApplicationActivity.this);
            }
        }else {
            Toast.makeText(this, "Offline", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFilePathFromURI(CaptureAndGalleryActivity context, Uri contentUri, String Name) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(wallpaperDirectory + File.separator +Name);
            // create folder if not exists

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    private void copy(CaptureAndGalleryActivity context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int copystream(InputStream input, OutputStream output) {

        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return count;
    }

    private String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    private void addCaptureImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.camera_layout, null, false);
        ImageView cancelImage, captureImage;
        CameraView cameraView = view.findViewById(R.id.camera_view);
        cancelImage = view.findViewById(R.id.cancel_image);
        captureImage = view.findViewById(R.id.capture_image);
        Fotoapparat fotoapparat = new Fotoapparat(this, cameraView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view);
        AlertDialog dialogue = builder.create();
        dialogue.show();
        fotoapparat.start();


        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogue.dismiss();
                fotoapparat.stop();
            }
        });
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoResult photoResult = fotoapparat.takePicture();
                photoResult.toBitmap().whenDone(new WhenDoneListener<BitmapPhoto>() {
                    @Override
                    public void whenDone(BitmapPhoto bitmapPhoto) {
                        if (bitmapPhoto != null) {
                            Log.d("captured_Image", bitmapPhoto.bitmap.toString());

//                            File f=new File(Tools.savePhoto(CaptureAndGalleryActivity.this,bitmapPhoto.bitmap));
//                            imageList.add(Uri.fromFile(f)+"");
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmapPhoto.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path1 = MediaStore.Images.Media.insertImage(getContentResolver(), bitmapPhoto.bitmap, "Title", null);

                            list.add(new ImageClass(Uri.parse(path1)));
                            adapter.notifyDataSetChanged();
                            fotoapparat.stop();

                        }
                        dialogue.dismiss();

                    }
                });

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                Log.d("DownloadImage",e.toString()+e.getLocalizedMessage()+e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(getApplicationContext(), result);
        }
    }

    public void saveImage(Context context, Bitmap b) {
        FileOutputStream foStream;
        Bitmap bitmap = null;
        String fileName = "IMG_"+System.currentTimeMillis() + ".jpg";
        try {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            foStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 100, foStream);
            foStream.close();
//            return stream.toByteArray();
            File folder = new File(context.getExternalFilesDir(null) + File.separator +"temp");

            if (!folder.exists()) {
                Log.d("folder created:", "" + folder.mkdir());
            }
            File file = new File(folder, fileName);

//            File file    = getApplicationContext().getFileStreamPath(fileName);
            String imageFullPath = file.getAbsolutePath();
            list.add(new ImageClass(Uri.fromFile(file)));

            Glide.with(context).load(imageFullPath).into(binding.imageView);
//            Glide.with(context).load("http://185.151.4.167/SangHRMS/Data/image/2021/12/27/22/IMG_1640623866464.jpg").into(binding.imageView);
//            adapter.notifyDataSetChanged();
            Log.d("imageFullPath", imageFullPath);
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            Log.d("saveImage",e.toString()+e.getLocalizedMessage()+e.getMessage());
            e.printStackTrace();
        }
    }
}