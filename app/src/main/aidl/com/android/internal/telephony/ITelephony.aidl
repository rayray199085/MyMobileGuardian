// ITelephony.aidl
package com.android.internal.telephony;

// Declare any non-default types here with import statements
import android.os.Bundle;
    interface ITelephony {
        boolean endCall();
     void dial(String number);
    void answerRingingCall();
    }

