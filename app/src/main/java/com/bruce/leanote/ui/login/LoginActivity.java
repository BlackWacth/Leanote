package com.bruce.leanote.ui.login;

import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bruce.leanote.R;
import com.bruce.leanote.entity.Login;
import com.bruce.leanote.global.C;
import com.bruce.leanote.net.HttpMethods;
import com.bruce.leanote.net.ObserverAdapter;
import com.bruce.leanote.ui.HomeActivity;
import com.bruce.leanote.ui.base.BaseActivity;
import com.bruce.leanote.ui.login.adapter.EmailAutoCompleteAdapter;
import com.bruce.leanote.ui.signIn.SignInActivity;
import com.bruce.leanote.ui.widgets.MAppCompatAutoCompleteTextView;
import com.bruce.leanote.ui.widgets.PasswordEditText;
import com.bruce.leanote.utils.L;
import com.bruce.leanote.utils.RegexUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.acet_login_email_content)
    MAppCompatAutoCompleteTextView mEmailEditText;

    @BindView(R.id.pet_login_pwd_content)
    PasswordEditText mPasswordEditText;

    @BindView(R.id.acb_login_login_btn)
    AppCompatButton mLoginBtn;

    @BindView(R.id.tv_login_sign_in)
    TextView mSignInTextView;

    private boolean isRightEmail = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        mEmailEditText.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mSignInTextView.setOnClickListener(this);

        Set<String> emailSet = mSharedPreferences.getStringSet(C.EXTRA_EMAIL_SET, new HashSet<String>());
        final EmailAutoCompleteAdapter adapter = new EmailAutoCompleteAdapter(this, R.layout.item_auto_complete_text_view, new ArrayList<>(emailSet));

        mEmailEditText.setAdapter(adapter);

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
        mEmailEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                if (!TextUtils.isEmpty(item)) {
                    mPasswordEditText.setText(mSharedPreferences.getString(item, ""));
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
        if (mEmailEditText == null || mPasswordEditText == null) {
            return;
        }
        if (!isRightEmail) {
            return;
        }
        final String pwd = mPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            mPasswordEditText.setError(getResources().getString(R.string.input_password_null));
            return;
        }
        final String email = mEmailEditText.getText().toString();

        HttpMethods.getInstance().login(email, pwd, new ObserverAdapter<Login>(this, true) {
            @Override
            public void onNext(Login login) {
                super.onNext(login);
                L.i("login = " + login.toString());
                mSharedPreferencesEditor.putString(C.EXTRA_TOKEN, login.getToken());
                C.TOKEN = login.getToken();
                mSharedPreferencesEditor.putString(C.EXTRA_USER_ID, login.getUserId());
                mSharedPreferencesEditor.putString(C.EXTRA_USER_NAME, login.getUsername());
                Set<String> emailSet = mSharedPreferences.getStringSet(C.EXTRA_EMAIL_SET, new HashSet<String>());
                emailSet.add(email);
                mSharedPreferencesEditor.putStringSet(C.EXTRA_EMAIL_SET, emailSet);
                mSharedPreferencesEditor.putString(email, pwd);
                mSharedPreferencesEditor.commit();
            }

            @Override
            public void requestFailed(String msg) {
                super.requestFailed(msg);
                mPasswordEditText.setError(msg);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                startActivity(HomeActivity.class);
                LoginActivity.this.finish();
            }
        });
    }
}
