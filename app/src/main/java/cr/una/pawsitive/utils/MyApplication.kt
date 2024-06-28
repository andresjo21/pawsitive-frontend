package cr.una.pawsitive.utils

import android.app.Application

class MyApplication : Application() {
    var isAdmin: Boolean = false

    override fun onCreate() {
        super.onCreate()
        createSessionManager(SessionManager(applicationContext))
    }

    companion object {
        var sessionManager: SessionManager? = null

        fun createSessionManager(newInstance: SessionManager){
            sessionManager = newInstance
        }
    }
}