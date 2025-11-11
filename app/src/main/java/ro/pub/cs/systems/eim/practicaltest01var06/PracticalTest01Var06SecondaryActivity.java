package ro.pub.cs.systems.eim.practicaltest01var06;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class PracticalTest01Var06SecondaryActivity extends AppCompatActivity {

    private Button okButton;
    private TextView text;

    private int nrBoxes = 0;
    private int gained = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test01_var06_secondary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        text = findViewById(R.id.resultTextView);
        okButton = findViewById(R.id.okButton);


        Intent intent = getIntent();

        String nr1 = null;
        String nr2 = null;
        String nr3 = null;
        ArrayList<Integer> nrs = new ArrayList<>();


        if (intent != null) {
            if (intent.getExtras().containsKey("number1")) {
                nr1 = intent.getStringExtra("number1");
                if (nr1 != null && !nr1.equals("*")) {
                    nrs.add(Integer.parseInt(nr1));
                }
            }
            if (intent.getExtras().containsKey("number2")) {
                nr2 = intent.getStringExtra("number2");
                if (nr2 != null && !nr2.equals("*")) {
                    nrs.add(Integer.parseInt(nr2));
                }
            }
            if (intent.getExtras().containsKey("number3")) {
                nr3 = intent.getStringExtra("number3");
                if (nr3 != null && !nr3.equals("*")) {
                    nrs.add(Integer.parseInt(nr3));
                }
            }
            if (intent.getExtras().containsKey("boxes")) {
                nrBoxes = (intent.getIntExtra("boxes", -1));
            }
        }

        if (nrBoxes == 0) {
            gained = 100;
        } else if (nrBoxes == 1) {
            gained = 50;
        } else if (nrBoxes == 2) {
            gained = 10;
        }

        boolean ok = true;
        if (nrs.size() > 0) {
            Integer el = nrs.get(0);
            for (int e : nrs) {
                if (e != el) {
                    ok = false;
                }
            }
        }

        if (ok == true) {
            text.setText("Gained: " + gained);
        }


        okButton.setOnClickListener(view -> {
            Intent intent1 = new Intent();
            intent1.putExtra("result", String.valueOf(gained));
            setResult(RESULT_OK, intent1);
            finish();
        });







    }
}