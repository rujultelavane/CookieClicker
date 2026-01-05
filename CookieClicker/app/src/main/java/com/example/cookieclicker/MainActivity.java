package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageView cookieView, newImageView, doublePoints, autoClick;
    ConstraintLayout constraintLayout;
    TextView textView, scoreView, doublePointsText, autoText;
    int score=0;
    int points=1;
    int price=20;
    int priceAuto = 100;
    MediaPlayer soundEffectPlayer, blueprintPlayer, chachingPlayer;
    CountDownTimer passiveIncomeTimer;
    boolean bought=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cookieView = findViewById(R.id.imageView);
        constraintLayout=findViewById(R.id.layout);
        scoreView = findViewById(R.id.textView);
        doublePoints = findViewById(R.id.imageView2);
        doublePointsText = findViewById(R.id.textView2);
        doublePointsText.setText("Double Points: "+price);
        autoClick = findViewById(R.id.imageView3);
        autoText = findViewById(R.id.textView3);

        soundEffectPlayer = MediaPlayer.create(this, R.raw.backdoor);
        blueprintPlayer = MediaPlayer.create(this, R.raw.blueprint_inst);
        blueprintPlayer.setLooping(true);
        blueprintPlayer.start();
        chachingPlayer = MediaPlayer.create(this, R.raw.chaching);

        final ScaleAnimation animation = new ScaleAnimation(1f, 0.9f, 1f, 0.9f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);

        AnimationDrawable animationDrawable = (AnimationDrawable)constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        cookieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cookieView.startAnimation(animation);
                makeImage();
                makeText();
                score+=points;
                scoreView.setText(score + " cookies");
                //soundEffectPlayer.start();
            }
        });

        doublePoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score>=price)
                {
                    doublePoints.startAnimation(animation);
                    score-=price;
                    price*=price;
                    chachingPlayer.start();
                    points*=2;
                    scoreView.setText(score + " cookies");
                    doublePointsText.setText("Double Points: "+price);
                }
                else Toast.makeText(getApplicationContext(), "you need more cookies!", Toast.LENGTH_SHORT).show();
            }
        });

        autoClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score>=priceAuto && !bought) {
                    bought=true;
                    chachingPlayer.start();
                    score-=priceAuto;
                    autoClick.startAnimation(animation);
                    //priceAuto*=2;
                    scoreView.setText(score + " cookies");
                    autoText.setText("Autoclick: "+priceAuto);
                    passiveIncome();
                }
                else if (bought)
                    Toast.makeText(getApplicationContext(), "you already have this upgrade!", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(), "you need more cookies!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void makeText()
    {
        textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setText("+"+points);
        textView.setTextColor(Color.RED);

        ConstraintLayout.LayoutParams textViewParams =
                new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textViewParams);

        constraintLayout.addView(textView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(textView.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP); //connect top of textview to top of constraint root layout
        constraintSet.connect(textView.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(textView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
        constraintSet.connect(textView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

        constraintSet.applyTo(constraintLayout);

        ObjectAnimator flyAnimation = ObjectAnimator.ofFloat(textView, "translationY", -2000f);
        flyAnimation.setDuration(5000);
        flyAnimation.start();


    }
    void makeImage()
    {
        newImageView = new ImageView(this);
        newImageView.setId(View.generateViewId());
        newImageView.setImageResource(R.drawable.newernachimbong);
        newImageView.setScaleX(0.15f);
        newImageView.setScaleY(0.15f);

        ConstraintLayout.LayoutParams imageViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        newImageView.setLayoutParams(imageViewParams);

        constraintLayout.addView(newImageView);

        ConstraintSet newconstraintSet = new ConstraintSet();
        newconstraintSet.clone(constraintLayout);

        newconstraintSet.connect(newImageView.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
        newconstraintSet.connect(newImageView.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
        newconstraintSet.connect(newImageView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
        newconstraintSet.connect(newImageView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

        newconstraintSet.applyTo(constraintLayout);

        ObjectAnimator flyAnimation = ObjectAnimator.ofFloat(newImageView, "translationY", -2005f);
        flyAnimation.setDuration(5000);
        flyAnimation.start();
    }
    public void passiveIncome()
    {
        passiveIncomeTimer = new CountDownTimer(6000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished) {
                score++;
                makeImage();
                makeText();
                scoreView.setText(score + " cookies");
            }

            @Override
            public void onFinish() {
                start();
            }
        };
        passiveIncomeTimer.start();
    }
}