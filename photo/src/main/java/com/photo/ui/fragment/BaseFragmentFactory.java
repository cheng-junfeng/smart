package com.photo.ui.fragment;


import com.photo.app.PhotoBaseCompatFragment;
import com.photo.config.PhotoConfig;


public class BaseFragmentFactory {
    private static BaseFragmentFactory mInstance;
    private PicViewFragment mViewFragment;
    private PicEditFragment mEditFragment;
    private PicCarouseFragment mCarouseFragment;
    private PicGalleryFragment mGalleryFragment;

    private BaseFragmentFactory() {
    }

    private PicViewFragment getViewFragment() {
        if (mViewFragment == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mViewFragment == null) {
                    mViewFragment = new PicViewFragment();
                }
            }
        }
        return mViewFragment;
    }

    private PicEditFragment getEditFragment() {
        if (mEditFragment == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mEditFragment == null) {
                    mEditFragment = new PicEditFragment();
                }
            }
        }
        return mEditFragment;
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

    public PhotoBaseCompatFragment getFragment(int pos) {
        PhotoBaseCompatFragment fragment = null;
        switch (pos) {
            case PhotoConfig.POS_VIEW:
                fragment = getViewFragment();
                break;
            case PhotoConfig.POS_EDIT:
                fragment = getEditFragment();
                break;
            case PhotoConfig.POS_CAROUSE:
                fragment = getCarouseFragment();
                break;
            case PhotoConfig.POS_GALLERY:
                fragment = getCamraFragment();
                break;
        }
        return fragment;
    }
}
