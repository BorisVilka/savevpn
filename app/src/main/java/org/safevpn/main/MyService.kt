package org.safevpn.main

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.*


class MyService : Service() {

    var binder: MyBinder = MyBinder()

    override fun onBind(intent: Intent): IBinder {
       return binder
    }
    inner class MyBinder : Binder() {
        fun getService() : MyService? {
            return this@MyService
        }
    }
    var time = 0;

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("not","not",NotificationManager.IMPORTANCE_LOW)
        channel.description = "not"
        manager.createNotificationChannel(channel)
        var notification = NotificationCompat.Builder(this, "not")
            .setChannelId("not")
            .setContentTitle("Подключено")
            . setContentText("Время подключения:")
            .setContentInfo("ВВВ")
            .setSmallIcon(R.drawable.ic_baseline_done_24)
            .setContentIntent(PendingIntent.getActivity(this,0,Intent(this,MainActivity::class.java),PendingIntent.FLAG_MUTABLE))
             .build()
        startForeground(2,notification)
        Thread {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    time++
                   notification = NotificationCompat.Builder(applicationContext, "not")
                        .setChannelId("not")
                        .setContentTitle("Подключено")
                        . setContentText("Время подключения: "+String.format("%d:%d",(time/60),time%60))
                        .setContentInfo("ВВВ")
                        .setSmallIcon(R.drawable.ic_baseline_done_24)
                       .setOngoing(true)
                       .setVibrate(null)
                       .setSound(null)
                        .setContentIntent(PendingIntent.getActivity(applicationContext,0,Intent(applicationContext,MainActivity::class.java),PendingIntent.FLAG_MUTABLE))
                        .build()
                    startForeground(2,notification)
                }
            }, 200, 1000)
        }.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
      }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }
}