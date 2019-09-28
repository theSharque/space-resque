package com.game.spaceResque;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy {
	public boolean bActive;
	public boolean bShowScore;
	public float fXposition;
	public float fYposition;
	public float fXmove;
	public float fYmove;
	public int iEnemyType;
	/* 0 - Spider
	 * 1 - Bat
	 * 2 - X-fly
	 * 3 - Snake
	 * 4 - Raft
	 */
	private float fCadr;
	private float fXstart;
	private float fYstart;
	private CharSequence iScore;
	private float fScoreBlending = 1; 

	public Enemy( final float fX, final float fY, final int iType, final float fEnemySpeed ) {
		iEnemyType = iType;
		fXstart = fXposition = fX;
		fYstart = fYposition = fY;
		bActive = true;
		switch( iType ) {
		case 0:
			fXmove = 0;
			fYmove = -1 * fEnemySpeed;
			iScore = "50";
			break;
		case 1:
			fXmove = 0;
			fYmove = -3 * fEnemySpeed;
			iScore = "50";
			break;
		case 2:
			fXmove = 2 * fEnemySpeed;
			fYmove = 2 * fEnemySpeed;
			iScore = "50";
			break;
		case 3:
			fXmove = 2 * fEnemySpeed;
			fYmove = 0;
			iScore = "50";
			break;
		case 4:
			fXmove = 4 * fEnemySpeed;
			fYmove = 0;
			break;
		}
	}

	public void move( final float fDeltaTime ) {
		if( bActive ) {
			fXposition += fXmove * fDeltaTime;
			fYposition += fYmove * fDeltaTime;
			switch( iEnemyType ) {
			case 0:
				if( fYmove > 0 && fYposition >= fYstart + 8 ) {
					fYmove = -1;
				}
				if( fYmove < 0 && fYposition <= fYstart ) {
					fYmove = 2;
				}
				break;
			case 1:
				if( fYmove > 0 && fYposition >= fYstart + 30 ) {
					fYmove = -3;
				}
				if( fYmove < 0 && fYposition <= fYstart ) {
					fYmove = 3;
				}
				break;
			case 2:
				if( fYmove > 0 && fYposition >= fYstart + 16 ) {
					fYmove = -2;
				}
				if( fYmove < 0 && fYposition <= fYstart ) {
					fYmove = 2;
				}
				if( fXmove > 0 && fXposition >= fXstart + 60 ) {
					fXmove = -2;
				}
				if( fXmove < 0 && fXposition <= fXstart ) {
					fXmove = 2;
				}
				break;
			case 3:
				if( fXmove > 0 && fXposition >= fXstart + 45 ) {
					fXmove = -1;
				}
				if( fXmove < 0 && fXposition <= fXstart ) {
					fXmove = 2;
				}
				break;
			case 4:
				if( fXmove > 0 && fXposition >= fXstart + 270 ) {
					fXmove = -4;
				}
				if( fXmove < 0 && fXposition <= fXstart ) {
					fXmove = 4;
				}
				break;
			}
		} else {
			if( bShowScore ) {
				fScoreBlending -= fDeltaTime / 10;
				if( fScoreBlending <= 0 ) {
					bShowScore = false;
				}
			}
		}
	}
	
	public void render( SpriteBatch sbGame, final Animation[] aEnemies, final int iTop, final float fDeltaTime, boolean bLight, BitmapFont bfScoreFont ) {
		if( bActive ) {
			fCadr += fDeltaTime;
			switch( iEnemyType ) {
			case 0:
			case 1:
			case 2:
			case 4:
				sbGame.draw( aEnemies[iEnemyType].getKeyFrame( fCadr, true ), fXposition + 4, iTop - fYposition - 80 + 4 );
				break;
			case 3:
				if( bLight ) {
					sbGame.draw( aEnemies[iEnemyType].getKeyFrame( fCadr, true ), fXposition, iTop - fYposition - 80 );
				}
				break;
			}
		} else {
			if( bShowScore ) {
				bfScoreFont.setColor( 1, 1, 1, fScoreBlending );
				fYposition -= 10 * fDeltaTime;
				bfScoreFont.draw( sbGame, iScore, fXposition + 4, iTop - fYposition - 80 + 4 );
			}
		}
	}
	
	public boolean check( final int iTop, final float fX, final float fY, final float fWidth, final float fHeight) {
		float fEnemyWidth = 0, fEnemyHeight = 0;
		boolean bXintesect = false, bYintesect = false;
		if( bActive ) {
			switch ( iEnemyType ) {
			case 0:
				fEnemyHeight = 48;
				fEnemyWidth = 32;
				break;
			case 1:
				fEnemyHeight = 16;
				fEnemyWidth = 32;
				break;
			case 2:
				fEnemyHeight = 32;
				fEnemyWidth = 32;
				break;
			case 3:
				fEnemyHeight = 8;
				fEnemyWidth = 32;
				break;
			case 4:
				fEnemyHeight = 16;
				fEnemyWidth = 32;
				break;
			}

			if( ( fX < fXposition + fEnemyWidth && fX > fXposition ) ||
				( fX + fWidth < fXposition + fEnemyWidth && fX + fWidth > fXposition ) ||
				( fXposition < fX + fWidth && fXposition > fX ) ||
				( fXposition + fEnemyWidth < fX + fWidth && fXposition + fEnemyWidth > fX ) ) {
				bXintesect = true;
			}

			float fTestY = iTop - fYposition - 80;
			if( ( fY < fTestY + fEnemyHeight && fY > fTestY ) ||
				( fY + fHeight < fTestY + fEnemyHeight && fY + fHeight > fTestY ) ||
				( fTestY < fY + fHeight && fTestY > fY ) ||
				( fTestY + fEnemyHeight < fY + fHeight && fTestY + fEnemyHeight > fY ) ) {
				bYintesect = true;
			}
		}

		return ( bXintesect && bYintesect );
	}
}
