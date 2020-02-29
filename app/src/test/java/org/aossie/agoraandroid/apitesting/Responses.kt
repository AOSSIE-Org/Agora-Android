package org.aossie.agoraandroid.apitesting

object Responses {

  const val AUTH_FORGOT_PASSWORD = "{\n" +
      "  \"message\": \"string\"\n" +
      "}"

  const val AUTH_LOGIN = "{\n" +
      "  \"username\": \"string\",\n" +
      "  \"email\": \"string\",\n" +
      "  \"firstName\": \"string\",\n" +
      "  \"lastName\": \"string\",\n" +
      "  \"avatarURL\": \"string\",\n" +
      "  \"twoFactorAuthentication\": true,\n" +
      "  \"token\": {\n" +
      "    \"token\": \"string\",\n" +
      "    \"expiresOn\": \"2020-02-09T04:36:46.001Z\"\n" +
      "  },\n" +
      "  \"trustedDevice\": \"string\"\n" +
      "}"

  const val AUTH_FACEBOOK_LOGIN="{\n" +
    "  \"token\": \"string\",\n" +
    "  \"expiresOn\": \"2020-02-28T14:04:49.337Z\"\n" +
    "}"

  const val AUTH_SIGNUP = "{\n" +
      "  \"token\": \"string\",\n" +
      "  \"expiresOn\": \"2020-02-09T07:06:42.197Z\"\n" +
      "}"

  const val ELECTION_CREATE = "{\n" +
      "  \"message\": \"string\"\n" +
      "}"

  const val INVITE_VOTERS = "{\n" +
      "  \"message\": \"string\"\n" +
      "}"
}