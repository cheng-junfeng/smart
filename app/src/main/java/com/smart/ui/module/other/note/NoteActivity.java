package com.smart.ui.module.other.note;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.base.app.event.RxBusHelper;
import com.base.utils.LogUtil;
import com.base.utils.TimeUtil;
import com.base.utils.ToolbarUtil;
import com.custom.widget.MultiEditInputView;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.app.event.NoteEvent;
import com.smart.app.event.NoteType;
import com.smart.config.Extra;
import com.smart.db.entity.NoteEntity;
import com.smart.db.helper.NoteHelper;

import butterknife.BindView;


public class NoteActivity extends BaseCompatActivity {

    private final static String TAG = "NoteActivity";

    @BindView(R.id.mev_view)
    MultiEditInputView mevView;

    private long noteId = 0;
    private String contentText;
    private Context mContext;

    private NoteHelper noteHelper;
    private NoteEntity myEntity;
    private boolean isEdit = false;

    @Override
    protected int setContentView() {
        return R.layout.activity_note;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        noteHelper = NoteHelper.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            noteId = bundle.getLong(Extra.NOTE_ID, 0);
        }
        String title = "新增笔记";
        myEntity = noteHelper.queryByNoteId(noteId);
        if(myEntity != null){
            title = "编辑笔记";
            isEdit = true;
            mevView.setContentText(myEntity.getNote_content());
        }

        ToolbarUtil.setToolbarLeft(toolbar, title, null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ToolbarUtil.setToolbarRight(toolbar, "保存", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotes();
            }
        });
    }

    private void saveNotes() {
        DialogUtils.showLoading(mContext);
        contentText = mevView.getContentText();
        if (TextUtils.isEmpty(contentText)) {
            Toast.makeText(NoteActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        long current = System.currentTimeMillis();
        String inTime = TimeUtil.milliseconds2String(current);
        if(!isEdit){
            myEntity = new NoteEntity();
        }
        myEntity.setNote_id(current);
        myEntity.setNote_lasttime(inTime);
        myEntity.setNote_content(contentText);
        boolean result = false;
        if(!isEdit){
            result = noteHelper.insert(myEntity);
        }else{
            result = noteHelper.update(myEntity);
        }
        LogUtil.d(TAG, "result"+result+":"+isEdit);
        DialogUtils.dismissLoading();
        if(!result){
            ToastUtils.showToast(mContext, "保存失败，请稍后重试");
        }else{
            ToastUtils.showToast(mContext, "保存成功");
        }
        RxBusHelper.post(new NoteEvent.Builder(NoteType.NEW).build());
        finish();
    }
}
