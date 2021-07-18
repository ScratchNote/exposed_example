package kr.socar.code101.codebook.service

import kr.socar.code101.codebook.AbstractServiceTest
import kr.socar.code101.codebook.dto.ApiEmptyResponse
import kr.socar.code101.codebook.dto.CreateComCodeInfoParams
import kr.socar.code101.codebook.infra.ComCodeInfoTable
import kr.socar.code101.codebook.repository.ComCodeInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ComCodeInfoServiceTest : AbstractServiceTest() {
    @Autowired
    lateinit var comCodeInfoService: ComCodeInfoService

    @Autowired
    lateinit var comCodeInfoRepository: ComCodeInfoRepository

    @Test
    @DisplayName("createComCodeInfo - description 이 null - 성공")
    fun createComCodeInfoParamsWithDescriptionIsNull() {
        val codeName = "테스트"
        val createComCodeInfoParamsWithDescriptionIsNull = CreateComCodeInfoParams(codeName = codeName)
        val result = comCodeInfoService.createNew(createComCodeInfoParamsWithDescriptionIsNull)
        assertThat(result).isEqualTo(ApiEmptyResponse())
        val dbResult = comCodeInfoRepository.findByName(codeName)
        assertThat(dbResult).isNotNull
        assertThat(dbResult!!.codeName).isEqualTo(codeName)
        assertThat(dbResult.description).isNull()
    }

    @Test
    @DisplayName("createComCodeInfo - description 이 존재 - 성공")
    fun createComCodeInfoParamsWithDescriptionExist() {
        val codeName = "테스트"
        val description = "테스트 설명"
        val createComCodeInfoParamsWithDescriptionExist = CreateComCodeInfoParams(codeName = codeName, description = description)
        val result = comCodeInfoService.createNew(createComCodeInfoParamsWithDescriptionExist)
        assertThat(result).isEqualTo(ApiEmptyResponse())
        val dbResult = comCodeInfoRepository.findByName(codeName)
        assertThat(dbResult).isNotNull
        assertThat(dbResult!!.codeName).isEqualTo(codeName)
        assertThat(dbResult.description).isEqualTo(description)
    }

    @Test
    @DisplayName("getComCodeInfo - 성공")
    fun getComCodeInfoTest() {
        val codeName = "테스트"
        val createComCodeInfoParams = CreateComCodeInfoParams(codeName = codeName)
        comCodeInfoService.createNew(createComCodeInfoParams)

        val result = comCodeInfoService.findByName(codeName)
        assertThat(result).isNotNull
        assertThat(result!!.codeName).isEqualTo(codeName)
    }

    @Test
    @DisplayName("getComCodeInfo - 존재 하지 않아서 null 리턴")
    fun getComCodeInfoReturnNull() {
        val codeName = "테스트"
        val createComCodeInfoParams = CreateComCodeInfoParams(codeName = codeName)
        comCodeInfoService.createNew(createComCodeInfoParams)

        val newCodeName = "새로운 테스트"
        val result = comCodeInfoService.findByName(newCodeName)
        assertThat(result).isNull()
    }

    @AfterEach
    fun cleanUp() {
        transaction(database) {
            ComCodeInfoTable.deleteAll()
        }
    }
}
