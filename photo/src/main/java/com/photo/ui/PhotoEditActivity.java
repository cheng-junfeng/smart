package com.photo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.photo.R;
import com.photo.R2;
import com.photo.app.PhotoBaseCompatActivity;
import com.photo.config.PhotoConfig;
import com.photo.ui.widget.EditImageView;
import com.photo.ui.widget.model.DrawPoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class PhotoEditActivity extends PhotoBaseCompatActivity {
    private static final String TAG = "PhotoEditActivity";

    @BindView(R2.id.edit_view)
    EditImageView editView;

    private int default_res = R.drawable.photo_default_image;

    Paint paint;
    private Canvas bitCanvas;
    private Context mContext;

    String urlStr;
    String titleStr;

    @Override
    protected int setContentView() {
        return R.layout.photo_activity_edit_pic;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        Bundle bundle = getIntent().getExtras();
        urlStr = "/storage/emulated/0/PIC/1527825287793.jpg";
//		urlStr = (bundle == null) ? null : bundle.getString(PhotoConfig.PHOTO_URL);
        titleStr = (bundle == null) ? PhotoConfig.DEFAULT_TITLE : bundle.getString(PhotoConfig.PHOTO_NAME, PhotoConfig.DEFAULT_TITLE);
        ToolbarUtil.setToolbarLeft(toolbar, titleStr, null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initView();
    }

    protected void initView() {
        editView.setIsEdit(true);
        paint = editView.getPaint();

        File file = new File(urlStr);
        //show the image if it exist in local path
        if (file != null && file.exists()) {
            Glide.with(this)
                    .load(file)
                    .asBitmap()
                    .error(default_res)
                    .into(editView);
        } else {
            editView.setImageResource(default_res);
        }
    }

    @OnClick({R2.id.btnSave})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if(viewId == R.id.btnSave){
            LogUtil.d(TAG, "save:onViewClicked");
            DialogUtils.showLoading(mContext, "正在保存");
            //现在是主线程，需要优化
            Glide.with(mContext)
                    .load(urlStr)
                    .asBitmap() //必须
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            LogUtil.d(TAG, "save:onResourceReady:"+Thread.currentThread());
                            drawLine(resource);
                        }
                    });
        }
    }

    int bitmapWidth;
    int bitmapHeight;
    private void drawLine(final Bitmap bitmap) {
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        bitCanvas = new Canvas(bitmap);
        List<DrawPoint> points = pointsChange();
        float x = 0f;
        float y = 0f;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).isStart) {
                x = points.get(i).x;
                y = points.get(i).y;
            } else {
                bitCanvas.drawLine(x, y, points.get(i).x, points.get(i).y, paint);
                x = points.get(i).x;
                y = points.get(i).y;
            }
        }
        final String filePath = saveImg(bitmap);
        LogUtil.d(TAG, "filePath:"+filePath);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtils.dismissLoading();
                if(filePath == null){
                    ToastUtils.showToast(mContext, "保存失败");
                }else{
                    ToastUtils.showToast(mContext, "成功保存："+filePath);
                }
            }
        });
    }

    private List<DrawPoint> pointsChange() {
        List<DrawPoint> points = new ArrayList<>();
        List<DrawPoint> pointList = editView.getPoints();
        for (DrawPoint point : pointList) {
            points.add(pointChange(point));
        }
        return points;
    }

    private DrawPoint pointChange(DrawPoint point) {
        float s = editView.getScale();
        int w = editView.getWidth();
        float r = w / 2;
        float c = editView.getDisplayRect().centerX();
        float scale = ((float) bitmapWidth) / w;
        float x = r + (point.x - r) / s;
        float pianyi = (c - r) / s;
        x = x - pianyi;
        x = x * scale;

        float imgHeight = bitmapHeight / scale;
        int h = editView.getHeight();
        float y;
        if (imgHeight * s < h) {
            float toTopEdit = (h - imgHeight * s) / 2;
            y = (point.y - toTopEdit) * scale / s;
        } else {
            float r1 = h / 2;
            float c1 = editView.getDisplayRect().centerY();
            y = r1 + (point.y - r1) / s;
            float pianyiY = (c1 - r1) / s;
            y = y - pianyiY;
            y = (y - (h - imgHeight) / 2) / imgHeight * bitmapHeight;
        }

        DrawPoint p = new DrawPoint(x, y, point.isStart);
        Log.e("editimage", "x=" + x + "y=" + y + "isStart:" + point.isStart);
        return p;
    }

    public String saveImg(Bitmap bt) {
        String sdCardDir = Environment.getExternalStorageDirectory() + "/PIC/";
        File dirFile = new File(sdCardDir);  //目录转化成文件夹
        if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
            dirFile.mkdirs();
        }
        File f = new File(sdCardDir + System.currentTimeMillis() + ".jpg");
        LogUtil.d(TAG, "saveImg:"+f.getAbsolutePath());
        try {
            f.createNewFile();
            FileOutputStream fOut = new FileOutputStream(f);
            bt.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
            return f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}