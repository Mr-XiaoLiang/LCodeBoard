package com.lollipop.codeboard

import android.inputmethodservice.InputMethodService
import android.view.View

class IMEService: InputMethodService() {

    override fun onCreateInputView(): View? {
        currentInputBinding.connection
        return super.onCreateInputView()
    }

}