package com.example.exampleproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.exampleproject.Alarm.AlarmActivity;
import com.example.exampleproject.BluetoothPrinter.PrintBluetoothActivity;
import com.example.exampleproject.BroadCast.BroadCastActivity;
import com.example.exampleproject.CaptureImage.CapturingImageActivity;
import com.example.exampleproject.Date.DateActivity;
import com.example.exampleproject.FCM.MessagesActivity;
import com.example.exampleproject.GraphicalRep.BubbleChartActivity;
import com.example.exampleproject.GraphicalRep.CandleStickChartActivity;
import com.example.exampleproject.GraphicalRep.GraphicalActivity;
import com.example.exampleproject.GraphicalRep.LineChartActivity;
import com.example.exampleproject.GraphicalRep.PieChartActivity;
import com.example.exampleproject.GraphicalRep.RadarChartActivity;
import com.example.exampleproject.GraphicalRep.ScatteredChartActivity;
import com.example.exampleproject.ImageOneByOne.PickImageActivity;
import com.example.exampleproject.LoadallImage.MainActivity;
import com.example.exampleproject.Map.MapActivity;
import com.example.exampleproject.Payment.PaymentActivity;
import com.example.exampleproject.RetroFit.RetrofitActivity;
import com.example.exampleproject.Watsp.BackGroundActivity;
import com.example.exampleproject.databinding.ActivityHomeBinding;
import com.example.exampleproject.notification.BackgroundTaskJava;
import com.example.exampleproject.otp.OTPActivity;
import com.example.exampleproject.wifi.WifiActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.snackbar.Snackbar;
import com.shasin.notificationbanner.Banner;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class Home extends AppCompatActivity {
    Button all, one, mapText, captureImage, network,graph,pieGraph,
            radar_graph_Home,line_graph_Home,bubbleChart,candle_graph_Home,scattered_graph_Home,pdfCreate;
    LinearLayout linearLayout;
    String dirpath;
    File file;
    WebView  webview;
    PDFView pdfView;
    Button payment,sign;

    ActivityHomeBinding binding;
    LocalReceiver myReceiver;



    @Override
    protected void onStop () {
        super .onStop() ;
//        startService( new Intent( this, NotificationService. class )) ;
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        all = findViewById(R.id.all_image);
        one = findViewById(R.id.one_by_one);
        mapText = findViewById(R.id.mapText);
        captureImage = findViewById(R.id.capture_image);
        network = findViewById(R.id.network);
        graph=findViewById(R.id.graph_Home);
        pieGraph=findViewById(R.id.pie_graph_Home);
        radar_graph_Home=findViewById(R.id.radar_graph_Home);
        line_graph_Home=findViewById(R.id.line_graph_Home);
        bubbleChart=findViewById(R.id.bubble_graph_Home);
        candle_graph_Home=findViewById(R.id.candle_graph_Home);
        scattered_graph_Home=findViewById(R.id.scattered_graph_Home);
        pdfCreate=findViewById(R.id.pdf);
//        webview=findViewById(R.id.webview);
        pdfView=findViewById(R.id.pdfView);
        payment=findViewById(R.id.payment);
        sign=findViewById(R.id.sign);
        myReceiver = new LocalReceiver();
        
        
        
        if(getIntent().getExtras()!=null){
            for (String key: getIntent().getExtras().keySet()){
                Log.d("keyyyy",key+"");

            }
        }


        Constraints constraints = new Constraints.Builder().setRequiresCharging(true).setRequiredNetworkType(NetworkType.UNMETERED).build();

        final PeriodicWorkRequest periodicWorkRequest1 = new PeriodicWorkRequest.Builder(BackgroundTaskJava.class,
                1, TimeUnit.MILLISECONDS)
                .setInitialDelay(2000,TimeUnit.MILLISECONDS)
                .build();
        WorkManager workManager =  WorkManager.getInstance(this);

        workManager.enqueue(periodicWorkRequest1);
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest1.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            Log.d("periodicWorkRequest", "Status changed to : " + workInfo.getState());

                        }
                    }
                });

        binding.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MessagesActivity.class));
            }
        });

        binding.imei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
                } else {
                    startActivity(new Intent(getApplicationContext(), IMEIActivity.class));
                }

            }
        });
        binding.otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), OTPActivity.class));
                }
        });
        binding.watspBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BackGroundActivity.class));
            }
        });
        binding.broadCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BroadCastActivity.class));
            }
        });


        binding.imageAndCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CaptureAndGalleryActivity.class));
            }
        });

        binding.alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AlarmActivity.class));
            }
        });
        binding.printBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PrintBluetoothActivity.class));
            }
        });
        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DateActivity.class));
            }
        });
        binding.wifiPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PackageManager.PERMISSION_GRANTED);
                startActivity(new Intent(getApplicationContext(), WifiActivity.class));

            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PaymentActivity.class));

            }
        });

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        pdfCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPDF();
            }
        });





        network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.isConnected(getApplicationContext())) {
                    Toasty.success(Home.this, "You have internet", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.error(Home.this,"No Internet!!",Toast.LENGTH_LONG).show();
                    Banner.make(v,Home.this,Banner.INFO,"not success",Banner.TOP).show();
                    Snackbar snackbar = Snackbar.make(v, "Please connect to network!! ", Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }
            }
        });

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CapturingImageActivity.class));
            }
        });
        binding.retrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RetrofitActivity.class));
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PickImageActivity.class));
            }
        });

        mapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
            }
        });
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GraphicalActivity.class));
            }
        });
        pieGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PieChartActivity.class));
            }
        });
        radar_graph_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RadarChartActivity.class));
            }
        });
        line_graph_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LineChartActivity.class));
            }
        });
        bubbleChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BubbleChartActivity.class));
            }
        });
        candle_graph_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CandleStickChartActivity.class));
            }
        });
        scattered_graph_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScatteredChartActivity.class));
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                String smsNumber = "+919605286342"; // E164 format without '+' sign
//                Intent sendIntent = new Intent(Intent.ACTION_SEND);
//                sendIntent.setType("text/plain");
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//                sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
//                sendIntent.setPackage("com.whatsapp");
//                if(getPackageManager() == null) {
//                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
////                    Toast.makeText(this, "Error/n" , Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                startActivity(sendIntent);

