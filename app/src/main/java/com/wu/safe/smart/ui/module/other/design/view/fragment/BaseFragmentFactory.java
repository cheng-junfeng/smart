package com.wu.safe.smart.ui.module.other.design.view.fragment;

import com.wu.safe.smart.app.activity.BaseCompatFragment;

public class BaseFragmentFactory {

    public static final int POS_CAROUSE = 1;
    public static final int POS_CHOOSE = 2;
    public static final int POS_GALLERY = 3;

    private static BaseFragmentFactory mInstance;
    private PicCarouseFragment mCarouseFragment;
    private PicChooseFragment mChooseFragment;
    private PicGalleryFragment mGalleryFragment;

    private BaseFragmentFactory() {
    }

    private PicCarouseFragment getCarouseFragment() {
        if (mCarouseFragment == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mCarouseFragment == null) {
                    mCarouseFragment = new PicCarouseFragment();
                }
            }
        }
        return mCarouseFragment;
    }

    private PicChooseFragment getChooseFragment() {
        if (mChooseFragment == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mChooseFragment == null) {
                    mChooseFragment = new PicChooseFragment();
                }
            }
        }
        return mChooseFragment;
    }

    private PicGalleryFragment getCamraFragment() {
        if (mGalleryFragment == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mGalleryFragment == null) {
                    mGalleryFragment = new PicGalleryFragment();
                }
            }
        }
        return mGalleryFragment;
    }

    public static BaseFragmentFactory getInstance() {
        if (mInstance == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mInstance == null) {
                    mInstance = new BaseFragmentFactory();
                }
            }
        }
        return mInstance;
    }

    public BaseCompatFragment getFragment(int pos) {
        BaseCompatFragment fragment = null;
        switch (pos) {
            case POS_CAROUSE:
                fragment = getCarouseFragment();
                break;
            case POS_CHOOSE:
                fragment = getChooseFragment();
                break;
            case POS_GALLERY:
                fragment = getCamraFragment();
                break;
        }
        return fragment;
    }
}
