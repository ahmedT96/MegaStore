package com.food.omar.food;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.food.omar.food.Model.ChackUserResponse;
import com.food.omar.food.Model.User;
import com.food.omar.food.Retrofit.IFoodApi;
import com.food.omar.food.Uitls.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {
    private static final int REQUEST_CODE = 1000;
    Button btn_next;

    IFoodApi mServise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mServise = Common.getAPI();

        btn_next = findViewById(R.id.next1);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
            }
        });

        if (AccountKit.getCurrentAccessToken() != null) {
            final AlertDialog alertDialog = new SpotsDialog(login.this);
            alertDialog.show();
            alertDialog.setMessage("Please waiting....");
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    mServise.chackUserResponse(account.getPhoneNumber().toString()).enqueue(new Callback<ChackUserResponse>() {
                        @Override
                        public void onResponse(Call<ChackUserResponse> call, Response<ChackUserResponse> response) {
                            ChackUserResponse userResponse = response.body();
                            if (userResponse.isExistes()) {

                                mServise.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {

                                        Common.currentuser = response.body();
                                        updateTokenTofirebase();
                                        alertDialog.dismiss();
                                        startActivity(new Intent(login.this, Home.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toast.makeText(login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                alertDialog.dismiss();
                                showRegisterDialog(account.getPhoneNumber().toString());

                            }
                        }

                        @Override
                        public void onFailure(Call<ChackUserResponse> call, Throwable t) {

                        }
                    });
                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    Log.d("ERROR", accountKitError.getErrorType().getMessage());
                }
            });

        }
    }

    private void startLoginPage(LoginType loginType) {
        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType, AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, builder.build());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null) {
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            } else if (result.wasCancelled()) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            } else {
                if (result.getAccessToken() != null) {
                    final AlertDialog alertDialog = new SpotsDialog(login.this);
                    alertDialog.show();
                    alertDialog.setMessage("Please waiting....");

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            mServise.chackUserResponse(account.getPhoneNumber().toString()).enqueue(new Callback<ChackUserResponse>() {
                                @Override
                                public void onResponse(Call<ChackUserResponse> call, Response<ChackUserResponse> response) {
                                    ChackUserResponse userResponse = response.body();
                                    if (userResponse.isExistes()) {

                                        mServise.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                Common.currentuser = response.body();
                                              //  updateTokenTofirebase();
                                                alertDialog.dismiss();
                                                startActivity(new Intent(login.this, Home.class));
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {
                                                Toast.makeText(login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        alertDialog.dismiss();
                                        showRegisterDialog(account.getPhoneNumber().toString());

                                    }
                                }

                                @Override
                                public void onFailure(Call<ChackUserResponse> call, Throwable t) {

                                }
                            });
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("ERROR", accountKitError.getErrorType().getMessage());
                        }
                    });

                }
            }
        }
    }

    private void showRegisterDialog(final String phone) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(login.this);
        alertDialog.setTitle("Register");
        LayoutInflater inflater = this.getLayoutInflater();
        View register_Layout = inflater.inflate(R.layout.register_layout, null);
        final MaterialEditText editName = register_Layout.findViewById(R.id.edt_name);
        final MaterialEditText editAdress = register_Layout.findViewById(R.id.edt_adress);
        final MaterialEditText editBirthday = register_Layout.findViewById(R.id.edt_birthday);

        Button button = register_Layout.findViewById(R.id.Coutinuen2);
        editBirthday.addTextChangedListener(new PatternedTextWatcher("####-##-##"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.create().dismiss();

                if (TextUtils.isEmpty(editName.getText().toString())) {
                    Toast.makeText(login.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(editAdress.getText().toString())) {
                    Toast.makeText(login.this, "Please enter your address", Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(editBirthday.getText().toString())) {
                    Toast.makeText(login.this, "Please enter your Birthday", Toast.LENGTH_SHORT).show();

                }
                final AlertDialog WaitDialog = new SpotsDialog(login.this);
                WaitDialog.show();
                WaitDialog.setMessage("Please waiting....");
                mServise.register(phone, editName.getText().toString(), editBirthday.getText().toString(), editAdress.getText().toString()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        WaitDialog.dismiss();
                        User user = response.body();
                        if (TextUtils.isEmpty(user.getEror_msg())) {
                            Common.currentuser = response.body();
                            updateTokenTofirebase();
                            Toast.makeText(login.this, "User register successfy", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(login.this, Home.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        WaitDialog.dismiss();
                    }
                });
            }
        });
        alertDialog.setView(register_Layout);
        alertDialog.show();
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.food.omar.food", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    boolean isbackButtonClicked = false;

    @Override
    public void onBackPressed() {

        if (isbackButtonClicked) {
            super.onBackPressed();
            return;
        }
        this.isbackButtonClicked = true;
        Toast.makeText(this, "Please click back again", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        isbackButtonClicked = false;
        super.onResume();
    }
    private void updateTokenTofirebase() {
       FirebaseInstanceId.getInstance()
               .getInstanceId()
               .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                   @Override
                   public void onSuccess(InstanceIdResult instanceIdResult) {
                       IFoodApi mService= Common.getAPI();
                       mService.ubdateToken(Common.currentuser.getPhone(), instanceIdResult.getToken(),"0").enqueue(new Callback<User>() {
                           @Override
                           public void onResponse(Call<User> call, Response<User> response) {
                               Log.d("debug",response.toString());
                           }

                           @Override
                           public void onFailure(Call<User> call, Throwable t) {
                               Log.d("debug",t.toString());

                           }
                       });

                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }

}
