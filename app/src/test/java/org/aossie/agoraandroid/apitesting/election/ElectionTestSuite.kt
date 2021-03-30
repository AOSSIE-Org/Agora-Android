package org.aossie.agoraandroid.apitesting.election

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
  CreateElectionTest::class,
  DeleteElectionTest::class,
  ElectionResultTest::class,
  GetBallotTest::class,
  GetVotersTest::class,
  InviteVotersTest::class
)
class ElectionTestSuite
