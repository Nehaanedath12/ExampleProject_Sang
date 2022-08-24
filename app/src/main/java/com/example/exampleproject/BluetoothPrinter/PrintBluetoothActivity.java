package com.example.exampleproject.BluetoothPrinter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.example.exampleproject.databinding.ActivityPrintBluetoothBinding;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import retrofit2.http.Body;

public class PrintBluetoothActivity extends AppCompatActivity {

    ActivityPrintBluetoothBinding binding;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    boolean stopWorker=false;
    int readBufferPosition=0;
    byte[] readBuffer;
    Thread workerThread;
    File file;
    List<BodyClass>bodyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrintBluetoothBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        createPDF();


        bodyList=new ArrayList<>();
        BodyClass bodyClass=new BodyClass("Product1","99","89","11","22","0928098w0");
        bodyList.add(bodyClass);
        BodyClass bodyClass1=new BodyClass("Product2","99","66","11","88","090828098w0");
        bodyList.add(bodyClass1);
//        BodyClass bodyClass2=new BodyClass("Product13","99","89","11","22","0928098w0");
//        bodyList.add(bodyClass2);
//        BodyClass bodyClass3=new BodyClass("Product14","99","89","11","22","0928098w0");
//        bodyList.add(bodyClass3);
//        BodyClass bodyClass4=new BodyClass("Product5","99","89","11","22","0928098w0");
//        bodyList.add(bodyClass4);


