package org.aossie.agoraandroid.apitesting.user

import org.aossie.agoraandroid.apitesting.authentication.ChangePasswordTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ChangePasswordTest::class,
    LogOutTest::class,
    GetUserTest::class,
    UpdateUserTest::class
)
class UserTestSuite