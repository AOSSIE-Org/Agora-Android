package org.aossie.agoraandroid.apitesting.user

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    LogOutTest::class,
    GetUserTest::class,
    UpdateUserTest::class
)
class UserTestSuite