        binding.connection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    findBT();
                    openBT();
                } catch (Exception ex) {
                }
            }
        });
        // send data typed by the user to be printed
        binding.send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    sendData();
                } catch (IOException ex) {
                }
            }
        });

        // close bluetooth connection
        binding.close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException ex) {
                }
            }
        });



    }

    private void createPDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo myPageinfo = new PdfDocument.PageInfo.Builder(250, 400, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(myPageinfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawText("pdf testing ", 40, 50, paint);
        pdfDocument.finishPage(myPage);
//         file=new File(this.getExternalFilesDir("/"),"FirstPDF.pdf");
        file = new File(Environment.getExternalStorageDirectory() + "/FirstPDF.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF Created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "PDF Not Created", Toast.LENGTH_SHORT).show();

        }
        pdfDocument.close();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Uri apkURI = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
//            intent.setDataAndType(apkURI, "application/pdf");
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        } else {
//            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//        }
//        startActivity(intent);
    }

    private void sendData() throws IOException {
        try {

            List<PrinterClass> list=new ArrayList<>();
            PrinterClass printerClass=new PrinterClass(1,0,0,"H","L",
                    "Customer:","Y",1);
            list.add(printerClass);
            PrinterClass printerClass1=new PrinterClass(1,0,0,"H","R",
                    "InvoiceNo:","Y",2);
            list.add(printerClass1);
            PrinterClass printerClass2=new PrinterClass(1,1,0,"H","R",
                    "Date:","Y",3);
            list.add(printerClass2);
            PrinterClass printerClass3=new PrinterClass(1,3,0,"H","C",
                    "===============","Y",0);
            list.add(printerClass3);


            PrinterClass printerClass4=new PrinterClass(1,0,0,"B","C",
                    "Product","N",1);
            list.add(printerClass4);
            PrinterClass printerClass5=new PrinterClass(1,0,0,"B","C",
                    "Qty","N",2);
            list.add(printerClass5);
            PrinterClass printerClass6=new PrinterClass(1,0,0,"B","C",
                    "UOM","N",3);
            list.add(printerClass6);
            PrinterClass printerClass7=new PrinterClass(1,0,0,"B","C",
                    "Rate","N",4);
            list.add(printerClass7);
            PrinterClass printerClass8=new PrinterClass(1,0,0,"B","C",
                    "GROSS","N",5);
            list.add(printerClass8);
            PrinterClass printerClass9=new PrinterClass(1,1,0,"B","L",
                    "BARCODE","N",6);
            list.add(printerClass9);
            PrinterClass printerClass10=new PrinterClass(1,2,0,"B","C",
                    "------------------","",0);
            list.add(printerClass10);

            PrinterClass printerClass11=new PrinterClass(1,0,0,"F","C",
                    "------------------","",0);
            list.add(printerClass11);
            PrinterClass printerClass12=new PrinterClass(1,1,0,"F","R",
                    "Total Qty","",1);
            list.add(printerClass12);
            PrinterClass printerClass13=new PrinterClass(1,2,0,"F","R",
                    "Total Net","",2);
            list.add(printerClass13);
            PrinterClass printerClass14=new PrinterClass(1,3,0,"F","C",
                    "------------------","",0);
            list.add(printerClass14);



            // the text typed by the user
            //String msg = myTextbox.getText().toString();
            //msg += "\n";

//            InputStream is = this.openFileInput("FirstPDF.pdf"); // Where this is Activity
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            byte[] b = new byte[1024];
//            int bytesRead;
//            while (( bytesRead = is.read(b)) != -1) {
//                bos.write(b, 0, bytesRead);
//            }
//            byte[] bytes = bos.toByteArray();
//
//            byte[] printformat = { 27, 33, 0 }; //try adding this print format
//
//            mmOutputStream.write(printformat);
//            mmOutputStream.write(bytes);
//
//            // tell the user data were sent
//            binding.myLabel.setText("Data Sent");

            {
                try {

                    String customer="CustomerName";
                    String docNo="docNo";
                    String Date="10-10-2020";


                    StringBuilder sb1 = new StringBuilder();
                    int k=0;
                    for (int i=0;i<list.size();i++){
                        if(list.get(i).Section.equals("H")){

                            Log.d("rowNo",list.get(i).RowNo+" "+k);
                            if(k!=list.get(i).RowNo){
                                sb1.append("\n");
                            }
//                            sb1.append("["+list.get(i).Alignment+"]<b>"+list.get(i).FieldName+" "+customer+"</b>");
                            switch (list.get(i).rawId){
                                case 1:
                                    sb1.append("["+list.get(i).Alignment+"]<b>"+list.get(i).FieldName+" "+customer+"</b>");
                                    break;
                                case 2:
                                    sb1.append("["+list.get(i).Alignment+"]<b>"+list.get(i).FieldName+" "+docNo+"</b>");
                                    break;
                                case 3:
                                    sb1.append("["+list.get(i).Alignment+"]<b>"+list.get(i).FieldName+" "+Date+"</b>");
                                    break;
                                case 0:
                                    sb1.append("["+list.get(i).Alignment+"]<b>"+list.get(i).FieldName+"</b>");
                                    break;
                                default:
                                    break;

                            }

                        }
                        k=list.get(i).RowNo;
                    }
                    int raw=0;
                    StringBuilder sb = new StringBuilder();
                    for (int i=0;i<list.size();i++) {
                        if (list.get(i).Section.equals("B")) {
                            Log.d("rowNo1",list.get(i).RowNo+" "+raw);
                            if(raw!=list.get(i).RowNo) {
                                sb.append("\n");
                            }
//                            sb.append("[C]Product[C]Qty[C]rate[C]gross[C]vat[C]net</b>\n");
                                sb.append("[" + list.get(i).Alignment + "]<b>" + list.get(i).FieldName + "</b>");
                            }
                        raw=list.get(i).RowNo;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    try{

                        for (int j=0;j<bodyList.size();j++) {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).Section.equals("B")) {

                                    String brkStart="";
                                    String brkEnd="";
                                    if(list.get(i).Break.equals("Y")){
                                        brkStart="<b>";
                                        brkEnd="</b>";
                                    }

                                    switch (list.get(i).rawId) {
                                        case 1:
                                            sb2.append("[" + list.get(i).Alignment + "]"+brkStart + bodyList.get(j).product +brkEnd);
                                            break;
                                        case 2:
                                            sb2.append("[" + list.get(i).Alignment + "]"+brkStart + bodyList.get(j).qty +brkEnd);
                                            break;
                                        case 3:
                                            sb2.append("[" + list.get(i).Alignment + "]"+brkStart + bodyList.get(j).uom + brkEnd);
                                            break;
                                        case 4:
                                            sb2.append("[" + list.get(i).Alignment + "]"+brkStart + bodyList.get(j).rate + brkEnd);
                                            break;
                                        case 5:
                                            sb2.append("[" + list.get(i).Alignment + "]"+brkStart+ bodyList.get(j).gross + brkEnd);
                                            break;
                                        case 6:
                                            sb2.append("\n");
                                            sb2.append("[" + list.get(i).Alignment + "]"+brkStart + bodyList.get(j).barcode + brkEnd);
                                            break;
                                        case 0:
                                            sb2.append("\n");
                                            sb2.append("[" + list.get(i).Alignment + "]"+brkStart + list.get(i).FieldName + brkEnd);
                                            break;
                                        default:
                                            break;

                                    }
                                }
                            }
                            sb2.append("\n");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("erroeee",e.toString());
                    }


                    StringBuilder sbFooter = new StringBuilder();
                    int newRow=0;
                    try{
                    for (int i=0;i<list.size();i++){
                        if(list.get(i).Section.equals("F")){

                            Log.d("rowNo",list.get(i).RowNo+" "+newRow);
                            if(newRow!=list.get(i).RowNo){
                                sbFooter.append("\n");
                            }
//                            sb1.append("["+list.get(i).Alignment+"]<b>"+list.get(i).FieldName+" "+customer+"</b>");
                            switch (list.get(i).rawId){
                                case 1:
                                    sbFooter.append("["+list.get(i).Alignment+"]<b>"+list.get(i).FieldName+" "+"90.00"+"</b>");
                                    break;
                                case 2:
                                    sbFooter.append("["+list.get(i).Alignment+"]<b>"+list.get(i).FieldName+" "+"9.88"+"</b>");
                                    break;
                                case 0:
                                    sbFooter.append("["+list.get(i).Alignment+"]<b>"+list.get(i).FieldName+"</b>");
                                    break;
                                default:
                                    break;

                            }

                        }
                        newRow=list.get(i).RowNo;
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("erroeee",e.toString());
                    }

                    EscPosPrinter printer=new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(),203,104f,69);
                    printer.printFormattedText("" +
                            "[C]<u><font size='big'>Van Sales</font></u>" +
                            "\n"+
                            sb1+
                            "\n"+
                            sb+
                            "\n"+
                            sb2+
                            "\n"+
                            sbFooter
                    );
                    binding.myLabel.setText("Sending Data");
                } catch (Exception e) {
                e.printStackTrace();
                Log.d("erroeee",e.toString());
            }
            }



            closeBT();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            binding.close.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openBT()throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            binding.myLabel.setText("Bluetooth Opened");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void findBT() {

        try {

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


            if (mBluetoothAdapter == null) {
//                myLabel.setText("No bluetooth adapter available");
                Toast.makeText(PrintBluetoothActivity.this, "No bluetooth  available", Toast.LENGTH_LONG).show();
                // startActivity(new Intent(MainActivity.this,NewAct.class));
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
                Toast.makeText(this, "OPen", Toast.LENGTH_LONG).show();

                //startActivity(new Intent(MainActivity.this,NewAct.class));
            }


            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();

            if (pairedDevices.size() > 0) {


                for (BluetoothDevice device : pairedDevices) {

                    // MP300 is the name of the bluetooth printer device

                    Log.d("BluetoothDevice",device.getName()+"");
//                    if (device.getName().equals("vivo 1929")) {
                        //openBT();
                        mmDevice = device;
                        break;
//                    }
//                    else {
//
//                    }
                }
            }
            {
                binding.myLabel.setText("Bluetooth Device Found");
                try {
                    // Standard SerialPortService ID
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                    mmSocket.connect();
                    mmOutputStream = mmSocket.getOutputStream();
                    mmInputStream = mmSocket.getInputStream();

                    beginListenForData();

                    binding.myLabel.setText("Bluetooth Opened");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                binding.myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}