package com.zxing.control.decode;

public enum DecodeType {
    /**
     * 单个二维码
     */
    SINGLE_QRCODE,
    /**
     * 单个条形码
     */
    SINGLE_BARCODE,
    /**
     * 单个二维码和条形码
     */
    SINGLE_ALL,

    /**
     * 多个二维码
     */
    MULTI_QRCODE,
    /**
     * 多个条形码
     */
    MULTI_BARCODE,
    /**
     * 多个条形码和二维码
     */
    MULTI_ALL;
}
