package com.example.exampleproject.BluetoothPrinter;

public class PrinterClass {
    int  LayoutId,RowNo,fontSize,rawId,bodyCount;
    String Section,Alignment,FieldName,Break;

    public PrinterClass(int layoutId, int rowNo, int fontSize, String section,
                        String alignment, String fieldName, String aBreak,int rawId) {
        LayoutId = layoutId;
        RowNo = rowNo;
        this.fontSize = fontSize;
        Section = section;
        Alignment = alignment;
        FieldName = fieldName;
        Break = aBreak;
        this.rawId=rawId;
    }
}
