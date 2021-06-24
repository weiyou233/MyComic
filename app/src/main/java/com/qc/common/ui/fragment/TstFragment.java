package com.qc.common.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.common.constant.AppConstant;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.ui.activity.MainActivity;
import com.qc.common.ui.activity.VideoPlayerActivity;
import com.qc.common.ui.adapter.ChapterItemAdapter;
import com.qc.common.util.DBUtil;
import com.qc.common.util.EntityHelper;
import com.qc.common.util.EntityUtil;

import java.util.List;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Entity;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/24 12:53
 * @ver 1.0
 */
public class TstFragment extends BaseDataFragment<ChapterInfo> {

    private List<ChapterInfo> list;
    private Entity entity;

    private ChapterItemAdapter itemAdapter;

    public static TstFragment getInstance(String key, Entity entity) {
        TstFragment fragment = new TstFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", entity);
        bundle.putString("key", key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.entity = (Entity) getArguments().get("entity");
        String key = (String) getArguments().get("key");
        this.list = entity.getInfo().getChapterInfoMap().get(key);
    }

    @Override
    protected int setType() {
        return TYPE_GRID;
    }

    @Override
    protected int setColumn() {
        return 3;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTopLayout.setVisibility(View.GONE);
        getAdapter().getLoadMoreModule().setOnLoadMoreListener(null);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        if (itemAdapter == null) {
            itemAdapter = new ChapterItemAdapter(list, entity);
        }
        return itemAdapter;
    }

    @Override
    protected void requestServer() {
        showContentPage();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view,
                            int position) {
        start(position);
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view,
                                   int position) {
        return false;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    public void updateData() {
        getAdapter().notifyDataSetChanged();
    }

    public void startRead() {
        if (EntityHelper.checkChapterId(entity.getInfo(), entity.getInfo().getCurChapterId())) {
            EntityHelper.initChapterId(entity.getInfo(), entity.getInfo().getCurChapterId());
            start();
        } else {
            start(EntityHelper.getPosition(entity.getInfo(), 0));
        }
    }

    public void start() {
        updateData();
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            startFragment(ComicReaderFragment.getInstance(entity));
        } else if (TmpData.contentCode == AppConstant.READER_CODE) {
            startFragment(NovelReaderFragment.getInstance(entity));
        } else {
            Intent intent = new Intent(MainActivity.getInstance(), VideoPlayerActivity.class);
            intent.putExtra("entity", entity);
            startActivity(intent);
        }
        EntityUtil.first(entity);
        if (TmpData.toStatus == Constant.SEARCH_TO_CHAPTER) {
            TmpData.toStatus = Constant.NORMAL;
            DBUtil.save(entity, DBUtil.SAVE_ALL);
        }
    }

    public void start(int position) {
        EntityHelper.initChapterId(entity.getInfo(), list.get(position).getId());
        start();
    }
}
