package org.zapomni.venturers

import android.content.Context
import android.preference.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import id.zelory.compressor.Compressor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import org.zapomni.venturers.data.repository.*
import org.zapomni.venturers.domain.*
import org.zapomni.venturers.domain.interceptor.AccessAuthenticator
import org.zapomni.venturers.domain.interceptor.RequestInterceptor
import org.zapomni.venturers.domain.mapper.ChatListMapper
import org.zapomni.venturers.domain.mapper.CuratorMapper
import org.zapomni.venturers.domain.mapper.MeetingListMapper
import org.zapomni.venturers.domain.mapper.MessageFireMapper
import org.zapomni.venturers.extensions.log
import org.zapomni.venturers.presentation.auth.AuthPresenter
import org.zapomni.venturers.presentation.auth.first.AuthFirstPresenter
import org.zapomni.venturers.presentation.auth.second.AuthSecondPresenter
import org.zapomni.venturers.presentation.auth.third.AuthThirdPresenter
import org.zapomni.venturers.presentation.chat.ChatPresenter
import org.zapomni.venturers.presentation.chat.meet.list.MeetsPresenter
import org.zapomni.venturers.presentation.chat.meet.newm.NewMeetPresenter
import org.zapomni.venturers.presentation.chat.topic.background.BackgroundTopicPresenter
import org.zapomni.venturers.presentation.chat.topic.main.TopicPresenter
import org.zapomni.venturers.presentation.curators.CuratorsPresenter
import org.zapomni.venturers.presentation.event.EventPresenter
import org.zapomni.venturers.presentation.event.list.EventListPresenter
import org.zapomni.venturers.presentation.home.HomePresenter
import org.zapomni.venturers.presentation.main.MainPresenter
import org.zapomni.venturers.presentation.profile.ProfilePresenter
import org.zapomni.venturers.presentation.profile.edit.ProfileEditPresenter
import org.zapomni.venturers.presentation.splash.SplashPresenter
import org.zapomni.venturers.presentation.trip.TripPresenter
import org.zapomni.venturers.presentation.trip.feedback.TripFeedbackPresenter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val mainModule = applicationContext {
    bean { provideGson() }
    bean { provideHttpLoggingInterceptor() }
    bean { provideOkHttpClient(get(), get(), get()) }
    bean { PreferenceManager.getDefaultSharedPreferences(get()) }
    bean {
        FirebaseFirestore.getInstance()
//            .apply { firestoreSettings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build() }
    }
    bean { FirebaseRepository(get()) }
    bean { getAssetsManager(get()) }
    bean { AssetsRepository(get()) }
    bean { Compressor(get()) }
}

val dataModule = applicationContext {
    bean { provideAuthRepository(get(), get()) }
    bean { provideUserRepository(get(), get()) }
    bean { provideFileRepository(get(), get()) }
    bean { provideChatRepository(get(), get()) }
    bean { provideEventRepository(get(), get()) }
    bean { provideTripRepository(get(), get()) }
    bean { PreferencesRepository(get(), get()) }

    bean { AccessAuthenticator(get(), get()) }
    bean { RequestInterceptor(get()) }
}

val domainModule = applicationContext {
    bean { UserInteractor(get(), get(), get(), get(), get(), get()) }
    bean { HomeInteractor() }
    bean { ChatInteractor(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    bean { EventInteractor(get()) }
    bean { TripInteractor(get(), get(), get()) }

    bean { CuratorMapper() }
    bean { MessageFireMapper(get()) }
    bean { ChatListMapper(get(), get()) }
    bean { MeetingListMapper() }
}

val presentationModule = applicationContext {
    factory { MainPresenter(get()) }
    factory { SplashPresenter(get(), get()) }
    factory { CuratorsPresenter(get()) }

    factory { AuthPresenter(get()) }
    factory { AuthFirstPresenter(get()) }
    factory { AuthSecondPresenter(get()) }
    factory { AuthThirdPresenter(get()) }

    factory { HomePresenter(get(), get(), get(), get()) }
    factory { TripPresenter(get()) }
    factory { TripFeedbackPresenter(get()) }

    factory { ProfilePresenter(get()) }
    factory { ProfileEditPresenter(get()) }

    factory { ChatPresenter(get(), get()) }
    factory { NewMeetPresenter() }
    factory { MeetsPresenter(get()) }
    factory { TopicPresenter(get()) }
    factory { BackgroundTopicPresenter(get()) }

    factory { EventListPresenter(get()) }
    factory { EventPresenter(get()) }
}

private fun getAssetsManager(context: Context) = context.assets

private fun provideGson() = GsonBuilder().create()

private fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor(HttpLoggingInterceptor
        .Logger { log(it, "network.log") }).setLevel(HttpLoggingInterceptor.Level.BODY)

private fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor,
                                accessAuthenticator: AccessAuthenticator,
                                requestInterceptor: RequestInterceptor) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(requestInterceptor)
        .authenticator(accessAuthenticator)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

private fun provideUserRepository(gson: Gson, okHttpClient: OkHttpClient) =
        Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(UserRepository::class.java)

private fun provideChatRepository(gson: Gson, okHttpClient: OkHttpClient) =
        Retrofit.Builder()
                .baseUrl("${BuildConfig.BASE_URL}api/chat/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(ChatRepository::class.java)

private fun provideAuthRepository(gson: Gson, httpLoggingInterceptor: HttpLoggingInterceptor) =
        Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(OkHttpClient.Builder()
                        .addInterceptor(httpLoggingInterceptor)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(AuthRepository::class.java)

private fun provideFileRepository(gson: Gson, okHttpClient: OkHttpClient) =
        Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(FileRepository::class.java)

private fun provideEventRepository(gson: Gson, okHttpClient: OkHttpClient) =
        Retrofit.Builder()
                .baseUrl("${BuildConfig.BASE_URL}api/event/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(EventRepository::class.java)

private fun provideTripRepository(gson: Gson, okHttpClient: OkHttpClient) =
        Retrofit.Builder()
                .baseUrl("${BuildConfig.BASE_URL}api/adventure/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(TripRepository::class.java)