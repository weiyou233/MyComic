package com.qc.mycomic.ui.presenter;

import com.qc.mycomic.ui.view.ReaderView;
import com.qc.mycomic.util.ComicHelper;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.self.SourceCallback;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ImageInfo;
import top.luqichuang.mycomic.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ReaderPresenter extends BasePresenter<ReaderView> {

    public void loadImageInfoList(Comic comic) {
        List<ChapterInfo> chapterInfoList = comic.getComicInfo().getChapterInfoList();
        int position = ComicHelper.getPosition(comic.getComicInfo());
        int chapterId = comic.getComicInfo().getCurChapterId();
        String url = chapterInfoList.get(position).getChapterUrl();
        Source source = ComicHelper.source(comic);
        Request request = source.getImageRequest(url);
        NetUtil.startLoad(request, new SourceCallback(request, source, Source.IMAGE) {
            @Override
            public void onFailure(String errorMsg) {
                ReaderView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        view.loadImageInfoListComplete(null, errorMsg);
                    }
                });
            }

            @Override
            public void onResponse(String html, Map<String, Object> map) {
                ReaderView view = getView();
                List<ImageInfo> imageInfoList = ComicHelper.getImageInfoList(comic, html, chapterId, map);
                if (imageInfoList.isEmpty()) {
                    onFailure("解析失败！");
                } else {
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            view.loadImageInfoListComplete(imageInfoList, null);
                        }
                    });
                }
            }
        });
    }
}
