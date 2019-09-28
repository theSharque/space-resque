package com.game.spaceResque;

import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class SpaceResqueActivity extends AndroidApplication {

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initialize(new MainGame( null, null ), false);
    }
}