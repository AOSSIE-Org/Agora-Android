package org.aossie.agoraandroid.apitesting

object Requests {

  const val AUTH_FORGOT_PASSWORD = "iamfake"

  const val AUTH_LOGIN = "{\n" +
      "  \"identifier\": \"string\",\n" +
      "  \"password\": \"string\",\n" +
      "  \"trustedDevice\": \"string\"\n" +
      "}"

  const val AUTH_SIGNUP = "{\n" +
      "  \"identifier\": \"james.bond\",\n" +
      "  \"password\": \"this!Password!Is!Very!Very!Strong!\",\n" +
      "  \"email\": \"james.bond@test.com\",\n" +
      "  \"firstName\": \"James\",\n" +
      "  \"lastName\": \"Bond\",\n" +
      "  \"securityQuestion\": {\n" +
      "    \"crypto\": \"string\",\n" +
      "    \"question\": \"string\",\n" +
      "    \"answer\": \"string\"\n" +
      "  }\n" +
      "}"

  const val AUTH_FACEBOOK_LOGIN = "fakeToken"

  const val ELECTION_CREATE = "{\n" +
      "  \"name\": \"string\",\n" +
      "  \"description\": \"string\",\n" +
      "  \"electionType\": \"string\",\n" +
      "  \"candidates\": [\n" +
      "    \"string\"\n" +
      "  ],\n" +
      "  \"ballotVisibility\": \"string\",\n" +
      "  \"voterListVisibility\": true,\n" +
      "  \"startingDate\": \"2020-02-09T08:16:11.691Z\",\n" +
      "  \"endingDate\": \"2020-02-09T08:16:11.691Z\",\n" +
      "  \"isInvite\": true,\n" +
      "  \"isRealTime\": true,\n" +
      "  \"votingAlgo\": \"string\",\n" +
      "  \"noVacancies\": 0,\n" +
      "  \"ballot\": [\n" +
      "    {\n" +
      "      \"voteBallot\": \"string\",\n" +
      "      \"hash\": \"string\"\n" +
      "    }\n" +
      "  ]\n" +
      "}"

  const val ELECTION_DISPLAY = ""

  const val USER_CHANGE_PASSWORD = "{\n" +
      "  \"password\": \"string\"\n" +
      "}"

  const val INVITE_VOTERS = "[\n" +
      "  {\n" +
      "    \"name\": \"string\",\n" +
      "    \"hash\": \"string\"\n" +
      "  }\n" +
      "]"
}