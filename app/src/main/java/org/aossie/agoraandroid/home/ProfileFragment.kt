package org.aossie.agoraandroid.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Types.NULL

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private var mNewPass: TextInputLayout? = null
    private var mConfirmPass: TextInputLayout? = null
    private var loadToast: LoadToast? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val sharedPrefs = SharedPrefs(activity)
        val view = inflater.inflate(R.layout.fragment_profile, null)
        val userName = view.findViewById<TextView>(R.id.text_user_name)
        val emailId = view.findViewById<TextView>(R.id.text_email_id)
        val fullName = view.findViewById<TextView>(R.id.text_full_name)
        val token = sharedPrefs.token
        val mChangePassButton = view.findViewById<Button>(R.id.button_change_password)

        mNewPass = view.findViewById(R.id.textInputLayout_new_password)
        mConfirmPass = view.findViewById(R.id.textInputLayout_confirm_password)

        mChangePassButton.setOnClickListener {
            val newPass: String = mNewPass!!.getEditText()!!.text.toString().trim { it <= ' ' }
            val confirmPass = mConfirmPass!!.getEditText()!!.text.toString().trim { it <= ' ' }
            if(newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(context,"Enter the password field correctly",Toast.LENGTH_LONG).show()
            }
            else{
                if (newPass == confirmPass) {
                    if (newPass == sharedPrefs.pass) {
                        mNewPass!!.setError("New Password should not be same as old one")
                        mConfirmPass!!.getEditText()!!.setText("")
                        mConfirmPass!!.setErrorEnabled(false)
                    } else doChangePasswordRequest(confirmPass, token)
                } else {
                    mConfirmPass!!.setError("Password Does Not Matches")
                    mNewPass!!.setErrorEnabled(false)
                }
            }
        }
        val username = sharedPrefs.userName
        val email = sharedPrefs.email
        val userFullName = sharedPrefs.getfullName()
        userName.text = username
        emailId.text = email
        fullName.text = userFullName
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun doChangePasswordRequest(password: String, token: String) {
        loadToast = LoadToast(activity)
        loadToast!!.setText("Changing Password")
        loadToast!!.show()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val apiService = RetrofitClient.getAPIService()
        val changePassResponse = apiService.changePassword(jsonObject.toString(), token)
        changePassResponse.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.message() == "OK") {
                    loadToast!!.success()
                    Toast.makeText(activity, "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("TAG", "onResponse:" + response.body())
                    loadToast!!.error()
                    Toast.makeText(activity, "Wrong User Name or Password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                loadToast!!.error()
                Toast.makeText(activity, "Something went wrong please try again", Toast.LENGTH_SHORT).show()
            }
        })
    }
}