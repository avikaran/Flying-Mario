package com.avikaran.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;


import java.util.Random;


public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture bottomTube;
	Texture topTube;
	Texture marios[];
	Texture gameOver;

	int flapState = 0;

	float birdY; //stores the position of the bird in y direction
	float birdX;

	float velocity=0; // defines the rate with which it will fall/jump ... will increase gradually by a factor of gravity
	float gravity = (float) 0.4; // defines the rate with which it will fall

	int gameState = 0;

	float gap=400;

	float maxTubeOffset;
	float tubeVelocity = 4;




	float distanceBetweenTubes;

	float tubeX1 ;
	float tubeOffset1 ;

	float tubeX2 ;
	float tubeOffset2 ;

	Random randomGenerator = new Random(); // to move the pipes randomly

	/*********************************************************/
	//Logic for collision

	ShapeRenderer shapeRenderer;
	Circle birdCircle;
	Circle marioCircle;
	Rectangle topTubeRectangle1;
	Rectangle bottomTubeRectangle1;

	Rectangle topTubeRectangle2;
	Rectangle bottomTubeRectangle2;

	/********************************/

	int score;
	boolean tubePair1Scorer;
	boolean tubePair2Scorer;
	/*******************************/


	BitmapFont font;

	Music gameOverSound;
	Music backgroundMusic;
	Music jump;




	@Override
	public void create () {

		batch = new SpriteBatch();
		background = new Texture("mariobg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		gameOver = new Texture("gameover.png");

		marios = new Texture[2];
		marios[0] = new Texture("marioup.png");
		marios[1] = new Texture("mariodown.png");




		bottomTube = new Texture("bottomtube.png");
		topTube = new Texture("toptube.png");
		distanceBetweenTubes = Gdx.graphics.getWidth();



		maxTubeOffset = Gdx.graphics.getHeight()/2-gap/2-100; //considering 100 is the extra bulging part on the tube

		birdCircle = new Circle();
		marioCircle = new Circle();
		//shapeRenderer = new ShapeRenderer();
		topTubeRectangle1 = new Rectangle();
		bottomTubeRectangle1 = new Rectangle();

		topTubeRectangle2 = new Rectangle();
		bottomTubeRectangle2 = new Rectangle();



		font= new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().scale(5);
		backgroundMusic= Gdx.audio.newMusic(Gdx.files.internal("sounds/mariotheme.wav"));
		gameOverSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameover2.wav"));
		jump = Gdx.audio.newMusic(Gdx.files.internal("sounds/jump.wav"));

		gameStart();
	}

	public void gameStart()
	{


		birdX= Gdx.graphics.getWidth()/2- birds[0].getWidth()/2; // Wont change
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

		velocity=0;
		gameState = 0;

		tubeX1 = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth()/2;
		tubeX2 = tubeX1 + distanceBetweenTubes;
		score = 0;
		tubePair1Scorer=true;
		tubePair2Scorer=true;



		backgroundMusic.play();
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		if(gameState==1)
		{
			batch.draw(topTube, tubeX1 ,Gdx.graphics.getHeight()/2+gap/2 + tubeOffset1  );
			batch.draw(bottomTube, tubeX1 , -bottomTube.getHeight()+ Gdx.graphics.getHeight()/2-gap/2 + tubeOffset1);
			topTubeRectangle1.set(tubeX1 ,Gdx.graphics.getHeight()/2+gap/2 + tubeOffset1, topTube.getWidth(),topTube.getHeight());
			bottomTubeRectangle1.set(tubeX1 , -bottomTube.getHeight()+ Gdx.graphics.getHeight()/2-gap/2 + tubeOffset1, bottomTube.getWidth(), bottomTube.getHeight());

			//shapeRenderer.rect(bottomTubeRectangle1.x, bottomTubeRectangle1.y, bottomTubeRectangle1.width, bottomTubeRectangle1.height);
			//shapeRenderer.rect(topTubeRectangle1.x, topTubeRectangle1.y, topTubeRectangle1.width, topTubeRectangle1.height);

			batch.draw(topTube, tubeX2 ,Gdx.graphics.getHeight()/2+gap/2 + tubeOffset2  );
			batch.draw(bottomTube, tubeX2 , -bottomTube.getHeight()+ Gdx.graphics.getHeight()/2-gap/2 + tubeOffset2);
			topTubeRectangle2.set(tubeX2 ,Gdx.graphics.getHeight()/2+gap/2 + tubeOffset2, topTube.getWidth(),topTube.getHeight());
			bottomTubeRectangle2.set(tubeX2 , -bottomTube.getHeight()+ Gdx.graphics.getHeight()/2-gap/2 + tubeOffset2, bottomTube.getWidth(), bottomTube.getHeight());

			//shapeRenderer.rect(bottomTubeRectangle2.x, bottomTubeRectangle2.y, bottomTubeRectangle2.width, bottomTubeRectangle2.height);
			//shapeRenderer.rect(topTubeRectangle2.x, topTubeRectangle2.y, topTubeRectangle2.width, topTubeRectangle2.height);


			tubeX1-=tubeVelocity;
			tubeX2-=tubeVelocity;
			if(tubeX1<-bottomTube.getWidth()-30)
			{
				tubeX1 = Gdx.graphics.getWidth()+ 2*bottomTube.getWidth();
				tubeOffset1 = randomGenerator.nextInt((int) maxTubeOffset); // for up and down the tube
				tubePair1Scorer=true;
			}
			if(tubeX2<-bottomTube.getWidth()-30)
			{
				tubeX2 = Gdx.graphics.getWidth()+ 2*bottomTube.getWidth();
				tubeOffset2 = randomGenerator.nextInt((int) maxTubeOffset); // for up and down the tube
				tubePair2Scorer= true;
			}

			if( (tubeX1< Gdx.graphics.getWidth()/2-bottomTube.getWidth() && tubePair1Scorer==true))
			{
				score+=1;
				tubePair1Scorer= false;
			}
			if(tubeX2< Gdx.graphics.getWidth()/2-bottomTube.getWidth()&& tubePair2Scorer==true)
			{
				score+=1;
				tubePair2Scorer=false;
			}

			if(Gdx.input.justTouched())
			{
				velocity=-12; // jump magnitude.... greater the negative greater the jump
				jump.play();
			}
			if(birdY>0 || velocity<0)
			{
				velocity += gravity;
				birdY -= velocity;
			}
		}
		else
		{
			if(Gdx.input.justTouched())
			{
				gameState=1;
			}
		}

		/**/

		if (flapState == 1) {
			flapState = 0;
		} else {
			flapState = 1;
		}
		//batch.draw(birds[flapState], birdX, birdY);
		batch.draw(marios[flapState],birdX,birdY);

		font.draw(batch,"Score: "+String.valueOf(score),100,150);


		birdCircle.set(Gdx.graphics.getWidth()/2, birdY+birds[0].getHeight()/2, Math.max(birds[0].getWidth()/2, birds[0].getHeight()/2)-20);
		marioCircle.set(Gdx.graphics.getWidth()/2, birdY+marios[0].getHeight()/2, Math.max(marios[0].getWidth()/2,marios[0].getHeight()/2)-20);



		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);



		if(Intersector.overlaps(marioCircle,topTubeRectangle1) ||
				Intersector.overlaps(marioCircle,topTubeRectangle2) ||
				Intersector.overlaps(marioCircle,bottomTubeRectangle1) ||
				Intersector.overlaps(marioCircle,bottomTubeRectangle2) ||
				birdY<10)
		{

			//batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2, Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
			gameState=2;


		}

		if(gameState==2)
		{
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2, Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
			//gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gameover111.wav"));

			backgroundMusic.stop();
			gameOverSound.play();



			if(birdY>-400) {
				birdY -= 10;

			}




			if(Gdx.input.justTouched())
			{
				gameOverSound.stop();
				gameStart();

			}

		}

		batch.end();





		//shapeRenderer.end();




	}

	@Override
	public void dispose () {
		gameOverSound.dispose();
		backgroundMusic.dispose();
		jump.dispose();
		batch.dispose();
	}
}
