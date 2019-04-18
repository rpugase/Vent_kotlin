package org.zapomni.venturers

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.facebook.stetho.Stetho
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(mainModule, dataModule, domainModule, presentationModule))
        Stetho.initializeWithDefaults(this)
        Fresco.initialize(this, ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true)
                .build())
//        if (!BuildConfig.DEBUG) {
//            Fabric.Builder(this)
//                    .kits(Crashlytics())
//                    .build()
//        }
    }
}