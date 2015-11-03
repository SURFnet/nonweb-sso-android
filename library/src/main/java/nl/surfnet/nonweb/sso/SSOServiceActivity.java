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


/**
 * Single-Sign-On Service Activity class for easy authorization flow.
 *
 * @author W.Elsinga
 */
public class SSOServiceActivity extends Activity {

    static final String TAG = SSOServiceActivity.class.getName();

    private static SSOCallback _callback;
    private static String _consumerId;
    private static String _endpoint;
    private static String _scheme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Starting task to retrieve token.");
        }
        new RequestTokenTask(this, _consumerId, _endpoint).execute();
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
        _callback = callback;
        _consumerId = consumerId;
        _endpoint = endpoint;
        _scheme = scheme;
        context.startActivity(new Intent().setClass(context, SSOServiceActivity.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SSOServiceActivity.this.finish();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (_callback != null) {
            final Uri uri = intent.getData();

            if (uri != null && _scheme.equals(uri.getScheme())) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Callback received : " + uri);
                    Log.d(TAG, "Retrieving Access Token");
                }
                _callback.success(new Credential(uri));
            } else {
                _callback.failure();
            }
        }
        SSOServiceActivity.this.finish();
    }
}
