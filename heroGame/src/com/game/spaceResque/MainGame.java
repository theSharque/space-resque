package com.game.spaceResque;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainGame implements ApplicationListener {

	private static final int LEVEL_COUNT = 290;
	private static final float FUEL_MAX = 500f;
	private static final int TNT_MAX = 6;

	private boolean bLaserFire;
	private boolean lMoveRight;
	private boolean bSwitchState = false;
	private boolean bFly = false;
	private boolean bYodaLeft;
	private boolean bPlaceBomb;
	private boolean bInLampSwitch;
	private boolean bShowLine;
	private boolean bShowDie;
	private boolean bFreeVersion = false;
	private boolean bKilled = false;

	private int iTnt = TNT_MAX;
	private int iPage = 0;
	private int iMoveH = 0;
	private int iInputMethod = 1;
	private int iRoom = 0;
	private int iLives = 8;
	private int iLevel = 1;
	private int iMaxLevel;
	private int iShowLevel = 0;
	private int iTop;
	private int oldX;
	private int oldZ;
	private int iWidth;
	private int iScore;
	private int iNextPage;
	private int iFireKey;
	private int iTutorial = 0;
	private int iReturnPage = 0;
	private int[][] iLampPosition;
	private int[] iEnemyCounter;
	private int[][] iNextRoom;
	private int[] iExitPosition;

	private float fFlamePos;
	private float fPressTime;
	private float fGravity = 0;
	private float fPlayerX = 120;
	private float fPlayerY = 160;
	private float fFuel = FUEL_MAX;
	private float fLastPlayerX = 120;
	private float fLastPlayerY = 160;
	private float fHeroState = 2;
	private float fScroller = 0;
	private float fMagmaState;
	private float fMusicVolume = 1;
	private float fSoundVolume = 1;
	private float fDieState;
	private float fAlign;

	private String[][] sCompletteMap;

	private SpriteBatch sbGame;

	private Texture tHorizontal;
	private Texture tHero_anim;
	private Texture tHeroDie_anim;
	private Texture tFlameSheet;
	private Texture tTntSheet;
	private Texture tBoomSheet;
	private Texture tYodaRight;
	private Texture tLamp;
	private Texture tDroid;
	private Texture tDroid2;
	private Texture tDroid3;
	private Texture tRedLaser;
	private Texture tLaserRight;
	private Texture tMagma;
	private Texture tMagma_h;
	private Texture tVertical_bg;
	private Texture tVertical_fg;
	private Texture tMagma_bg;
	private Texture tMagma_fg;
	private Texture tMagmaBottom;
	private Texture tRaft;
	private Texture tMenuBack;
	private Texture tOptionsBack;
	private Texture tLine;
	private Texture tLifeIcon;
	private Texture tLevelIcons;
	private Texture tLevelMap;

	private TextureRegion trYodaLeft;
	private TextureRegion trLaserLeft; 
	private TextureRegion trMagma;
	private TextureRegion[] trLamp;
	private TextureRegion[] trFlame;
	private TextureRegion[] trHero;
	private TextureRegion[] trHeroDie;
	private TextureRegion[] trTnt;
	private TextureRegion[] trBoom;
	private TextureRegion[] trDroid;
	private TextureRegion[] trDroid2;
	private TextureRegion[] trDroid3;
	private TextureRegion[] trRedLaser;
	private TextureRegion[] trRaft;
	private TextureRegion[] trMagmaBottom;
	private TextureRegion[][] trPanel;
	private TextureRegion[][] trLevelIcons;

	private Animation aTnt;
	private Animation aBoom;
	private Animation aHero;
	private Animation aHeroDie;
	private Animation aFlame;
	private Animation aMagmaBottom;
	private Animation[] aEnemies;

	private BitmapFont main_font;
	private BitmapFont cap_font;
	private BitmapFont menu_font;

	private Music sndScream;
	private Music sndFly;
	private Music sndLaser;
	private Music sndBomb;
	private Music sndYoda;
	private Music mBackMusic;
	private Music mVader;

	private Tnt tBomb = null;
	private Enemy[][] eStack;
	private Preferences prefs;
	private GameKeyInput myInput;
	private CharSequence sPaused;
	private IntentOpener intentOpener;
	private IActivityRequestHandler myRequestHandler;
	private boolean bAds = true;
	private Texture tBackCells;

	public MainGame( IntentOpener iOpener, IActivityRequestHandler handler ) {
		intentOpener = iOpener;
		myRequestHandler = handler;
	}

	@Override
	public void create() {
		// Get all keys in one hand
		Gdx.input.setCatchBackKey( true );
		Gdx.input.setCatchMenuKey( true );
		myInput = new GameKeyInput();
		Gdx.input.setInputProcessor( myInput );

		// load preferences
		prefs = Gdx.app.getPreferences( "settings" );
		iRoom = prefs.getInteger( "room", 0 );
		iMaxLevel = prefs.getInteger( "maxLevel", 1 );
		iLives = prefs.getInteger( "lives", 6 );
		iLevel = prefs.getInteger( "level", 1 );
		iInputMethod = prefs.getInteger( "inputMethod", 1 );
		iScore = prefs.getInteger( "score", 0 );
		iTnt = prefs.getInteger( "tntCount", TNT_MAX );
		iShowLevel = prefs.getInteger( "showLevel", 0 );
		iFireKey = prefs.getInteger( "fireKey", Input.Keys.ANY_KEY );
		fMusicVolume = prefs.getFloat( "musicVolume", 1 );
		fSoundVolume = prefs.getFloat( "soundVolume", 1 );
		fPlayerX = prefs.getFloat( "playerX", 120 );
		fPlayerY = prefs.getFloat( "playerY", 160 );
		fFuel = prefs.getFloat( "fuel", FUEL_MAX );
		lMoveRight = prefs.getBoolean( "laserRight", true );
		iTutorial = prefs.getInteger( "tutorPage", 0 );

		// load sounds
		sndScream = Gdx.audio.newMusic( Gdx.files.internal( "r2d2small.mp3") );
		sndFly = Gdx.audio.newMusic( Gdx.files.internal( "flysmall.mp3") );
		sndBomb = Gdx.audio.newMusic( Gdx.files.internal( "timebomb.mp3") );
		sndYoda = Gdx.audio.newMusic( Gdx.files.internal( "yoda.mp3") );
		sndLaser = Gdx.audio.newMusic( Gdx.files.internal( "laser.mp3") );
		mBackMusic = Gdx.audio.newMusic( Gdx.files.internal( "smus.mp3") );
		mVader = Gdx.audio.newMusic( Gdx.files.internal( "vader.mp3") );

		// set volumes
		mBackMusic.setVolume( fMusicVolume );
		sndScream.setVolume( fSoundVolume );
		sndFly.setVolume( fSoundVolume );
		sndBomb.setVolume( fSoundVolume );
		sndYoda.setVolume( fSoundVolume );
		sndLaser.setVolume( fSoundVolume );

		// load render textures
		sbGame = new SpriteBatch();
		tLamp = new Texture( Gdx.files.internal( "lamp.png" ) );
		tHorizontal = new Texture( Gdx.files.internal( "horizon.png" ) );
		tVertical_bg = new Texture( Gdx.files.internal( "vertical_bg.png" ) );
		tVertical_fg = new Texture( Gdx.files.internal( "vertical_fg.png" ) );
		tMagma_bg = new Texture( Gdx.files.internal( "magma_bg.png" ) );
		tMagma_fg = new Texture( Gdx.files.internal( "magma_fg.png" ) );
		tLifeIcon = new Texture( Gdx.files.internal( "life_icon.png" ) );
		tMagma = new Texture( Gdx.files.internal( "magma.png" ) );
		tLine = new Texture( 512, 512, Format.RGBA8888 );
		tMagma_h = new Texture( Gdx.files.internal( "magma_h.png" ) );
		tMenuBack = new Texture( Gdx.files.internal( "main_menu.jpg" ) );
		tOptionsBack = new Texture( Gdx.files.internal( "options.jpg" ) );
		tLevelMap = new Texture( Gdx.files.internal( "levels_bg.png" ) );
		trMagma = new TextureRegion( tMagma );
		trMagma.setRegion( 0, 0, 15, 80 );

		tHero_anim = new Texture( Gdx.files.internal( "hero_anim.png" ) );
		TextureRegion[][] trTmp = TextureRegion.split( tHero_anim, 30, 42 );
		trHero = new TextureRegion[5];
		int index = 0;
        for (int i = 0; i < 5; i++) {
        	trHero[index++] = trTmp[0][i];
        }
        aHero = new Animation(0.5f, trHero);

		tHeroDie_anim = new Texture( Gdx.files.internal( "hero_die.png" ) );
		trTmp = TextureRegion.split( tHeroDie_anim, 30, 42 );
		trHeroDie = new TextureRegion[10];
		index = 0;
        for (int i = 0; i < 10; i++) {
        	trHeroDie[index++] = trTmp[0][i];
        }
        aHeroDie = new Animation(0.25f, trHeroDie);

		trTmp = TextureRegion.split( tLamp, 4, 64 );
		trLamp = new TextureRegion[2];
		index = 0;
        for (int i = 0; i < 2; i++) {
        	trLamp[index++] = trTmp[0][i];
        }
        aHeroDie = new Animation(0.25f, trHeroDie);

        tMagmaBottom = new Texture( Gdx.files.internal( "magma_bottom.png" ) );
		trTmp = TextureRegion.split( tMagmaBottom, 33, 16 );
		trMagmaBottom = new TextureRegion[5];
		index = 0;
        for (int i = 0; i < 5; i++) {
        	trMagmaBottom[index++] = trTmp[0][i];
        }
        aMagmaBottom = new Animation(0.4f, trMagmaBottom);

        tBackCells = new Texture( Gdx.files.internal( "bgcells.png" ) );

        Texture tBackGround = new Texture( Gdx.files.internal( "panel.png" ) );
		trPanel = TextureRegion.split( tBackGround, 15, 80 );

		tLevelIcons = new Texture( Gdx.files.internal( "levels.png" ) );
		trLevelIcons = TextureRegion.split( tLevelIcons, 50, 50 );

		tLaserRight = new Texture( Gdx.files.internal( "laser.png" ) );
		trLaserLeft = new TextureRegion( tLaserRight );
		trLaserLeft.flip( true, false );

		tYodaRight = new Texture( Gdx.files.internal("yoda.png") );
		trYodaLeft = new TextureRegion( tYodaRight );
		trYodaLeft.flip( true, false );

		tTntSheet = new Texture( Gdx.files.internal("tnt_anim.png") );
		trTmp = TextureRegion.split( tTntSheet, 16, 16 );
		trTnt = new TextureRegion[8];
		index = 0;
        for (int i = 0; i < 8; i++) {
        	trTnt[index++] = trTmp[0][i];
        }
        aTnt = new Animation(2.4f, trTnt);

		tBoomSheet = new Texture( Gdx.files.internal("boom_anim.png") );
		trTmp = TextureRegion.split( tBoomSheet, 32, 32 );
		trBoom = new TextureRegion[8];
		index = 0;
        for (int i = 0; i < 8; i++) {
        	trBoom[index++] = trTmp[0][i];
        }
        aBoom = new Animation(0.2f, trBoom);

		tFlameSheet = new Texture( Gdx.files.internal("flame.png") );
		trTmp = TextureRegion.split( tFlameSheet, 16, 32 );
		trFlame = new TextureRegion[3];
		index = 0;
        for (int i = 0; i < 3; i++) {
        	trFlame[index++] = trTmp[0][i];
        }
        aFlame = new Animation(1f, trFlame);

        aEnemies = new Animation[5];
        
		tDroid = new Texture( Gdx.files.internal("droid.png") );
		trTmp = TextureRegion.split( tDroid, 32, 48 );
		trDroid = new TextureRegion[4];
		index = 0;
        for (int i = 0; i < 4; i++) {
        	trDroid[index++] = trTmp[0][i];
        }
        aEnemies[0] = new Animation(0.5f, trDroid);

		tDroid2 = new Texture( Gdx.files.internal("droid2.png") );
		trTmp = TextureRegion.split( tDroid2, 32, 16 );
		trDroid2 = new TextureRegion[4];
		index = 0;
        for (int i = 0; i < 4; i++) {
        	trDroid2[index++] = trTmp[0][i];
        }
        aEnemies[1] = new Animation(0.5f, trDroid2);

		tDroid3 = new Texture( Gdx.files.internal("droid3.png") );
		trTmp = TextureRegion.split( tDroid3, 32, 32 );
		trDroid3 = new TextureRegion[4];
		index = 0;
        for (int i = 0; i < 4; i++) {
        	trDroid3[index++] = trTmp[0][i];
        }
        aEnemies[2] = new Animation(0.5f, trDroid3);

        tRedLaser = new Texture( Gdx.files.internal("laser_red.png") );
		trTmp = TextureRegion.split( tRedLaser, 32, 8 );
		trRedLaser = new TextureRegion[4];
		index = 0;
        for (int i = 0; i < 4; i++) {
        	trRedLaser[index++] = trTmp[0][i];
        }
        aEnemies[3] = new Animation(0.5f, trRedLaser);

		tRaft = new Texture( Gdx.files.internal("raft.png") );
		trTmp = TextureRegion.split( tRaft, 40, 32 );
		trRaft = new TextureRegion[6];
		index = 0;
        for (int i = 0; i < 6; i++) {
        	trRaft[index++] = trTmp[0][i];
        }
        aEnemies[4] = new Animation(0.5f, trRaft);

        main_font = new BitmapFont( Gdx.files.internal("main_font.fnt"), false );
		main_font.setColor( Color.WHITE );

		cap_font = new BitmapFont( Gdx.files.internal("level_cap.fnt"), false );
		cap_font.setColor( Color.WHITE );

		menu_font = new BitmapFont( Gdx.files.internal("buttons_font.fnt"), false );

		// Load game map
        eStack = new Enemy[LEVEL_COUNT][4];
        iEnemyCounter = new int[LEVEL_COUNT];
		sCompletteMap = new String[LEVEL_COUNT][3];
		iNextRoom = new int[LEVEL_COUNT][4];
		iExitPosition = new int[LEVEL_COUNT]; // Room, x-position
		iLampPosition = new int[LEVEL_COUNT][3];

		reloadGameMap();
	}

	private void reloadGameMap() {
		FileHandle fMap = Gdx.files.internal("map.txt");
		String[] sContent = fMap.readString().split("\r\n");

		for( int i = 0; i < LEVEL_COUNT; i++ ) {
			float fEnemySpeed;
			if( i > 10 ) {
				fEnemySpeed = 2;
			} else {
				fEnemySpeed = 1;
			}
			sCompletteMap[i][0] = sContent[ i * 4 ];
			sCompletteMap[i][1] = sContent[ i * 4 + 1 ];
			sCompletteMap[i][2] = sContent[ i * 4 + 2 ];

			if( sCompletteMap[i][1].contains( "A" ) ) {
				int x = sCompletteMap[i][1].indexOf( "A" );
				sCompletteMap[i][1] = sCompletteMap[i][1].substring( 0, x )+"0"+sCompletteMap[i][1].substring( x + 1 );
				iExitPosition[i] = x;
			} else {
				iExitPosition[i] = -1;
			}

			iLampPosition[i][0] = -1;
			iLampPosition[i][1] = -1;
			iLampPosition[i][2] = 1;
			iEnemyCounter[i] = 0;
			for( int t = 0; t < 3; t++ ) {
				if( sCompletteMap[i][t].contains( "L" ) ) {
					int x = sCompletteMap[i][t].indexOf( "L" );
					sCompletteMap[i][t] = sCompletteMap[i][t].substring( 0, x )+"0"+sCompletteMap[i][t].substring( x + 1 );
					iLampPosition[i][0] = x;
					iLampPosition[i][1] = t;
					iLampPosition[i][2] = 1;
				}

				if( sCompletteMap[i][t].contains( "S" ) ) {
					int x = sCompletteMap[i][t].indexOf( "S" );
					sCompletteMap[i][t] = sCompletteMap[i][t].substring( 0, x )+"0"+sCompletteMap[i][t].substring( x + 1 );
					eStack[i][iEnemyCounter[i]] = new Enemy( x * 15, t * 80 - 8, 0, fEnemySpeed );
					iEnemyCounter[i]++;
				}

				if( sCompletteMap[i][t].contains( "B" ) ) {
					int x = sCompletteMap[i][t].indexOf( "B" );
					sCompletteMap[i][t] = sCompletteMap[i][t].substring( 0, x )+"0"+sCompletteMap[i][t].substring( x + 1 );
					eStack[i][iEnemyCounter[i]] = new Enemy( x * 15, t * 80 - 64, 1, fEnemySpeed );
					iEnemyCounter[i]++;
				}

				if( sCompletteMap[i][t].contains( "X" ) ) {
					int x = sCompletteMap[i][t].indexOf( "X" );
					sCompletteMap[i][t] = sCompletteMap[i][t].substring( 0, x )+"0"+sCompletteMap[i][t].substring( x + 1 );
					eStack[i][iEnemyCounter[i]] = new Enemy( x * 15, t * 80 - 32, 2, fEnemySpeed );
					iEnemyCounter[i]++;
				}

				if( sCompletteMap[i][t].contains( "N" ) ) {
					int x = sCompletteMap[i][t].indexOf( "N" );
					sCompletteMap[i][t] = sCompletteMap[i][t].substring( 0, x )+"1"+sCompletteMap[i][t].substring( x + 1 );
					eStack[i][iEnemyCounter[i]] = new Enemy( x * 15 - 30, t * 80 - 32, 3, fEnemySpeed );
					iEnemyCounter[i]++;
				}

				if( sCompletteMap[i][t].contains( "P" ) ) {
					int x = sCompletteMap[i][t].indexOf( "P" );
					sCompletteMap[i][t] = sCompletteMap[i][t].substring( 0, x )+"0"+sCompletteMap[i][t].substring( x + 1 );
					eStack[i][iEnemyCounter[i]] = new Enemy( x * 15, t * 80, 4, fEnemySpeed );
					iEnemyCounter[i]++;
				}
			}

			String[] sTmp = sContent[ i * 4 + 3 ].split( "," );
			for( int t = 0; t < 4; t++ ) {
				iNextRoom[i][t] = Integer.parseInt( sTmp[t] );
			}

			if( i == 28 && iNextRoom[i][0] == -1 && iNextRoom[i][1] == -1 && iNextRoom[i][2] == -1 && iNextRoom[i][3] == -1 ) {
				bFreeVersion  = true;
				break;
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		iTop = height;
		iWidth = width;
		fAlign = (float)width / 480.0f;
        Gdx.gl.glViewport( 0, 0, (int) ((float)width * fAlign), 320 );
	}

	@Override
	public void render() {
		/* 0 - main menu
		 * 1 - highscore
		 * 2 - options
		 * 3 - game inside
		 * 4   - level scores
		 * 5   - pause
		 * 6   - game over
		 */
		if( mBackMusic.isPlaying() && iPage == 3 ) {
			mBackMusic.stop();
		}

		switch( iPage ) {
		case 0:
			if( bAds == true ) {
				bAds = false;
				if( myRequestHandler != null ) {
					myRequestHandler.showAds( false );
				}
			}

			iReturnPage = 0;
			if( !mBackMusic.isPlaying() ) {
				mBackMusic.setLooping( true );
				mBackMusic.setVolume( fMusicVolume );
				mBackMusic.play();
				mVader.stop();
			}
			renderMenu();
			break;
		case 1:
			if( bAds == false ) {
				bAds = true;
				if( myRequestHandler != null ) {
					myRequestHandler.showAds( true );
				}
			}

			iReturnPage = 1;
			renderDefine();
			break;
		case 2:
			if( bAds == true ) {
				bAds = false;
				if( myRequestHandler != null ) {
					myRequestHandler.showAds( false );
				}
			}

			iReturnPage = 2;
			renderOptions();
			break;
		case 3:
			if( bAds == true ) {
				bAds = false;
				if( myRequestHandler != null ) {
					myRequestHandler.showAds( false );
				}
			}
			iReturnPage = 3;
			renderGame();
			break;
		case 4:
			if( bAds == false ) {
				bAds = true;
				if( myRequestHandler != null ) {
					myRequestHandler.showAds( true );
				}
			}

			iReturnPage = 0;
			renderScore();
			break;
		case 5:
			if( bAds == false ) {
				bAds = true;
				if( myRequestHandler != null ) {
					myRequestHandler.showAds( true );
				}
			}

			renderPause();
			break;
		case 6:
			if( bAds == false ) {
				bAds = true;
				if( myRequestHandler != null ) {
					myRequestHandler.showAds( true );
				}
			}

			iReturnPage = 0;
			renderGameOver();
			break;
		case 8:
			iReturnPage = 8;
			renderLevelMap();
			break;
		}
	}

	private void renderLevelMap() {
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		sbGame.begin();
		sbGame.draw( tLevelMap, 0, iTop - 512 );
		for( int i=0; i < 5; i++ ) {
			if( iMaxLevel >= i * 5 ) {
				sbGame.draw( trLevelIcons[0][i], 185, iTop - i * 55 - 65 );
			} else {
				sbGame.draw( trLevelIcons[1][i], 185, iTop - i * 55 - 65 );
			}
		}
		sbGame.end();

		if( !bSwitchState && Gdx.input.isKeyPressed( Input.Keys.BACK) ) {
			iPage = 0;
		}

		if( !bSwitchState && Gdx.input.isTouched() ) {
			bSwitchState = true; 
			int x = (int)((float)Gdx.input.getX() / fAlign );
			int y = Gdx.input.getY();

			if( x > 185 && x < 235 ) {
				if( y > 15 && y < 65 ) {
					iLevel = 1;
					iRoom = 0;
					iShowLevel = 0;
					iPage = 0;
				}
				if( y > 70 && y < 120 && iMaxLevel >= 5 ) {
					iLevel = 5;
					iRoom = 20;
					iShowLevel = 20;
					iPage = 0;
				}
				if( y > 125 && y < 175 ) {
					if( iMaxLevel >= 10 ) {
						iLevel = 10;
						iRoom = 80;
						iShowLevel = 80;
						iPage = 0;
					} else {
						if( bFreeVersion && intentOpener != null ) {
							iPage = 0;
							intentOpener.showMarket();
						}
					}
				}
				if( y > 180 && y < 230 ) {
					if( iMaxLevel >= 15 ) {
						iLevel = 15;
						iRoom = 161;
						iShowLevel = 161;
						iPage = 0;
					} else {
						if( bFreeVersion && intentOpener != null ) {
							iPage = 0;
							intentOpener.showMarket();
						}
					}
				}
				if( y > 235 && y < 285 ) {
					if( iMaxLevel >= 20 ) {
						iLevel = 20;
						iRoom = 241;
						iShowLevel = 241;
						iPage = 0;
					} else {
						if( bFreeVersion && intentOpener != null ) {
							iPage = 0;
							intentOpener.showMarket();
						}
					}
				}
			}
		} else {
			bSwitchState = false;
		}
	}

	private void renderGameOver() {
		renderPicture( 0 );

		sbGame.begin();
		main_font.setColor( Color.WHITE );
		main_font.draw( sbGame, "Game Over", 180, 180 );
		sbGame.end();
		if( Gdx.input.isTouched() || Gdx.input.isKeyPressed( Input.Keys.ANY_KEY ) ) {
			if( bSwitchState == false ) {
				reloadGameMap();
				bSwitchState = true;
				iScore = 0;
				iTnt = TNT_MAX;
				iLevel = 0;
				iShowLevel = 0;
				fPlayerX = 120;
				fPlayerY = 160;
				fGravity = 0;
				fFuel = FUEL_MAX;
				bShowDie = false;
				iNextPage = 0;
				iPage = 0;
			}
		} else {
			bSwitchState = false;
		}
	}

	private void renderPause() {
		renderPicture( 0 );

		sbGame.begin();
		main_font.setColor( Color.WHITE );
		main_font.drawWrapped( sbGame, sPaused, 0, 180, 480, BitmapFont.HAlignment.CENTER );
		sbGame.end();
		if( Gdx.input.isTouched() || Gdx.input.isKeyPressed( Input.Keys.ANY_KEY ) ) {
			if( bSwitchState == false ) {
				bShowDie = false;
				bSwitchState = true;
				iPage = iReturnPage;
				if( bKilled ) {
					bKilled = false;
					fPlayerX = fLastPlayerX;
					fPlayerY = fLastPlayerY;
				}
			}
		} else {
			bSwitchState = false;
		}
	}

	private void renderDefine() {
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		sbGame.begin();
		sbGame.draw( tOptionsBack, 0, iTop - 512 );
		menu_font.drawWrapped( sbGame, "REDEFINE KEY", 30, 244, 240, BitmapFont.HAlignment.CENTER );
		menu_font.drawWrapped( sbGame, "MUSIC VOLUME", 30, 186, 240, BitmapFont.HAlignment.CENTER );
		sbGame.draw( tLaserRight, 50, 148, 200 * fMusicVolume, 8 );
		menu_font.drawWrapped( sbGame, "SOUND VOLUME", 30, 124, 240, BitmapFont.HAlignment.CENTER );
		sbGame.draw( tLaserRight, 50, 86, 200 * fSoundVolume, 8 );
		menu_font.drawWrapped( sbGame, "MAIN MENU", 30, 58, 240, BitmapFont.HAlignment.CENTER );

		Pixmap pmBlack;
		pmBlack = new Pixmap( 16, 16, Format.RGBA8888 );
		pmBlack.setColor( 0, 0, 0, 0.7f );
		pmBlack.fill();
		Texture tBlack = new Texture( pmBlack );
		sbGame.draw( tBlack, 0, 0, iWidth, iTop );
		main_font.setColor( Color.WHITE );
		main_font.draw( sbGame, "Press a fire key", 160, 180 );
		sbGame.end();

		tBlack.dispose();
		pmBlack.dispose();

		if( Gdx.input.isKeyPressed( Input.Keys.ANY_KEY ) ) {
			iFireKey = myInput.iKeyCode;
			iNextPage = 2;
		} else {
			iPage = iNextPage;
			bSwitchState = false;
		}
	}

	private void renderScore() {
		renderPicture( 0 );

		sbGame.begin();
		Pixmap pmBlack;
		pmBlack = new Pixmap( 16, 16, Format.RGBA8888 );
		pmBlack.setColor( 0, 0, 0, 0.7f );
		pmBlack.fill();
		Texture tBlack = new Texture( pmBlack );
		sbGame.draw( tBlack, 0, 0, iWidth, iTop );
		sbGame.end();

		tBlack.dispose();
		pmBlack.dispose();

		if( fFuel > 10 ) {
			fFuel -= 10;
			iScore += 10;
		} else {
			if( fFuel > 0 ) {
				fFuel--;
				iScore++;
			} else {
				if( iTnt > 0 ) {
					iTnt--;
					iScore += 100;
				} else {
					sbGame.begin();
					main_font.setColor( Color.WHITE );
					main_font.drawWrapped( sbGame, "Level complette\nTap to continue", 0, 180, 480, BitmapFont.HAlignment.CENTER );
					sbGame.end();
					if( Gdx.input.isTouched() || Gdx.input.isKeyPressed( Input.Keys.ANY_KEY ) ) {
						if( bSwitchState == false ) {
							fHeroState = 2;
							bSwitchState = true; 
							iPage = 3;
							if( bYodaLeft ) {
								iRoom = iNextRoom[ iRoom ][1];
							} else {
								iRoom = iNextRoom[ iRoom ][0];
							}
							fFuel = FUEL_MAX;
							iTnt = TNT_MAX;
							fPlayerX = 120;
							fPlayerY = 160;
							fLastPlayerX = 120;
							fLastPlayerY = 160;
						}
					} else {
						bSwitchState = false;
					}
				}
			}
		}
	}

	private void renderPicture( float ftimeCorrection ) {
		// Clear picture
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		sbGame.begin();

		// render background
		if( iLampPosition[iRoom][2] == 1 || tBomb != null ) {
			sbGame.draw( tBackCells, 0, iTop - 248 );
			for( int t = 0; t < 3; t++ ) {
				for( int i = 0; i < 32; i++ ) {
					switch( Integer.parseInt( sCompletteMap[iRoom][t].substring( i, i + 1 ) ) ) {
					case 1:
					case 9:
						if( t > 0 && Integer.parseInt( sCompletteMap[iRoom][t - 1].substring( i, i + 1 ) ) == 0 ) {
							sbGame.draw( tHorizontal, i * 15, iTop - t * 80 );
						}
						if( i < 31 && Integer.parseInt( sCompletteMap[iRoom][t].substring( i + 1, i + 2 ) ) == 0 ) {
							sbGame.draw( tVertical_bg, i * 15 + 15, iTop - t * 80 - 112 );
						}
						break;
					case 2:
					case 8:
						if( t > 0 && Integer.parseInt( sCompletteMap[iRoom][t - 1].substring( i, i + 1 ) ) == 0 ) {
							sbGame.draw( tMagma_h, i * 15, iTop - t * 80 );
						}
						if( i < 31 && Integer.parseInt( sCompletteMap[iRoom][t].substring( i + 1, i + 2 ) ) == 0 ) {
							sbGame.draw( tMagma_bg, i * 15 + 15, iTop - t * 80 - 112 );
						}
						break;
					}
				}
			}
		} 

		// render flame
		if( bFly ) {
			TextureRegion currentFrame = aFlame.getKeyFrame( (int)fFlamePos, false );
			sbGame.draw( currentFrame, fPlayerX + 10, fPlayerY - 25 );
			fFlamePos += ftimeCorrection;
			if( fFlamePos > 3 ) {
				fFlamePos = 1;
			}
		} else {
			fFlamePos = 0;
		}

		// render Enemies
		for( int i = 0; i < iEnemyCounter[iRoom]; i++ ) {
			eStack[iRoom][i].render( sbGame, aEnemies, iTop, ftimeCorrection, iLampPosition[iRoom][2] == 1 || tBomb != null, main_font );
		}

		// render Yoda
		if( iExitPosition[iRoom] > 0 ) {
			if( iExitPosition[iRoom] < 16 ) {
				bYodaLeft = false;
				sbGame.draw( tYodaRight, iExitPosition[iRoom] * 15 + 16 + 4, 164 );
			} else {
				bYodaLeft = true;
				sbGame.draw( trYodaLeft, iExitPosition[iRoom] * 15 - 16 + 4, 164 );
			}
		}

		// render laser
		if( bLaserFire ) {
			Gdx.input.vibrate( 5 );
			if( lMoveRight ) {
				sbGame.draw( tLaserRight, fPlayerX + 16, fPlayerY + 25 );
			} else {
				sbGame.draw( trLaserLeft, fPlayerX - 48, fPlayerY + 25 );
			}
		}

		// render person
		if( !bShowDie ) {
			sbGame.draw( aHero.getKeyFrame( fHeroState, false ), fPlayerX + 4, fPlayerY );
		} else {
			fDieState += 0.1f;
			sbGame.draw( aHeroDie.getKeyFrame( fDieState, true ), fPlayerX + 4, fPlayerY );
		}

		// render bombs
		if( tBomb != null ) {
			if( tBomb.iState == 2 ) {
				TextureRegion currentFrame = aTnt.getKeyFrame( (float) (tBomb.fCadr - Math.random()), true );
				sbGame.draw( currentFrame, tBomb.xPos + 2, tBomb.yPos + 2 );
				tBomb.tick( this, ftimeCorrection );
			} else {
				TextureRegion currentFrame = aBoom.getKeyFrame( tBomb.fCadr, true );
				sbGame.draw( currentFrame, tBomb.xPos, tBomb.yPos );
				tBomb.tick( this, ftimeCorrection );
			}
		}

		// render bottom magma
		if( iNextRoom[iRoom][3] == -2 ) {
			fMagmaState += ftimeCorrection;
			for( int t = 0; t < 480; t += 33 ) {
				sbGame.draw( aMagmaBottom.getKeyFrame( fMagmaState, true ), t, 80 );
			}
		}

		// render lamp
		if( iLampPosition[iRoom][0] > -1 ) {
			if( iLampPosition[iRoom][2] == 1 ) {
				sbGame.draw( trLamp[0], iLampPosition[iRoom][0] * 15 + 12, iTop - iLampPosition[iRoom][1] * 80 - 70 );
			} else {
				sbGame.draw( trLamp[1], iLampPosition[iRoom][0] * 15 + 12, iTop - iLampPosition[iRoom][1] * 80 - 70 );
			}
		}

		// render foreground
		if( iLampPosition[iRoom][2] == 1 || tBomb != null ) {
			for( int t = 0; t < 3; t++ ) {
				for( int i = 0; i < 32; i++ ) {
					switch( Integer.parseInt( sCompletteMap[iRoom][t].substring( i, i + 1 ) ) ) {
					case 1:
					case 9:
						if( i < 31 && Integer.parseInt( sCompletteMap[iRoom][t].substring( i + 1, i + 2 ) ) == 0 ) {
							sbGame.draw( tVertical_fg, i * 15 + 15, iTop - t * 80 - 112 );
						}
						sbGame.draw( trPanel[t][i], i * 15, iTop - t * 80 - 80 );
						break;
					case 2:
					case 8:
						if( i < 31 && Integer.parseInt( sCompletteMap[iRoom][t].substring( i + 1, i + 2 ) ) == 0 ) {
							sbGame.draw( tMagma_fg, i * 15 + 15, iTop - t * 80 - 112 );
						}
						trMagma.setRegion( 0, (int)fScroller, 15, 80 );
						sbGame.draw( trMagma, i * 15, iTop - t * 80 - 80 );
						break;
					}
				}
			}
		} 

		fScroller  += 0.25;
		if( fScroller > 160 ) {
			fScroller = 0;
		}

		// render bottom info
		Pixmap pmYellow = new Pixmap( 16, 16, Format.RGBA8888 );
		pmYellow.setColor( 1, 1, 0, 1 );
		pmYellow.fill();
		Texture tFuel = new Texture( pmYellow );
		sbGame.draw( tFuel, 10, 75, ( fFuel / FUEL_MAX ) * 460, -10 );

		main_font.setColor( Color.WHITE );
		main_font.draw( sbGame, "Score : "+iScore, 240, 55 );
		main_font.draw( sbGame, "Level : "+iLevel, 240, 35 );

		for( int i=0; i < iLives; i++ ) {
			sbGame.draw( tLifeIcon, 10 + i * 20, 25 );
		}

		for( int i=0; i < iTnt; i++ ) {
			sbGame.draw( aTnt.getKeyFrame( 0, false ), 10 + i * 20, 15 );
		}

		// render level capital
		if( iRoom == iShowLevel ) {
			cap_font.draw( sbGame, "LEVEL "+iLevel, 10, 140 );
		}

		// render line
		if( bShowLine ) {
			sbGame.draw( tLine, 0, 0 );
		}

		// free winner text
		if( iRoom == 28 && bFreeVersion ) {
			main_font.setColor( Color.WHITE );
			main_font.drawWrapped( sbGame, "Congratulations!\nFree version contains only 5 levels\nPlease buy the full version to get all the levels", 0, 310, 480, BitmapFont.HAlignment.CENTER );
		}

		// winner text
		if( iRoom == 289 ) {
			main_font.setColor( Color.WHITE );
			main_font.drawWrapped( sbGame, "Congratulations!\nYou won this battle\nNow take your R2D2 and run from the death star\nIt is about to explode!", 0, 310, 480, BitmapFont.HAlignment.CENTER );
		}

		sbGame.end();

		tFuel.dispose();
		pmYellow.dispose();
	}

	private void renderGame() {
		float ftimeCorrection = 0.03f;
		float fTotalCorrection = Gdx.graphics.getDeltaTime() * 6;

		// check input
		switch( iInputMethod ) {
		case 0:
			inputAccelerometer( fTotalCorrection );
			break;
		case 1:
			inputTouch( fTotalCorrection );
			break;
		}

		fFuel -= ftimeCorrection / 2;
		
		for( float fStepCorrection = 0; fStepCorrection < fTotalCorrection; fStepCorrection += 0.03f ) {
			if( fTotalCorrection - fStepCorrection < 0.03 ) {
				ftimeCorrection = fTotalCorrection - fStepCorrection; 
			}

			// check tutorials
			bKilled = false;
			switch( iTutorial ) {
			case 0:
				if( iRoom == 0 ) {
					bSwitchState = true;
					iTutorial++;
					sPaused = "Touch the screen anywhere\nand R2D2 go at this point.";
					iPage = 5;
				}
				break;
			case 1:
				if( iRoom == 0 && fPlayerX > 150 && fPlayerY < 170 ) {
					bSwitchState = true;
					iTutorial++;
					sPaused = "Press fire button (default is any button)\n3 seconds or more\nSet the bomb next to a wall.";
					iPage = 5;
				}
				break;
			case 2:
				if( iRoom == 1 ) {
					bSwitchState = true;
					iTutorial++;
					sPaused = "You need to go to Yoda. Find and just walk up to him.";
					iPage = 5;
				}
				break;
			}

			// check collision
			int cMatrix[][];
			cMatrix = buildCollisionMatrix();

			// move person
			if( iMoveH > 0 && cMatrix[2][1] == 0 ) {
				if( fHeroState < 2 ) {
					fHeroState += 0.2f;
				}
				lMoveRight = true;
				fPlayerX += 10 * ftimeCorrection;
			} else if ( iMoveH < 0 && cMatrix[0][1] == 0 ) {
				if( fHeroState > 0 ) {
					fHeroState -= 0.2f;
				}
				lMoveRight = false;
				fPlayerX -= 10 * ftimeCorrection;
			}
			fHeroState = Math.round( fHeroState * 10 ) / 10.0f;
			if( bFly && fFuel > 0 ) {
				if( sndScream.isPlaying() ) {
					sndScream.stop();
				}
				if( !sndFly.isPlaying() ) {
					sndFly.setLooping( true );
					sndFly.play();	
				}
				fFuel -= 1 * ftimeCorrection;
				if( fGravity > 0 ) {
					fGravity += 5 * ftimeCorrection;
				} else if ( fGravity <= 0 ) {
					fGravity += 10 * ftimeCorrection;
				}
			} else {
				sndFly.stop();	
				fGravity -= 5 * ftimeCorrection;
			}

			if( fGravity > 10 ) fGravity = 10;
			if( fGravity < -30 ) {
				if( !sndScream.isPlaying() ) {
					sndScream.play();
				}
				fGravity = -30;
			}

			if( cMatrix[1][2] != 0 ) {
				if( sndScream.isPlaying() ) {
					sndScream.stop();
				}
			}

			if( fGravity > 0 && cMatrix[1][0] == 0 ) {
				fPlayerY += fGravity * ftimeCorrection;
			} else if ( fGravity < 0 ) {
				if( cMatrix[1][2] == 0 ) {
					fPlayerY += fGravity * ftimeCorrection;
	/*			} else if( fGravity <= -30 ) {
					fGravity = 0;
					killPlayer();
	*/			} else {
					fGravity = 0;
				}
			}

			// check Kill Enemies
			for( int i = 0; i < iEnemyCounter[iRoom]; i++ ) {
				if( bLaserFire ) {
					if( !sndLaser.isPlaying() ) {
						sndLaser.setLooping( true );
						sndLaser.play();
					}
					if( lMoveRight ) {
						if( eStack[iRoom][i].check( iTop, fPlayerX + 16, fPlayerY + 16, 48, 12 ) ) {
							eStack[iRoom][i].bActive = false;
							eStack[iRoom][i].bShowScore = true;
							iScore += 50;
						}
					} else {
						if( eStack[iRoom][i].check( iTop, fPlayerX - 48, fPlayerY + 16, 48, 12 ) ) {
							eStack[iRoom][i].bActive = false;
							eStack[iRoom][i].bShowScore = true;
							iScore += 50;
						}
					}
				} else {
					sndLaser.stop();
				}
			}

			// check Enemies
			for( int i = 0; i < iEnemyCounter[iRoom]; i++ ) {
				if( eStack[iRoom][i].check( iTop, fPlayerX + 2, fPlayerY, 25, 40 ) ) {
					if( eStack[iRoom][i].iEnemyType != 4 ) {
						eStack[iRoom][i].bActive = false;
						eStack[iRoom][i].bShowScore = false;
						killPlayer();
					} else {
						fGravity = 0;
						fPlayerX += eStack[iRoom][i].fXmove * ftimeCorrection;
						fPlayerY = iTop - eStack[iRoom][i].fYposition - 64;
					}
				}
			}

			// check Magma walls
			if( cMatrix[1][0] == 2 || cMatrix[0][1] == 2 || cMatrix[2][1] == 2 || cMatrix[1][2] == 2 ||
				cMatrix[1][0] == 8 || cMatrix[0][1] == 8 || cMatrix[2][1] == 8 || cMatrix[1][2] == 8 ) {
				killPlayer();
			}

			// check magma bottom
			if( iNextRoom[iRoom][3] == -2 ) {
				if( fPlayerY < 90 ) {
					killPlayer();
				}
			}

			// check room out
			if( fPlayerX < 0 && iRoom > 0 ) {
				if( iNextRoom[ iRoom ][0] >= 0 ) {
					if( tBomb != null ) {
						tBomb.tick( this, 25 );
					}
					iRoom = iNextRoom[ iRoom ][0];
					fPlayerX = 440;
					fLastPlayerX = 440;
					fLastPlayerY = fPlayerY;
					break;
				} else {
					fPlayerX = 0;
				}
			}
			if( fPlayerX > 448 && iRoom > 0 ) {
				if( iNextRoom[ iRoom ][1] >= 0 ) {
					if( tBomb != null ) {
						tBomb.tick( this, 25 );
					}
					iRoom = iNextRoom[ iRoom ][1];
					fPlayerX = 8;
					fLastPlayerX = 8;
					fLastPlayerY = fPlayerY;
					break;
				} else {
					fPlayerX = 448;
				}
			}
			if( fPlayerY < 80 ) {
				if( iNextRoom[ iRoom ][3] >= 0 ) {
					if( tBomb != null ) {
						tBomb.tick( this, 25 );
					}
					iRoom = iNextRoom[ iRoom ][3];
					fPlayerY = 280;
					fLastPlayerX = fPlayerX;
					fLastPlayerY = 280;
					break;
				} else {
					fPlayerY = 80;
				}
			}
			if( fPlayerY > 284 ) {
				if( iNextRoom[ iRoom ][2] >= 0 ) {
					if( tBomb != null ) {
						tBomb.tick( this, 25 );
					}
					iRoom = iNextRoom[ iRoom ][2];
					fPlayerY = 80;
					fLastPlayerX = fPlayerX;
					fLastPlayerY = 80;
					break;
				} else {
					fPlayerY = 284;
				}
			}

			// check level out
			int iMatrixPlayerX = (int)( ( fPlayerX + 16 ) / 15 );
			int iMatrixPlayerY = (int)( ( iTop - fPlayerY - 40 ) / 80 );
			if( !bYodaLeft && iExitPosition[ iRoom ] > 0 && iExitPosition[ iRoom ] == iMatrixPlayerX - 3 && iMatrixPlayerY == 1 ) {
				if( sndFly.isPlaying() ) {
					sndFly.stop();
				}
				if( sndLaser.isPlaying() ) {
					sndLaser.stop();
				}
				sndYoda.play();
				bSwitchState = true; 
				iLevel++;
				if( iMaxLevel < iLevel ) {
					iMaxLevel = iLevel;
				}
				iShowLevel = iNextRoom[ iRoom ][0];
				iPage = 4;
				tBomb = null;
				break;
			} else if( bYodaLeft && iExitPosition[ iRoom ] > 0 && iExitPosition[ iRoom ] == iMatrixPlayerX + 2 && iMatrixPlayerY == 1 ) {
				if( sndFly.isPlaying() ) {
					sndFly.stop();
				}
				if( sndLaser.isPlaying() ) {
					sndLaser.stop();
				}
				sndYoda.play();
				bSwitchState = true; 
				iLevel++;
				if( iMaxLevel < iLevel ) {
					iMaxLevel = iLevel;
				}
				iShowLevel = iNextRoom[ iRoom ][1];
				iPage = 4;
				tBomb = null;
				break;
			}

			// check lamp collision
			if( iLampPosition[iRoom][0] >= 0 ) {
				if( iMatrixPlayerX == iLampPosition[iRoom][0] - 1 && iMatrixPlayerY == iLampPosition[iRoom][1] ) {
					if( !bInLampSwitch ) {
						bInLampSwitch = true;
						if( iLampPosition[iRoom][2] == 1 ) {
							iLampPosition[iRoom][2] = 0;
						} else {
							iLampPosition[iRoom][2] = 1;
						}
					}
				} else {
					bInLampSwitch = false;
				}
			}

			// move Enemies
			for( int i = 0; i < iEnemyCounter[iRoom]; i++ ) {
				eStack[iRoom][i].move( ftimeCorrection );
			}

			// place bomb
			if( tBomb == null && bPlaceBomb && cMatrix[1][2] != 0 ) {
				sndBomb.play();
				iTnt--;
				tBomb = new Tnt( fPlayerX + 8, fPlayerY );
			}
		}

		renderPicture( fTotalCorrection );
	}

	public void killPlayer() {
		bShowDie = true;
		fDieState = 0;
		iLives--;
		sndBomb.stop();
		sndFly.stop();
		sndLaser.stop();
		sndScream.stop();
		sndYoda.stop();
		Gdx.input.cancelVibrate();
		if( iLives <= 0 ) {
			mVader.setLooping( false );
			mVader.play();
			iPage = 6;
		} else {
			bKilled = true;
			bSwitchState = true;
			sPaused = "Tap to continue";
			fGravity = 0;
			iPage = 5;
		}
	}

	public void makeBoom( final int x, final int y ) {
		int xMap = (int)( ( x + 8 ) / 15 );
		int yMap = (int)( ( iTop - y - 40 ) / 80 );
		int xPlayer = (int)( ( fPlayerX + 16 ) / 15 );
		int yPlayer = (int)( ( fPlayerY - 16 ) / 80 );
		int iStrPos =0;

		if( Math.abs( xMap - xPlayer ) < 4 && yMap == yPlayer ) {
			killPlayer();
		}

		if( yMap >= 0 && yMap < 3 && xMap >= 0 && xMap < 32 ) {
			int iMin = xMap - 2, iMax = xMap + 3;
			if( iMin < 0 ) {
				iMin = 0;
			}
			if( iMax > 32 ) {
				iMax = 32;
			}
			if( sCompletteMap[iRoom][yMap].substring( iMin, iMax ).contains( "8" ) ) {
				iStrPos = sCompletteMap[iRoom][yMap].indexOf( "8" );
				sCompletteMap[iRoom][yMap] = sCompletteMap[iRoom][yMap].substring( 0, iStrPos )+"0"+sCompletteMap[iRoom][yMap].substring( iStrPos + 1 );
				iScore += 50;
			}

			if( sCompletteMap[iRoom][yMap].substring( iMin, iMax ).contains( "9" ) ) {
				iStrPos = sCompletteMap[iRoom][yMap].indexOf( "9" );
				sCompletteMap[iRoom][yMap] = sCompletteMap[iRoom][yMap].substring( 0, iStrPos )+"0"+sCompletteMap[iRoom][yMap].substring( iStrPos + 1 );
				iScore += 50;
			}
		}

		tBomb = null;
	}

	private int[][] buildCollisionMatrix() {
		int x, y;
		int iRet[][] = new int[3][3];

		// top left
		x = (int)( ( fPlayerX + 5 ) / 15 );
		y = (int)( ( iTop - fPlayerY - 40 ) / 80 );
		if( y >= 0 && y < 3 && x >= 0 && x < 32 ) {
			iRet[1][0] = Integer.parseInt( sCompletteMap[iRoom][y].substring( x, x+1 ) );
		}

		// top center
		x = (int)( ( fPlayerX + 15 ) / 15 );
		y = (int)( ( iTop - fPlayerY - 40 ) / 80 );
		if( y >= 0 && y < 3 && x >= 0 && x < 32 ) {
			iRet[1][0] = Math.max( iRet[1][0], Integer.parseInt( sCompletteMap[iRoom][y].substring( x, x+1 ) ) );
		}

		// top right
		x = (int)( ( fPlayerX + 25 ) / 15 );
		y = (int)( ( iTop - fPlayerY - 40 ) / 80 );
		if( y >= 0 && y < 3 && x >= 0 && x < 32 ) {
			iRet[1][0] = Math.max( iRet[1][0], Integer.parseInt( sCompletteMap[iRoom][y].substring( x, x+1 ) ) );
		}

		// left top
		x = (int)( ( fPlayerX + 2 ) / 15 );
		y = (int)( ( iTop - fPlayerY - 38 ) / 80 ); 
		if( y >= 0 && y < 3 && x >= 0 && x < 32 ) {
			iRet[0][1] = Integer.parseInt( sCompletteMap[iRoom][y].substring( x, x+1 ) );
		}

		// left bottom
		x = (int)( ( fPlayerX + 2 ) / 15 );
		y = (int)( ( iTop - fPlayerY - 5 ) / 80 ); 
		if( y >= 0 && y < 3 && x >= 0 && x < 32 ) {
			iRet[0][1] = Math.max( iRet[0][1], Integer.parseInt( sCompletteMap[iRoom][y].substring( x, x+1 ) ) );
		}

		// Right top
		x = (int)( ( fPlayerX + 27 ) / 15 );
		y = (int)( ( iTop - fPlayerY - 38 ) / 80 ); 
		if( y >= 0 && y < 3 && x >= 0 && x < 32 ) {
			iRet[2][1] = Integer.parseInt( sCompletteMap[iRoom][y].substring( x, x+1 ) );
		}

		// Right bottom
		x = (int)( ( fPlayerX + 27 ) / 15 );
		y = (int)( ( iTop - fPlayerY - 5 ) / 80 ); 
		if( y >= 0 && y < 3 && x >= 0 && x < 32 ) {
			iRet[2][1] = Math.max( iRet[2][1], Integer.parseInt( sCompletteMap[iRoom][y].substring( x, x+1 ) ) );
		}

		// bottom left
		x = (int)( ( fPlayerX + 5 )/ 15 );
		y = (int)( ( iTop - fPlayerY ) / 80 ); 
		if( y >= 0 && y < 3 && x >= 0 && x < 32 ) {
			iRet[1][2] = Integer.parseInt( sCompletteMap[iRoom][y].substring( x, x+1 ) );
		}

		// bottom center
		x = (int)( ( fPlayerX + 16 ) / 15 );
		y = (int)( ( iTop - fPlayerY ) / 80 ); 
		if( y >= 0 && y < 3 && x >= 0 && x < 32 ) {
			iRet[1][2] = Math.max( iRet[1][2], Integer.parseInt( sCompletteMap[iRoom][y].substring( x, x+1 ) ) );
		}

		// bottom right
		x = (int)( ( fPlayerX + 25 ) / 15 );
		y = (int)( ( iTop - fPlayerY ) / 80 ); 
		if( y >= 0 && y < 3 && x >= 0 && x < 32 ) {
			iRet[1][2] = Math.max( iRet[1][2], Integer.parseInt( sCompletteMap[iRoom][y].substring( x, x+1 ) ) );
		}

		return iRet;
	}

	private void inputTouch( final float fDeltaTime ) {
		if( Gdx.input.isKeyPressed( Input.Keys.ANY_KEY )) {
			if( !bSwitchState && Gdx.input.isKeyPressed( Input.Keys.BACK) ) {
				bSwitchState = true;
				iPage = 0;
				iNextPage = 0;
			}
			if( Gdx.input.isKeyPressed( iFireKey ) ) {
				bLaserFire = true;
				fPressTime += fDeltaTime;
				if( fPressTime > 3 && iTnt > 0 ) {
					bPlaceBomb = true;
				}
			}
		} else {
			bSwitchState = false;
			bLaserFire = false;
			fPressTime = 0;
			bPlaceBomb = false;
		}

		if( Gdx.input.isTouched() ) {
			int iTargetX = (int)((float)Gdx.input.getX() / fAlign);
			int iTargetY = iTop - Gdx.input.getY();
			if( iLevel < 3 ) {
				Pixmap pLine;
				pLine = new Pixmap( 480, 320, Format.RGBA8888 );
				pLine.setColor( 0, 0, 0, 0);
				pLine.fill();
				pLine.setColor( 1, 1, 1, 0.5f);
				pLine.drawLine( (int)fPlayerX + 16, iTop - (int)fPlayerY - 8, iTargetX, iTop - iTargetY );
				tLine.draw( pLine, 0, 512 - iTop );
				bShowLine = true;
				pLine.dispose();
			}

			if( iTargetX < fPlayerX + 10 ) {
				iMoveH = -1;
			} else if( iTargetX > fPlayerX + 30 ) {
				iMoveH = 1;
			} else {
				iMoveH = 0;
			}

			if( iTargetY > fPlayerY + 10 ) {
				bFly = true;
			} else {
				bFly = false;
			}

			if( iRoom == 28 && bFreeVersion ) {
				if( Gdx.input.getY() < 80 ) {
					if( intentOpener != null ) {
						intentOpener.showMarket();
					}
				}
			}
		} else {
			iMoveH = 0;
			bFly = false;
			bShowLine = false;
		}
	}

	private void inputAccelerometer( final float fDeltaTime ) {
		int aX = (int) Gdx.input.getAccelerometerX();
		int aY = (int) Gdx.input.getAccelerometerY();
		int aZ = (int) Gdx.input.getAccelerometerZ();

		if( Gdx.input.isKeyPressed( Input.Keys.ANY_KEY ) ) {
			if( Gdx.input.isKeyPressed( Input.Keys.BACK ) ) {
				iPage = 0;
				iNextPage = 0;
			}
			if( Gdx.input.isKeyPressed( iFireKey ) ) {
				bLaserFire = true;
				fPressTime += fDeltaTime;
				if( fPressTime > 3 && iTnt > 0 ) {
					bPlaceBomb = true;
				}
			}
		} else {
			bSwitchState = false;
			bLaserFire = false;
			fPressTime = 0;
			bPlaceBomb = false;
		}

		if( aY < -2 ) {
			iMoveH = -1;
		} else if( aY > 2 ) {
			iMoveH = 1;
		} else {
			iMoveH = 0;
		}

		if( oldX - 2 > aX || oldZ - 2 > aZ ) {
			oldX = (int) aX;
			oldZ = (int) aZ;
			
			bFly = false;
		}

		if( oldX - 2 > aX || oldZ + 2 < aZ ) {
			oldX = (int) aX;
			oldZ = (int) aZ;

			bFly = true;
		}
	}

	private void renderOptions() {
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		sbGame.begin();
		sbGame.draw( tOptionsBack, 0, iTop - 512 );
		menu_font.drawWrapped( sbGame, "REDEFINE KEY", 30, 244, 240, BitmapFont.HAlignment.CENTER );
		menu_font.drawWrapped( sbGame, "MUSIC VOLUME", 30, 186, 240, BitmapFont.HAlignment.CENTER );
		sbGame.draw( tLaserRight, 50, 148, 200 * fMusicVolume, 8 );
		menu_font.drawWrapped( sbGame, "SOUND VOLUME", 30, 124, 240, BitmapFont.HAlignment.CENTER );
		sbGame.draw( tLaserRight, 50, 86, 200 * fSoundVolume, 8 );
		menu_font.drawWrapped( sbGame, "MAIN MENU", 30, 58, 240, BitmapFont.HAlignment.CENTER );
		sbGame.end();
		if( !bSwitchState && Gdx.input.isKeyPressed( Input.Keys.BACK) ) {
			bSwitchState = true;
			iNextPage = 0;
		}

		if( !bSwitchState && Gdx.input.isTouched() ) {
			bSwitchState = true; 
			float x = (int)((float)Gdx.input.getX() / fAlign);
			float y = Gdx.input.getY();
			if( x > 27 && x < 270 ) {
				if( y > 72 && y < 108 ) {
					iNextPage = 1;
				}
				if( y > 135 && y < 171 ) {
					if( x > 50 && x < 250 ) {
						fMusicVolume = ( x - 50 ) / 200;

						mBackMusic.setVolume( fMusicVolume );
					}
				}
				if( y > 197 && y < 233 ) {
					if( x > 50 && x < 250 ) {
						fSoundVolume = ( x - 50 ) / 200;

						sndScream.setVolume( fSoundVolume );
						sndFly.setVolume( fSoundVolume );
						sndBomb.setVolume( fSoundVolume );
						sndYoda.setVolume( fSoundVolume );
						sndLaser.setVolume( fSoundVolume );
					}
				}
				if( y > 258 && y < 294 ) {
					iNextPage = 0;
				}
			}
		}

		if( bSwitchState && !Gdx.input.isTouched() && !Gdx.input.isKeyPressed( Input.Keys.ANY_KEY ) ) {
			bSwitchState = false;
			iPage = iNextPage;
		}
	}

	private void renderMenu() {
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		sbGame.begin();
		sbGame.draw( tMenuBack, 0, iTop - 512 );
		menu_font.setColor( 0.8f, 0.8f, 1, 1 );
		if( iRoom != 0 ) {
			menu_font.drawWrapped( sbGame, "CONTINUE", 116, 252, 240, BitmapFont.HAlignment.CENTER );
		} else {
			menu_font.drawWrapped( sbGame, "START", 116, 252, 240, BitmapFont.HAlignment.CENTER );
		}
		menu_font.drawWrapped( sbGame, "OPTIONS", 116, 186, 240, BitmapFont.HAlignment.CENTER );
		menu_font.drawWrapped( sbGame, "RESET GAME", 116, 120, 240, BitmapFont.HAlignment.CENTER );
		menu_font.drawWrapped( sbGame, "CLOSE", 116, 53, 240, BitmapFont.HAlignment.CENTER );
		sbGame.end();
		if( !bSwitchState && Gdx.input.isKeyPressed( Input.Keys.BACK) ) {
			Gdx.app.exit();
		}

		if( !bSwitchState && Gdx.input.isTouched() ) {
			bSwitchState = true; 
			int x = (int)((float)Gdx.input.getX() / fAlign );
			int y = Gdx.input.getY();
			if( x > 115 && x < 358 ) {
				if( y > 64 && y < 105 ) {
					iNextPage = 3;
				}
				if( y > 130 && y < 173 ) {
					iNextPage = 2;
				}
				if( y > 198 && y < 239 ) {
					iLives = 6;
					iLevel = 1;
					iInputMethod = 1;
					iScore = 0;
					iTnt = TNT_MAX;
					iShowLevel = 0;
					fPlayerX = 120;
					fPlayerY = 160;
					fFuel = FUEL_MAX;
					iPage = 8;
				}

				if( y > 264 && y < 306 ) {
					Gdx.app.exit();
				}
			}
		}

		if( bSwitchState && !Gdx.input.isTouched() && !Gdx.input.isKeyPressed( Input.Keys.ANY_KEY ) ) {
			bSwitchState = false;
			iPage = iNextPage;
		}
	}

	@Override
	public void pause() {
		prefs.putInteger( "room", iRoom );
		prefs.putInteger( "maxLevel", iMaxLevel );
		prefs.putInteger( "lives", iLives );
		prefs.putInteger( "level", iLevel );
		prefs.putInteger( "inputMethod", iInputMethod );
		prefs.putInteger( "score", iScore );
		prefs.putInteger( "tntCount", iTnt );
		prefs.putInteger( "showLevel", iShowLevel );
		prefs.putInteger( "fireKey", iFireKey );
		prefs.putFloat( "musicVolume", fMusicVolume );
		prefs.putFloat( "soundVolume", fSoundVolume );
		prefs.putFloat( "playerX", fPlayerX );
		prefs.putFloat( "playerY", fPlayerY );
		prefs.putFloat( "fuel", fFuel );
		prefs.putBoolean( "laserRight", lMoveRight );
		prefs.putInteger( "tutorPage", iTutorial );
		prefs.flush();

		bSwitchState = Gdx.input.isTouched();
		sPaused = "Tap to continue";

		// free sounds
		sndScream.dispose();
		sndFly.dispose();
		sndBomb.dispose();
		sndYoda.dispose();
		sndLaser.dispose();
		mBackMusic.dispose();
		mVader.dispose();

		// free textures
		sbGame.dispose();
		tLamp.dispose();
		tHorizontal.dispose();
		tVertical_bg.dispose();
		tVertical_fg.dispose();
		tMagma_bg.dispose();
		tMagma_fg.dispose();
		tLifeIcon.dispose();
		tMagma.dispose();
		tMagma_h.dispose();
		tMenuBack.dispose();
		tLevelMap.dispose();
		tOptionsBack.dispose();
		tLine.dispose();
		tHero_anim.dispose();
		tHeroDie_anim.dispose();
        tMagmaBottom.dispose();
		tLaserRight.dispose();
		tYodaRight.dispose();
		tTntSheet.dispose();
		tBoomSheet.dispose();
		tFlameSheet.dispose();
		tLevelIcons.dispose();
		tDroid.dispose();
		tDroid2.dispose();
		tDroid3.dispose();
        tRedLaser.dispose();
		tRaft.dispose();
        main_font.dispose();
		cap_font.dispose();
		menu_font.dispose();
	}

	@Override
	public void resume() {
		iRoom = prefs.getInteger( "room", 0 );
		iMaxLevel = prefs.getInteger( "maxLevel", 1 );
		iLives = prefs.getInteger( "lives", 6 );
		iLevel = prefs.getInteger( "level", 1 );
		iInputMethod = prefs.getInteger( "inputMethod", 1 );
		iScore = prefs.getInteger( "score", 0 );
		iTnt = prefs.getInteger( "tntCount", TNT_MAX );
		iShowLevel = prefs.getInteger( "showLevel", 0 );
		iFireKey = prefs.getInteger( "fireKey", Input.Keys.ANY_KEY );
		fMusicVolume = prefs.getFloat( "musicVolume", 1 );
		fSoundVolume = prefs.getFloat( "soundVolume", 1 );
		fPlayerX = prefs.getFloat( "playerX", 120 );
		fPlayerY = prefs.getFloat( "playerY", 160 );
		fFuel = prefs.getFloat( "fuel", FUEL_MAX );
		lMoveRight = prefs.getBoolean( "laserRight", true );
		iTutorial = prefs.getInteger( "tutorPage", 0 );

		bKilled = false;
		bSwitchState = true;
		sPaused = "Tap to continue";
		iPage = 5;

		// load sounds
		sndScream = Gdx.audio.newMusic( Gdx.files.internal( "r2d2small.mp3") );
		sndFly = Gdx.audio.newMusic( Gdx.files.internal( "flysmall.mp3") );
		sndBomb = Gdx.audio.newMusic( Gdx.files.internal( "timebomb.mp3") );
		sndYoda = Gdx.audio.newMusic( Gdx.files.internal( "yoda.mp3") );
		sndLaser = Gdx.audio.newMusic( Gdx.files.internal( "laser.mp3") );
		mBackMusic = Gdx.audio.newMusic( Gdx.files.internal( "smus.mp3") );
		mVader = Gdx.audio.newMusic( Gdx.files.internal( "vader.mp3") );

		// set volumes
		mBackMusic.setVolume( fMusicVolume );
		sndScream.setVolume( fSoundVolume );
		sndFly.setVolume( fSoundVolume );
		sndBomb.setVolume( fSoundVolume );
		sndYoda.setVolume( fSoundVolume );
		sndLaser.setVolume( fSoundVolume );

		// load render textures
		sbGame = new SpriteBatch();
		tLamp = new Texture( Gdx.files.internal( "lamp.png" ) );
		tHorizontal = new Texture( Gdx.files.internal( "horizon.png" ) );
		tVertical_bg = new Texture( Gdx.files.internal( "vertical_bg.png" ) );
		tVertical_fg = new Texture( Gdx.files.internal( "vertical_fg.png" ) );
		tMagma_bg = new Texture( Gdx.files.internal( "magma_bg.png" ) );
		tMagma_fg = new Texture( Gdx.files.internal( "magma_fg.png" ) );
		tLifeIcon = new Texture( Gdx.files.internal( "life_icon.png" ) );
		tMagma = new Texture( Gdx.files.internal( "magma.png" ) );
		tLine = new Texture( 512, 512, Format.RGBA8888 );
		tMagma_h = new Texture( Gdx.files.internal( "magma_h.png" ) );
		tMenuBack = new Texture( Gdx.files.internal( "main_menu.jpg" ) );
		tOptionsBack = new Texture( Gdx.files.internal( "options.jpg" ) );
		tLevelMap = new Texture( Gdx.files.internal( "levels_bg.png" ) );
		trMagma = new TextureRegion( tMagma );
		trMagma.setRegion( 0, 0, 15, 80 );

		tHero_anim = new Texture( Gdx.files.internal( "hero_anim.png" ) );
		TextureRegion[][] trTmp = TextureRegion.split( tHero_anim, 30, 42 );
		trHero = new TextureRegion[5];
		int index = 0;
        for (int i = 0; i < 5; i++) {
        	trHero[index++] = trTmp[0][i];
        }
        aHero = new Animation(0.5f, trHero);

		tHeroDie_anim = new Texture( Gdx.files.internal( "hero_die.png" ) );
		trTmp = TextureRegion.split( tHeroDie_anim, 30, 42 );
		trHeroDie = new TextureRegion[10];
		index = 0;
        for (int i = 0; i < 10; i++) {
        	trHeroDie[index++] = trTmp[0][i];
        }
        aHeroDie = new Animation(0.25f, trHeroDie);

		trTmp = TextureRegion.split( tLamp, 4, 64 );
		trLamp = new TextureRegion[2];
		index = 0;
        for (int i = 0; i < 2; i++) {
        	trLamp[index++] = trTmp[0][i];
        }
        aHeroDie = new Animation(0.25f, trHeroDie);

        tMagmaBottom = new Texture( Gdx.files.internal( "magma_bottom.png" ) );
		trTmp = TextureRegion.split( tMagmaBottom, 33, 16 );
		trMagmaBottom = new TextureRegion[5];
		index = 0;
        for (int i = 0; i < 5; i++) {
        	trMagmaBottom[index++] = trTmp[0][i];
        }
        aMagmaBottom = new Animation(0.4f, trMagmaBottom);

        Texture tBackGround = new Texture( Gdx.files.internal( "panel.png" ) );
		trPanel = TextureRegion.split( tBackGround, 15, 80 );

		tLevelIcons = new Texture( Gdx.files.internal( "levels.png" ) );
		trLevelIcons = TextureRegion.split( tLevelIcons, 50, 50 );

		tLaserRight = new Texture( Gdx.files.internal( "laser.png" ) );
		trLaserLeft = new TextureRegion( tLaserRight );
		trLaserLeft.flip( true, false );

		tYodaRight = new Texture( Gdx.files.internal("yoda.png") );
		trYodaLeft = new TextureRegion( tYodaRight );
		trYodaLeft.flip( true, false );

		tTntSheet = new Texture( Gdx.files.internal("tnt_anim.png") );
		trTmp = TextureRegion.split( tTntSheet, 16, 16 );
		trTnt = new TextureRegion[8];
		index = 0;
        for (int i = 0; i < 8; i++) {
        	trTnt[index++] = trTmp[0][i];
        }
        aTnt = new Animation(2.4f, trTnt);

		tBoomSheet = new Texture( Gdx.files.internal("boom_anim.png") );
		trTmp = TextureRegion.split( tBoomSheet, 32, 32 );
		trBoom = new TextureRegion[8];
		index = 0;
        for (int i = 0; i < 8; i++) {
        	trBoom[index++] = trTmp[0][i];
        }
        aBoom = new Animation(0.2f, trBoom);

		tFlameSheet = new Texture( Gdx.files.internal("flame.png") );
		trTmp = TextureRegion.split( tFlameSheet, 16, 32 );
		trFlame = new TextureRegion[3];
		index = 0;
        for (int i = 0; i < 3; i++) {
        	trFlame[index++] = trTmp[0][i];
        }
        aFlame = new Animation(1f, trFlame);

		tDroid = new Texture( Gdx.files.internal("droid.png") );
		trTmp = TextureRegion.split( tDroid, 32, 48 );
		trDroid = new TextureRegion[4];
		index = 0;
        for (int i = 0; i < 4; i++) {
        	trDroid[index++] = trTmp[0][i];
        }
        aEnemies[0] = new Animation(0.5f, trDroid);

		tDroid2 = new Texture( Gdx.files.internal("droid2.png") );
		trTmp = TextureRegion.split( tDroid2, 32, 16 );
		trDroid2 = new TextureRegion[4];
		index = 0;
        for (int i = 0; i < 4; i++) {
        	trDroid2[index++] = trTmp[0][i];
        }
        aEnemies[1] = new Animation(0.5f, trDroid2);

		tDroid3 = new Texture( Gdx.files.internal("droid3.png") );
		trTmp = TextureRegion.split( tDroid3, 32, 32 );
		trDroid3 = new TextureRegion[4];
		index = 0;
        for (int i = 0; i < 4; i++) {
        	trDroid3[index++] = trTmp[0][i];
        }
        aEnemies[2] = new Animation(0.5f, trDroid3);

        tRedLaser = new Texture( Gdx.files.internal("laser_red.png") );
		trTmp = TextureRegion.split( tRedLaser, 32, 8 );
		trRedLaser = new TextureRegion[4];
		index = 0;
        for (int i = 0; i < 4; i++) {
        	trRedLaser[index++] = trTmp[0][i];
        }
        aEnemies[3] = new Animation(0.5f, trRedLaser);

		tRaft = new Texture( Gdx.files.internal("raft.png") );
		trTmp = TextureRegion.split( tRaft, 40, 32 );
		trRaft = new TextureRegion[6];
		index = 0;
        for (int i = 0; i < 6; i++) {
        	trRaft[index++] = trTmp[0][i];
        }
        aEnemies[4] = new Animation(0.5f, trRaft);

        main_font = new BitmapFont( Gdx.files.internal("main_font.fnt"), false );
		main_font.setColor( Color.WHITE );

		cap_font = new BitmapFont( Gdx.files.internal("level_cap.fnt"), false );
		cap_font.setColor( Color.WHITE );

		menu_font = new BitmapFont( Gdx.files.internal("buttons_font.fnt"), false );
	}

	@Override
	public void dispose() {
		// free input
		Gdx.input.setInputProcessor( null );

		// save preferences
		prefs.flush();

		// free sounds
		sndScream.dispose();
		sndFly.dispose();
		sndBomb.dispose();
		sndYoda.dispose();
		sndLaser.dispose();
		mBackMusic.dispose();
		mVader.dispose();

		// free textures
		sbGame.dispose();
		tLamp.dispose();
		tHorizontal.dispose();
		tVertical_bg.dispose();
		tVertical_fg.dispose();
		tMagma_bg.dispose();
		tMagma_fg.dispose();
		tLifeIcon.dispose();
		tMagma.dispose();
		tMagma_h.dispose();
		tMenuBack.dispose();
		tLevelMap.dispose();
		tOptionsBack.dispose();
		tLine.dispose();
		tHero_anim.dispose();
		tHeroDie_anim.dispose();
        tMagmaBottom.dispose();
		tLaserRight.dispose();
		tYodaRight.dispose();
		tTntSheet.dispose();
		tBoomSheet.dispose();
		tFlameSheet.dispose();
		tLevelIcons.dispose();
		tDroid.dispose();
		tDroid2.dispose();
		tDroid3.dispose();
        tRedLaser.dispose();
		tRaft.dispose();
        main_font.dispose();
		cap_font.dispose();
		menu_font.dispose();
	}
}
