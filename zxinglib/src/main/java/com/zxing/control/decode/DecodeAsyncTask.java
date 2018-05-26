package com.zxing.control.decode;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.oned.MultiFormatOneDReader;
import com.zxing.control.ICropRect;
import com.zxing.control.inter.OnDiscernListener;
import com.zxing.result.ErrorResult;
import com.zxing.result.SuccessResult;

import java.util.EnumMap;
import java.util.Map;
import java.util.Vector;


public class DecodeAsyncTask extends AsyncTask<DecodeInfo, Void, DecodeAsyncTask.DecodeResult> {

    private static MultiFormatReader singleQrcodeFormatReader;
    private static MultiFormatReader singleBarcodeFormatReader;
    private static MultiFormatReader singleAllFormatReader;

    private static MultipleBarcodeReader multiQrcodeFormatReader;
    private static MultipleBarcodeReader multiBarcodeFormatReader;
    private static MultipleBarcodeReader multiAllFormatReader;

    private static MultipleBarcodeReader getMultiQrcodeFormatReader() {
        if (multiQrcodeFormatReader == null){
            synchronized (DecodeAsyncTask.class){
                if (multiQrcodeFormatReader == null){
                    Map<DecodeHintType, Object> hints = getDecodeHintTypeWithDecodeType(DecodeType.MULTI_QRCODE);
                    //初始化解码器为多格式类型
                    Reader reader = new MultiFormatOneDReader(hints);
                    //开启单张图片多个条码的支持
                    multiQrcodeFormatReader = new GenericMultipleBarcodeReader(reader);
                }
            }
        }

        return multiQrcodeFormatReader;
    }

    private static MultipleBarcodeReader getMultiBarcodeFormatReader() {
        if (multiBarcodeFormatReader == null){
            synchronized (DecodeAsyncTask.class){
                if (multiBarcodeFormatReader == null){
                    Map<DecodeHintType, Object> hints = getDecodeHintTypeWithDecodeType(DecodeType.MULTI_BARCODE);
                    //初始化解码器为多格式类型
                    Reader reader = new MultiFormatOneDReader(hints);
                    //开启单张图片多个条码的支持
                    multiBarcodeFormatReader = new GenericMultipleBarcodeReader(reader);
                }
            }
        }
        return multiBarcodeFormatReader;
    }

    private static MultipleBarcodeReader getMultiAllFormatReader() {
        if (multiAllFormatReader == null){
            synchronized (DecodeAsyncTask.class){
                if (multiAllFormatReader == null){
                    Map<DecodeHintType, Object> hints = getDecodeHintTypeWithDecodeType(DecodeType.MULTI_ALL);
                    //初始化解码器为多格式类型
                    Reader reader = new MultiFormatOneDReader(hints);
                    //开启单张图片多个条码的支持
                    multiAllFormatReader = new GenericMultipleBarcodeReader(reader);
                }
            }
        }
        return multiAllFormatReader;
    }

    private static MultiFormatReader getSingleQrcodeFormatReader() {
        if (singleQrcodeFormatReader == null){
            synchronized (DecodeAsyncTask.class){
                if (singleQrcodeFormatReader == null){
                    singleQrcodeFormatReader = createSingleFormatReader(DecodeType.SINGLE_QRCODE);
                }
            }
        }
        return singleQrcodeFormatReader;
    }

    private static MultiFormatReader getSingleBarcodeFormatReader() {
        if (singleBarcodeFormatReader == null){
            synchronized (DecodeAsyncTask.class){
                if (singleBarcodeFormatReader == null){
                    singleBarcodeFormatReader = createSingleFormatReader(DecodeType.SINGLE_BARCODE);
                }
            }
        }
        return singleBarcodeFormatReader;
    }

    private static MultiFormatReader getSingleAllFormatReader() {
        if (singleAllFormatReader == null){
            synchronized (DecodeAsyncTask.class){
                if (singleAllFormatReader == null){
                    singleAllFormatReader = createSingleFormatReader(DecodeType.SINGLE_ALL);
                }
            }
        }
        return singleAllFormatReader;
    }

    private static MultiFormatReader createSingleFormatReader(DecodeType decodeType) {

        Map<DecodeHintType, Object> hints = getDecodeHintTypeWithDecodeType(decodeType);

        MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);

