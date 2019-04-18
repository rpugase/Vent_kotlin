package org.zapomni.venturers.data.repository

import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import io.reactivex.Single
import org.zapomni.venturers.domain.model.chat.BackgroundModel

class AssetsRepository(private val assets: AssetManager) {

    fun getBackgrounds(): Single<List<BackgroundModel>> {
        return Single.create {
            it.onSuccess(assets.list("chat_backgrounds").map {
                val inputStream = assets.open("chat_backgrounds/$it")
                val drawable = Drawable.createFromStream(inputStream, it)
                inputStream.close()
                BackgroundModel(it, drawable)
            })
        }
    }
}