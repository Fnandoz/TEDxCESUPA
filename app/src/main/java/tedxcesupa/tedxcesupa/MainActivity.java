package tedxcesupa.tedxcesupa;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import tedxcesupa.tedxcesupa.Fragments.AnotacoesFragment;
import tedxcesupa.tedxcesupa.Fragments.InformacoesFragment;
import tedxcesupa.tedxcesupa.Fragments.InicioFragment;
import tedxcesupa.tedxcesupa.Fragments.PalestrantesFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomNavigationView.setSelectedItemId(R.id.menu_item1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        /** Por padrão inicia o {@link tedxcesupa.tedxcesupa.Fragments.InicioFragment} **/
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.conteudo, InicioFragment.newInstance());
        transaction.commit();
        bottomNavigationView.setSelectedItemId(R.id.menu_item1);
    }
}
