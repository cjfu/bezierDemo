package com.cjf.bezierdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cjf.bezierdemo.View.BezierView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_clean;
    Button btn_one_point;
    Button btn_two_point;
    BezierView v_bezier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v_bezier = findViewById(R.id.v_bezier);
        btn_clean = findViewById(R.id.btn_clean);
        btn_one_point = findViewById(R.id.btn_one_point);
        btn_two_point = findViewById(R.id.btn_two_point);
        btn_clean.setOnClickListener(this);
        btn_one_point.setOnClickListener(this);
        btn_two_point.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_clean:
                v_bezier.clean();
                break;
            case R.id.btn_one_point:
                v_bezier.setOnePoint(true);
                break;
            case R.id.btn_two_point:
                v_bezier.setOnePoint(false);
                break;
        }
    }
}
