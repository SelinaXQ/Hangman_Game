package com.example.administrator.w4_p5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

public class Letter extends BaseAdapter {

    private String[] letters;
    private LayoutInflater letterInf;
    private List<Integer> clickedLetters;//clicked letters

    //    //new add
//    private ArrayList<String> selectedLetters = new ArrayList<>();
//    //new add
    public Letter(Context c, List<Integer> clickedLetters) {
        letters = new String[26];
        for (int a = 0; a < letters.length; a++) {
            letters[a] = "" + (char) (a + 'A');
        }
        letterInf = LayoutInflater.from(c);
        this.clickedLetters = clickedLetters;
    }

    //    //new add
//    ArrayList<String> getState(){
//        return selectedLetters;
//    }
//    void restoreStore(ArrayList<String> selectedLetters){
//        this.selectedLetters = selectedLetters;
//        notifyDataSetInvalidated();
//    }
//    //new add
    @Override
    public int getCount() {
        return letters.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Button btnLetter;
        if (convertView == null) {
            btnLetter = (Button) letterInf.inflate(R.layout.letter, parent, false);
        } else {
            btnLetter = (Button) convertView;
        }
        //new add
//        btnLetter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectedLetters.add(letters[position]);
//            }
//        });
//        if(selectedLetters.contains(letters[position])){
//            btnLetter.setEnabled(true);
//        }else{
//            btnLetter.setEnabled(false);
//        }
//        //new add


        btnLetter.setTag(position);
        btnLetter.setText(letters[position]);

        //if clicked set state
        if (clickedLetters.contains(position)) {
            btnLetter.setEnabled(false);
            btnLetter.setBackgroundResource(R.drawable.letter_down);
        }

        return btnLetter;
    }
}
