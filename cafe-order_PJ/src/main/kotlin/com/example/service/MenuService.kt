package com.example.service

import com.example.domain.model.CafeMenu
import com.example.domain.repository.CafeMenuRepository

// Service Layer
class MenuService(
    private val cafeMenuRepository : CafeMenuRepository
) {
    fun findAll() : List<CafeMenu> {
        return cafeMenuRepository.findAll();
    }


    // Service 로 1번 감싸서 Not Null Type을 받아올 수 있다
    fun getMenu(id : Long) : CafeMenu {
        return cafeMenuRepository.read(id) ?: throw  IllegalArgumentException("Menu not found")
    }

    // 메뉴 수정 , 업데이트 삭제
    fun createMenu(menu : CafeMenu) : CafeMenu {
        return cafeMenuRepository.create(menu)
    }

    fun updateMenu(menu : CafeMenu) : CafeMenu {
        return cafeMenuRepository.update(menu)
    }

    fun deleteMenu(id: Long) {
        cafeMenuRepository.delete(id)
    }

}