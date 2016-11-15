package com.qc.corelibrary.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * A simple {@link android.support.v4.app.Fragment } subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected View mRootView;

    /**
     *
     */
    public BaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initRootView(inflater);
        ButterKnife.bind(this, mRootView);
        initViews();
        return mRootView;
    }

    protected abstract void initRootView(LayoutInflater inflater);

    protected abstract void initViews();


}
