package com.game.spaceResque;

import com.badlogic.gdx.InputProcessor;

public class GameKeyInput implements InputProcessor {

	public boolean isPressed;
	public int iKeyCode;
	
	@Override
	public boolean keyDown(int keycode) {
		isPressed = true;
		iKeyCode = keycode;
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		isPressed = false;
		iKeyCode = keycode;
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
