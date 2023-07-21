package org.aossie.agoraandroid.apitesting.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
  ChangeAvatarTest::class,
  LogOutTest::class,
  GetUserTest::class,
  UpdateUserTest::class
)
class UserTestSuite
