package nl.surfnet.nonweb.sso;

/*
 * Copyright 2015 SURFnet BV, The Netherlands
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import nl.surfnet.nonweb.sso.data.Credential;
import nl.surfnet.nonweb.sso.util.StringUtil;


/**
 * Single-Sign-On Service Activity class for easy authorization flow.
 *
 * @author W.Elsinga
 */
public class SSOServiceActivity extends Activity {

    static final String TAG = SSOServiceActivity.class.getName();

    private static SSOCallback _callback = new SSOCallback() {

        @Override
        public void success(Credential credential) {
            Log.i(TAG, "has token: " + credential.getAccessToken());
        }

        @Override
        public void failure(String message) {
            Log.e(TAG, "fail: " + message);
        }
    };
    private static String _consumerId;
    private static String _endpoint;
    private static String _scheme;
    private static String _state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Starting task to retrieve token.");
        }
        new RequestTokenTask(this, _consumerId, _endpoint, _state).execute();
    }


    /**
     * Authorize the {@code consumerId}
     *
     * @param context    the context
     * @param consumerId consumer ID to be validated
     * @param endpoint   oauth-server endpoint
     * @param scheme     scheme
     */
    public static void authorize(@NonNull final Context context, //
                                 @NonNull final String consumerId, //
                                 @NonNull final String endpoint,
                                 @NonNull final String scheme) {
        authorize(context, consumerId, endpoint, scheme, null);
    }

    /**
     * Authorize the {@code consumerId}
     *
     * @param context    the context
     * @param consumerId consumer ID to be validated
     * @param endpoint   oauth-server endpoint
     * @param scheme     scheme
     * @param callback   {@link SSOCallback} to invoke when the request completes or fails.
     */
    public static void authorize(@NonNull final Context context, //
                                 @NonNull final String consumerId, //
                                 @NonNull final String endpoint, //
                                 @NonNull final String scheme,
                                 @Nullable final SSOCallback callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Authorize consumer " + consumerId);
        }
        if (callback != null) {
            _callback = callback;
        }
        String message = null;
        if (StringUtil.isBlank(consumerId)) {
            message = context.getString(R.string.no_consumer_id);
        } else if (StringUtil.isBlank(endpoint)) {
            message = context.getString(R.string.no_endpoint);
        } else if (StringUtil.isBlank(scheme)) {
            message = context.getString(R.string.no_schema);
        }

        if (StringUtil.isBlank(message)) {
            _consumerId = consumerId;
            _endpoint = endpoint;
            _scheme = scheme;
            _state = StringUtil.generateSessionString();

            context.startActivity(new Intent().setClass(context, SSOServiceActivity.class));
        } else {
            _callback.failure(message);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SSOServiceActivity.this.finish();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final Uri uri = intent.getData();

        if (uri != null && _scheme.equals(uri.getScheme())) {
            Credential credential = new Credential(uri);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Callback received : " + uri);
                Log.d(TAG, "Retrieving Access Token");
            }
            if (_state.equals(credential.getSessionIdentifier())) {
                _callback.success(credential);
            } else {
                _callback.failure(getString(R.string.state_error));
            }
        } else {
            _callback.failure(getString(R.string.cannot_handle_token));
        }
        SSOServiceActivity.this.finish();
    }
}
