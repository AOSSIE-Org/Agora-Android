package org.aossie.agoraandroid.apitesting.authentication

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
  ChangePasswordTest::class,
  FacebookLoginTest::class,
  ForgotPasswordTest::class,
  LogInTest::class,
  SignUpTest::class
)
class AuthTestSuite
