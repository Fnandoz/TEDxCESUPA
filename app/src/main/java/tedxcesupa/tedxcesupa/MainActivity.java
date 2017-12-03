package tedxcesupa.tedxcesupa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tedxcesupa.tedxcesupa.fragments.AnotacoesFragment;
import tedxcesupa.tedxcesupa.fragments.InformacoesFragment;
import tedxcesupa.tedxcesupa.fragments.InicioFragment;
import tedxcesupa.tedxcesupa.fragments.PalestrantesFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseUser mUser;
    FirebaseAuth mAuth;
    LoginManager loginManager;

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomNavigationView.setSelectedItemId(R.id.menu_item1);
    }

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
                startActivity(intent);
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

}
