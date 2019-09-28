package com.saturn7.game.spaceResque.free;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.game.spaceResque.IntentOpener;

public class IntentOpenerAndroid implements IntentOpener {

	private Context appContext;

	public IntentOpenerAndroid(SpaceResqueActivity appContext) {
        this.appContext = appContext;
    }

	@Override
	public void showMarket() {
		Intent intent = new Intent( Intent.ACTION_VIEW );
		intent.setData( Uri.parse( "market://details?id=com.saturn7.game.spaceResque.full" ) );
		appContext.startActivity( intent );
	}
}
