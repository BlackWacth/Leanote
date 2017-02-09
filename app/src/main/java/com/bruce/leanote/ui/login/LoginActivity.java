package com.bruce.leanote.ui.login;

import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.bruce.leanote.R;
import com.bruce.leanote.ui.base.BaseActivity;
import com.bruce.leanote.ui.signIn.SignInActivity;
import com.bruce.leanote.ui.widgets.PasswordEditText;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private AppCompatAutoCompleteTextView mEmailEditText;
    private PasswordEditText mPasswordEditText;
    private AppCompatButton mLoginBtn;
    private TextView mSignInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.acet_login_email_content);
        mPasswordEditText = (PasswordEditText) findViewById(R.id.pet_login_pwd_content);
        mLoginBtn = (AppCompatButton) findViewById(R.id.acb_login_login_btn);
        mSignInTextView = (TextView) findViewById(R.id.tv_login_sign_in);

        mLoginBtn.setOnClickListener(this);
        mSignInTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acb_login_login_btn:

                break;

            case R.id.tv_login_sign_in:
                startActivity(SignInActivity.class);
                break;
        }
    }
}
