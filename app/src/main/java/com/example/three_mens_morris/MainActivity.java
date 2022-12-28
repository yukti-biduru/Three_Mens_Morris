package com.example.three_mens_morris;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    Button button_left_top;
    Button button_left_mid;
    Button button_left_down;
    Button button_mid_top;
    Button button_mid_mid;
    Button button_mid_down;

    Button button_right_top;
    Button button_right_mid;
    Button button_right_down;

    Button player1;
    Button player2;
    Button start;

    //update the zeroes when players go to that position
    HashMap<Integer,Integer> board_status = new HashMap<>(), reset_board_status = new HashMap<>();
    int[] player1_pieces;
    int[] player2_pieces;

    static int player_number = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_left_top = findViewById(R.id.left_top);
        button_left_mid = findViewById(R.id.left_mid);
        button_left_down = findViewById(R.id.left_down);

        button_mid_top = findViewById(R.id.mid_top);
        button_mid_mid = findViewById(R.id.mid_mid);
        button_mid_down = findViewById(R.id.mid_down);

        button_right_top = findViewById(R.id.right_top);
        button_right_mid = findViewById(R.id.right_mid);
        button_right_down = findViewById(R.id.right_down);

        player1 = findViewById(R.id.player_1);
        player2 = findViewById(R.id.player_2);
        start = findViewById(R.id.start);


        board_status.put(R.id.left_top,0);
        board_status.put(R.id.left_mid,0);
        board_status.put(R.id.left_down,0);
        board_status.put(R.id.mid_top,0);
        board_status.put(R.id.mid_mid,0);
        board_status.put(R.id.mid_down,0);
        board_status.put(R.id.right_top,0);
        board_status.put(R.id.right_mid,0);
        board_status.put(R.id.right_down,0);

        reset_board_status = board_status;

        View.OnClickListener listener = v -> {
            int selection = board_status.get(v.getId());
            if(selection == 0)
            {
                if(player_number == 1)
                {
                    v.setBackgroundColor(Color.parseColor("#0474BA"));
                    board_status.replace(v.getId(),1);
                }
                else
                {
                    v.setBackgroundColor(Color.parseColor("#F17720"));
                    board_status.replace(v.getId(),2);
                }
            }
            else
            {
                v.setBackgroundColor(Color.MAGENTA);
            }

        };

        // if start button is pressed - reset
        View.OnClickListener start_listener = v -> board_status = reset_board_status;

        button_left_top.setOnClickListener(listener);
        button_left_mid.setOnClickListener(listener);
        button_left_down.setOnClickListener(listener);
        button_mid_top.setOnClickListener(listener);
        button_mid_mid.setOnClickListener(listener);
        button_mid_down.setOnClickListener(listener);
        button_right_top.setOnClickListener(listener);
        button_right_mid.setOnClickListener(listener);
        button_right_down.setOnClickListener(listener);

        start.setOnClickListener(start_listener);
        player1_pieces = new int[3];
        player2_pieces = new int[3];

        WorkerThread player1_worker_thread = new WorkerThread("player1_worker_thread", player1_pieces,board_status);
        WorkerThread player2_worker_thread = new WorkerThread("player2_worker_thread",player2_pieces, board_status);

        Thread ui_thread = new Thread(new Runnable() {
            @Override
            public void run() {

                boolean win = false;
                while (win == false) {
                    //alternate between players
                    player1_worker_thread.start();
                    player2_worker_thread.start();

                    //change button colours and add buttons to players arrays
                    if (player_number == 1) {
                        player1.setBackgroundColor(Color.parseColor("#0474BA"));
                    } else {
                        player2.setBackgroundColor(Color.parseColor("#F17720"));
                    }
                    player1_pieces = player1_worker_thread.getPieces();
                    player2_pieces = player2_worker_thread.getPieces();
                    //check if anyone won
                    String data = winCheck(player1_pieces, player2_pieces);
                    if (data != null) {
                        win = true;
                        Toast.makeText(getApplicationContext(), String.format("{0} WON", data), Toast.LENGTH_SHORT);
                        Toast.makeText(getApplicationContext(), "GAME OVER", Toast.LENGTH_LONG);
                        //stop threads
                        player1_worker_thread.stop();
                        player2_worker_thread.stop();
                    }
                }
            }
        });


    }

    //check if one of the players win
    String winCheck(int[] arr1, int[] arr2)
    {
        int[][] win_combinations = {{1,2,3},{1,4,7},{4,5,6},{7,8,9},{2,5,8},{3,6,9}};
        if (Arrays.asList(win_combinations).contains(arr1))
        {
            return "Player 1";
        }
        else if (Arrays.asList(win_combinations).contains(arr2))
        {
            return "Player 2";
        }
        else
        {
            return null;
        }
    }

}

