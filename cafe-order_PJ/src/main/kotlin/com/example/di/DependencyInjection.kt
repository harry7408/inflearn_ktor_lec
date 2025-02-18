package com.example.di

import com.example.domain.CafeMenuTable
import com.example.domain.CafeOrderTable
import com.example.domain.CafeUserTable
import com.example.domain.repository.CafeMenuRepository
import com.example.domain.repository.CafeOrderRepository
import com.example.domain.repository.CafeUserRepository
import com.example.service.LoginService
import com.example.service.MenuService
import com.example.service.UserService
import com.example.shared.BCryptPasswordEncoder
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    // Singleton Pattern 으로 의존성들을 사용하겠다
    single { CafeMenuRepository(CafeMenuTable) }
    single { CafeUserRepository(CafeUserTable) }
    single { CafeOrderRepository(CafeOrderTable) }
    single { BCryptPasswordEncoder()}

    // get() 을 사용하면 알아서 필요한 Type을 주입해주겠다는 것을 의미
    // CafeMenuRepository를 주입 받으므로 아래쪽에 작성해야함
    single { MenuService(get()) }
    single { UserService(get(),get())}
    single { LoginService(get()) }
}

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}