package com.example.three_mens_morris;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.Arrays;
import java.util.HashMap;

public class WorkerThread extends HandlerThread {

    Handler handler;
    int[] pieces = new int[3];
    public WorkerThread(String name, int[] arr, HashMap<Integer,Integer> board_status) {
        super(name);
        pieces = arr;
    }
    protected void onLooperPrepared()
    {
        handler = new Handler(getLooper()){
          public void handleMessage(Message msg)
          {
              //code
              try
              {
                  wait(2);



              }
              catch(Exception ex)
              {

              }
          }
        };
    }

    public int[] getPieces() {
        Arrays.sort(pieces);
        return pieces;
    }
}
