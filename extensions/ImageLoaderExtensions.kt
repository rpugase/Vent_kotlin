package org.zapomni.venturers.extensions

import android.net.Uri
import com.facebook.common.util.UriUtil
import com.facebook.drawee.view.SimpleDraweeView
import org.zapomni.venturers.BuildConfig

fun String.getPhotoUrlById() = if (startsWith("http")) this else "${BuildConfig.BASE_URL}api/file/get?file=$this"

fun SimpleDraweeView.loadImageFromAssets(id: String?) {
    setImageURI(Uri.Builder()
            .scheme(UriUtil.LOCAL_ASSET_SCHEME)
            .path("chat_backgrounds/$id")
            .build(), this)
}

fun SimpleDraweeView.loadImage(filePath: Uri) {
    setImageURI(filePath, this)
}

fun SimpleDraweeView.loadImage(url: String?) {
    setImageURI(url, this)
}