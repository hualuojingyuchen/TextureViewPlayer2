package test1.dongfang.dayima.hymplayeritem.player;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: xdf on 2016/10/24
 */
public class VideoPlayerHelper {

    private static volatile VideoPlayerHelper sVideoPlayerHelper;
    private HYMPlayerView mVideoPlayerView;
    private ViewGroup mParent;//全屏前或是小窗口前正在播放视频的ListItem
    private ViewGroup mSmallVideoPlayerContainer;//小窗口播放父容器
    private int mCurrPlayPosition = -1;//列表中当前正在播放的视频的位置索引
    private int mLastPlayPosition = -1;//列表中上次播放的视频的位置索引

    private VideoPlayerHelper(Context context) {

        mVideoPlayerView = new HYMPlayerView(context);

    }

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {

        if (sVideoPlayerHelper == null) {
            synchronized (VideoPlayerHelper.class) {
                if (sVideoPlayerHelper == null) {
                    sVideoPlayerHelper = new VideoPlayerHelper(context);
                }
            }
        }
    }

    /**
     * 设置小窗口播放
     *
     * @param smallVideoPlayerContainer
     */
    public void setSmallVideoPlayerContainer(ViewGroup smallVideoPlayerContainer) {

        if (sVideoPlayerHelper == null) {
            throw new NullPointerException("must be invoke init() method first");
        }
        sVideoPlayerHelper.stop();
        this.mSmallVideoPlayerContainer = smallVideoPlayerContainer;
    }

    /**
     * 获得播放Helper的实例
     *
     * @return
     */
    public static VideoPlayerHelper getInstance() {

        if (sVideoPlayerHelper == null) {
            throw new NullPointerException("must be invoke init() method first");
        }
        return sVideoPlayerHelper;
    }

    /**
     * 播放
     * @param parent
     * @param videoUrl
     * @param position
     */
    public void play(ViewGroup parent, String videoUrl, int position) {

        if (getVideoPlayState() != VideoPlayState.STOP) {
            mVideoPlayerView.onDestroy();
            if (mSmallVideoPlayerContainer != null) {
                mSmallVideoPlayerContainer.setVisibility(View.GONE);
            }
        }
        parent.addView(mVideoPlayerView);
        mVideoPlayerView.play(videoUrl);
        mCurrPlayPosition = position;
    }

    /**
     * 暂停
     */
    public void pause() {

        mVideoPlayerView.pause();
    }

    /**
     * 播放
     */
    public void play() {

        mVideoPlayerView.play();
    }

    /**
     * 进入全屏界面
     *
     * @param context
     */
    public void gotoFullScreen(Context context) {

        context.startActivity(new Intent(context, FullScreenPlayVideoActivity.class));
    }

    /**
     * 开始全屏播放
     *
     * @param parent
     * @param exitFullScreenListener
     */
    public void fullScreen(ViewGroup parent, HYMPlayerView.ExitFullScreenListener exitFullScreenListener) {

        mParent = (ViewGroup) mVideoPlayerView.getParent();
        mParent.removeView(mVideoPlayerView);
        mVideoPlayerView.setPlayScreenState(PlayScreenState.FULL_SCREEN);
        mVideoPlayerView.setExitFullScreenListener(exitFullScreenListener);
        parent.addView(mVideoPlayerView);
        mVideoPlayerView.play();
    }

    /**
     * 退出全屏播放
     *
     * @param state
     */
    public void exitFullScreen(VideoPlayState state) {

        mVideoPlayerView.setPlayScreenState(PlayScreenState.NORMAL);
        ViewGroup parent = (ViewGroup) mVideoPlayerView.getParent();
        if (parent != null) {
            parent.removeView(mVideoPlayerView);
        }
        mVideoPlayerView.setExitFullScreenListener(null);
        mParent.addView(mVideoPlayerView);
        mParent = null;
        if (state == VideoPlayState.PLAY) {
            mVideoPlayerView.play();
        }
    }

    /**
     * 小窗口播放
     */
    public void smallWindowPlay() {

        mLastPlayPosition = mCurrPlayPosition;
        mCurrPlayPosition = -1;
        mSmallVideoPlayerContainer.setVisibility(View.VISIBLE);
        mParent = (ViewGroup) mVideoPlayerView.getParent();
        mParent.removeView(mVideoPlayerView);
        mVideoPlayerView.setPlayScreenState(PlayScreenState.SMALL);
        mSmallVideoPlayerContainer.addView(mVideoPlayerView, 0);
        if (getVideoPlayState() != VideoPlayState.LOADING) {
            mVideoPlayerView.play();
        }
    }

    /**
     * 小窗口恢复到列表播放
     */
    public void smallWindowToListPlay() {

        mCurrPlayPosition = mLastPlayPosition;
        mLastPlayPosition = -1;
        mSmallVideoPlayerContainer.setVisibility(View.GONE);
        mVideoPlayerView.setPlayScreenState(PlayScreenState.NORMAL);
        mSmallVideoPlayerContainer.removeView(mVideoPlayerView);
        mParent.addView(mVideoPlayerView);
        if (getVideoPlayState() != VideoPlayState.LOADING) {
            mVideoPlayerView.play();
        }
        mParent = null;
    }

    /**
     * 停止
     */
    public void stop() {

        if (mVideoPlayerView.getPlayScreenState() == PlayScreenState.SMALL) {
            if (mSmallVideoPlayerContainer != null) {
                mSmallVideoPlayerContainer.setVisibility(View.GONE);
            }
        }
        mVideoPlayerView.onDestroy();
        clear();
    }

    /**
     * 得到播放状态
     *
     * @return
     */
    public VideoPlayState getVideoPlayState() {

        return mVideoPlayerView.getVideoPlayState();
    }
    public void setVideoPlayState(VideoPlayState state) {

         mVideoPlayerView.setVideoPlayState(state);
    }

    /**
     * 得到当前的条目
     *
     * @return
     */
    public int getCurrPlayPosition() {

        return mCurrPlayPosition;
    }

    /**
     * 得到上次播放的条目
     *
     * @return
     */
    public int getLastPlayPosition() {

        return mLastPlayPosition;
    }

    public void clear() {

        mCurrPlayPosition = -1;
        mLastPlayPosition = -1;
        mParent = null;
    }
}
