package test1.dongfang.dayima.hymplayeritem;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test1.dongfang.dayima.hymplayeritem.adapter.VideoAdapter;
import test1.dongfang.dayima.hymplayeritem.bean.VideoData;
import test1.dongfang.dayima.hymplayeritem.player.VideoPlayerHelper;
import test1.dongfang.dayima.hymplayeritem.recycler.CommonRecyclerView;
import test1.dongfang.dayima.hymplayeritem.recycler.SpacesItemDecoration;

/**
 * author ：  xdf
 * TextureView创建的时显示图片,然后初始化播放器,预加载视频,如果视频文件不存在,从assets下copy一份到sdcard目录下,
 * 视频加载完毕隐藏图片，我这边图片默认显示的是android项目自带的图片,你们可以根据需求显示想要的图片。
 */
public class MainActivity extends AppCompatActivity implements CommonRecyclerView.LoadMoreListener{



//    @BindView(R.id.contentView)
//    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.video_recycler_view)
    CommonRecyclerView mRecyclerView;
    @BindView(R.id.small_video_player_container)
    RelativeLayout mSmallVideoPlayerContainer;

    private View mFooterView;
    private VideoAdapter mVideoAdapter;
    private boolean mIsFirstLoad = true;
    private List<VideoData> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mTextureView = new TextureView(this);
//        mTextureView.setSurfaceTextureListener(this);
        setContentView(R.layout.activity_main);
        VideoPlayerHelper.init(this);
        ButterKnife.bind(this);
        addData();
        setData();


    }

    private void addData() {
        for (int i=0;i<8;i++){
            VideoData videoData = new VideoData();
//            videoData.setDataType();
            videoData.setDuration(""+i);
            videoData.setImageUrl("http://192.168.124.91:8080/tomcat.gif");
            videoData.setVideoUrl("http://192.168.124.91:8080/ansen.mp4");
            videoData.setId(""+i);
            videoData.setTitle("hello"+i);
            data.add(videoData);
        }
        VideoPlayerHelper.getInstance().setSmallVideoPlayerContainer(mSmallVideoPlayerContainer);
    }

    private void setData() {

        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.d_10)));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mSwipeRefreshLayout.setr
//        mRecyclerView.setOnLoadMoreListener(this);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                loadData(true);
//            }
//        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int curPlayPosition = VideoPlayerHelper.getInstance().getCurrPlayPosition();
                int lastPlayPosition = VideoPlayerHelper.getInstance().getLastPlayPosition();
                if (curPlayPosition != -1 && (curPlayPosition < mRecyclerView.getFirstVisiblePosition() ||
                        curPlayPosition > mRecyclerView.getLastVisiblePosition())) {
                    VideoPlayerHelper.getInstance().smallWindowPlay();//移除屏幕之后进入小窗口播放
                } else if (curPlayPosition == -1 && lastPlayPosition >= mRecyclerView.getFirstVisiblePosition()
                        && lastPlayPosition <= mRecyclerView.getLastVisiblePosition()) {
                    VideoPlayerHelper.getInstance().smallWindowToListPlay();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        if (mVideoAdapter == null) {
            mVideoAdapter = new VideoAdapter(this, data);
//            mVideoAdapter.enableFooterView();
//            mFooterView = LayoutInflater.from(this).inflate(R.layout.footer_layout, mRecyclerView, false);
//            mVideoAdapter.addFooterView(mFooterView);
            mRecyclerView.setAdapter(mVideoAdapter);
        } else {
            mVideoAdapter.addAll(data, 0);
        }



    }

    @OnClick(R.id.iv_video_close)
    public void onClickCloseVideo(View view) {

        VideoPlayerHelper.getInstance().stop();
    }
    /**
     * 如果没有文件就复制过去
     */
    private void copyFile() {
        AssetManager assets = this.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assets.open("ansen.mp4");
            String fileName = Environment.getExternalStorageDirectory() + "/ansen.mp4";
            out = new FileOutputStream(fileName);
            byte[] bytes = new byte[1024];
            int read;
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG...HYMPLayerItem:", "IO错误" + e.toString());
        }

    }
    public void loadData(boolean pullToRefresh) {

        showLoading(pullToRefresh);
//        presenter.refreshData(pullToRefresh);
        if(mIsFirstLoad) {
            VideoPlayerHelper.getInstance().setSmallVideoPlayerContainer(mSmallVideoPlayerContainer);
            mIsFirstLoad = false;
        }
    }
    @Override
    public void onLoadMore() {
//        showLoadMoreView();
//        presenter.loadMoreData();
    }
    public void showLoadMoreView() {

        if(mFooterView.getVisibility() == View.GONE) {
            mFooterView.setVisibility(View.VISIBLE);
        }
        Toast.makeText(this,R.string.no_more_videos,Toast.LENGTH_SHORT).show();
    }
    public void showLoading(boolean pullToRefresh) {

        if(pullToRefresh) {
//            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    public interface PlayerController {
        void play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoAdapter = null;
        mIsFirstLoad = true;
    }
}
