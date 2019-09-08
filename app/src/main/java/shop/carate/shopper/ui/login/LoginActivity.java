package shop.carate.shopper.ui.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import shop.carate.shopper.MainActivity;
import shop.carate.shopper.R;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    public static Integer INVALIDEMAIL = 1;
    private Integer INVLAIDPASS = 2;
    private Integer EMPTYEMAIL = 3;
    private Integer EMPTYPASS = 4;
    private Integer VALID = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);
        ProgressBar loadingProgressBar = findViewById(R.id.loading);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(validate()){
                    case 1:
                        Toast.makeText(LoginActivity.this,MainActivity.getInstance().getString(R.string.Error_Email_format),Toast.LENGTH_LONG).show();
                        usernameEditText.setText("");
                        break;
                    case 2:
                        Toast.makeText(LoginActivity.this,MainActivity.getInstance().getString(R.string.Error_password_length),Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(LoginActivity.this,MainActivity.getInstance().getString(R.string.Error_empty_email),Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(LoginActivity.this,MainActivity.getInstance().getString(R.string.Error_empty_password),Toast.LENGTH_LONG).show();
                        break;
                    case 0:
                        login();
                        break;
                    default:
                        break;
                }

            }
        });

    }

    private void login() {
        String username = String.valueOf(usernameEditText.getText());
        String password = String.valueOf(passwordEditText.getText());

    }

    private Integer validate() {
        String username = String.valueOf(usernameEditText.getText());
        String password = String.valueOf(passwordEditText.getText());
        if(username.length()==0){
            return EMPTYEMAIL;
        }
        if(password.length()==0){
            return EMPTYPASS;
        }
        if(!username.contains("@")){
            return INVALIDEMAIL;
        }
        if(password.length()<6){
            return INVLAIDPASS;
        }
        return VALID;
    }

}
