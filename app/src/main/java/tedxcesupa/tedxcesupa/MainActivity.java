/*
 * Copyright (c) 2018. TEDxCESUPA
 * Grupo de Estudos em Tecnologia Assistiva - Centro Universitário do Estado do Pará
 * dgp.cnpq.br/dgp/espelhogrupo/6411407947674167
 * Desenvolvido por:
 *   Luis Fernando Gomes Sales - lfgsnando@gmail.com
 *   Matheus Henrique dos Santos - mhenrique.as@gmail.com
 *
 */

package tedxcesupa.tedxcesupa;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tedxcesupa.tedxcesupa.fragments.AnotacoesFragment;
import tedxcesupa.tedxcesupa.fragments.InformacoesFragment;
import tedxcesupa.tedxcesupa.fragments.InicioFragment;
import tedxcesupa.tedxcesupa.fragments.PalestrantesFragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseUser mUser;
    FirebaseAuth mAuth;
    LoginManager loginManager;

    ProgressBar mProgress;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sair:
                FirebaseAuth.getInstance().signOut();
                loginManager.logOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.menu_comprar:
                dialogComprarIngresso();
                break;

            case R.id.menu_alterar_senha:
                dialogAlterarSenha();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        loginManager = LoginManager.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.menu_item1:
                        selectedFragment = InicioFragment.newInstance();
                        break;

                    case R.id.menu_item2:
                        selectedFragment = PalestrantesFragment.newInstance();
                        break;

                    case R.id.menu_item3:
                        selectedFragment = AnotacoesFragment.newInstance();
                        break;

                    case R.id.menu_item4:
                        selectedFragment = InformacoesFragment.newInstance();
                        break;

                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.conteudo, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        /** Por padrão inicia o {@link tedxcesupa.tedxcesupa.fragments.InicioFragment} **/
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.conteudo, InicioFragment.newInstance());
        transaction.commit();
        bottomNavigationView.setSelectedItemId(R.id.menu_item1);
    }

    public void dialogComprarIngresso(){
        LayoutInflater inflater = this.getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = inflater.inflate(R.layout.comprar_ingresso_dialog, null);

        builder.setView(dialogView);

        Button comprarButton = dialogView.findViewById(R.id.comprar_button);
        comprarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://tedxcesupaconsolidar.eventbrite.com.br?discount=aplicativoandroid";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(MATCH_PARENT, MATCH_PARENT);
    }

    public void dialogAlterarSenha(){
        LayoutInflater inflater = this.getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = inflater.inflate(R.layout.alterar_senha_dialog, null);
        builder.setView(view);

        final EditText senha1 = view.findViewById(R.id.ns_senha1),
                senha2 = view.findViewById(R.id.ns_senha2),
                email  = view.findViewById(R.id.ns_email),
                senha_atual = view.findViewById(R.id.ns_senha_atual);

        mProgress = view.findViewById(R.id.ns_progress);
        Button salvar = view.findViewById(R.id.ns_salvar);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reautenticacao(email.getText().toString(), senha_atual.getText().toString(),
                        senha1.getText().toString(), senha2.getText().toString());
                mProgress.setVisibility(View.VISIBLE);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void reautenticacao(String email, String senha, final String senha1, final String senha2){

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    alterar_senha(senha1, senha2);
                }else {
                    Toast.makeText(MainActivity.this, "Email ou senha atual inválido.", Toast.LENGTH_SHORT).show();
                    mProgress.setVisibility(View.GONE);
                }
            }
        });
    }

    public void alterar_senha(String senha1, String senha2){
        if (senha1.equals(senha2)) {
            mUser.updatePassword(senha1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Senha inválida!" + task.getException(), Toast.LENGTH_SHORT).show();
                        mProgress.setVisibility(View.GONE);
                    }
                }
            });
        }else {
            Toast.makeText(this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
            mProgress.setVisibility(View.GONE);
        }
    }

}
