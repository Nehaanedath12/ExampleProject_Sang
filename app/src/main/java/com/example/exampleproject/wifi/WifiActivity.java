package com.example.exampleproject.wifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.exampleproject.databinding.ActivityWifiBinding;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class WifiActivity extends AppCompatActivity {
    ActivityWifiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWifiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                createPDF(Commons.getAppPath(WifiActivity.this)+"test_pdf.pdf");
                createPDF1(Commons.getAppPath(WifiActivity.this)+"test_pdf.pdf");
            }
        });

    }

    private void createPDF1(String path) {

        File file=new File(path);

        if(new File(path).exists()){
            new File(path).delete();
        }

        Document document = new Document();
        document.setMargins(0f,0f,20f,20f);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Paragraph paragraph;
            paragraph = new Paragraph("SALES INVOICE");
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            PdfPTable tableH=new PdfPTable(2);
            tableH.addCell(getCell("DocNo:  Usr-01-01", PdfPCell.ALIGN_LEFT));
            tableH.addCell(getCell("Date: 2020-02-02", PdfPCell.ALIGN_RIGHT));
            tableH.setSpacingAfter(20);
            document.add(tableH);

            PdfPTable tableH1=new PdfPTable(2);
            tableH1.addCell(getCell("Customer:  Cus Name", PdfPCell.ALIGN_LEFT));
            tableH1.addCell(getCell("Order No: ORDER-01", PdfPCell.ALIGN_RIGHT));
            tableH1.setSpacingAfter(20);
            document.add(tableH1);

            DottedLineSeparator separator = new DottedLineSeparator();
//            separator.setPercentage(59500f / 523f);
            Chunk linebreak = new Chunk(separator);
            document.add(linebreak);

            PdfPTable table = new PdfPTable(8);


            table.setSpacingBefore(10);
            table.setWidths(new int[]{100, 100,100,100,40,60,50,50});
            table.addCell("Date");
            table.addCell("Start");
            table.addCell("End");
            table.addCell("Product");
            table.addCell("Total1");
            table.addCell("Total2");
            table.addCell("Total3");
            table.addCell("Total4");

            for (int i=0;i<2;i++){

                table.addCell("date "+i);
                table.addCell("start "+i);
                table.addCell("End "+i);
                table.addCell("Product Name "+i);
                table.addCell("Total1 "+i);
                table.addCell("Total2 "+i);
                table.addCell("Total3 "+i);
                table.addCell("Total4 "+i);

            }
            document.add(table);
            document.addCreationDate();
            document.close();


            printPDF(file);


//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                Uri apkURI = FileProvider.getUriForFile(this,getPackageName() + ".provider", file);
//                intent.setDataAndType(apkURI, "application/pdf");
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            } else {
//                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//            }
//            startActivity(intent);


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private void createPDF(String path) {
        android.graphics.pdf.PdfDocument pdfDocument=new android.graphics.pdf.PdfDocument();
        Paint paint=new Paint();
//        android.graphics.pdf.PdfDocument.PageInfo myPageinfo=new android.graphics.pdf.PdfDocument.PageInfo.Builder(250,400,1).create();

        PdfDocument.PageInfo myPageinfo = new PdfDocument
                .PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page myPage=pdfDocument.startPage(myPageinfo);
        Canvas canvas=myPage.getCanvas();

        int startYPosition = 20;

        paint.setTextSize(20f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("TEST PRINT ",20, startYPosition += 20, paint);

        paint.setTextSize(13f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Date", 20, startYPosition += 40, paint);
        canvas.drawText(": "+"2020-02-02", 120, startYPosition , paint);

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Doc No ",20, startYPosition += 25, paint);
        canvas.drawText(": "+"Doc -01-01",120, startYPosition , paint);

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Vendor Name", 20, startYPosition += 25, paint);
        canvas.drawText(": "+"customer", 120, startYPosition, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Narration", 20, startYPosition += 25, paint);
        canvas.drawText(": "+"narration", 120, startYPosition, paint);

        int endXPosition = myPageinfo.getPageWidth() - 10;
        paint.setTextSize(10f);
//        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.LEFT);


        canvas.drawText("S.no", 20, startYPosition += 30, paint);
        canvas.drawText("Code", 50, startYPosition, paint);
        canvas.drawText("Product Name", 150, startYPosition, paint);
        canvas.drawText("Unit", myPageinfo.getPageWidth()-260 ,startYPosition, paint);
        canvas.drawText("Qty", myPageinfo.getPageWidth()-230, startYPosition, paint);
        canvas.drawText("PO No.", myPageinfo.getPageWidth()-190, startYPosition, paint);
        canvas.drawText("Remarks", myPageinfo.getPageWidth()-135, startYPosition, paint);

        canvas.drawLine(10,startYPosition+3,endXPosition,startYPosition+3,paint);

        paint.setTextSize(8f);

//        canvas.drawText("welcome Nehaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//                "sdnlvnlssssssssssssssssssssssss111111111111111111111111111111111111111111111111111111111111111111111111111" +
//                "22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222" +
//                "33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333" +
//                "44444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444" +
//                "55555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555" +
//                "66666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666" +
//                "77777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777" +
//                "88888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888" +
//                "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999" +
//                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
//                "",40,50,paint);
        

        pdfDocument.finishPage(myPage);
        File file=new File(path);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF Created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "PDF Not Created", Toast.LENGTH_SHORT).show();

        }
        pdfDocument.close();

        printPDF(file);

    }

    private void printPDF(File file) {
        PrintManager printManager=(PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            PDFDocumentAdapter adapter=new PDFDocumentAdapter(WifiActivity.this,Commons.getAppPath(this)+"test_pdf.pdf");
            printManager.print("Document",adapter,new PrintAttributes.Builder().build());
        }catch (Exception e){
            e.printStackTrace();
            Log.d("errorWifi",e.toString()+""+e.getLocalizedMessage());
        }
    }
}