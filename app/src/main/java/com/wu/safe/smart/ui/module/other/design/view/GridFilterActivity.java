package com.wu.safe.smart.ui.module.other.design.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.smart.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.smart.ui.module.other.design.adapter.GvFilterAdapter;
import com.wu.safe.smart.ui.module.other.design.bean.FilterAreaBean;
import com.wu.safe.smart.ui.widget.ScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GridFilterActivity extends BaseCompatActivity {

    public final static String TAG = "GridFilterActivity";

    @BindView(R.id.tv_area_name)
    TextView tvAreaName;
    @BindView(R.id.gv_area)
    ScrollGridView gvArea;

    private List<FilterAreaBean> areaListData;
    private GvFilterAdapter filterAreaAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_grid_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "格子点选", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ToolbarUtil.setToolbarRight(toolbar, "重置", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filterAreaAdapter != null){
                    filterAreaAdapter.reset();
                    tvAreaName.setText("");
                    tvAreaName.setVisibility(View.INVISIBLE);
                }
            }
        });
        initListData();
        initAreaData();
    }

    private void initAreaData() {
        filterAreaAdapter = new GvFilterAdapter(this, areaListData);
        gvArea.setAdapter(filterAreaAdapter);

        gvArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                filterAreaAdapter.setSelect(position);
                tvAreaName.setText(filterAreaAdapter.getSelectStr());
                tvAreaName.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initListData() {
        areaListData = new ArrayList<FilterAreaBean>();
        FilterAreaBean bean1 = new FilterAreaBean();
        bean1.setId("1");
        bean1.setParentId("0");
        bean1.setName("1号楼");
        bean1.setAreaType(1);
        FilterAreaBean bean2 = new FilterAreaBean();
        bean2.setId("2");
        bean2.setParentId("1");
        bean2.setName("1层");
        bean2.setAreaType(1);
        FilterAreaBean bean3 = new FilterAreaBean();
        bean3.setId("3");
        bean3.setParentId("1");
        bean3.setName("2层");
        bean3.setAreaType(1);
        FilterAreaBean bean4 = new FilterAreaBean();
        bean4.setId("4");
        bean4.setParentId("2");
        bean4.setName("财务室");
        bean4.setAreaType(0);
        FilterAreaBean bean5 = new FilterAreaBean();
        bean5.setId("5");
        bean5.setParentId("3");
        bean5.setName("人事办公室");
        bean5.setAreaType(0);

        FilterAreaBean bean6 = new FilterAreaBean();
        bean6.setId("6");
        bean6.setParentId("0");
        bean6.setName("2号楼");
        bean6.setAreaType(1);
        FilterAreaBean bean7 = new FilterAreaBean();
        bean7.setId("7");
        bean7.setParentId("6");
        bean7.setName("B层");
        bean7.setAreaType(0);
        FilterAreaBean bean8 = new FilterAreaBean();
        bean8.setId("8");
        bean8.setParentId("6");
        bean8.setName("C层");
        bean8.setAreaType(0);

        FilterAreaBean bean9 = new FilterAreaBean();
        bean9.setId("9");
        bean9.setParentId("0");
        bean9.setName("3号楼");
        bean9.setAreaType(1);
        FilterAreaBean bean10 = new FilterAreaBean();
        bean10.setId("10");
        bean10.setParentId("9");
        bean10.setName("11层");
        bean10.setAreaType(0);

        FilterAreaBean bean11 = new FilterAreaBean();
        bean11.setId("11");
        bean11.setParentId("0");
        bean11.setName("4号楼");
        bean11.setAreaType(1);
        FilterAreaBean bean12 = new FilterAreaBean();
        bean12.setId("12");
        bean12.setParentId("11");
        bean12.setName("12层");
        bean12.setAreaType(0);
        areaListData.add(bean1);
        areaListData.add(bean2);
        areaListData.add(bean3);
        areaListData.add(bean4);
        areaListData.add(bean5);
        areaListData.add(bean6);
        areaListData.add(bean7);
        areaListData.add(bean8);
        areaListData.add(bean9);
        areaListData.add(bean10);
        areaListData.add(bean11);
        areaListData.add(bean12);
    }
}
