package com.qc.mycomic.fragment;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qc.mycomic.R;
import com.qc.mycomic.util.Utils;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.widge.TheSearchView;

public class SearchFragment extends BaseFragment {

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void initView(View rootView) {
        addTopBarBackBtn();
        QMUIRoundButton btSearch = new QMUIRoundButton(_mActivity);
        btSearch.setText("搜索");
        btSearch.setLetterSpacing(0.2f);
        mTopLayout.addRightView(btSearch, R.id.topbar_right_view);

        TheSearchView searchView = new TheSearchView(_mActivity);
        searchView.setOnTextChangedListener(new TheSearchView.OnTextChangedListener() {
            String content = "";
            boolean isEmpty = true;

            @Override
            public void onChanged(String content, boolean isEmpty) {
                this.content = content;
                this.isEmpty = isEmpty;
            }

            @Override
            public void onSearch() {
                if (isEmpty) {
                    showFailTips("请输入搜索内容");
                } else {
                    startFragment(new SearchResultFragment(content));
                }
            }
        });
        mTopLayout.setCenterView(searchView);

        btSearch.setOnClickListener(v -> {
            EditText etSearch = searchView.getSearchEditText();
            String searchString = etSearch.getText().toString();
            if (searchString.trim().equals("")) {
                showFailTips("请输入搜索内容");
            } else {
                startFragment(new SearchResultFragment(searchString));
            }
        });
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
