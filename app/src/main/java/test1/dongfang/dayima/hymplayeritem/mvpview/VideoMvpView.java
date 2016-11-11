package test1.dongfang.dayima.hymplayeritem.mvpview;


import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import test1.dongfang.dayima.hymplayeritem.bean.VideoData;

/**
 * @author xdf
 */
public interface VideoMvpView extends MvpLceView<List<VideoData>> {

    void showLoadMoreErrorView();

    void showLoadMoreView();

    void setLoadMoreData(List<VideoData> videos);
}
