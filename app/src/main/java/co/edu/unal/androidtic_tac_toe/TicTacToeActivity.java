package co.edu.unal.androidtic_tac_toe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TicTacToeActivity extends AppCompatActivity {
    // Represents the internal state of the game
    private TicTacToeGame mGame;
    // Buttons making up the board
    private Button mBoardButtons[];
    private Toast toast;
    private int selected;
    private boolean mEndgame=false;
    // Various text displayed
    private TextView mInfoTextView,pcScore,human,empate;


    private boolean mGameOver, humanStarts;
    private Integer humanSc=0,androidScore=0,empateScore=0;
    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                mEndgame=false;
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};
// TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
// selected is the radio button that should be selected.
                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss(); // Close dialog
// TODO: Set the diff level of mGame based on which item was selected.
// Display the selected difficulty level
                                switch (item){
                                    case 0:

                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                        break;

                                    case 1:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                        break;

                                    case 2:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                        break;
                                }

                                Toast.makeText(getApplicationContext(), levels[item], Toast.LENGTH_SHORT).show();

                            }
                        });
                dialog = builder.create();
                break;

            case DIALOG_QUIT_ID:
// Create the quit confirmation dialog
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                TicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mGame = new TicTacToeGame();
        startNewGame();
    }

    // Set up the game board.
    private void startNewGame() {
        mGame.clearBoard();
        mGameOver=false;
        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        // Human goes first
        this.mGameOver = false;
        if(humanStarts) {
            mInfoTextView.setText("you go first.");
            humanStarts=!humanStarts;
        }
        else {
            mInfoTextView.setText("android go first.");
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            humanStarts=!humanStarts;
        }
    } // End of startNewGame

    // Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location)
        {
            this.location = location;
        }
        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled() && mGameOver==false) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);
                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0 ) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_human);
                }else if (winner == 1) {
                    mInfoTextView.setText(R.string.result_tie);
                    mGameOver = true;
                    empateScore++;
                    empate.setText(empateScore.toString());
                    mEndgame=true;
                }else if (winner == 2){
                    mInfoTextView.setText(R.string.result_human_wins);
                    mGameOver = true;
                    humanSc++;
                    human.setText(humanSc.toString());
                    mEndgame=true;
                }else{
                    mInfoTextView.setText(R.string.result_computer_wins);
                    mGameOver = true;
                    androidScore++;
                    pcScore.setText(androidScore.toString());
                    mEndgame=true;
                }
            }
        }
    }

    private void setMove(char player, int location) {
        if (!mGameOver) {
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }

    }
}