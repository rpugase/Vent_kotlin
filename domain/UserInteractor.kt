package org.zapomni.venturers.domain

import id.zelory.compressor.Compressor
import io.reactivex.Completable
import io.reactivex.Single
import org.zapomni.venturers.data.model.BaseResponse
import org.zapomni.venturers.data.model.response.UserResponse
import org.zapomni.venturers.data.repository.AuthRepository
import org.zapomni.venturers.data.repository.FileRepository
import org.zapomni.venturers.data.repository.PreferencesRepository
import org.zapomni.venturers.data.repository.UserRepository
import org.zapomni.venturers.domain.exception.ApiException
import org.zapomni.venturers.domain.exception.IncorrectPhoneException
import org.zapomni.venturers.domain.exception.IncorrectSubmitCodeException
import org.zapomni.venturers.domain.mapper.CuratorMapper
import org.zapomni.venturers.domain.mapper.toDomainModel
import org.zapomni.venturers.domain.model.*
import org.zapomni.venturers.domain.model.navigation.AuthNavigation
import org.zapomni.venturers.domain.model.navigation.ModuleNavigation
import org.zapomni.venturers.extensions.getPhotoUrlById
import org.zapomni.venturers.extensions.processPhoneNumber
import org.zapomni.venturers.extensions.toMultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserInteractor constructor(private val authRepository: AuthRepository,
                                 private val userRepository: UserRepository,
                                 private val fileRepository: FileRepository,
                                 private val preferences: PreferencesRepository,
                                 private val curatorMapper: CuratorMapper,
                                 private val compressor: Compressor) {

    var authNavigation: AuthNavigation
        get() = preferences.authNavigation ?: AuthNavigation.PHONE
        set(value) {
            preferences.authNavigation = value
        }

    fun login(phoneNumber: String): Completable {
        return Completable.create {
            val response = authRepository.loginSync(phoneNumber.processPhoneNumber()).execute()
            val body = response.body()
            if (response.isSuccessful && body?.isSuccessful == true) {
                it.onComplete()
            } else if (response.code() == 406) {
                it.onError(IncorrectPhoneException())
            } else {
                it.onError(ApiException())
            }
        }
    }

    fun checkCode(phoneNumber: String, code: String): Single<ModuleNavigation> {
        return Single.create {
            val submitResponse = authRepository.submitLoginSync(phoneNumber.processPhoneNumber(), code).execute()
            if (submitResponse.isSuccessful) {
                val submitData = submitResponse.body()?.data

                if (submitData != null) {
                    preferences.refreshToken = submitData.refreshToken
                    val userResponse = userRepository.getUserSync().execute()
                    if (userResponse.isSuccessful) {
                        val userData = userResponse.body()?.data

                        if (userData?.name != null && userData.photo != null) {
                            saveUser(userData.toDomainModel(), localSave = true).subscribe()

                            it.onSuccess(ModuleNavigation.MAIN)
                        } else if (userData != null) {
                            saveUser(userData.toDomainModel(), localSave = true).subscribe()
                            it.onSuccess(ModuleNavigation.PROFILE)
                        } else it.onError(ApiException("Error get user"))

                    } else it.onError(ApiException("Error get user"))
                }
            } else if (submitResponse.code() == 406) {
                it.onError(IncorrectSubmitCodeException())
            } else {
                it.onError(ApiException())
            }
        }
    }

    fun saveUser(user: User?, imageFile: File? = null, localSave: Boolean = false): Completable {
        if (user == null) {
            return Completable.error(ApiException())
        }
        return Completable.create {
            var localUser = preferences.user

            if (localUser != null) {
                if (localUser.name canRewrite user.name) localUser.name = user.name
                if (localUser.surname canRewrite user.surname) localUser.surname = user.surname

            } else {
                localUser = user
            }

            var fileId: String? = null
            if (imageFile != null) {

                val fileResponse = fileRepository.uploadFileSync(compressor.compressToFile(imageFile).toMultipartBody("image/*")).execute()

                fileId = fileResponse.body()?.data
                if (fileResponse.isSuccessful && fileId != null) {
                    localUser.image = fileId.getPhotoUrlById()
                }
            }

            preferences.user = localUser
            if (!localSave)
                userRepository.saveUserSync(localUser.name, localUser.surname, fileId)
                        .enqueue(object : Callback<BaseResponse<UserResponse>> {
                            override fun onResponse(call: Call<BaseResponse<UserResponse>>?, response: Response<BaseResponse<UserResponse>>?) {}
                            override fun onFailure(call: Call<BaseResponse<UserResponse>>?, t: Throwable?) {}
                        })
            it.onComplete()
        }
    }

    fun getUser(forceApi: Boolean = false): Single<User> {
        return getUserForTest()
//        return Single.create {
//            val localUser = preferences.user
//            if (localUser != null && !forceApi) {
//                it.onSuccess(localUser)
//            } else {
//                val userResponse = userRepository.getUserSync().execute()
//                if (userResponse.isSuccessful) {
//                    var user = userResponse.body()?.data.toDomainModel()
//                    user = user!!.copy(image = user.image?.getPhotoUrlById())
//
//                    preferences.user = user
//
//                    it.onSuccess(user)
//                } else {
//                    it.onError(ApiException())
//                }
//            }
//        }
    }

    private infix fun String?.canRewrite(s: String?) = when {
        s.isNullOrBlank() -> false
        this == s -> false
        else -> true
    }

    private fun getUserForTest(): Single<User> {

//        val events = listOf(
//                Event("Идем в музей анатомии в Одессе"),
//                Event("Космической небо в обсерватории"),
//                Event("Цветение Сакур в Ужгороде"),
//                Event("В Польшу с Островским"),
//                Event("Едем на Буковель всей бандой"),
//                Event("Катаемся на лыжах на Буковеле")
//        )

        val bonuses = listOf(
                Bonus("123-456-789-123", 100),
                Bonus("123-456-789-123", 200),
                Bonus("123-456-789-123", 400)
        )

        return Single.create {
            it.onSuccess(User("+380631370489", "Andrei", "Gridin",
                    bonusCard = BonusCard(CardType.GREEN, 24, bonuses), events = mutableListOf()))
        }
    }

    fun issueWithdrawBonus(price: Int): Single<String> {
        return Single.create { it.onSuccess("123-456-789-123") }
    }

    fun getCurators(): Single<List<Curator>> {
        return userRepository.getCurators()
                .map { it.data?.map { curatorMapper.apply(it) } }
    }

    fun getCuratorsForTest(): Single<List<Curator>> {
        val conductors = listOf(
                Curator("Алексей", "Иванов", 9.5f, 360, "Я родился в москве в 70 ом на краю города моча рано ударила в голову в 4 активно ругался матом", null, null),
                Curator("Витя", "Максимов", 8.5f, 250, "Я родился в москве в 70 ом на краю города моча рано ударила в голову в 4 активно ругался матом", null, null),
                Curator("Женя", "Зайцева", 8.3f, 122, "Я родился в москве в 70 ом на краю города моча рано ударила в голову в 4 активно ругался матом", null, null),
                Curator("Миша", "Козлов", 7.5f, 112, "Я родился в москве в 70 ом на краю города моча рано ударила в голову в 4 активно ругался матом", null, null),
                Curator("Леня Попков", "Попков", 7.3f, 62, "Я родился в москве в 70 ом на краю города моча рано ударила в голову в 4 активно ругался матом", null, null),
                Curator("Денис Косяков", "Косяков", 6.5f, 47, "Я родился в москве в 70 ом на краю города моча рано ударила в голову в 4 активно ругался матом", null, null),
                Curator("Стремный Тип", "Тип", 5.5f, 11, "Я родился в москве в 70 ом на краю города моча рано ударила в голову в 4 активно ругался матом", null, null)
        )

        return Single.create { it.onSuccess(conductors) }
    }
}