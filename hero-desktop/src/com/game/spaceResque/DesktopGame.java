package com.game.spaceResque;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopGame {
	public static void main( String[] args ) {
		new LwjglApplication( new MainGame( null, null ), "Test game", 480, 320, false );
	}
}
