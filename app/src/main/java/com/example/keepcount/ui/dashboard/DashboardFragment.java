package com.example.keepcount.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.keepcount.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    public Boolean stared = true;
    public Boolean clickable = true;
    public TextView high_score;
    public TextView your_score;
    public TextView textTime;
    public Button buttonpush;
    public CountDownTimer countDown;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        high_score = root.findViewById(R.id.high_score);
        your_score = root.findViewById(R.id.your_score);
        textTime = root.findViewById(R.id.textTime);
        buttonpush = root.findViewById(R.id.button_push);

        final Button button = root.findViewById(R.id.button_push);
        button.setOnClickListener(new View.OnClickListener() {
            final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            int defaultHigh = getResources().getInteger(R.integer.high_score);
            int highScore = sharedPref.getInt(getString(R.string.textHighScore), defaultHigh);

            int defaultYours = getResources().getInteger(R.integer.your_score);
            int yourScore = sharedPref.getInt(getString(R.string.textYourScore), defaultYours);

            @Override
            public void onClick(View V) {
                if(clickable){
                    if(stared){

                        stared = false;
                        countDown = new CountDownTimer(15000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                if(millisUntilFinished < 10000){
                                    textTime.setText("00:00:0"+ millisUntilFinished / 1000);
                                    textTime.setTextColor(getResources().getColor(R.color.myred, null));
                                }
                                else
                                    textTime.setText("00:00:"+ millisUntilFinished / 1000);
                            }
                            public void onFinish() {
                                textTime.setText("00:00:00");
                                your_score.setTextSize(40);
                                your_score.setText("FINAL SCORE: " + yourScore);
                                yourScore = 0;
                                clickable = false;
                                buttonpush.setBackground(getResources().getDrawable(R.drawable.red_img, null));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        your_score.setText("Click the button to start!");
                                        clickable = true;
                                        stared = true;

                                        textTime.setTextColor(getResources().getColor(R.color.myblack, null));
                                        buttonpush.setBackground(getResources().getDrawable(R.drawable.green_img, null));

                                    }
                                }, 2500); // Millisecond 1000 = 1 sec
                            }
                        };
                        countDown.start();
                    }

                    yourScore = yourScore + 1;

                    your_score.setText("Your Score: " + (yourScore));
                    your_score.setTextSize(18);

                    if (yourScore > highScore){
                        highScore = yourScore;
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(getString(R.string.textHighScore), highScore);
                        editor.commit();
                    }
                    high_score.setText("High Score: " + (highScore));
//                    highScore = 0;
                }
            }
        });

        return root;
    }
}