RestClient
======

Type-safe HTTP client for Android and Java by Square, Inc.


Library for call RestApi service with different type of Authorization.  


download
======

Gradle
latest version on jitpack [![](https://jitpack.io/v/alishatergholi/RestClient.svg)](https://jitpack.io/#alishatergholi/RestClient)
```groovy
    
    repositories{
        maven { url 'https://jitpack.io' }
    }
    
    dependencies {
        implementation 'com.github.alishatergholi:rest-client:1.1.3'
    }
```




How do i use RestClient
=======================
```kotlin
    /**
    *
    * add custom header into RestClient  
    * 
    */
    val headers = ArrayMap<String,String>()
    headers["Cache-Control"] = "no-cache"
    headers["accept-encoding"] = "gzip, deflate"
            
    /**
    * 
    * for using RestClient you need to create instance of library with Base Url 
    * 
    */        
    val restClient = RestClient.Builder(this)
            .setBaseUrl("Base URL")
            .setAuthorizationOauth2("AuthoriaztionUrl","clientId","clientSecret")
            .setDebugEnable(true)
            .setHeader(headers)
            .build()
    
    /**
    * 
    * You can add body with RequestParams 
    * also you can define type of body with RequestBodyType 
    * default RequestBodyType is FormUrlEncode
    * 
    */
    val params = RequestParams(RequestBodyType.FormData)
    params.put("key","value")
    
    
    /**
    *
    * get service with params
    * @param url        It's the path you want to have call 
    * @param tag        this parameter using for find the specific service you have called 
    * @param callBack   as you can see callBack use to get response of web service and need class of your response for Parse 
    * you can have different type of response like Text, File Or Json 
    * for json response you're able to define class of response.
    * 
    */
    restClient.get(url,tag, object : ResponseJsonHandler<WebResult>(WebResult::class.java){
                override fun onSuccess(result: WebResult) {
                    Toast.makeText(this@MainActivity,"onSuccess",Toast.LENGTH_LONG).show()
                }

                override fun onFailure(errorCode: Int, errorMsg: String) {
                    Toast.makeText(this@MainActivity,"onFailure",Toast.LENGTH_LONG).show()
                }
    })
    
    
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

