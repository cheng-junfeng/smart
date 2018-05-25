package com.wu.safe.smart.ui.module.other.design.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.base.utils.DialogUtils;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatFragment;
import com.wu.safe.smart.ui.module.other.design.adapter.ViewPagerAdapter;
import com.wu.safe.smart.ui.widget.MarqueeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PicCarouseFragment extends BaseCompatFragment {
    private final static String TAG = "PicCarouseFragment";
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_pager_title)
    TextView tvPagerTitle;
    @BindView(R.id.lineLayout_dot)
    LinearLayout lineLayoutDot;

    private List<ImageView> mImageList; //轮播的图片集合
    private String[] mImageTitles;      //标题集合
    private int previousPosition = 0;//前一个被选中的position
    private List<View> mDots;           //小点

    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 3000;//间隔时间

    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private int[] imgae_ids = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4};
    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.fragment_pic_carouse;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);

        mContext = this.getContext();
        initData();//初始化数据
        initView();//初始化View，设置适配器

        autoPlayView();//开启线程，自动播放
        return containerView;
    }


    public void initData() {
        mImageTitles = new String[]{"这是一个好看的标题1", "这是一个优美的标题2", "这是一个快乐的标题3", "这是一个开心的标题4"};
        int[] imageRess = new int[]{R.mipmap.wallpaper1, R.mipmap.wallpaper2, R.mipmap.wallpaper3, R.mipmap.wallpaper4};

        mImageList = new ArrayList<>();
        ImageView iv;
        for (int i = 0; i < imageRess.length; i++) {
            iv = new ImageView(mContext);
            iv.setBackgroundResource(imageRess[i]);//设置图片
            iv.setId(imgae_ids[i]);//顺便给图片设置id
            iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
            mImageList.add(iv);
        }

        mDots = addDots(lineLayoutDot, fromResToDrawable(mContext, R.drawable.ic_dot_normal), mImageList.size());//其中fromResToDrawable()方法是我自定义的，目的是将资源文件转成Drawable
    }

    private class pagerImageOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pager_image1:
                    DialogUtils.showToast(mContext, "图片1被点击");
                    break;
                case R.id.pager_image2:
                    DialogUtils.showToast(mContext, "图片2被点击");
                    break;
                case R.id.pager_image3:
                    DialogUtils.showToast(mContext, "图片3被点击");
                    break;
                case R.id.pager_image4:
                    DialogUtils.showToast(mContext, "图片4被点击");
                    break;
            }
        }
    }

    public void initView() {
        List<String> info1 = new ArrayList<>();
        info1.add("1.坚持读书，写作，源于内心的动力！");
        info1.add("2.坚持锻炼！");
        info1.add("3.早睡！");
        info1.add("4.学习！");
        marqueeView.startWithList(info1);
        // 在代码里设置自己的动画
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                DialogUtils.showToast(mContext, "click:" + position);
            }
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mImageList, viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mImageList.size();
                // 把当前选中的点给切换了, 还有描述信息也切换
                tvPagerTitle.setText(mImageTitles[newPosition]);//图片下面设置显示文本
                //设置轮播点
                LinearLayout.LayoutParams newDotParams = (LinearLayout.LayoutParams) mDots.get(newPosition).getLayoutParams();
                newDotParams.width = 24;
                newDotParams.height = 24;

                LinearLayout.LayoutParams oldDotParams = (LinearLayout.LayoutParams) mDots.get(previousPosition).getLayoutParams();
                oldDotParams.width = 16;
                oldDotParams.height = 16;

                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setFirstLocation();
    }

    /**
     * 第四步：设置刚打开app时显示的图片和文字
     */
    private void setFirstLocation() {
        tvPagerTitle.setText(mImageTitles[previousPosition]);
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImageList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        viewPager.setCurrentItem(currentPosition);
    }

    private void autoPlayView() {
        //自动播放图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    if(viewPager != null){
                        viewPager.post(new Runnable() {
                            @Override
                            public void run() {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            }
                        });
                    }
                    SystemClock.sleep(PAGER_TIOME);
                }
            }
        }).start();
    }

    public Drawable fromResToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    public int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(mContext);
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4, 0, 4, 0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        linearLayout.addView(dot);
        return dot.getId();
    }

    public List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, backgount);
            dots.add(linearLayout.findViewById(dotId));
        }
        return dots;
    }
}
