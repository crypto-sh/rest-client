package com.github.restclient

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.ArrayMap
import com.github.library.RestClient
import com.github.library.enums.RequestBodyType
import com.github.library.response.ResponseJsonHandler
import com.github.library.utils.RequestParams
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    lateinit var restClient : RestClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRestClient()
        text.setOnClickListener {
            val params = RequestParams(RequestBodyType.FormData)
            restClient.get("/home","", object : ResponseJsonHandler<WebResult>(WebResult::class.java){
                override fun onSuccess(result: WebResult) {
                    Toast.makeText(this@MainActivity,"onSuccess",Toast.LENGTH_LONG).show()
                }

                override fun onFailure(errorCode: Int, errorMsg: String) {
                    Toast.makeText(this@MainActivity,"onFailure",Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun initRestClient() {
        val headers = ArrayMap<String,String>()
        headers["User-Agent"] = "PostmanRuntime/7.15.0"
        headers["Accept"] = "*/*"
        headers["Cache-Control"] = "no-cache"
        headers["Postman-Token"] = "bbd5030d-d215-4bad-8363-61089a2a4046"
        headers["Host"] = "d3d482f5-17cd-4f37-aa22-7cde6196f5db.mock.pstmn.io"
        headers["accept-encoding"] = "gzip, deflate"
        restClient = RestClient.Builder(this)
                .setBaseUrl("https://14171f10-9805-43cb-ba36-f2849fc0a422.mock.pstmn.io")
                .setDebugEnable(true)
                .setHeader(headers)
                .build()
    }
}
