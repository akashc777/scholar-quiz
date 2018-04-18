package org.sairaa.scholarquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG_LOGIN = "LoginActivity";
    private SharedPreferenceConfig sharedPreferenceConfig;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private String username, pass;
    private TextView register;
    private EditText email, password;
    private Button signIn;
    private AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // check wheather the user already logged in or not

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        Log.i(LOG_LOGIN, "" + sharedPreferenceConfig.readLoginStatus());
        if (sharedPreferenceConfig.readLoginStatus()) {
           startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            this.finish();
        }

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        //Use this checkBox ID
        saveLoginCheckBox = findViewById(R.id.rememberMe_CheckBox);
        signIn = findViewById(R.id.signin_login);
        signIn.setOnClickListener(this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            email.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.signin_login:
                if (email.getText().toString().equals("")
                        || password.getText().equals("")) {
                    alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertBuilder.setTitle("User Datails");
                    alertBuilder.setMessage("Please Fill all required field");
                    alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                } else {
                    BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(LoginActivity.this);
                    backgroundLoginTask.execute("login", email.getText().toString(), password.getText().toString());
                }


                break;
            default:
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(email.getWindowToken(), 0);

        username = email.getText().toString();
        pass = password.getText().toString();

        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", pass);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }


    }




}


