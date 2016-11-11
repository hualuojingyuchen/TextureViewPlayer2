package test1.dongfang.dayima.hymplayeritem;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import butterknife.OnClick;
import test1.dongfang.dayima.hymplayeritem.player.HYMPlayerView;
import test1.dongfang.dayima.hymplayeritem.player.VideoPlayState;
import test1.dongfang.dayima.hymplayeritem.player.VideoPlayerHelper;

public class Main2Activity extends AppCompatActivity implements HYMPlayerView.ExitFullScreenListener {
    private VideoPlayState mCurrPlayState;

    RelativeLayout mParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_main2);
        mParent = (RelativeLayout) findViewById(R.id.root);
        Button btn_me = (Button) findViewById(R.id.btn_me);
        ImageView btn_me2 = (ImageView) findViewById(R.id.btn_me2);
        VideoPlayerHelper.init(this);
//        VideoPlayerHelper.getInstance().fullScreen(mParent, this);

        Glide.with(this).load("http://192.168.124.91:8080/tomcat.gif")
                .placeholder(R.drawable.video_image_place_holder)
                .error(R.drawable.video_image_place_holder)
                .into(btn_me2);
        btn_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayerHelper.getInstance().play(mParent,"http://192.168.124.91:8080/ansen.mp4",0);
            }
        });



    }


    @Override
    protected void onPause() {

        super.onPause();
        mCurrPlayState = VideoPlayerHelper.getInstance().getVideoPlayState();
        VideoPlayerHelper.getInstance().pause();
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (mCurrPlayState == VideoPlayState.PLAY) {
            VideoPlayerHelper.getInstance().play();
        }
    }

    @Override
    protected void onStop() {

        VideoPlayerHelper.getInstance().exitFullScreen(mCurrPlayState);
        super.onStop();
    }

    @Override
    public void exitFullScreen() {

        finish();
    }


    @OnClick(R.id.root)
    public void mParent() {

    }


}
