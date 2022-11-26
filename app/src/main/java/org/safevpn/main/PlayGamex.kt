package org.safevpn.main

import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.onesignal.OneSignal
import java.io.IOException
import java.lang.Error

var sub1:String? = null
var sub2:String? = null
var sub3:String? = null
var sub4:String? = null
var sub5:String? = null
var flowkey:String? = null
var af_status:String? = null
var af_id:String? = null
var ad:String? = null
var appsflyer_key= "WwSSmZfzaG5e9r2TEFfsUR"


class PlayGamex:Application() {
    override fun onCreate() {
        super.onCreate()
        //FirebaseAnalytics.getInstance(this)
        //FirebaseAuth.getInstance()
        init()


        MobileAds.initialize(this) {}
        Thread{
            try {
                val info = AdvertisingIdClient.getAdvertisingIdInfo(this)
                ad = info.id
                Log.e("ad", "$ad")
            } catch (exception: IOException) {
            } catch (exception: GooglePlayServicesRepairableException) {
            } catch (exception: GooglePlayServicesNotAvailableException) {
            }
        }.start()

        af_id = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
        Log.e("af_id", "$af_id")

        val conversionListener  = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                Log.d("TAG","SUCCESS CONV")
                p0?.let { cvData ->
                    cvData.map {
                        Log.e("APS", "conversion_attribute:  ${it.key} = ${it.value}")
                    }

                    af_status = cvData.getValue("af_status").toString()
                    if (af_status == "Non-organic"){
                        val campaign = cvData.getValue("campaign").toString().split("_")
                        Log.e("campaign", "$campaign")
                        try {sub1 = campaign[0]} catch (e: Error){sub1 = ""}
                        try {sub2 = campaign[1]} catch (e: Error){sub2 = ""}
                        try {sub3 = campaign[2]} catch (e: Error){sub3 = ""}
                        try {sub4 = campaign[3]} catch (e: Error){sub4 = ""}
                        try {sub5 = campaign[4]} catch (e: Error){sub5 = ""}
                        flowkey = cvData.getValue("flowkey").toString()
                    }
                    Log.d("TAG", af_status+" STATUS")
                }
            }
            override fun onConversionDataFail(p0: String?) {
                Log.d("TAG","CONV FAIL "+p0)
            }
            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                Log.d("TAG","ATTR")
            }
            override fun onAttributionFailure(p0: String?) {
                Log.d("TAG", "ATTR FAIL")
            }
        }

        AppsFlyerLib.getInstance().run {
            init(appsflyer_key, conversionListener, this@PlayGamex)
            start(this@PlayGamex)
        }
    }
    private fun init(){
        AppsFlyerLib.getInstance()
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("03a508ee-a78e-4551-9683-93f6de5096d3")

    }
}