        return multiFormatReader;
    }

    private static Map<DecodeHintType, Object> getDecodeHintTypeWithDecodeType(DecodeType decodeType) {
        Vector<BarcodeFormat> decodeFormats = new Vector<>();
        if (decodeType == DecodeType.SINGLE_QRCODE || decodeType == DecodeType.MULTI_QRCODE) {
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
        } else if (decodeType == DecodeType.SINGLE_BARCODE || decodeType == DecodeType.MULTI_BARCODE) {
            decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
        } else {
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
        }

        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        //设置编码为utf-8
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        return hints;
    }


    private OnDiscernListener onDiscernListener;
    private ICropRect iCropRect;

    private boolean debug;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public DecodeAsyncTask(OnDiscernListener onDiscernListener, ICropRect iCropRect) {
        this.onDiscernListener = onDiscernListener;
        this.iCropRect = iCropRect;
    }

    @Override
    protected DecodeResult doInBackground(DecodeInfo... params) {
        return decode(params[0]);
    }

    @Override
    protected void onPostExecute(DecodeResult decodeResult) {
        super.onPostExecute(decodeResult);

        if (onDiscernListener == null){
            return;
        }

        boolean isSingle = decodeResult.decodeType == DecodeType.SINGLE_QRCODE
                || decodeResult.decodeType == DecodeType.SINGLE_BARCODE
                || decodeResult.decodeType == DecodeType.SINGLE_ALL;

        if (isSingle){
            if (decodeResult.result == null) {
                //失败
                ErrorResult errorResult = new ErrorResult();
                errorResult.decodeType = decodeResult.decodeType;
                errorResult.previewBitmap = decodeResult.previewBitmap;
                errorResult.message = "解析失败";

                onDiscernListener.onError(errorResult);
            } else {
                //成功
                SuccessResult successResult = new SuccessResult();
                successResult.decodeType = decodeResult.decodeType;
                successResult.rawResult = decodeResult.result;
                successResult.previewBitmap = decodeResult.previewBitmap;

                onDiscernListener.onSuccess(successResult);
            }
        }else {
            if (decodeResult.results == null){
                //失败
                ErrorResult errorResult = new ErrorResult();
                errorResult.decodeType = decodeResult.decodeType;
                errorResult.previewBitmap = decodeResult.previewBitmap;
                errorResult.message = "解析失败";

                onDiscernListener.onError(errorResult);
            }else {
                //成功
                SuccessResult successResult = new SuccessResult();
                successResult.decodeType = decodeResult.decodeType;
                successResult.rawResults = decodeResult.results;
                successResult.previewBitmap = decodeResult.previewBitmap;

                onDiscernListener.onSuccess(successResult);
            }
        }
    }

    /**
     * 解码
     *
     * @param decodeInfo
     */
    private DecodeResult decode(DecodeInfo decodeInfo) {

        DecodeResult decodeResult = new DecodeResult();
        decodeResult.decodeType = decodeInfo.decodeType;

        Size previewSize = decodeInfo.cameraPreviewSize;

        byte[] sourceData = decodeInfo.decodeData;

//        // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
        byte[] rotatedData = rotateYUV420Degree90(sourceData, previewSize.width, previewSize.height);

        //修改反转后的图片尺寸
        int tmp = previewSize.width;
        previewSize.width = previewSize.height;
        previewSize.height = tmp;

        PlanarYUVLuminanceSource source = buildLuminanceSource(rotatedData, previewSize);
        if (source == null) {
            return decodeResult;
        }

        if (debug) {
            decodeResult.previewBitmap = Bitmap.createBitmap(source.renderThumbnail(), 0, source.getThumbnailWidth(),
                    source.getThumbnailWidth(), source.getThumbnailHeight(), Bitmap.Config.ARGB_4444);
        }

        if (decodeInfo.decodeType == DecodeType.SINGLE_QRCODE
                || decodeInfo.decodeType == DecodeType.SINGLE_BARCODE
                || decodeInfo.decodeType == DecodeType.SINGLE_ALL) {
            decodeResult.result = decodeSingle(decodeInfo.decodeType, source);
        } else {
            decodeResult.results = decodeMulti(decodeInfo.decodeType, source);
        }
        return decodeResult;
    }

    private Result[] decodeMulti(DecodeType decodeType, PlanarYUVLuminanceSource source) {
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        MultipleBarcodeReader multipleBarcodeReader;
        if (decodeType == DecodeType.MULTI_BARCODE){
            multipleBarcodeReader = getMultiBarcodeFormatReader();
        }else if (decodeType == DecodeType.MULTI_QRCODE){
            multipleBarcodeReader = getMultiQrcodeFormatReader();
        }else {
            multipleBarcodeReader = getMultiAllFormatReader();
        }
        try {
            return multipleBarcodeReader.decodeMultiple(bitmap);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Result decodeSingle(DecodeType decodeType, PlanarYUVLuminanceSource source) {
        MultiFormatReader multiFormatReader;
        if (decodeType == DecodeType.SINGLE_QRCODE){
            multiFormatReader = getSingleQrcodeFormatReader();
        }else if (decodeType == DecodeType.SINGLE_BARCODE){
            multiFormatReader = getSingleBarcodeFormatReader();
        }else {
            multiFormatReader = getSingleAllFormatReader();
        }

        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            return multiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException re) {
            // continue
        } finally {
            multiFormatReader.reset();
        }
        return null;
    }

    /*
  * 图像翻转90度
  *
  * @prama  data  原始YUV图像
  * @prama  imageWidth  宽度
  * @prama  imageHeight  高度
  *
  * return 翻转后的YUV图像
  * */
    public static byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth)
                        + (x - 1)];
                i--;
            }
        }
        return yuv;
    }

    private PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, Size previewSize) {
//        Rect rect = getFramingRectInPreview();
        Rect rect = new Rect();
        if (iCropRect != null && iCropRect.needCrop() && iCropRect.getCropRect(previewSize.width) != null) {
            rect = iCropRect.getCropRect(previewSize.width);
        } else {
            rect.left = 0;
            rect.top = 0;
            rect.right = previewSize.width;
            rect.bottom = previewSize.height;
        }

        // Go ahead and assume it's YUV rather than die.
        return new PlanarYUVLuminanceSource(data, previewSize.width, previewSize.height, rect.left, rect.top,
                rect.width(), rect.height(), false);
    }

    public class DecodeResult {
        private DecodeType decodeType;
        private Bitmap previewBitmap;
        private Result result;
        private Result[] results;
    }
}
