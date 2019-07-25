package com.mattdog.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mattdog.smack.Utilities.URL_CREATE_USER
import com.mattdog.smack.Utilities.URL_LOGIN
import com.mattdog.smack.Utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){

        val jsonBody = JSONObject()
        //JSON object that is being passed on with the web request
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()
        //JSON body

        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
            //StringRequest is the type of web request, expecting a String response
            //POST is the method type
            //Response.Listener is the response
            println(response)
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not register user: $error")
            complete(false)
            //Error response
        })  {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
            //Content type body
        }

        Volley.newRequestQueue(context).add(registerRequest)

    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){
        val jsonBody = JSONObject()
        //JSON object that is being passed on with the web request
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()
        //JSON body

        val loginRequest = object: JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener {response ->
            //parse the JSON object
            println(response)

            try {
                userEmail = response.getString("user")
                authToken = response.getString("token")
                isLoggedIn = true
                complete(true)
                //Setting the values of the user data variables using the data passed in the response
                //Mainly for authenticating the log in of the user
            } catch (e: JSONException){
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }

            //how to access values inside keys in a JSON object
        }, Response.ErrorListener {error ->
            Log.d("ERROR", "Could not register user: $error")
            complete(false)
        })  {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(loginRequest)
    }

    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String, complete:(Boolean) -> Unit){
        //User is created
        val jsonBody = JSONObject()
        //JSON object that is being passed on with the web request
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()
        //JSON body

        val createRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener { response ->
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                //Setting the data of the user by putting the values of the response into the user variables stored in UserdData Service

                complete(true)
            } catch (e: JSONException){
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }


        }, Response.ErrorListener {error ->
            Log.d("ERROR", "Could not add user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }
            //override the function getHeaders
            //put the Authorization into the 'put' Hashmap
        }

        Volley.newRequestQueue(context).add(createRequest)
    }

}