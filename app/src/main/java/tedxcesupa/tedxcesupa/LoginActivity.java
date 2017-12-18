package tedxcesupa.tedxcesupa;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    CallbackManager mCallbackManager;
    EditText emailText, senhaText;
    LoginButton loginButton;
    ProgressBar loginProgress;

    String TAG = "FACEBOOK";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();


        emailText = findViewById(R.id.emailLoginEdit);
        senhaText = findViewById(R.id.senhaLoginEdit);
        loginButton = findViewById(R.id.facebookLogin);
        loginProgress = findViewById(R.id.loginProgress);

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void buttonClick(View v){
        switch (v.getId()){
            case R.id.loginButton:
                login(emailText.getText().toString(), senhaText.getText().toString());
                loginProgress.setVisibility(View.VISIBLE);
                break;

            case R.id.cadastrarButton:
                dialogCadastro();
                break;

            case R.id.lostpasswordButton:
                recuperar_Senha(emailText.getText().toString());
                break;
        }
    }

    public void login(String email, String senha){
        if (!email.isEmpty() & !senha.isEmpty()){
            mAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                mUser = mAuth.getCurrentUser();
                                if (mUser.isEmailVerified()){
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(LoginActivity.this, "Verifique sua conta", Toast.LENGTH_SHORT).show();
                                    mUser.sendEmailVerification();
                                    loginProgress.setVisibility(View.GONE);
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, "Email ou senha inválido.", Toast.LENGTH_SHORT).show();
                                loginProgress.setVisibility(View.GONE);
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_SHORT).show();
            loginProgress.setVisibility(View.GONE);
        }
    }

    public void cadastro(String email, String senha){
        if (!email.isEmpty() & !senha.isEmpty()){
            mAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                mUser = mAuth.getCurrentUser();
                                assert mUser != null;
                                mUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(LoginActivity.this,
                                                            "Confirme sua conta através do link" +
                                                                    " enviado por e-mail.",
                                                            Toast.LENGTH_LONG).show();
                                                }else {
                                                    // Erro
                                                }
                                            }
                                        });

                            }else {
                                Toast.makeText(LoginActivity.this, "Erro ao criar conta.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void recuperar_Senha(String email){
        if (email.isEmpty()){
            Toast.makeText(this, "Informe seu e-mail!", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Enviado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Erro, e-mail não encontrado.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void dialogCadastro(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.criar_conta_dialog, null);
        builder.setView(view);

        final AlertDialog alert = builder.create();
        alert.show();

        final EditText email = alert.findViewById(R.id.nc_email_edit);
        final EditText senha1 = alert.findViewById(R.id.nc_senha1_edit);
        final EditText senha2 = alert.findViewById(R.id.nc_senha2_edit);

        Button criar = alert.findViewById(R.id.nc_criar_button);

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().isEmpty() & senha1.getText().toString().equals(senha2.getText().toString())){
                    String e = email.getText().toString();
                    String s = senha1.getText().toString();
                    cadastro(e, s);
                    alert.dismiss();
                }else {
                    Toast.makeText(LoginActivity.this, "Dados incorretos", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

}
