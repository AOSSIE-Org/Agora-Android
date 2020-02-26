package org.aossie.agoraandroid.createelection

interface ElectionListener {



    fun onSuccess()

    fun onFailure(message :String)

    fun nameError(message:String)

    fun descError(message:String)

    fun startDateError(message:String)

    fun endDateError(message:String)



}