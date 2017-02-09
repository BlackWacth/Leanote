package com.bruce.leanote.ui.login;

import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bruce.leanote.R;
import com.bruce.leanote.model.Account;
import com.bruce.leanote.ui.base.BaseActivity;
import com.bruce.leanote.ui.login.adapter.EmailAutoCompleteAdapter;
import com.bruce.leanote.ui.signIn.SignInActivity;
import com.bruce.leanote.ui.widgets.PasswordEditText;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private AppCompatAutoCompleteTextView mEmailEditText;
    private PasswordEditText mPasswordEditText;
    private AppCompatButton mLoginBtn;
    private TextView mSignInTextView;

    private List<Account> mAccounts;
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

        mAccounts = new ArrayList<>();
        mAccounts.add(new Account("huazhongwei@yeah.net", "12345"));
        mAccounts.add(new Account("1078824402@qq.com", "12345"));
        mAccounts.add(new Account("huaweiysh@gmail.com", "12345"));
        mAccounts.add(new Account("1070273909@qq.com", "12345"));
        ArrayList<String> list = new ArrayList<>();
        for (Account account : mAccounts) {
            list.add(account.getEmail());
        }
//        mEmailEditText.setAdapter(new EmailAutoCompleteAdapter(this, R.layout.item_auto_complete_text_view, mAccounts));
        mEmailEditText.setAdapter(new ArrayAdapter<String>(this, R.layout.item_auto_complete_text_view, list));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acet_login_email_content:
                showToast("email_content");
                mEmailEditText.showContextMenu();
                break;

            case R.id.acb_login_login_btn:

                break;

            case R.id.tv_login_sign_in:
                startActivity(SignInActivity.class);
                break;
        }
    }
}
