package nl.surfnet.nonweb.sso.demo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import nl.surfnet.nonweb.sso.SSOCallback;
import nl.surfnet.nonweb.sso.SSOServiceActivity;
import nl.surfnet.nonweb.sso.data.Credential;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * The main activity which displays a login button and handles the login flow.
 * Also has some animations for changing between the states.
 **/
public class MainActivity extends AppCompatActivity {

    private static final String SERVER_ENDPOINT = "https://nonweb.demo.surfconext.nl/php-oauth-as/authorize.php";
    private static final String CLIENT_ID = "4dca00da67c692296690e90c50c96b79";
    private static final String SCHEME = "sfoauth";

    private static final int WAIT_SECONDS = 10;

    private View _loginButton;
    private View _tokenLabel;
    private TextView _token;

    private Handler _handler = new Handler();
    private boolean _comingFromBrowser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    _goFullscreen();
                }
            }
        });
        _goFullscreen();
        _loginButton = findViewById(R.id.login);
        _tokenLabel = findViewById(R.id.token_label);
        _token = (TextView)findViewById(R.id.token);
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Make a callback to handle the response
                SSOCallback callback = new SSOCallback() {

                    @Override
                    public void success(final Credential credential) {
                        _comingFromBrowser = true;
                        _animateTokenLayoutWithToken(credential.getAccessToken());
                    }

                    @Override
                    public void failure(String message) {
                        // Just display an alert dialog.
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.error_title)
                                .setMessage(message)
                                .show();
                    }
                };

                // Now call to authorize with a clientId/customerId, endpoint, schema and the option callback
                SSOServiceActivity.authorize(v.getContext(), CLIENT_ID, SERVER_ENDPOINT, SCHEME, callback);

            }
        });
    }

    /**
     * Animates the layouts which will display the token.
     *
     * @param accessToken The access token to be displayed inside to the user.
     */
    private void _animateTokenLayoutWithToken(String accessToken) {
        _token.setText(accessToken);
        _loginButton.setVisibility(View.GONE);
        _tokenLabel.setTranslationY(140);
        _tokenLabel.setAlpha(1);
        _token.setTranslationY(-50);
        _token.setScaleX(0.35f);
        _token.setScaleY(1f);
        _tokenLabel.setVisibility(View.VISIBLE);
        _token.setVisibility(View.VISIBLE);
        _handler.post(new Runnable() {
            @Override
            public void run() {
                if (_tokenLabel == null || _token == null) {
                    return;
                }
                _tokenLabel.animate().translationY(0).setDuration(800).setStartDelay(200).start();
                _token.animate().translationY(0).scaleX(1).setDuration(800).setStartDelay(200).start();
            }
        });
        _token.postDelayed(new Runnable() {
            @Override
            public void run() {
                _animateBackToLogin();
            }
        }, WAIT_SECONDS * 1000);
    }

    /**
     * Animates back to the login button.
     */
    private void _animateBackToLogin() {
        if (_tokenLabel == null || _token == null) {
            return;
        }
        _tokenLabel.animate().translationY(140).setDuration(800).start();
        _token.animate().translationY(-50).scaleX(0.35f).scaleY(0.8f).setDuration(800).start();
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _token.setVisibility(View.GONE);
                _loginButton.setTranslationY(-300);
                _loginButton.setAlpha(0);
                _loginButton.setVisibility(View.VISIBLE);
                _tokenLabel.animate().translationY(300).alpha(0).setDuration(500).start();
                _loginButton.animate().translationY(0).alpha(1).setDuration(500).start();
            }
        }, 1100);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Make sure the animations do not get stuck
        if (!_comingFromBrowser) {
            _handler.removeCallbacksAndMessages(null);
            if (_loginButton.getVisibility() == View.GONE) {
                _animateBackToLogin();
            }
        } else {
            _comingFromBrowser = false;
        }
    }

    /**
     * Puts the app in immersive mode.
     */
    @SuppressLint("InlinedApi")
    private void _goFullscreen() {
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
