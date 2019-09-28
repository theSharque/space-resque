package com.game.spaceResque;

public class Tnt {
	private float fTimer = 20;
	public float fCadr = 0;
	public int xPos;
	public int yPos;
	public int iState = 2;
	
	public Tnt( float fPlayerX, float fPlayerY ) {
		xPos = (int) fPlayerX;
		yPos = (int) fPlayerY;
	}

	public void tick( MainGame parent, float fDeltaTime ) {
		fTimer -= fDeltaTime;
		fCadr += fDeltaTime;

		if( iState == 0 ) {
			parent.makeBoom( xPos, yPos );
		}

		if( fTimer <= 0 ) {
			iState--;
			fTimer = 1;
		}
	}
}
