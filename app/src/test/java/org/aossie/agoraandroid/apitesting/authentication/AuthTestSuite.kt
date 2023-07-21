package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
  ChangePasswordTest::class,
  FacebookLoginTest::class,
  ForgotPasswordTest::class,
  LogInTest::class,
  RefreshAccessTokenTest::class,
  ResendOtpTest::class,
  SignUpTest::class,
  VerifyOtpTest::class,
  ToggleTwoFactorAuthTest::class,
  VerifyOtpTest::class
)
class AuthTestSuite
