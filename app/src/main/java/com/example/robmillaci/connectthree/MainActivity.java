package com.example.robmillaci.connectthree;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean gameWon = false;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    boolean newGame = true;
    //initial game state = 2 meaning neither 0(red) or 1(yellow)
    String imageIdentifier = "";
    int gameCounter = 0;
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    ArrayList<ImageView> buttonArray = new ArrayList<>();
    int clickCount = 0;
    Button resetBtn;
    TextView winText;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        winText = findViewById(R.id.winSign);

        ImageView btn1 = findViewById(R.id.btn1);
        ImageView btn2 = findViewById(R.id.btn2);
        ImageView btn3 = findViewById(R.id.btn3);
        ImageView btn4 = findViewById(R.id.btn4);
        ImageView btn5 = findViewById(R.id.btn5);
        ImageView btn6 = findViewById(R.id.btn6);
        ImageView btn7 = findViewById(R.id.btn7);
        ImageView btn8 = findViewById(R.id.btn8);
        ImageView btn9 = findViewById(R.id.btn9);

        resetBtn = findViewById(R.id.reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newGame) {
                    resetGame();
                } else {
                    Toast.makeText(MainActivity.this,"You have already started a new game!",Toast.LENGTH_LONG).show();
                }
            }
        });
        buttonArray.add(btn1);
        buttonArray.add(btn2);
        buttonArray.add(btn3);
        buttonArray.add(btn4);
        buttonArray.add(btn5);
        buttonArray.add(btn6);
        buttonArray.add(btn7);
        buttonArray.add(btn8);
        buttonArray.add(btn9);
        gameStart();
    }

    public void resetGame() {
        gameWon = false;
        newGame = true;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }
        imageIdentifier = "";
        winText.animate().cancel();
        winText.clearAnimation();
        winText.setAnimation(null);
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        for (int i = 0; i < buttonArray.size(); i++) {
            buttonArray.get(i).startAnimation(slideDown);
        }
        winText.setTextColor(getResources().getColor(android.R.color.transparent));
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                for (int i = 0; i < buttonArray.size(); i++) {
                    buttonArray.get(i).setImageResource(android.R.color.transparent);
               }
           }
//        }, 500);
//    }

    public void gameStart() {
        winText.setTextColor(Color.WHITE);
        //winText.setTranslationY(-1000);
        final MediaPlayer click = MediaPlayer.create(MainActivity.this, R.raw.counter_click);
        final MediaPlayer win = MediaPlayer.create(MainActivity.this, R.raw.win_sound);

        for (final ImageView iv : buttonArray) {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gameWon) {
                        Toast.makeText(MainActivity.this, "Game has already been won! Press 'New game' to start again", Toast.LENGTH_LONG).show();
                    } else {
                        ImageView counter = (ImageView) v;
                        gameCounter++;
                        newGame=false;

                        if (gameState[Integer.parseInt(counter.getTag().toString())] == 2) {

                            counter.setTranslationY(-1000);
                            if (clickCount % 2 == 0) {
                                clickCount++;
                                counter.setImageResource(R.drawable.yellow);
                                imageIdentifier = "yellow";
                                counter.animate().translationYBy(-(counter.getTranslationY())).rotation(3600).setDuration(500);
                            } else {
                                counter.setImageResource(R.drawable.red);
                                imageIdentifier = "red";
                                clickCount++;
                                counter.animate().translationYBy(-(counter.getTranslationY())).rotation(3600).setDuration(500);
                            }

                            click.start();
                            int tappedCounter = Integer.parseInt(counter.getTag().toString());

                            if (imageIdentifier.equals("yellow")) {
                                gameState[tappedCounter] = 1;
                            } else gameState[tappedCounter] = 0;

                            for (int[] winningPosition : winningPositions) {

                                if ((gameState[winningPosition[0]] == gameState[winningPosition[1]]) && (gameState[winningPosition[1]] == gameState[winningPosition[2]])
                                        && (gameState[winningPosition[0]] != 2)) {
                                    final Animation rotation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate_text);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (imageIdentifier.equals("red")) {
                                                winText.setText("RED WINS!!");
                                                winText.setTextColor(Color.RED);
                                                winText.startAnimation(rotation);
                                                gameWon = true;
                                                gameCounter = 0;
                                            } else {
                                                winText.setText("YELLOW WINS!!");
                                                winText.setTextColor(getResources().getColor(R.color.DarkYellow));
                                                winText.startAnimation(rotation);
                                                gameWon = true;
                                                gameCounter = 0;
                                            }
                                            Animation rotate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
                                            win.start();
                                            for (int i = 0; i < buttonArray.size(); i++) {

                                                buttonArray.get(i).startAnimation(rotate);
                                            }
                                        }
                                    }, 400);
                                }


                            }
                        } else {
                            Toast.makeText(MainActivity.this, "A counter has already been placed here! Try again", Toast.LENGTH_LONG).show();
                        }
                    }

                    //draw
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (gameCounter == 9 && !gameWon) {
                                //must be a draw
                                final Animation rotation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate_text);
                                MediaPlayer drawSound = MediaPlayer.create(MainActivity.this, R.raw.draw_sound);
                                winText.setText("DRAW!!");
                                winText.setTextColor(Color.BLUE);
                                winText.setAnimation(rotation);
                                drawSound.start();
                            }
                        }
                    }, 400);
                }
            });
        }
    }
}

