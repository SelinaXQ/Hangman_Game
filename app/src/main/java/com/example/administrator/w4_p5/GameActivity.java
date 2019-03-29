package com.example.administrator.w4_p5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends Activity {

    private ImageView[] bodyParts;//body part images
    private int numParts = 6;//number of body parts
    private int currPart;//increase value when answer is wrong
    private int numChars;//number of character of current number
    private int numCorr;//number of correctly guessed character

    private String[] words;
    private String currWord;//current game round word
    private String hint;
    private LinearLayout wordLayout;
    private TextView[] charViews;//view for each letter
    private GridView letters;//letter button grid
    private Letter ltrAdapter;
    private Random rand;
    private TextView tvHint;//hint text

    private ArrayList<Integer> clickedLetters;//record letters clicked for onSave & onRestore

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //initialize body part img
        bodyParts = new ImageView[numParts];
        bodyParts[0] = (ImageView) findViewById(R.id.head);
        bodyParts[1] = (ImageView) findViewById(R.id.body);
        bodyParts[2] = (ImageView) findViewById(R.id.arm1);
        bodyParts[3] = (ImageView) findViewById(R.id.arm2);
        bodyParts[4] = (ImageView) findViewById(R.id.leg1);
        bodyParts[5] = (ImageView) findViewById(R.id.leg2);

        Resources res = getResources();//get words in
        words = res.getStringArray(R.array.words);

        rand = new Random();
        currWord = "";

        wordLayout = (LinearLayout) findViewById(R.id.word);//answer part
        letters = (GridView) findViewById(R.id.letters);//letter button

        tvHint = (TextView) findViewById(R.id.tvHint);
        playGame();
    }

    private void playGame() {
        //initialize
        clickedLetters = new ArrayList<>();

        //initialize words
        String newWord = words[rand.nextInt(words.length)];

        while (newWord.equals(currWord)) {//make sure not the same word as the previous one
            newWord = words[rand.nextInt(words.length)];
        }
        String[] splitWord = newWord.split(",");
        currWord = splitWord[0];
        hint = splitWord[1];
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //Toast.makeText(getBaseContext(), "This is portrait!", Toast.LENGTH_SHORT).show();
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//get orientation
            tvHint.setText("Hint:" + hint);// if landscape, show hint
            //Toast.makeText(getBaseContext(), "This is landscape!", Toast.LENGTH_SHORT).show();
        }

        charViews = new TextView[currWord.length()];

        wordLayout.removeAllViews();

        for (int c = 0; c < currWord.length(); c++) {
            charViews[c] = new TextView(this);
            charViews[c].setText("" + currWord.charAt(c));
            charViews[c].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.WHITE);
            charViews[c].setBackgroundResource(R.drawable.letter_bg);
            wordLayout.addView(charViews[c]);
        }

        ltrAdapter = new Letter(this, clickedLetters);
        letters.setAdapter(ltrAdapter);

        //initialize body
        currPart = 0;
        numChars = currWord.length();
        numCorr = 0;

        for (int p = 0; p < numParts; p++) {//all body parts invisible when game first start
            bodyParts[p].setVisibility(View.INVISIBLE);
        }
    }

    public void letterClick(View view) {
        //record clicked letter
        clickedLetters.add((Integer) view.getTag());

        String ltr = ((TextView) view).getText().toString();
        char letterChar = ltr.charAt(0);
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_down);

        boolean correct = false;
        for (int k = 0; k < currWord.length(); k++) {
            if (currWord.charAt(k) == letterChar) {
                correct = true;
                numCorr++;
                charViews[k].setTextColor(Color.BLACK);
            }
        }
        if (correct) {//case correct
            if (numCorr == numChars) {
                // disable all the buttons
                int numLetters = letters.getChildCount();
                for (int l = 0; l < numLetters; l++) {
                    letters.getChildAt(l).setEnabled(false);
                }
                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle("WOW");
                winBuild.setMessage("You win!");
                winBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.playGame();
                            }
                        });
                winBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }
                        });
                winBuild.show();
            }
        } else if (currPart < numParts) {
            bodyParts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        } else {//case incorrect
            int numLetters = letters.getChildCount();
            for (int l = 0; l < numLetters; l++) {
                letters.getChildAt(l).setEnabled(false);
            }
            AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
            loseBuild.setTitle("BAD LUCK");
            loseBuild.setMessage("You lose!");
            loseBuild.setPositiveButton("Play Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameActivity.this.playGame();
                        }
                    });
            loseBuild.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameActivity.this.finish();
                        }
                    });
            loseBuild.show();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //save body info
        outState.putInt("currPart", currPart);
        outState.putInt("numChars", numChars);
        outState.putInt("numCorr", numCorr);
        int[] bodyPartVisibility = new int[numParts];
        for (int i = 0; i < bodyPartVisibility.length; i++) {
            bodyPartVisibility[i] = bodyParts[i].getVisibility();
        }
        outState.putIntArray("bodyPartVisibility", bodyPartVisibility);
        outState.putIntegerArrayList("clickedLetters", clickedLetters);

        //saved words
        outState.putString("currWord", currWord);
        outState.putString("hint", hint);
        //charView color
        int[] charViewColor = new int[currWord.length()];
        for (int i = 0; i < charViewColor.length; i++) {
            charViewColor[i] = charViews[i].getCurrentTextColor();
        }
        outState.putIntArray("charViewColor", charViewColor);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currPart = savedInstanceState.getInt("currPart");
        numChars = savedInstanceState.getInt("numChars");
        numCorr = savedInstanceState.getInt("numCorr");
        clickedLetters = savedInstanceState.getIntegerArrayList("clickedLetters");
        int[] savedBodyPartVisibility = savedInstanceState.getIntArray("bodyPartVisibility");
        for (int i = 0; i < savedBodyPartVisibility.length; i++) {
            bodyParts[i].setVisibility(savedBodyPartVisibility[i]);
        }
        //saved word
        currWord = savedInstanceState.getString("currWord");
        hint = savedInstanceState.getString("hint");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//get orientation
            tvHint.setText("Hint:" + hint);// if landscape, show hint
            //Toast.makeText(getBaseContext(), "This is landscape!", Toast.LENGTH_SHORT).show();
        }
        charViews = new TextView[currWord.length()];

        wordLayout.removeAllViews();

        for (int c = 0; c < currWord.length(); c++) {
            charViews[c] = new TextView(this);
            charViews[c].setText("" + currWord.charAt(c));

            charViews[c].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.WHITE);
            charViews[c].setBackgroundResource(R.drawable.letter_bg);
            wordLayout.addView(charViews[c]);

        }
        //saved charView
        int[] savedCharViewColor = savedInstanceState.getIntArray("charViewColor");
        for (int i = 0; i < savedCharViewColor.length; i++) {
            charViews[i].setTextColor(savedCharViewColor[i]);
        }

        //saved button letter
        //need to new Adapter and setAdapter to getView, else would cause an error
        ltrAdapter = new Letter(this, clickedLetters);
        letters.setAdapter(ltrAdapter);
        ltrAdapter.notifyDataSetChanged();
    }
}
