package test1.dongfang.dayima.hymplayeritem;


import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import test1.dongfang.dayima.hymplayeritem.player.HYMPlayerView;

/**
 * Created by xdf on 16-10-31.
 */
public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        HYMPlayerView hymPlayerView = new HYMPlayerView(this);
        setContentView(hymPlayerView);

//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        addContentView(hymPlayerView, layoutParams);

    }


}
