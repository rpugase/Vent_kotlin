package org.zapomni.venturers.extensions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.tbruyelle.rxpermissions2.RxPermissions
import gun0912.tedbottompicker.TedBottomPicker
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.zapomni.venturers.App
import org.zapomni.venturers.BuildConfig
import org.zapomni.venturers.R
import java.io.File
import java.util.*


val Activity.app: App
    get() = application as App

fun <T> Single<T>.execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit = {}) {
    subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess::invoke, onError::invoke)
}

fun <T> Single<T>.executeOnUi(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit = {}) {
    observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess::invoke, onError::invoke)
}

fun log(message: String?, tag: String = "logger"): String? {
    if (BuildConfig.DEBUG) {
        Log.i(tag, message)
    }
    return message
}

fun loge(message: String, throwable: Throwable? = null, tag: String = "logger"): String {
    if (BuildConfig.DEBUG) {
        if (throwable == null) Log.e(tag, message)
        else Log.e(tag, message, throwable)
    }
    return message
}

fun String.processPhoneNumber(): String = replace(Regex("( |\\+)"), "")
fun String.toPrettyPhoneNumber(): String {
    if (length != 12) {
        return this
    }
    return "+ (" + take(3) + ") " + substring(3..4) + "-" + substring(5..7) + "-" + substring(8..9) + "-" + substring(10..11)
}

fun EditText.watchText(body: (text: String) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            body.invoke(s.toString())
        }
    }

    addTextChangedListener(textWatcher)

    return textWatcher
}

fun Activity.pickImageFromGallery(onImageSelectedListener: (Uri) -> Unit) {
    (this as AppCompatActivity).apply {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    if (it) {
                        TedBottomPicker.Builder(this)
                                .showCameraTile(false)
                                .showGalleryTile(false)
                                .showTitle(false)
                                .setOnImageSelectedListener(onImageSelectedListener::invoke)
                                .create()
                                .show(supportFragmentManager)
                    }
                }
    }
}

fun File.toMultipartBody(mimeType: String, name: String = "file") = MultipartBody.Part.createFormData(name, this.name,
        RequestBody.create(MediaType.parse(mimeType), this))


fun Int.dpToPx() = this * Resources.getSystem().displayMetrics.density.toInt()
fun Int.pxToDp() = this / Resources.getSystem().displayMetrics.density.toInt()

fun Int.spToPx() = this * Resources.getSystem().displayMetrics.scaledDensity.toInt()
fun Int.pxToSp() = this / Resources.getSystem().displayMetrics.scaledDensity.toInt()

fun Fragment.replace(fragmentManager: FragmentManager?) {
    fragmentManager?.beginTransaction()?.replace(R.id.container, this, this::class.java.name)?.commit()
}

fun Fragment.add(fragmentManager: FragmentManager?, withBackStack: Boolean = false) {
    val transaction = fragmentManager?.beginTransaction()?.add(R.id.container, this, this::class.java.name)
    if (withBackStack) {
        transaction?.addToBackStack(this::class.java.name)
    }
    transaction?.commit()
}

fun Currency.getDisplayNameShort() = when (currencyCode) {
    "UAH" -> R.string.currency_uah
    else -> R.string.currency_uah
}

fun Activity.makeCall(number: String) = RxPermissions(this)
        .request(Manifest.permission.CALL_PHONE)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            try {
                val intent = Intent(if (it) Intent.ACTION_CALL else Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }