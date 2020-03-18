package org.aossie.agoraandroid.apitesting.user

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ChangePasswordTest::class,
    LogOutTest::class,
    GetUserTest::class
)
class UserTestSuite