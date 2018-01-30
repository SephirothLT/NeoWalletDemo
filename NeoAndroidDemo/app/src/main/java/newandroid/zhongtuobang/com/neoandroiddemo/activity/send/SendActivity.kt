package newandroid.zhongtuobang.com.neoandroiddemo.activity.send

import android.app.Activity
import android.app.Fragment
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.zxing.integration.android.IntentIntegrator
import network.o3.o3wallet.Wallet.toastUntilCancel
import newandroid.zhongtuobang.com.neoandroiddemo.Account
import newandroid.zhongtuobang.com.neoandroiddemo.PersistentStore
import newandroid.zhongtuobang.com.neoandroiddemo.R
import newandroid.zhongtuobang.com.neoandroiddemo.api.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class SendActivity : AppCompatActivity() {

    lateinit var addressTextView: TextView
    lateinit var amountTextView: TextView
    lateinit var noteTextView: TextView
    lateinit var selectedAssetTextView: TextView
    lateinit var sendButton: Button
    lateinit var pasteAddressButton: Button
    lateinit var scanAddressButton: Button
    lateinit var view: View
    lateinit var currentAccountState: AccountState
    lateinit var neoBalance: Balance
    lateinit var gasBalance: Balance
    lateinit var assetListView: ListView
    var assets: ArrayList<AccountAsset> = arrayListOf<AccountAsset>()

    var isNativeAsset = true
    //if native asset this refers to assetid, otherwise tokenhash
    var assetID = NeoNodeRPC.Asset.NEO.assetID()
    var shortName = "NEO"

    public val ARG_REVEAL_SETTINGS: String = "arg_reveal_settings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_activity_send)

        this.title = resources.getString(R.string.send)
        view = findViewById<View>(R.id.root_layout)
        addressTextView = findViewById<EditText>(R.id.addressTextView)
        amountTextView = findViewById<EditText>(R.id.amountTextView)
        noteTextView = findViewById<EditText>(R.id.noteTextView)
        sendButton = findViewById<Button>(R.id.sendButton)
        pasteAddressButton = findViewById<Button>(R.id.pasteAddressButton)
        scanAddressButton = findViewById<Button>(R.id.scanAddressButton)
        selectedAssetTextView = findViewById<TextView>(R.id.selectedAssetTextView)
//        assetListView = view.findViewById<ListView>(R.id.assetListView)
        amountTextView.keyListener = DigitsKeyListener.getInstance("0123456789")
        selectedAssetTextView.text = shortName.toUpperCase()
        addressTextView.afterTextChanged { checkEnableSendButton() }
        amountTextView.afterTextChanged { checkEnableSendButton() }
        selectedAssetTextView.setOnClickListener { chooseAssert() /*displayAssets()*/ }

        sendButton.isEnabled = false
        sendButton.setOnClickListener { sendTapped() }

        pasteAddressButton.setOnClickListener { pasteAddressTapped() }
        scanAddressButton.setOnClickListener { scanAddressTapped() }

        val extras = intent.extras
        val address = extras.getString("address")

        if (address != "") {
            addressTextView.text = address
            amountTextView.requestFocus()
            showFoundContact(address)
        }
    }

    fun showFoundContact(address: String) {
        val contacts = PersistentStore.getContacts()
        val foundContact = contacts.find { it.address == address }
        val toLabel = findViewById<TextView>(R.id.sendToLabel)
        if (foundContact != null) {
            toLabel.text = "To: %s".format(foundContact.nickname)
        } else {
            toLabel.text = "To"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.home) {
            NavUtils.navigateUpFromSameTask(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun checkEnableSendButton() {
        showFoundContact(addressTextView.text.trim().toString())
        sendButton.isEnabled = (addressTextView.text.trim().count() > 0 && amountTextView.text.count() > 0)
    }

    private fun chooseAssert() {
        isNativeAsset = true     //全部置为源生，暂不考虑NEP-5
        alert("Choose NEO/GAS") {
            positiveButton("NEO") {
                shortName = "NEO"
                selectedAssetTextView.text = shortName.toUpperCase()
                assetID = NeoNodeRPC.Asset.NEO.assetID()
            }
            negativeButton("GAS"){
                shortName = "GAS"
                assetID = NeoNodeRPC.Asset.GAS.assetID()
                selectedAssetTextView.text = shortName.toUpperCase()
            }
            neutralPressed("Cancel"){

            }

        }.show()

    }

    private fun displayAssets() {
        val assetSelectorSheet = AssetSelectionBottomSheet()
        assets = initData()
//        val assets = intent.extras.getSerializable("assets")

        assetSelectorSheet.assets = assets as ArrayList<AccountAsset>
        assetSelectorSheet.show(this.supportFragmentManager, assetSelectorSheet.tag)
    }

    private fun initData(): ArrayList<AccountAsset> {
        var list: ArrayList<AccountAsset> = arrayListOf<AccountAsset>()
//        var address = Account.getWallet()!!.address
//        async(UI) {
//            bg {
        //替换 正式环境的地址
        NeoNodeRPC(PersistentStore.getNodeURL()).getAccountState(address = Account.getWallet()!!.address) {
            //                    onUiThread {
            showAccountState(it)
//                    }
//                }
//            }
        }

        return list


    }

    fun showAccountState(it: Pair<AccountState?, Error?>): ArrayList<AccountAsset> {
        val error = it.second
        val accountState = it.first
        //此处忽略error判断
        this.currentAccountState = accountState!!
        for (balance in accountState!!.balances.iterator()) {
            if (balance.asset.contains(NeoNodeRPC.Asset.NEO.assetID())) {
                this.neoBalance = balance
//                this.enable
            } else if (balance.asset.contains(NeoNodeRPC.Asset.GAS.assetID())) {
                this.gasBalance = balance
            }
        }
        assets.clear()
        //Neo
        var neo = AccountAsset(assetID = NeoNodeRPC.Asset.NEO.assetID(),
                name = NeoNodeRPC.Asset.NEO.name,
                symbol = NeoNodeRPC.Asset.NEO.name,
                decimal = 0,
                type = AssetType.NATIVE,
                value = neoBalance.value
        )
        assets.add(neo)
        //Gas
        var gas = AccountAsset(assetID = NeoNodeRPC.Asset.GAS.assetID(),
                name = NeoNodeRPC.Asset.GAS.name,
                symbol = NeoNodeRPC.Asset.GAS.name,
                decimal = 0,
                type = AssetType.NATIVE,
                value = gasBalance.value
        )
        assets.add(gas)
        val selectedToken = PersistentStore.getSelectedNEP5Tokens()
        selectedToken.all { t ->
            var token = t.value
            var asset = AccountAsset(assetID = token.assetID,
                    name = token.name,
                    symbol = token.symbol,
                    decimal = token.decimal,
                    type = AssetType.NEP5TOKEN,
                    value = 0.0)
            assets.add(asset)
        }
//        val adapter = AccountAssetsAdapter(this,this,Account.getWallet()!!.address, assets.toTypedArray())
//
//        assetListView.adapter = adapter

        return assets


    }

    public fun addNewNEP5Token() {
//        val bottomSheet = NEP5ListFragment()
//        bottomSheet.delegate = this
//        bottomSheet.show(activity!!.supportFragmentManager, "nep5list")
    }

    fun updateSelectedAsset() {
        selectedAssetTextView.text = shortName
        if (shortName != "NEO") {
            amountTextView.keyListener = DigitsKeyListener.getInstance("0123456789.")
        } else {
            amountTextView.keyListener = DigitsKeyListener.getInstance("0123456789")
        }
    }

    private fun send() {
        //validate field
        val address = addressTextView.text.trim().toString()
        var amount = amountTextView.text.trim().toString().toDouble()

        if (amount == 0.0) {
            baseContext.toast(resources.getString(R.string.amount_must_be_nonzero))
            return
        }

        sendButton.isEnabled = false
        if (isNativeAsset) {
            sendNativeAsset(address, amount)
        } else {
            sendTokenAsset(address, amount)
        }

    }

    private fun sendNativeAsset(address: String, amount: Double) {
        val wallet = Account.getWallet()
        val toast = baseContext.toastUntilCancel(resources.getString(R.string.sending_in_progress))
        var toSendAsset: NeoNodeRPC.Asset
        if (shortName == "NEO") {
            toSendAsset = NeoNodeRPC.Asset.NEO
        } else {
            toSendAsset = NeoNodeRPC.Asset.GAS
        }

        NeoNodeRPC(PersistentStore.getNodeURL()).sendNativeAssetTransaction(wallet!!, toSendAsset, amount, address, null) {
            runOnUiThread {
                toast.cancel()
                val error = it.second
                val success = it.first
                if (success == true) {
                    baseContext!!.toast(resources.getString(R.string.sent_successfully))
                    Handler().postDelayed(Runnable {
                        finish()
                    }, 1000)
                } else {
                    this.checkEnableSendButton()
                    val message = resources.getString(R.string.send_error)
                    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    snack.setAction("Close") {
                        finish()
                    }
                    snack.show()
                }
            }
        }
    }

    private fun sendTokenAsset(address: String, amount: Double) {
        val wallet = Account.getWallet()
        val toast = baseContext.toastUntilCancel(resources.getString(R.string.sending_in_progress))

        NeoNodeRPC(PersistentStore.getNodeURL()).sendNEP5Token(wallet!!, assetID, wallet!!.address, address, amount) {
            runOnUiThread {
                toast.cancel()
                val error = it.second
                val success = it.first
                if (success == true) {
                    baseContext!!.toast(resources.getString(R.string.sent_successfully))
                    Handler().postDelayed(Runnable {
                        finish()
                    }, 1000)
                } else {
                    this.checkEnableSendButton()
                    val message = resources.getString(R.string.send_error)
                    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    snack.setAction("Close") {
                        finish()
                    }
                    snack.show()
                }
            }
        }
    }

    // 发送消息
    private fun sendTapped() {
        this.hideKeyboard()
        //validate field
        val address = addressTextView.text.trim().toString()
        var amount = amountTextView.text.trim().toString().toDouble()

        if (amount == 0.0) {
            baseContext.toast(resources.getString(R.string.amount_must_be_nonzero))
            return
        }

        NeoNodeRPC(PersistentStore.getNodeURL()).validateAddress(address) {
            if (it.second != null || it?.first == false) {
                runOnUiThread {
                    alert(resources.getString(R.string.invalid_neo_address), resources.getString(R.string.error)) {
                        yesButton {
                            addressTextView.requestFocus()
                        }
                    }.show()
                }
            } else {
                runOnUiThread {
                    alert(resources.getString(R.string.send_confirmation, amount.toString(),
                            this.shortName.toUpperCase(), address)) {
                        positiveButton(resources.getString(R.string.send)) {
                            send()
                        }
                        negativeButton(resources.getString(R.string.cancel)) {

                        }
                    }.show()
                }
            }
        }
    }

    private fun pasteAddressTapped() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        if (clip != null) {
            val item = clip.getItemAt(0)
            addressTextView.text = item.text.toString()
        }
    }


    fun scanAddressTapped() {//扫描二维码 ，需要在AndroidManifest中添加CaptureActivity的设置

        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        integrator.setPrompt(resources.getString(R.string.scan_prompt_qr_send))
        integrator.setOrientationLocked(false)
        integrator.initiateScan()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, resources.getString(R.string.cancelled), Toast.LENGTH_LONG).show()
            } else {
                addressTextView.setText(result.contents)//设置文本
            }
        }
    }
}

fun TextView.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}


fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    activity.hideKeyboard(view)
}

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}