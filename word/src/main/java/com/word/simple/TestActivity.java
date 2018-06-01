package com.word.simple;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.base.utils.ToolbarUtil;
import com.hint.utils.ToastUtils;
import com.word.R;
import com.word.R2;
import com.word.app.WordBaseCompatActivity;
import com.word.ui.PdfViewActivity;
import com.word.ui.WordViewActivity;
import com.word.utils.FileUtils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

public class TestActivity extends WordBaseCompatActivity {

    private static final String demoPath = "/mnt/sdcard/doc/test.doc";
    private static final String newPath = "/mnt/sdcard/doc/testS.doc";

    @Override
    protected int setContentView() {
        return R.layout.word_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "测试Word", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R2.id.pdf_button, R2.id.other_view, R2.id.html_view, R2.id.write_vi})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if(viewId == R.id.pdf_button) {
            Intent intent = new Intent(TestActivity.this, PdfViewActivity.class);
            startActivity(intent);
        }else if(viewId == R.id.other_view){
            try {
                InputStream inputStream = getAssets().open("test.doc");
                FileUtils.writeFile(new File(demoPath), inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            String fileMimeType = "application/msword";
            intent.setDataAndType(Uri.fromFile(new File(demoPath)), fileMimeType);
            try {
                TestActivity.this.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                ToastUtils.showToast(this, "未找到软件");
            }
        }else if(viewId == R.id.html_view){
            Intent intent = new Intent(TestActivity.this, WordViewActivity.class);
            startActivity(intent);
        }else if(viewId == R.id.write_vi){
            doWrite();
        }
    }

    private void doWrite() {
        File demoFile = new File(demoPath);
        File newFile = new File(newPath);
        Map<String, String> map = new HashMap<String, String>();
        map.put("$QYMC$", "xxx科技股份有限公司");
        map.put("$QYDZ$", "上海市杨浦区xx路xx号");
        map.put("$QYFZR$", "张三");
        map.put("$FRDB$", "李四");
        map.put("$CJSJ$", "2000-11-10");
        map.put("$SCPZMSJWT$", "5");
        map.put("$XCJCJBQ$", "6");
        map.put("$JLJJJFF$", "7");
        map.put("$QYFZRQM$", "张三");
        map.put("$CPRWQM$", "赵六");
        map.put("$ZFZH$", "100001");
        map.put("$BZ$", "无");
        writeDoc(demoFile, newFile, map);
        ToastUtils.showToast(this, "写入："+newFile.getAbsolutePath());
    }

    public void writeDoc(File demoFile, File newFile, Map<String, String> map) {
        try {
            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            Range range = hdt.getRange();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
