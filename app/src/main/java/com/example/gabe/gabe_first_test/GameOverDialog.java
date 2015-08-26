package com.example.gabe.gabe_first_test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Gabe on 1/28/2015.
 */
public class GameOverDialog extends DialogFragment {

    EditText text;
    String HSFILENAME;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        HSFILENAME = activity.getFilesDir() + "/highscores_file.txt";
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //LayoutInflater inflater = LayoutInflater.from(BirdActivity.this);
        View view = inflater.inflate(R.layout.gameover, null);
        builder.setView(view);

        int highscore = getArguments().getInt("highscore");
        boolean newRecord = getArguments().getBoolean("newRecord");
        //TextView highscoretext = (TextView) view.findViewById(R.id.highscore_text);
        LinearLayout rootLayout = (LinearLayout) view.findViewById(R.id.baselinear);
        text = new EditText(getActivity());
        TextView textView = new TextView(getActivity());
        text.setId(12345);

        if (newRecord) {
            textView.setText("New highscore! Enter your name, then hit Quit or Play Again:");
            rootLayout.addView(textView);
            rootLayout.addView(text);
        } else {
            try {
                FileReader fileIn = new FileReader(HSFILENAME);
                BufferedReader buffIn = new BufferedReader(fileIn);
                String str = buffIn.readLine();
                str = buffIn.readLine();
                textView.setText(str);
                rootLayout.addView(textView);
            } catch (IOException e) { }
        }
        //highscoretext.setText(String.format("Highscore: %d", highscore));
        builder.setTitle("Game Over");
        builder.setMessage("Game over!")
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(GameOverDialog.this);
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(GameOverDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    static GameOverDialog newInstance(int highscore, boolean newRecord) {
        GameOverDialog gameOver = new GameOverDialog();
        Bundle args = new Bundle();
        args.putInt("highscore", highscore);
        args.putBoolean("newRecord", newRecord);
        gameOver.setArguments(args);

        return gameOver;
    }

    public String getName() { return text.getText().toString(); }

}
