package com.sitri.equationgen;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tv_Equation;
    private TextView tv_CalcProcess;
    private TextView tv_CalcResult;
    private Chronometer cm_CounterDown;
    private Button bt_Gen;
    private Button bt_ShowResult;
    private String tv_Equation_Str;
    private String tv_CalcProcess_Str = new String("计算过程：\n");
    private boolean tv_hasCalcProcess_Str = false;
    private int a = 1;
    private int b = 1;
    private char ops = '+';
    private int result = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_Gen = (Button) findViewById(R.id.button_gen);
        bt_Gen.setOnClickListener(this::OnClick_Gen);

        bt_ShowResult = (Button) findViewById(R.id.button_result);
        bt_ShowResult.setOnClickListener(this::OnClick_ShowResult);
        bt_ShowResult.setEnabled(false);

        tv_Equation = (TextView) findViewById(R.id.textView_equation);
        tv_CalcProcess = (TextView) findViewById(R.id.textView_calc_process);
        tv_CalcResult = (TextView) findViewById(R.id.editTextNumber_result);
        tv_CalcResult.addTextChangedListener(et_Watcher);

        cm_CounterDown = (Chronometer) findViewById(R.id.chronometer);
        cm_CounterDown.setBase(SystemClock.elapsedRealtime());
        cm_CounterDown.setCountDown(false);
        cm_CounterDown.setOnChronometerTickListener(this::OnChronometerTick_CounterDown);
    }

    public void OnClick_Gen(View view){
        StringBuilder stringBuilder = new StringBuilder();
        Random rand = new Random();

        ops = ((rand.nextInt() % 2) == 0) ? '+' : '-';
        a = Math.abs(rand.nextInt() % 100);
        b = Math.abs(rand.nextInt() % 100);
        result = a+b;
        if (ops == '-') {
            while(a == 0)
            {
                a = Math.abs(rand.nextInt() % 100);
            }
            b = Math.abs(rand.nextInt() % a);
            result = a-b;
        }

        tv_Equation_Str = stringBuilder.append(String.format("%d ", a)).append(String.format("%c", ops)).append(String.format(" %d = ", b)).toString();
        tv_Equation.setText(tv_Equation_Str);
        tv_Equation.setBackgroundColor(Color.rgb(255, 255, 255));
        tv_CalcProcess_Str.replaceAll(".*","\0");
        StringBuilder temp = new StringBuilder();
        tv_CalcProcess_Str = temp.append(("计算过程：\n\n")).toString();
        tv_CalcProcess.setText("");
        tv_CalcProcess.setText(tv_CalcProcess_Str);
        tv_CalcResult.setText("");
        tv_hasCalcProcess_Str = false;
        cm_CounterDown.setBase(SystemClock.elapsedRealtime());
        cm_CounterDown.setCountDown(false);
        cm_CounterDown.start();
        cm_CounterDown.setBackgroundColor(Color.rgb(255,255,255));
        bt_ShowResult.setEnabled(false);
    }

    public void OnClick_ShowResult(View view) {
        StringBuilder stringBuilder = new StringBuilder();
        tv_CalcProcess_Str = stringBuilder.append(tv_CalcProcess_Str).toString();
        if (tv_hasCalcProcess_Str == false) {
            if (ops == '+') {
                if (((a % 10 == 0) && (b % 10 == 0))
                 || ((a % 10 == 0) && (b < 10))
                 || ((b % 10 == 0) && (a < 10))
                 || ((b < 10) && (a < 10)))
                {
                    tv_CalcProcess_Str = stringBuilder.append(String.format("\t(1)\t%d + %d = %d\n\n", a, b, result)).toString();
                }
                else
                {
                    tv_CalcProcess_Str = stringBuilder.append(String.format("\t(1)\t%d + %d = %d\n\n", a % 10, b % 10, (a % 10) + (b % 10))).toString();
                    if ((a < 10) && (b > 10))
                    {
                        tv_CalcProcess_Str = stringBuilder.append(String.format("\t(2)\t%d + %d = %d\n\n", (a % 10) + (b % 10), 10 * (b / 10), result)).toString();
                    }
                    else if ((a > 10) && (b < 10))
                    {
                        tv_CalcProcess_Str = stringBuilder.append(String.format("\t(2)\t%d + %d = %d\n\n", (a % 10) + (b % 10), 10 * (a / 10), result)).toString();
                    }
                    else {
                        tv_CalcProcess_Str = stringBuilder.append(String.format("\t(2)\t%d + %d = %d\n\n", 10 * (a / 10), 10 * (b / 10), 10*(a/10)+10*(b/10))).toString();
                        tv_CalcProcess_Str = stringBuilder.append(String.format("\t(3)\t%d + %d = %d\n\n", 10 * (a / 10) + 10 * (b / 10), (a % 10) + (b % 10), result)).toString();
                    }
                }
            } else {
                if (((a < 10) && (b < 10))
                 || ((a % 10 == 0) && (b % 10 == 0))
                 || (b % 10 == 0))
                {
                    tv_CalcProcess_Str = stringBuilder.append(String.format("\t(1)\t%d - %d = %d\n\n", a, b, result)).toString();
                }
                else
                {
                    if ((a%10) >= (b%10))
                    {
                        tv_CalcProcess_Str = stringBuilder.append(String.format("\t(1)\t%d - %d = (%d - %d)(%d - %d) = %d\n\n", a, b, a/10, b/10, a%10, b%10, result)).toString();
                    }
                    else {
                        tv_CalcProcess_Str = stringBuilder.append(String.format("\t(1)\t%d + %d = %d\n\n", b, 10 - (b % 10), b + (10 - b % 10))).toString();
                        tv_CalcProcess_Str = stringBuilder.append(String.format("\t(2)\t%d + %d = %d\n\n", a, 10 - (b % 10), a + 10 - (b % 10))).toString();
                        tv_CalcProcess_Str = stringBuilder.append(String.format("\t(3)\t%d - %d = %d\n\n", a + 10 - (b % 10), b + (10 - b % 10), result)).toString();
                    }
                }
            }
            tv_CalcProcess.setText(tv_CalcProcess_Str);
            bt_ShowResult.setEnabled(false);
            tv_hasCalcProcess_Str = true;
        }
    }

    public void OnChronometerTick_CounterDown(Chronometer cm){
        if ((SystemClock.elapsedRealtime() - cm.getBase()) > 20 * 1000)
        {
            if ((((SystemClock.elapsedRealtime() - cm.getBase())/1000) % 2) == 0) {
                cm.setBackgroundColor(Color.rgb(255, 0, 0));
            }
            else
            {
                cm.setBackgroundColor(Color.rgb(255,255,255));
            }
        }
        if (SystemClock.elapsedRealtime() - cm.getBase() > 30 * 1000)
        {
            bt_ShowResult.setEnabled(true);
        }

        if (SystemClock.elapsedRealtime() - cm.getBase() > 60 * 1000)
        {
            cm.stop();
        }
    }

    private TextWatcher et_Watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            View.OnKeyListener onKeyListener = new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                    {
                        if (s.toString().length() > 0)
                        {
                            if (Integer.parseInt(s.toString().trim()) == result) {
                                tv_Equation.setBackgroundColor(Color.rgb(0, 255, 0));
                                bt_ShowResult.setEnabled(true);
                                cm_CounterDown.stop();
                            } else {
                                tv_Equation.setBackgroundColor(Color.rgb(255, 0, 0));
                            }
                        }
                    }
                    return false;
                }
            };
            tv_CalcResult.setOnKeyListener(onKeyListener);
        }
    };


}

