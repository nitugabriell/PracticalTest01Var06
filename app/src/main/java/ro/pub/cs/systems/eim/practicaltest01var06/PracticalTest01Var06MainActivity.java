package ro.pub.cs.systems.eim.practicaltest01var06;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest01Var06MainActivity extends AppCompatActivity {

    private EditText text1;
    private EditText text2;
    private EditText text3;
    private CheckBox box1;
    private CheckBox box2;
    private CheckBox box3;
    private Button playButton;
    private Button seeResultsButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    int totalScore = 0;
    private boolean serviceStarted = false;
    private final IntentFilter intentFilter = new IntentFilter();



    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("score");
            Log.d("[broadcast]", "Received broadcast: action=" + intent.getAction() + " data=" + data);
            Toast.makeText(PracticalTest01Var06MainActivity.this, "Broadcast: " + data, Toast.LENGTH_SHORT).show();
        }
    }

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(android.view.View view) {
            if (view.getId() == R.id.playButton) {
                // generate 3 random values from [1,2,3,*]
                String[] options = {"1", "2", "3", "*"};
                String value1 = options[(int)(Math.random() * options.length)];
                String value2 = options[(int)(Math.random() * options.length)];
                String value3 = options[(int)(Math.random() * options.length)];

                if (!box1.isChecked()) {
                    text1.setText(value1);
                }
                if (!box2.isChecked()) {
                    text2.setText(value2);
                }
                if (!box3.isChecked()) {
                    text3.setText(value3);
                }

                Toast.makeText(PracticalTest01Var06MainActivity.this, "Numbers generated: " + value1 + " " + value2 + " " + value3, Toast.LENGTH_LONG).show();



            } else if (view.getId() == R.id.nextActivityButton) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Var06SecondaryActivity.class);

                int nr = 0;

                if (box1.isChecked()) {
                    nr++;
                }
                if (box2.isChecked()) {
                    nr++;
                }
                if (box3.isChecked()) {
                    nr++;
                }

                intent.putExtra("number1", text1.getText().toString());
                intent.putExtra("number2", text2.getText().toString());
                intent.putExtra("number3", text3.getText().toString());
                intent.putExtra("boxes", nr);

                activityResultLauncher.launch(intent);
            }

//            if (totalScore > 10) {
//                Intent intent = new Intent(getApplicationContext(), PracticalTest01Var06Service.class);
//
//                intent.putExtra("score", totalScore);
//
//                if (!serviceStarted) {
//                    getApplicationContext().startService(intent);
//                    serviceStarted = true;
//                }
//            }



        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test01_var06_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        text1 = findViewById(R.id.editText1);
        text2 = findViewById(R.id.editText2);
        text3 = findViewById(R.id.editText3);

        box1 = findViewById(R.id.checkbox1);
        box2 = findViewById(R.id.checkbox2);
        box3 = findViewById(R.id.checkbox3);

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(buttonClickListener);

        seeResultsButton = findViewById(R.id.nextActivityButton);
        seeResultsButton.setOnClickListener(buttonClickListener);

//        Intent intent = new Intent(getApplicationContext(), PracticalTest01Var06Service.class);
//        intent.putExtra("score", totalScore);

        intentFilter.addAction("ACTION");


        activityResultLauncher = registerForActivityResult(
                new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String returnedText = data.getStringExtra("result");

                            if (returnedText != null) {
                                int gained = Integer.parseInt(returnedText);
                                totalScore += gained;
                                Toast.makeText(PracticalTest01Var06MainActivity.this, returnedText, Toast.LENGTH_SHORT).show();
                                Log.d("SCORE", "Score: " + totalScore);
                            }
                        }
                    }
                });

        Intent intent = new Intent(getApplicationContext(), PracticalTest01Var06Service.class);
        intent.setAction("ACTION");
        intent.putExtra("score", totalScore);

        if (!serviceStarted) {
            getApplicationContext().startService(intent);
            serviceStarted = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter);
        }
    }

    @Override
    protected void onPause() {
        // unregister the dynamic receiver once
        try {
            unregisterReceiver(messageBroadcastReceiver);
        } catch (IllegalArgumentException ignored) {
            // receiver was not registered
            Log.w("TAGGGG", "Receiver not registered during onPause");
        }
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score", totalScore);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Integer score = savedInstanceState.getInt("score");
        if (score != null) {
            totalScore = score;
        }

        Log.d("SCORE_RESTORED", "score: " + totalScore);
    }
}