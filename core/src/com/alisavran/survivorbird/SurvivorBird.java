package com.alisavran.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture airship1;
	Texture airship2;
	Texture airship3;

	float birdx = 0;
	float birdy = 0;
	int gameState=0;
	float velocity=0;
	float gravity=0.1f;
	float enemyVelocity = 3;
	Random random;
	int score = 0 ;
	int scoreEnemy = 0;
	BitmapFont font ;
	BitmapFont font2;
	int highScore = 0 ;

	Circle birdCircle;

	ShapeRenderer  shapeRenderer;

	/*düşman döngüsü*/int numberofEnemies = 4;
	float [] enemyx = new float[numberofEnemies];
	float[] enemyOfSet1 = new float[numberofEnemies];
	float[] enemyOfSet2 = new float[numberofEnemies];
	float[] enemyOfSet3 = new float[numberofEnemies];

	float distance = 0;

	Circle[] enemyCircles;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;


	@Override
	public void create () { //oyun açıldığında ne olacağını söyleyen sınıf
		batch = new SpriteBatch();
		background = new Texture("tilesetOpenGameBackground.png");
		bird = new Texture("frame-1.png");
		airship1 = new Texture("gif-sample.gif");
		airship2 = new Texture("gif-sample.gif");
		airship3 = new Texture("gif-sample.gif");

		distance = Gdx.graphics.getWidth() / 2; // düşman aralığı
		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.BLUE);
		font.getData().setScale(4);

		font2 = new BitmapFont();
		font2.setColor(Color.RED);
		font2.getData().setScale(6);

		birdx = Gdx.graphics.getWidth() / 2 - Gdx.graphics.getHeight() / 2; /*kuşun başlangıç konumu*/
		birdy = Gdx.graphics.getHeight() / 3;

		shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		enemyCircles = new Circle[numberofEnemies];
		enemyCircles2 = new Circle[numberofEnemies];
		enemyCircles3 = new Circle[numberofEnemies];

		float minimumDistance = Gdx.graphics.getHeight() / 5;

		for (int i = 0; i < numberofEnemies; i++) {
			boolean isPositionValid = false;

			while (!isPositionValid) {
				// Yeni pozisyonları rastgele belirle
				enemyOfSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				enemyOfSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				enemyOfSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

				// Pozisyonların geçerli olup olmadığını kontrolü
				isPositionValid = true;
				for (int j = 0; j < i; j++) {
					if (Math.abs(enemyOfSet1[i] - enemyOfSet1[j]) < minimumDistance ||
							Math.abs(enemyOfSet2[i] - enemyOfSet2[j]) < minimumDistance ||
							Math.abs(enemyOfSet3[i] - enemyOfSet3[j]) < minimumDistance) {
						isPositionValid = false;
						break;
					}
				}
			}

			// Düşmanların x eksenindeki konumları
			enemyx[i] = Gdx.graphics.getWidth() - airship1.getWidth() / 2 + i * distance;

			// Çarpma alanları
			enemyCircles[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}

	}

	@Override
	public void render () { // oyun devam ederken ne olacağını söyleyen sınıf
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gameState == 1){ // oyun devam ediyorsa

			if (enemyx[scoreEnemy] <Gdx.graphics.getWidth() / 2 - Gdx.graphics.getHeight() / 2){
				score++;
				if (scoreEnemy < numberofEnemies -1){
					scoreEnemy++;
				}else {
					scoreEnemy = 0;
				}
			}

			if (birdy > Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 10) {
				birdy = Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 10;
				velocity = 0;
			}

			if (enemyx[scoreEnemy] < Gdx.graphics.getWidth() / 2 - Gdx.graphics.getHeight() / 2) {
				score++;
				if (scoreEnemy < numberofEnemies - 1) {
					scoreEnemy++;
				} else {
					scoreEnemy = 0;
				}
			}

			if (Gdx.input.justTouched()){//kuşun yukarı hareketi
				velocity = -6;
			}
			for (int i = 0;i<numberofEnemies;i++){ //düşam hareketi ve konumunu güncelleme

				if (enemyx[i] < Gdx.graphics.getWidth() / 15){ // düşman ekranın sonuna yaklaşmışsa
					enemyx[i] = enemyx[i] + numberofEnemies*distance;

					enemyOfSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOfSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOfSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-200);
				}else {
					enemyx[i] = enemyx[i] - enemyVelocity;  // yaklaşmamışsa devam eder
				}

				enemyx[i] = enemyx[i] - enemyVelocity;

				batch.draw(airship1,enemyx[i],Gdx.graphics.getHeight()/2 + enemyOfSet1[i],Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
				batch.draw(airship2,enemyx[i],Gdx.graphics.getHeight()/2 + enemyOfSet2[i],Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
				batch.draw(airship3,enemyx[i],Gdx.graphics.getHeight()/2 + enemyOfSet3[i],Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);

				enemyCircles[i] = new Circle(enemyx[i] +Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight()/2 + enemyOfSet1[i]
						+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
				enemyCircles2[i] = new Circle(enemyx[i] +Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight()/2 + enemyOfSet2[i]
						+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
				enemyCircles3[i] = new Circle(enemyx[i] +Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight()/2 + enemyOfSet3[i]
						+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
			}



			if (birdy > 0){ // kuş yere çapmamışsa
				velocity = velocity+gravity; // kuşun hızı yerçekimine göre artar gravtiy= yerçekimi
				birdy = birdy -velocity; //kuşun hızı yerçekimine göre azalır
			}else { // kuş yere çapmışsa oyun biter
				gameState = 2;
				if (score > highScore){
					highScore = score;
				}
			}
		}else if (gameState ==0){
			if (Gdx.input.justTouched()){
				gameState=1;
			}
		}else if (gameState == 2){

			float textWidth = font2.getRegion().getRegionWidth();
			float textHeight = font2.getRegion().getRegionHeight();
			font2.draw(batch,"Game Over... \n" +
					"High Score:"+highScore,Gdx.graphics.getWidth() / 2 - textWidth / 2 ,Gdx.graphics.getHeight() / 2 - textHeight / 2);

			if (Gdx.input.justTouched()){
				gameState=1;

				birdy = Gdx.graphics.getHeight() / 3;

				for (int i=0; i<numberofEnemies; i++){

					enemyOfSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOfSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOfSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-200);

					enemyx[i]=Gdx.graphics.getWidth() - airship1.getWidth()/2 + i*distance;

					enemyCircles[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}

				velocity = 0;
				scoreEnemy=0;
				score=0;

			}
		}


		batch.draw(bird,birdx,birdy,Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();
		birdCircle.set(birdx +Gdx.graphics.getWidth() / 30,birdy+Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth()/30);
		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);*/

		for (int i = 0;i<numberofEnemies; i++){ // kuşun canavarlara çaptığını kontrol eder
		/*	shapeRenderer.circle(enemyx[i] +Gdx.graphics.getWidth() / 40, Gdx.graphics.getHeight()/2 + enemyOfSet1[i]
					+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 40);
			shapeRenderer.circle(enemyx[i] +Gdx.graphics.getWidth() / 40, Gdx.graphics.getHeight()/2 + enemyOfSet2[i]
					+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 40);
			shapeRenderer.circle(enemyx[i] +Gdx.graphics.getWidth() / 40, Gdx.graphics.getHeight()/2 + enemyOfSet3[i]
					+ Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 40);*/

			if (Intersector.overlaps(birdCircle,enemyCircles[i]) || Intersector.overlaps(birdCircle,enemyCircles2[i]) || Intersector.overlaps(birdCircle,enemyCircles3[i])){
				gameState = 2;
			}
		}
	//	shapeRenderer.end();
	}

	@Override
	public void dispose () {

	}
}