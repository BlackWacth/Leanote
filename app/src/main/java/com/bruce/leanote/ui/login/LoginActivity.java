package com.bruce.leanote.ui.login;

import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bruce.leanote.R;
import com.bruce.leanote.ui.base.BaseActivity;
import com.bruce.leanote.ui.login.adapter.EmailAutoCompleteAdapter;
import com.bruce.leanote.ui.signIn.SignInActivity;
import com.bruce.leanote.ui.widgets.PasswordEditText;
import com.bruce.leanote.utils.RegexUtils;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private AppCompatAutoCompleteTextView mEmailEditText;
    private PasswordEditText mPasswordEditText;
    private AppCompatButton mLoginBtn;
    private TextView mSignInTextView;

    private List<String> mEmailList;

    private boolean isRightEmail = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.acet_login_email_content);
        mPasswordEditText = (PasswordEditText) findViewById(R.id.pet_login_pwd_content);
        mLoginBtn = (AppCompatButton) findViewById(R.id.acb_login_login_btn);
        mSignInTextView = (TextView) findViewById(R.id.tv_login_sign_in);

        mEmailEditText.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mSignInTextView.setOnClickListener(this);

        mEmailList = new ArrayList<>();
        mEmailList.add("huazhongwei@yeah.net");
        mEmailList.add("1078824402@qq.com");
        mEmailList.add("huaweiysh@gmail.com");
        mEmailList.add("1070273909@qq.com");
        mEmailEditText.setAdapter(new EmailAutoCompleteAdapter(this, R.layout.item_auto_complete_text_view, mEmailList));

        mEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String email = mEmailEditText.getText().toString();
                    if(TextUtils.isEmpty(email)) {
                        mEmailEditText.setError(getResources().getString(R.string.input_email_null));
                        isRightEmail = false;
                    } else {
                        if(!RegexUtils.isEmail(email)) {
                            mEmailEditText.setError(getResources().getString(R.string.input_email_error));
                            isRightEmail = false;
                        } else {
                            isRightEmail = true;
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acet_login_email_content:
                mEmailEditText.showDropDown();
                break;

            case R.id.acb_login_login_btn:
                login();
                break;

            case R.id.tv_login_sign_in:
                startActivity(SignInActivity.class);
                break;
        }
    }

    private void login() {
        if(mEmailEditText == null || mPasswordEditText == null) {
            return ;
        }
        if(!isRightEmail) {
            return;
        }
        final String pwd = mPasswordEditText.getText().toString();
        if(TextUtils.isEmpty(pwd)) {
            mPasswordEditText.setError(getResources().getString(R.string.input_password_null));
            return;
        }
        final String email = mEmailEditText.getText().toString();
//        HttpMethods.getInstance().login(email, pwd, new Observer<Login>() {
//
//            @Override
//            public void onNext(Login login) {
//                L.i("login = " + login);
//            }
//
//            @Override
//            public void onCompleted() {
//                L.i("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                L.i("onError = " + e.getMessage());
//            }
//        });
    }
}
