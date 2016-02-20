package com.byteme.lima.domain

import org.springframework.data.annotation.Id

class BaseDomain {
    @Id
    String id
    String code
    String name
}