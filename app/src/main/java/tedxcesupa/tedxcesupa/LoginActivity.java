package tedxcesupa.tedxcesupa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void buttonClick(View v){
        switch (v.getId()){
            case R.id.loginButton:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.cadastrarButton:
                break;

            case R.id.lostpasswordButton:
                break;
        }
    }
}
