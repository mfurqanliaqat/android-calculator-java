package com.f18.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView screen;
    TextView runningTotal;
    boolean clearResult, checkDecimal;
    int charCount = 0;
    String except = "";
    SoundPool soundPool;
    int soundCommon, soundEqual, soundClear, soundOperator, sound;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screen = findViewById(R.id.tv_screen);
        runningTotal = findViewById(R.id.tv_rtotal);

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundCommon = soundPool.load(MainActivity.this, R.raw.btn_common, 1);
        soundEqual = soundPool.load(MainActivity.this, R.raw.btn_equal, 1);
        soundClear = soundPool.load(MainActivity.this, R.raw.btn_ce, 1);
        soundOperator = soundPool.load(MainActivity.this, R.raw.btn_ce, 1);

        ImageButton btnSettings = findViewById(R.id.btnSetting);
        ImageButton btnRotate = findViewById(R.id.btnOrientation);

        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (btnSettings != null) {
            btnSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (btnRotate != null) {
            btnRotate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    else
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("screen", screen.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        screen.setText(savedInstanceState.getString("screen"));
    }

    public void onOperandClick(View v){
        String currentText = screen.getText().toString();
        int len = currentText.length();

        if (prefs.getBoolean("sound", false)) soundPool.play(soundCommon, 1, 1, 0, 0, 1);

        Character character = ((Button) v).getText().charAt(0);
        if ((isOperand(character) && clearResult)) screen.setText("");

        if (this.isOperator(character)) {
            checkDecimal = false;
            if (len == 0) {
                this.updateScreen(character.toString());
                return;
            }else if (isOperator(currentText.charAt(len - 1))) {
                screen.setText(currentText.subSequence(0, len - 1) + character.toString());
                return;
            }
        }

        clearResult = false;
        updateScreen(character.toString());
        updateRunningTotal();
    }

    public void onEqualClick(View v) {
        if (prefs.getBoolean("sound", false))  soundPool.play(soundEqual, 1, 1, 0, 0, 1);
        screen.setText(fmt(solveForResult(screen.getText().toString())));
        clearResult = true;
        checkDecimal = false;
        charCount = 0;
        adjustScreenSize();
        updateRunningTotal();
    }

    public void onClearAll(View v) {
        if (prefs.getBoolean("sound", false)) soundPool.play(soundClear, 1, 1, 0, 0, 1);
        screen.setText("");
        runningTotal.setText("");
        clearResult = checkDecimal = false;
        charCount = 0;
        adjustScreenSize();
    }

    public void onDelete(View v) {
        if (prefs.getBoolean("sound", false)) soundPool.play(soundClear, 1, 1, 0, 0, 1);
        String currentText = screen.getText().toString();
        int len = currentText.length();
        int f = 1;
        f = endsWithAny(new String[]{"³√", "ʸ√", "^2", "^3"}, currentText) ? 2 : 1;
        if (f == 1) f = endsWithAny(new String[]{"\uD835\uDC52^", "ln(", "10^", "^-1"}, currentText) ? 3 : 1;
        if (f == 1) f = endsWithAny(new String[]{"cos(", "sin(", "tan(", "log(", "deg(", "rad("}, currentText) ? 4 : 1;
        if (f == 1) f = endsWithAny(new String[]{"cosh(", "sinh(", "tanh("}, currentText) ? 5 : 1;
        if (f == 1) f = endsWithAny(new String[]{"cos⁻¹(", "sin⁻¹(", "tan⁻¹("}, currentText) ? 6 : 1;

        if (len > 0) {
            screen.setText(currentText.subSequence(0, len - f));
            this.updateRunningTotal();
        } else clearResult = false;
    }

    public void onSpecialOperatorClick(View v) {
        if (prefs.getBoolean("sound", false)) soundPool.play(soundOperator, 1, 1, 0, 0, 1);

        String operator = (String) ((Button) v).getText();
        String currentText = screen.getText().toString();
        clearResult = checkDecimal = false;

        if (operator.contains("³√")) updateScreen("³√");
        else if (operator.contains("ʸ√")) updateScreen("ʸ√");
        else if (operator.contains("√")) updateScreen("√");
        else if (operator.contains("π")) updateScreen("π");
        else if (operator.contains("!")) updateScreen("!");
        else if (operator.contains("\uD835\uDC65²")) updateScreen("^2");
        else if (operator.contains("\uD835\uDC65³")) updateScreen("^3");
        else if (operator.contains("10ˣ")) updateScreen("10^");
        else if (operator.contains("\uD835\uDC52ˣ")) updateScreen("\uD835\uDC52^");
        else if (operator.contains("\uD835\uDC65ʸ")) updateScreen("^");
        else if (operator.equals("1/\uD835\uDC65")) updateScreen("^-1");
        else if (operator.equals("Rand")) updateScreen(String.valueOf(999999999 * new Random().nextDouble()));
        else if (operator.equals("R0")) screen.setText(String.valueOf(Math.round(solveForResult(currentText))));
        else if (operator.equals("R2")) screen.setText(String.format("%.2f", solveForResult(currentText)));


        // update running total, for some operators
        if (operator.matches(".*[−+×÷π^!].*")) updateRunningTotal();
        if (operator.equals("R0") || operator.equals("R1")) runningTotal.setText("");
    }

    protected double solveForResult(String exp) {
        int len = exp.length();
        if (len <= 0) return 0;
        if (isOperator(exp.charAt(len - 1))) exp = exp.substring(0, len - 1);

        //prepare expression for solving
        exp = exp.replace("sin⁻¹", "asin")
                .replace("cos⁻¹", "cos")
                .replace("tan⁻¹", "atan");

        try { return FCal.evaluate(exp); }
        catch (Exception ex) { this.except = "Syntax Error"; }

        return 0;
    }

    protected void updateScreen(String value) {
        String currentText = screen.getText().toString();

        if (currentText.equals("0") && value.equals("0")) return;
        else if (currentText.equals("0") && isOperand(value.charAt(0)) && !value.equals(".")) currentText = "";
        else if (currentText.endsWith(".") && value.equals(".")) return;


        if (value.equals(".") && checkDecimal) return;

        if (Character.isDigit(value.charAt(0))) charCount++;
        else charCount = 0;

        if (charCount >= 15) {
            Toast.makeText(MainActivity.this, "Reached the maximum number of digits (15)", Toast.LENGTH_SHORT).show();
            return;
        }

        /*if (currentText.lastIndexOf(",") == currentText.length() - 4)
            currentText += ",";*/

        screen.setText(currentText + value);
        adjustScreenSize();

        if (value.equals(".")) checkDecimal = true;
    }

    protected void adjustScreenSize () {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (screen.getText().length() >= 11)
                screen.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 34);
            else screen.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 60);
        }
    }
    protected void updateRunningTotal () {
        String currentText = screen.getText().toString();
        int len = screen.getText().length();

        if (len <=0 || clearResult) runningTotal.setText("");
        else if (currentText.matches(".*[−+×÷π^!].*")) runningTotal.setText(fmt(solveForResult(screen.getText().toString())));

        if (!except.equals("")) runningTotal.setText(except);
        except = "";
    }

    private boolean endsWithAny(String[] suffixes, String str) {
        for (String suffix: suffixes) if (str.endsWith((suffix))) return true;
        return false;
    }

    private boolean isOperator(Character value) { return (value.toString().matches(".*[−+×÷].*")); }

    private boolean isOperand(Character value) { return Character.isDigit(value) || value == '.'; }

    private String fmt(double d) { return (d == (long) d) ? String.format("%d",(long)d) : String.format("%s",d); }

    public void onTrigClick (View v) {
        clearResult = checkDecimal = false;
        if (prefs.getBoolean("sound", false)) soundPool.play(soundOperator, 1, 1, 0, 0, 1);
        updateScreen(((Button) v).getText() + "(");
    }
}