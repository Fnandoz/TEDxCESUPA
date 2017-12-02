package tedxcesupa.tedxcesupa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    EditText emailText, senhaText;

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

        emailText = findViewById(R.id.emailLoginEdit);
        senhaText = findViewById(R.id.senhaLoginEdit);
    }

    public void buttonClick(View v){
        switch (v.getId()){
            case R.id.loginButton:
                login(emailText.getText().toString(), senhaText.getText().toString());
                break;

            case R.id.cadastrarButton:
                cadastro(emailText.getText().toString(), senhaText.getText().toString());
                break;

            case R.id.lostpasswordButton:
                recuperar_Senha(emailText.getText().toString());
                break;

            case R.id.facebookLogin:
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
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, "Email ou senha inválido.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

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
                                                    // OK
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

    public void facebook(){}

    public void recuperar_Senha(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Enviado", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "Erro!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
