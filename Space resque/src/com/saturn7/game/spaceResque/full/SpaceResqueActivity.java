package com.saturn7.game.spaceResque.full;

import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.game.spaceResque.MainGame;

public class SpaceResqueActivity extends AndroidApplication {

	@Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initialize(new MainGame( null, null ), false);
    }
}