package org.aossie.agoraandroid.adapters
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

class TextWatcherAdapter(vararg editTexts:TextInputLayout) : TextWatcher {

  private var editTexts: Array<TextInputLayout>
  init {
    this.editTexts = editTexts as Array<TextInputLayout>
  }

  override fun afterTextChanged(p0: Editable?) {

    for (editText: TextInputLayout in editTexts) {
      if (!editText.editText!!.text.isNullOrEmpty()) {
        editText.error = null
      }
    }
  }

    override fun beforeTextChanged(
      p0: CharSequence?,
      p1: Int,
      p2: Int,
      p3: Int
    ) {

    }

    override fun onTextChanged(
      p0: CharSequence?,
      p1: Int,
      p2: Int,
      p3: Int
    ) {

    }

  }



