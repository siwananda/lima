package com.byteme.lima.util

interface Constants {
    static final String APPLICATION_NAME = "Lima"

    interface Bootstrap {
        static final String USERS =
                """
[{
  "code": "USER-87195",
  "name": "Douglas Evans",
  "email": "devans0@yelp.com",
  "type": "GUEST"
}, {
  "code": "USER-46141",
  "name": "Justin Mills",
  "email": "jmills1@un.org",
  "type": "GUEST"
}, {
  "code": "USER-51216",
  "name": "Nicole Washington",
  "email": "nwashington2@vimeo.com",
  "type": "USER"
}, {
  "code": "USER-69340",
  "name": "Billy Austin",
  "email": "baustin3@oracle.com",
  "type": "USER"
}, {
  "code": "USER-74608",
  "name": "Walter Stevens",
  "email": "wstevens4@multiply.com",
  "type": "USER"
}, {
  "code": "USER-13802",
  "name": "Kathy Wright",
  "email": "kwright5@pagesperso-orange.fr",
  "type": "USER"
}, {
  "code": "USER-45681",
  "name": "Christine James",
  "email": "cjames6@virginia.edu",
  "type": "GUEST"
}, {
  "code": "USER-81972",
  "name": "Kimberly Fernandez",
  "email": "kfernandez7@vk.com",
  "type": "USER"
}, {
  "code": "USER-91113",
  "name": "Peter Wallace",
  "email": "pwallace8@state.tx.us",
  "type": "GUEST"
}, {
  "code": "USER-57350",
  "name": "Judy Stevens",
  "email": "jstevens9@behance.net",
  "type": "GUEST"
}]
"""
    }
}
