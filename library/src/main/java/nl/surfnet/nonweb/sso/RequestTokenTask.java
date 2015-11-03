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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import nl.surfnet.nonweb.sso.util.Constants;

/**
 * An asynchronous task that communicates with SURFnet to retrieve a request token.
 *
 * @author W.Elsinga
 */
public class RequestTokenTask extends AsyncTask<Void, Void, Void> {

    final String TAG = RequestTokenTask.class.getName();

    private Context _context;
    private String _consumerId;
    private String _endpoint;

    /**
     * Default constructor
     *
     * @param context    Required to be able to start the intent to launch the browser.
     * @param consumerId The consumer id to be validated
     * @param endpoint   oauth-server endpoint
     */
    public RequestTokenTask(@NonNull Context context, @NonNull String consumerId, @NonNull String endpoint) {
        _context = context;
        _consumerId = consumerId;
        _endpoint = endpoint;
    }

    /**
     * Present a browser to the user to authorize the token.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(_endpoint);
            sb.append("?" + Constants.CLIENT_ID);
            sb.append(_consumerId);
            sb.append(Constants.PARAM_AND + Constants.RESPONSE_TYPE);
            sb.append(Constants.PARAM_AND + Constants.STATE);
            sb.append(Constants.PARAM_AND + Constants.SCOPE);

            String url = sb.toString();

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Retrieving request token");
                Log.d(TAG, "Popping a browser with the authorize URL : " + url);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
            _context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error during retrieve request token", e);
        }
        return null;
    }
}
