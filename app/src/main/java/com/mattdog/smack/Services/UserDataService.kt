package com.mattdog.smack.Services

import android.graphics.Color
import java.util.*

object UserDataService {
    var id =""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout(){
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        //Setting the variables back to blank defaults after logging out

        AuthService.authToken = ""
        AuthService.userEmail = ""
        AuthService.isLoggedIn = false
        //Setting other variables back to blank defaults after logging out
    }

    fun returnAvatarColor(components: String) : Int {
        // 0.4549019607843137 0.6980392156862745 0.6823529411764706 1

        val strippedColor = components
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
        //replaces unneeded characters in the RGB string

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)
        //Scanner can scan through a string or line of characters and select certain components (such as nextDouble)
        if (scanner.hasNext()){
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }

        return Color.rgb(r,g,b)
    }

}