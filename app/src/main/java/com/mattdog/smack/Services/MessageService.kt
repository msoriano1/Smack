package com.mattdog.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.mattdog.smack.Model.Channel
import com.mattdog.smack.Utilities.URL_GET_CHANNELS
import okhttp3.Response
import org.json.JSONException

object MessageService {
    //Download the channels and messages

    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, complete:(Boolean) -> Unit){
        val channelsRequest = object: JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, com.android.volley.Response.Listener{response ->
            try{
                for (x in 0 until response.length()){
                    //Loop through the array of JSON objects of currently existing channels
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val channelDesc = channel.getString("description")
                    val channelId = channel.getString("_id")

                    val newChannel = Channel(name, channelDesc, channelId)
                    this.channels.add(newChannel)
                }
                complete(true)
            } catch(e:JSONException){
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }
        }, com.android.volley.Response.ErrorListener {error ->
            Log.d("ERROR", "Could not retrieve channels")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${AuthService.authToken}")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(channelsRequest)
    }
}
