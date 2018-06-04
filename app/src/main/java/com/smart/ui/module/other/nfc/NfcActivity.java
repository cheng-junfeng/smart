package com.smart.ui.module.other.nfc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.hint.listener.OnConfirmListener;
import com.hint.utils.DialogUtils;
import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.ui.module.other.nfc.utils.StringHexUtils;

import java.io.IOException;

import butterknife.BindView;


public class NfcActivity extends BaseCompatActivity {

    private final static String TAG = "NfcActivity";

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.rb_read)
    RadioButton rbRead;
    @BindView(R.id.rb_Change)
    RadioButton rbChange;
    @BindView(R.id.rb_write)
    RadioButton rbWrite;
    @BindView(R.id.rdgb)
    RadioGroup rdgb;
    @BindView(R.id.etSector)
    EditText etSector;
    @BindView(R.id.etBlock)
    EditText etBlock;
    @BindView(R.id.etData)
    EditText etData;
    @BindView(R.id.tv3)
    TextView tv3;

    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private int mCount = 0;
    String info = "";

    private int bIndex;
    private int bCount;

    byte[] code = MifareClassic.KEY_NFC_FORUM;//读写标签中每个块的密码
    private byte[] data3, b0;
    private String temp = "";
    private NfcAdapter mNfcAdapter;
    private Context mContext;
    int block[] = {4, 5, 6, 8, 9, 10, 12, 13, 14, 16, 17, 18, 20, 21, 22, 24,
            25, 26, 28, 29, 30, 32, 33, 34, 36, 37, 38, 40, 41, 42, 44, 45, 46,
            48, 49, 50, 52, 53, 54, 56, 57, 58, 60, 61, 62};


    @Override
    protected int setContentView() {
        return R.layout.activity_nfc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "NFC", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        checkNFCFunction();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent,
                    mFilters, mTechLists);
        }
    }

    private void checkNFCFunction() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            DialogUtils.showConfirmAlertDialog(mContext, "没发现NFC设备，请确认您的设备支持NFC功能!", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            if (!mNfcAdapter.isEnabled()) {
                DialogUtils.showConfirmDialog(mContext, "现在去开启NFC功能!", new OnConfirmListener() {
                    @Override
                    public void onClickPositive() {
                        Intent setnfc = new Intent(
                                Settings.ACTION_NFC_SETTINGS);
                        startActivity(setnfc);
                    }

                    @Override
                    public void onClickNegative() {
                        finish();
                    }
                });
            }
        }
    }

    private void initData(){
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[]{ndef,};
        mTechLists = new String[][]{new String[]{NfcA.class.getName()}};
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tv1.setText("发现新的 Tag:  " + ++mCount + "\n");// mCount 计数
        String intentActionStr = intent.getAction();// 获取到本次启动的action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intentActionStr)// NDEF类型
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intentActionStr)// 其他类型
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intentActionStr)) {// 未知类型
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] bytesId = tag.getId();// 获取id数组

            info += StringHexUtils.ByteArrayToHexString(bytesId) + "\n";
            tv2.setText("标签UID:  " + "\n" + info);

            // 读取存储信息
            if (rbRead.isChecked()) {
                // mChange=false;
                tv3.setText("读取成功! " + readTag(tag));
                // readNfcVTag(tag);
                etSector.setText("");
                etBlock.setText("");
                etData.setText("");
            }

            // 写数据
            if (rbWrite.isChecked()) {
                //writeTag(tag);
                String str = etData.getText().toString();
                writeTag(tag, str);
            }

            // 转换为ASCll
            if (rbChange.isChecked()) {
                tv3.setText(change(tag));
                Toast.makeText(getBaseContext(), "转换成功", Toast.LENGTH_SHORT).show();
                etSector.setText("");
                etBlock.setText("");
                etData.setText("");
            }
        }
    }

    public void writeTag(Tag tag, String str) {
        MifareClassic mfc = MifareClassic.get(tag);
        try {
            if (mfc != null) {
                mfc.connect();
            } else {
                Toast.makeText(mContext, "写入失败", Toast.LENGTH_SHORT).show();
                return;
            }
            LogUtil.d(TAG, "write----connect-------------");
            boolean CodeAuth = false;
            byte[] b1 = str.getBytes();
            if (b1.length <= 720) {
                int num = b1.length / 16;
                int next = b1.length / 48 + 1;
                b0 = new byte[16];
                if (!(b1.length % 16 == 0)) {
                    for (int i = 1, j = 1; i <= num; i++) {
                        CodeAuth = mfc.authenticateSectorWithKeyA(j, code);
                        System.arraycopy(b1, 16 * (i - 1), b0, 0, 16);
                        mfc.writeBlock(block[i - 1], b0);
                        if (i % 3 == 0) {
                            j++;
                        }
                    }
                    CodeAuth = mfc.authenticateSectorWithKeyA(next,// 非常重要------
                            code);
                    byte[] b2 = {0};
                    b0 = new byte[16];
                    System.arraycopy(b1, 16 * num, b0, 0, b1.length % 16);
                    System.arraycopy(b2, 0, b0, b1.length % 16, b2.length);
                    mfc.writeBlock(block[num], b0);
                    mfc.close();
                    Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    for (int i = 1, j = 1; i <= num; i++) {
                        if (i % 3 == 0) {
                            j++;
                        }
                        CodeAuth = mfc.authenticateSectorWithKeyA(j,// 非常重要---------
                                code);
                        System.arraycopy(b1, 16 * (i - 1), b0, 0, 16);
                        mfc.writeBlock(block[i - 1], b0);
                        str += StringHexUtils.ByteArrayToHexString(b0);
                        System.out.println("Block" + i + ": " + str);
                    }
                    mfc.close();
                    Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(getBaseContext(), "字符过长，内存不足", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String readTag(Tag tag) {
        MifareClassic mfc = MifareClassic.get(tag);
        for (String tech : tag.getTechList()) {
            System.out.println(tech);// 显示设备支持技术
        }
        boolean auth = false;
        try {
            StringBuilder metaInfo = new StringBuilder();
            mfc.connect();
            int type = mfc.getType();// 获取TAG的类型
            int sectorCount = mfc.getSectorCount();// 获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;

            }
            metaInfo.append("  卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
                    + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize()
                    + "B\n");
            for (int j = 0; j < sectorCount; j++) {
                auth = mfc.authenticateSectorWithKeyA(j,
                        MifareClassic.KEY_NFC_FORUM);// 逐个获取密码

                if (auth) {
                    metaInfo.append("Sector " + j + ":验证成功\n");
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        metaInfo.append("Block " + bIndex + " : "
                                + StringHexUtils.ByteArrayToHexString(data)
                                + "\n");
                        bIndex++;
                    }
                } else {
                    metaInfo.append("Sector " + j + ":验证失败\n");
                }
            }
            return metaInfo.toString();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (mfc != null) {
                try {
                    mfc.close();
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
        return null;
    }

    public String change(Tag tag) {
        MifareClassic mfc = MifareClassic.get(tag);
        LogUtil.d(TAG, "change----------");
        boolean auth = false;
        String ChangeInfo = "";
        String Ascll = "";
        try {
            mfc.connect();
            int sectorCount = mfc.getSectorCount();// 获取TAG中包含的扇区数
            for (int j = 1; j < sectorCount; j++) {
                // Authenticate a sector with key A.
                auth = mfc.authenticateSectorWithKeyA(j,
                        MifareClassic.KEY_NFC_FORUM);
                if (auth) {
                    LogUtil.d(TAG,"change 的auth验证成功, 开始读取模块信息");

                    byte[] data0 = mfc.readBlock(4 * j);
                    byte[] data1 = mfc.readBlock(4 * j + 1);
                    byte[] data2 = mfc.readBlock(4 * j + 2);
                    data3 = new byte[data0.length + data1.length + data2.length];
                    System.arraycopy(data0, 0, data3, 0, data0.length);
                    System.arraycopy(data1, 0, data3, data0.length,
                            data1.length);
                    System.arraycopy(data2, 0, data3, data0.length
                            + data1.length, data2.length);

                    ChangeInfo = StringHexUtils.ByteArrayToHexString(data3);
                    temp = "扇区" + (j) + "里的内容为："
                            + StringHexUtils.decode(ChangeInfo) + "\n";
                }
                Ascll += temp;
            }
            return Ascll;
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "转换失败", Toast.LENGTH_SHORT).show();
        } finally {
            if (mfc != null) {
                try {
                    mfc.close();
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
        return "";
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
}
