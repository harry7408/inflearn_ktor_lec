package com.example.config


import com.example.domain.CafeMenuTable
import com.example.domain.CafeOrderTable
import com.example.domain.CafeUserTable
import com.example.domain.model.CafeOrder
import com.example.shared.CafeOrderStatus
import com.example.shared.dummyMenuQueryList
import com.example.shared.dummyUserQueryList
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.h2.tools.Server
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random


fun Application.configureDatabase() {
    configureH2()
    connectDatabase()
    initData()
}


// H2 데이터베이스 동작 설정 (실행 및 종료)
fun Application.configureH2() {
    val h2server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "8001")

    environment.monitor.subscribe(ApplicationStarted) { application ->
        h2server.start()
        application.environment.log.info("H2 Server Started. ${h2server.url}")
    }

    environment.monitor.subscribe(ApplicationStopped) { application ->
        h2server.stop()
        application.environment.log.info("H2 Server Stopped. ${h2server.url}")
    }
}

// 데이터베이스 설정 (H2 Database)
private fun connectDatabase() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:h2:mem:cafedb"
        driverClassName = "org.h2.Driver"
        validate()
    }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
}

private fun initData() {
    transaction {
        // Jpa 는 SQL Log 출력을 하려면 설정이 번거롭지만 Exposed 는 매우 간편
        addLogger(StdOutSqlLogger)

        // 테이블 생성 (원하는 테이블을 create 의 인자에 담는다)
        SchemaUtils.create(
            CafeMenuTable,
            CafeUserTable,
            CafeOrderTable
        )

        execInBatch(dummyUserQueryList)
        // 배치 Insert 작업
        execInBatch(dummyMenuQueryList)
        batchInsertOrder()
    }
}

// 랜덤하게 300개의 주문을 만들어내는 작업
private fun batchInsertOrder(): List<ResultRow> {
    // 테이블에서 데이터 다 가져오기
    val menuPairs = CafeMenuTable.selectAll()
        .toList()
        .map { it[CafeMenuTable.id].value to it[CafeMenuTable.price] }

    val iterator =
        (1..300).map { id ->
            val (menuId, price) = menuPairs.random()

            CafeOrder(
                orderCode = "OC${UUID.randomUUID()}",
                cafeMenuId = menuId,
                cafeUserId = 1L,
                price = price,
                status = CafeOrderStatus.READY,
                orderedAt = LocalDateTime.now().minusDays(Random.nextLong(10))

            )
        }

    return CafeOrderTable.batchInsert(
        iterator,
        shouldReturnGeneratedValues = false,
        body = {
            this[CafeOrderTable.orderCode] = it.orderCode
            this[CafeOrderTable.cafeMenuId] = it.cafeMenuId
            this[CafeOrderTable.cafeUserId] = it.cafeUserId
            this[CafeOrderTable.price] = it.price
            this[CafeOrderTable.status] = it.status
            this[CafeOrderTable.orderedAt] = it.orderedAt
        }
    )
}