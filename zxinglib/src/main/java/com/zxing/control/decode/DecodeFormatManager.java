package com.zxing.control.decode;


import com.google.zxing.BarcodeFormat;

import java.util.Vector;
import java.util.regex.Pattern;


public class DecodeFormatManager {

    private static final Pattern COMMA_PATTERN = Pattern.compile(",");

    public static final Vector<BarcodeFormat> PRODUCT_FORMATS;
    public static final Vector<BarcodeFormat> ONE_D_FORMATS;
    public static final Vector<BarcodeFormat> QR_CODE_FORMATS;
    public static final Vector<BarcodeFormat> DATA_MATRIX_FORMATS;
    static {
        //一维码:商业
        PRODUCT_FORMATS = new Vector<BarcodeFormat>(5);
        PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
        PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
        PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
        PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);

        //一维码:工业
        ONE_D_FORMATS = new Vector<BarcodeFormat>(PRODUCT_FORMATS.size() + 4);
        ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
        ONE_D_FORMATS.add(BarcodeFormat.ITF);

        //二维码
        QR_CODE_FORMATS = new Vector<BarcodeFormat>(1);
        QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);

        //一张图有多个码
        DATA_MATRIX_FORMATS = new Vector<BarcodeFormat>(1);
        DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);
    }

}
