SURFnet Android SSO Library
===================================================

Now you can easily integrate the [SURFnet](https://www.surf.nl) SSO process flow in your Android application by using this library.

Do you want to see it in action, check out the sample app in this repository.

For background information and investigation into the best practices that were implemented in this SDK, see [this page](https://github.com/SURFnet/nonweb-demo/wiki).

For a detailed investigation of the Android authentication possibilities, see [this page](https://github.com/SURFnet/nonweb-demo/wiki/Android).


HOW TO USE THE LIBRARY
-----

Before you can use the library you need to have your `consumerId` registered by SURFnet and have received your `consumer secret`.

1. Download the [latest version](https://github.com/SURFnet/nonweb-sso-android/releases) of the library.
2. Add the library to your project as lib.
3. Declare the `SSOServiceActivity` class inside your `AndroidManifest.xml`: 
 ```xml
<activity 
    android:name="nl.surfnet.nonweb.sso.SSOManager" 
    android:launchMode="singleTask">
    <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        
        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>

        <data
            android:host="...."
            android:scheme="...."/>
    </intent-filter>
</activity>
```
4. Add your `consumer secret` as host and your `scheme` information inside the data section. 
5. Inside your Activity call `SSOServiceActivity.authorize` with your `consumerId`, `endpoint` and `scheme` to start the authentication process.
 ```java
 SSOServiceActivity.authorize(v.getContext(), consumerId, endpoint, scheme, callback);
```

6. You can optionally provide a `SSOCallback` to handle the authorize result

```java
SSOCallback callback = new SSOCallback() {
 
 @Override
 public void success(Credential credential) {}
 
 @Override
 public void failure(String message) {}
};
```

 
[CHANGELOG](https://github.com/SURFnet/nonweb-sso-android/wiki/Changelog)
-----

Current version: 0.1.1



DEVELOPED BY
------------

* SURFnet - [https://www.surf.nl](https://www.surf.nl)
* Egeniq - [https://www.egeniq.com/](https://www.egeniq.com/)


LICENSE
-----

    Copyright 2015 SURFnet BV, The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
