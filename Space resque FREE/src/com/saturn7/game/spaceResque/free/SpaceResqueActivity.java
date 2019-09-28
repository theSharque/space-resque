package com.saturn7.game.spaceResque.free;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.game.spaceResque.IActivityRequestHandler;
import com.game.spaceResque.MainGame;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class SpaceResqueActivity extends AndroidApplication implements IActivityRequestHandler {
	
	IntentOpenerAndroid iOpener;

    protected AdView adView;

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case SHOW_ADS:
				adView.setVisibility(View.VISIBLE);
				break;

			case HIDE_ADS:
				adView.setVisibility(View.GONE);
				break;
			}
		}
	};

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        iOpener = new IntentOpenerAndroid( this );

        // Create the layout
		RelativeLayout layout = new RelativeLayout(this);

		// Do the stuff that initialize() would do for you
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		// Create the libgdx View
		View gameView = initializeForView(new MainGame( iOpener, this ), false);

		// Create and setup the AdMob view
		adView = new AdView(this, AdSize.BANNER, "a14ed4e32d7ff08"); // Put in your secret key here
		AdRequest newAdReq = new AdRequest();
//		newAdReq.setTesting(true);
		adView.loadAd( newAdReq );

		// Add the libgdx view
		layout.addView(gameView);

		// Add the AdMob view
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		layout.addView(adView, adParams);

		// Hook it all up
		setContentView(layout);		
    }

	@Override
	public void showAds( boolean show ) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
}