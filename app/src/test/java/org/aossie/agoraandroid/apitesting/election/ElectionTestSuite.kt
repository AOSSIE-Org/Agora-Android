package org.aossie.agoraandroid.apitesting.election

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
  CastVoteTest::class,
  CreateElectionTest::class,
  DeleteElectionTest::class,
  ElectionResultTest::class,
  GetAllElectionsTest::class,
  GetBallotTest::class,
  GetVotersTest::class,
  InviteVotersTest::class,
  VerifyVoterTest::class
)
class ElectionTestSuite