////////////////////////////////////////////////////////////////////////////////////////////////
                PackageManager packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ "+919605286342" +"&text=" + URLEncoder.encode("Hai", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }


//                startActivity(new Intent(getApplicationContext(), SignMainActivity.class));
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter("NOTIFICATION_LOCAL_BROADCAST");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, filter);
    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    private void updateUI(Intent intent) {
        binding.noti.setText("You have Notification");
        // do what you need to do
    }

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    }


    private void createPDF() {
        PdfDocument pdfDocument=new PdfDocument();
        Paint paint=new Paint();
        PdfDocument.PageInfo myPageinfo=new PdfDocument.PageInfo.Builder(250,400,1).create();


        PdfDocument.Page myPage=pdfDocument.startPage(myPageinfo);
        Canvas canvas=myPage.getCanvas();
        canvas.drawText("welcome Neha",40,50,paint);
        pdfDocument.finishPage(myPage);
//         file=new File(this.getExternalFilesDir("/"),"FirstPDF.pdf");
        file=new File(Environment.getExternalStorageDirectory()+"/FirstPDF.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF Created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "PDF Not Created", Toast.LENGTH_SHORT).show();

        }
        pdfDocument.close();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkURI = FileProvider.getUriForFile(this,getPackageName() + ".provider", file);
            intent.setDataAndType(apkURI, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        }
        startActivity(intent);

//        openPDF();

//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setPluginsEnabled(true);
//        webview.getSettings().setAllowFileAccess(true);
//        File file = new File(Environment.getExternalStorageDirectory() + "/test.pdf");

//        final Uri uri = Uri.fromFile(file);

//        webview.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + file.getAbsolutePath() + "#zoom=page-width");


    }

    private void openPDF() {



//        File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/FirstPDF.pdf");
//        Uri path = Uri.fromFile(file);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setData(path);
//        intent.setType("application/pdf");
//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(this, "No application found",
//                    Toast.LENGTH_SHORT).show();
//        }



//        File pdfFile = new File(Environment.getExternalStorageDirectory(),"FirstPDF.pdf");//File path
//        if (pdfFile.exists()) //Checking if the file exists or not
//        {
//            Uri path = Uri.fromFile(pdfFile);
//            Intent objIntent = new Intent(Intent.ACTION_VIEW);
//            objIntent.setDataAndType(path, "application/pdf");
//            objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(objIntent);//Starting the pdf viewer
//        } else {
//
//            Toast.makeText(this, "The file not exists! ", Toast.LENGTH_SHORT).show();
//
//        }
    }



}