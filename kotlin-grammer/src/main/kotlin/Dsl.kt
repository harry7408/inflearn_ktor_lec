package com.example

class JobScheduler {
    private val jobs = mutableListOf<Job>()

    // 두 번째 인자 configure 는 Job Class 의 중괄호 영역이 실행되는 형식인 것 같다
    fun job(name : String, configure : Job.()->Unit) {
        val job= Job(name).apply(configure)
        jobs.add(job)
    }

    fun listJobs() {
        jobs.forEach { println(it) }
    }
}


class Job(val name: String) {
    var description: String = ""
    private val steps = mutableListOf<Step>()

    fun step(name: String, action: () -> Unit) {
        val step = Step(name, action)
        steps.add(step)
    }

    override fun toString(): String {
        // map 을 활용해서 단계의 이름 속성만 추출하여 새로운 리스트 생성
        return "Job '$name' : $description, Steps : ${steps.map(Step::name)}"
    }
}


data class Step(
    val name: String,
    val action: () -> Unit,
)

fun jobScheduler(init: JobScheduler.() -> Unit) : JobScheduler {
    val scheduler = JobScheduler()
    scheduler.init()
    return scheduler
}

fun main() {
    val scheduler = jobScheduler {
        job("Data Backup") {
            description="Back up the database and file system"

            step("Database backup") {
                // 단계의 이름과 어떤 동작을 할 지
                println("Backing up database....")
                // TODO : 데이터 백업 로직 작성

            }

            step("File System Bakcup") {
                // TODO : 파일시스템 백업 로직 작성
                println("Backing up files...")
            }
        }

        job("System Update") {
            description= "Updates System Software"

            step("Check Disk Space") {
                // TODO : 디스크 공간 확인하는 로직 작성
                println("Checking disk space...")
            }

            step("Download Updates") {
                // TODO : 업데이트 다운로드 로직 작성
                println("Downloading updates...")

            }

            step("Apply Updates") {
                // TODO : 업데이트 적용 로직 작성
                println("Applying updates...")

            }

        }
    }
    scheduler.listJobs()
}