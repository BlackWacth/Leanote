package com.bruce.leanote.ui.signIn;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bruce.leanote.R;
import com.bruce.leanote.entity.Result;
import com.bruce.leanote.global.C;
import com.bruce.leanote.net.HttpMethods;
import com.bruce.leanote.net.ObserverAdapter;
import com.bruce.leanote.ui.base.BaseActivity;
import com.bruce.leanote.ui.login.LoginActivity;
import com.bruce.leanote.ui.widgets.PasswordEditText;
import com.bruce.leanote.utils.RegexUtils;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tiet_sign_in_email_content)
    TextInputEditText mEmailEditText;

    @BindView(R.id.pet_sign_in_pwd_content)
    PasswordEditText mPwdEditText;

    @BindView(R.id.pet_sign_in_pwd_again_content)
    PasswordEditText mPwdAgainEditText;

    @BindView(R.id.acb_sign_in_btn)
    AppCompatButton mSignInBtn;

    @BindView(R.id.tv_sign_in_login)
    TextView mLoginText;

    private boolean isRightEmail = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void init() {

        mSignInBtn.setOnClickListener(this);
        mLoginText.setOnClickListener(this);

        mEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final String email = mEmailEditText.getText().toString();
                    if (TextUtils.isEmpty(email)) {
                        mEmailEditText.setError(getResources().getString(R.string.input_email_null));
                        isRightEmail = false;
                    } else {
                        if (!RegexUtils.isEmail(email)) {
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
            case R.id.acb_sign_in_btn:
                signIn();
                break;

            case R.id.tv_sign_in_login:
                startActivity(LoginActivity.class);
                finish();
                break;
        }
    }

    private void signIn() {
        if (mEmailEditText == null || mPwdEditText == null || mPwdAgainEditText == null) {
            return;
        }
        if (!isRightEmail) {
            return;
        }

        final String email = mEmailEditText.getText().toString();

        final String pwd = mPwdEditText.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            mPwdEditText.setError(getResources().getString(R.string.input_password_null));
            return;
        }
        final String pwdAgain = mPwdAgainEditText.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            mPwdAgainEditText.setError(getResources().getString(R.string.input_password_null));
            return;
        }
        if(!pwd.equals(pwdAgain)) {
            mPwdAgainEditText.setError(getResources().getString(R.string.input_pwd_inconsistent));
            return;
        }
        HttpMethods.getInstance().register(email, pwd, new ObserverAdapter<Result>(this, true) {
            @Override
            public void onComplete() {
                super.onComplete();
                Set<String> emailSet = mSharedPreferences.getStringSet(C.EXTRA_EMAIL_SET, new HashSet<String>());
                emailSet.add(email);
                mSharedPreferencesEditor.putStringSet(C.EXTRA_EMAIL_SET, emailSet);
                mSharedPreferencesEditor.putString(email, pwd);
                startActivity(LoginActivity.class);
                SignInActivity.this.finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showToast(e.getMessage());
            }

            @Override
            public void requestFailed(String msg) {
                super.requestFailed(msg);
                showToast(msg);
            }
        });


    }
}
