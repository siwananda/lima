package com.byteme.lima.service

import com.byteme.lima.domain.User
import org.springframework.stereotype.Service

@Service
class UserService extends AbstractService {
    List<User> findAll() {
        [
                new User(
                        id: 1,
                        code: "code-001",
                        name: "name-001"
                ),
                new User(
                        id: 2,
                        code: "code-002",
                        name: "name-002"
                )
        ]
    }
}
