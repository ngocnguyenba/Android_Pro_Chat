
package com.androidprochatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidprochatapplication.Common.Common;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class UserProfile extends AppCompatActivity {
    EditText edtOldPass, edtNewPass, edtFullname, edtEmail, edtPhone;
    Button btnUpdate, btnCancel;
    Toolbar toolbar;
    ProgressDialog mDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_update_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.user_update_logout:
                logout();
                break;
            default:
                break;
        }
        return true;
    }

    private void logout() {
        QBUsers.signOut().performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                QBChatService.getInstance().logout(new QBEntityCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, Bundle bundle) {
                        Toast.makeText(UserProfile.this, "Ban da Dang xuat", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserProfile.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(UserProfile.this, "ERROR"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(UserProfile.this, "ERROR"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar)findViewById(R.id.user_update_toolbar);
        toolbar.setTitle("Android Chat Application");
        setSupportActionBar(toolbar);

        initViews();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = edtOldPass.getText().toString();
                String newPassword = edtNewPass.getText().toString();
                String email = edtEmail.getText().toString();
                String fullName = edtFullname.getText().toString();
                String phone = edtPhone.getText().toString();

                QBUser user = new QBUser();
                user.setId(QBChatService.getInstance().getUser().getId());
                if (!Common.isNullOrEmptyString(oldPassword));
                    user.setOldPassword(oldPassword);
                if (!Common.isNullOrEmptyString(newPassword));
                    user.setPassword(newPassword);
                if (!Common.isNullOrEmptyString(fullName));
                    user.setFullName(fullName);
                if (!Common.isNullOrEmptyString(email));
                    user.setEmail(email);
                if (!Common.isNullOrEmptyString(phone));
                    user.setPhone(phone);

                mDialog = new ProgressDialog(UserProfile.this);
                mDialog.setMessage("Xin vui long cho doi ...");
                mDialog.show();
                QBUsers.updateUser(user).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        Toast.makeText(UserProfile.this, "Tai khoan: "+qbUser.getLogin()+"da cap nhat thong tin", Toast.LENGTH_LONG).show();
                        mDialog.dismiss();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(UserProfile.this, "ERROR"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initViews() {
        btnCancel = (Button)findViewById(R.id.user_btnCancel);
        btnUpdate = (Button)findViewById(R.id.user_btnUpdate);

        edtOldPass = (EditText)findViewById(R.id.user_edtPassword);
        edtNewPass = (EditText)findViewById(R.id.user_edtNewPassword);
        edtFullname = (EditText)findViewById(R.id.user_edtFullname);
        edtEmail = (EditText)findViewById(R.id.user_edtEmail);
        edtPhone = (EditText)findViewById(R.id.user_edtPhone);
    }
}
