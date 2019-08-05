RestClient
======

Type-safe HTTP client for Android and Java by Square, Inc.


implement Oauth2 Authorization


download
======

Gradle
latest version on jitpack [![](https://jitpack.io/v/alishatergholi/RestClient.svg)](https://jitpack.io/#alishatergholi/RestClient)
```groovy
    
    repositories{
        maven { url 'https://jitpack.io' }
    }
    
    //java version required
    compileOptions {
        sourceCompatibility  = JavaVersion.VERSION_1_8
        targetCompatibility  = JavaVersion.VERSION_1_8
    }
    
    dependencies {
        implementation 'com.github.alishatergholi:rest-client:1.1.5'
    }
```




How do i use RestClient
=======================
```java
    RestClient client = new RestClient
             .Builder(context)
             /* for add custom header you need 
             ArrayMap<String,String> header = new ArrayMap<>();
             header.put("appid","0e8f8fd2-1acb-11e7-8ab0-ac162d7938f0");
             header.put("accept-language", "fa");
             .setHeader(header)
             */
             /* for add Authorization Header */
             .setAuthorization("Authorization url","clientId","client",AuthType.BASIC_AUTH)
             .setUserInfo("username for Authorization","password for Authorization")
             .build();
    
    //for add body use.
    RequestParams params = new RequestParams(RequestBodyType.FormData);
    params.put("key","value");
    
    client.POST("url",
                "tag " /*you can set tag when you need to cancel your service*/,
                params /* except get method we can add body for other method as body */,
                new ResponseTextHandler() {
                        @Override
                        protected void onSuccess(String result) {
                            Log.d(TAG,result);
                        }
                        
                        @Override
                        public void onProgress(double percent, long bytesWritten, long totalSize) {
                            super.onProgress(percent, bytesWritten, totalSize);
                            Log.d(TAG,"percent " + percent);
                        }

                        @Override
                        public void onFailure(int errorCode, String errorMsg) {
                            Log.d(TAG,"onFailure " + errorMsg);
                        }
                    }); 
```
                    

License
=======

    Copyright 2013 Square, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

