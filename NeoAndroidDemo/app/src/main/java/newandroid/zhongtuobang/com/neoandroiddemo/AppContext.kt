package newandroid.zhongtuobang.com.neoandroiddemo

import android.app.Application
import android.content.Context


class NeoAndroidDemo : Application() {
    override fun onCreate() {
        super.onCreate()
        NeoAndroidDemo.appContext = getApplicationContext()
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}