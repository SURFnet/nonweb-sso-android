package nl.surfnet.nonweb.sso.sample;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import nl.surfnet.nonweb.sso.SSOService;
import nl.surfnet.nonweb.sso.data.Credential;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button authorizeButton = (Button)findViewById(R.id.authorize_btn);
        authorizeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Make a callback to handle the response
                SSOService.SSOCallback callback = new SSOService.SSOCallback() {

                    @Override
                    public void success(Credential credential) {

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.authorize)
                                .setMessage("Token : " + credential.getAccessToken())
                                .show();
                    }

                    @Override
                    public void failure() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.authorize)
                                .setMessage("Couldn't get token")
                                .show();
                    }
                };

                // Now call to authorize with a clientId/customerId and a callback (optional)
                SSOService.authorize(v.getContext(), "4dca00da67c692296690e90c50c96b79", callback);
            }
        });
    }
}
