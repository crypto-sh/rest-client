# restclient

RestClient
======

Type-safe HTTP client for Android with Java base on Okhttp3.

implement Basic Authorization 


download
======

Gradle
```groovy
    repositories{
        maven { url 'https://jitpack.io' }
    }
    
    dependencies {
        implementation 'com.github.alishatergholi:restclient:1.0.0'
    }
```
How do i use RestClient
=======================

    RestClient client = new RestClient
            .Builder(context)
            
            /* you can add Accept encoding for encode your response */
            /* for now we just support gzip */
            .setAcceptEnconding(EncodingType.GZIP) 
            
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
    RequestParams params = new RequestParams();
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
                    
                    
