package network.o3.o3wallet.Wallet

import android.content.Context
import android.widget.Toast


/**
 * Created by apisit on 12/9/17.
 */
fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun Context.toastUntilCancel(message: String): Toast {
  return Toast.makeText(this, message, Int.MAX_VALUE)
}