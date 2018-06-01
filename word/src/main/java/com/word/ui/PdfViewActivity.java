package com.word.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.word.R;
import com.word.R2;
import com.word.app.WordBaseCompatActivity;

import butterknife.BindView;


public class PdfViewActivity extends WordBaseCompatActivity {
    private static final String TAG = "PdfViewActivity";

    @BindView(R2.id.pdfView)
    PDFView pdfView;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.word_pdf_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "Pdf", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView() {
        DialogUtils.showLoading(mContext);
//        pdfView.fromUri(Uri)
//        pdfView.fromFile(new File("/mnt/sdcard/doc/test.pdf")) //ok
//        pdfView.fromBytes(byte[])
//        pdfView.fromStream(inputStream) // stream is written to bytearray - native code cannot use Java Streams
//        pdfView.fromSource(DocumentSource)
        pdfView.fromAsset("test.pdf")
                .pages(0, 1) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
//                .onDraw(onDrawListener)
//                // allows to draw something on all pages, separately for every page. Called only for visible pages
//                .onDrawAll(onDrawListener)
                .onLoad(loadCompleteListener) // called after document is loaded and starts to be rendered
//                .onPageChange(onPageChangeListener)
//                .onPageScroll(onPageScrollListener)
                .onError(errorListener)
//                .onPageError(onPageErrorListener)
//                .onRender(onRenderListener) // called after document is rendered for the first time
//                // called on single tap, return true if handled, false to toggle scroll handle visibility
//                .onTap(onTapListener)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load();
    }

    OnLoadCompleteListener loadCompleteListener = new OnLoadCompleteListener() {
        @Override
        public void loadComplete(int nbPages) {
            DialogUtils.dismissLoading();
        }
    };

    OnErrorListener errorListener = new OnErrorListener() {
        @Override
        public void onError(Throwable t) {
            LogUtil.d(TAG, "error:" + t.getMessage());
            DialogUtils.dismissLoading();
            ToastUtils.showToast(mContext, "加载失败");
        }
    };
}
