package com.vdevtor.schedulemeapp.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vdevtor.data.repository.UserRepositoryImp
import com.vdevtor.domain.repository.UserRepository
import com.vdevtor.database.UserDataBase
import com.vdevtor.database.UserDataBase.Companion.DATABASE_USER
import com.vdevtor.schedulemeapp.core.AuthManager
import com.vdevtor.schedulemeapp.core.UploadFileToFB
import com.vdevtor.schedulemeapp.feature_login.data.repository.AuthGoogleSignImp
import com.vdevtor.schedulemeapp.feature_login.data.repository.AuthRepositoryImp
import com.vdevtor.schedulemeapp.feature_login.domain.model.ProvideAccountArray
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthGoogleSign
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthRepository
import com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth.*
import com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth.AuthUseCases
import com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth.LoginAnonymously
import com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth.RegisterWithEmailPassWord
import org.koin.dsl.module

val repositoriesModule = module {
    //Repositories
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single<AuthRepository> { AuthRepositoryImp(get(), get(), get(),get()) }
    single<AuthGoogleSign> { AuthGoogleSignImp(get(), get()) }
    single <UserRepository>{ UserRepositoryImp(get()) }
}

val useCasesModule = module{
    //UseCases

    //login feature
    factory { AuthUseCases(get(),get(),get(),get(),get(),get()) }
    single { AuthManager(get(), get()) }
    factory { LoginAnonymously(get()) }
    factory { RegisterWithEmailPassWord(get()) }
    factory { LoginWithGoogle(get()) }
    factory { BuildGoogleClient(get()) }
    single { ProvideAccountArray(get()) }
    factory { LoginWithEmail(get()) }
    factory { RegisterValidationManager() }
    factory { UploadFileToFB(get(),get(),get()) }
}

val appDataBaseModule = module {

    //Dao's
    single { get<UserDataBase>().userDao }


    //DB Instance

    single {
        Room.databaseBuilder(
            get(),
            UserDataBase::class.java,
            DATABASE_USER
        ).allowMainThreadQueries().fallbackToDestructiveMigration()
            .build()
    }
}