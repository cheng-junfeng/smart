package com.smart.ui.module.other.share;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.base.utils.ToolbarUtil;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;

import java.util.ArrayList;

import butterknife.OnClick;


public class ShareActivity extends BaseCompatActivity {
    private final static String TAG = "ShareActivity";

    @Override
    protected int setContentView() {
        return R.layout.activity_share_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "分享", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.text_view, R.id.pic_view, R.id.file_view, R.id.wechat_view, R.id.qq_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_view:
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, "这是一段分享的文字");
                startActivity(Intent.createChooser(textIntent, "分享"));
                break;
            case R.id.pic_view:
                String path = getResourcesUri(R.mipmap.bg_1);
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/jpeg");
                imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                startActivity(Intent.createChooser(imageIntent, "分享"));
                break;
            case R.id.file_view:
                ArrayList<Uri> imageUris = new ArrayList<>();
                Uri uri1 = Uri.parse(getResourcesUri(R.mipmap.bg_1));
                Uri uri2 = Uri.parse(getResourcesUri(R.mipmap.bg_2));
                imageUris.add(uri1);
                imageUris.add(uri2);
                Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                mulIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(mulIntent,"多文件分享"));
                break;
            case R.id.wechat_view:
                Intent wechatIntent = new Intent(Intent.ACTION_SEND);
                wechatIntent.setPackage("com.tencent.mm");
                wechatIntent.setType("text/plain");
                wechatIntent.putExtra(Intent.EXTRA_TEXT, "分享到微信的内容");
                startActivity(wechatIntent);
                break;
            case R.id.qq_view:
                Intent qqIntent = new Intent(Intent.ACTION_SEND);
                qqIntent.setPackage("com.tencent.mobileqq");
                qqIntent.setType("text/plain");
                qqIntent.putExtra(Intent.EXTRA_TEXT, "分享到QQ的内容");
                startActivity(qqIntent);
                break;
        }
    }

    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }
}
