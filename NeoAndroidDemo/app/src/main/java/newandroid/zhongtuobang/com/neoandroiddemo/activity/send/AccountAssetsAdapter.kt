package  newandroid.zhongtuobang.com.neoandroiddemo.activity.send

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import newandroid.zhongtuobang.com.neoandroiddemo.PersistentStore
import newandroid.zhongtuobang.com.neoandroiddemo.R
import newandroid.zhongtuobang.com.neoandroiddemo.api.AccountAsset
import newandroid.zhongtuobang.com.neoandroiddemo.api.AssetType
import newandroid.zhongtuobang.com.neoandroiddemo.api.NeoNodeRPC
import org.jetbrains.anko.runOnUiThread
import java.text.NumberFormat

/**
 * Created by apisit on 12/20/17.
 */
class AccountAssetsAdapter(activity: SendActivity, context: Context, address: String, assets: Array<AccountAsset>) : BaseAdapter() {

    private var arrayOfAccountAssets = assets
    private var address = address
    private var mContext = context
    private val inflator: LayoutInflater
    private val mFragment = activity


    init {
        this.inflator = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return arrayOfAccountAssets.count() + 1 //for add token button row
    }

    override fun getItem(p0: Int): AccountAsset {
        return arrayOfAccountAssets[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    private fun loadTokenBalance(position: Int,v: AccountAssetRow) {
        val asset = getItem(position)
        NeoNodeRPC(PersistentStore.getNodeURL()).getTokenBalanceOf(asset.assetID, address = address) {
            var amountInt = it.first
            var error = it.second
            if (error == null) {
                mContext.runOnUiThread {
                    val amountDecimal:Double= (amountInt!!.toDouble() / (Math.pow(10.0,asset.decimal.toDouble())))
                    arrayOfAccountAssets[position].value = amountDecimal
                    configureRow(position,v)
                }
            }
        }
    }
    fun configureRow(position: Int, vh: AccountAssetRow) {
        val asset = arrayOfAccountAssets[position]
        if (asset.assetID.contains(NeoNodeRPC.Asset.NEO.assetID())) {
            vh.assetNameTextView.text = NeoNodeRPC.Asset.NEO.name
            vh.assetAmountTextView.text = "%d".format(asset.value.toInt())
        } else if (asset.assetID.contains(NeoNodeRPC.Asset.GAS.assetID())) {
            vh.assetNameTextView.text = NeoNodeRPC.Asset.GAS.name
            vh.assetAmountTextView.text = "%.8f".format(asset.value)
        } else {
            vh.assetNameTextView.text = asset.symbol
            var formatter = NumberFormat.getNumberInstance()
            formatter.maximumFractionDigits = asset.decimal
            vh.assetAmountTextView.text = formatter.format(asset.value)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if (position != getCount() - 1) {
            val view: View?
            val vh: AccountAssetRow
            if (convertView == null) {
                view = this.inflator.inflate(R.layout.wallet_account_asset_row, parent, false)
                vh = AccountAssetRow(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as AccountAssetRow
            }

            configureRow(position,vh)

            val asset = arrayOfAccountAssets[position]
            if (asset.type == AssetType.NEP5TOKEN) {
                loadTokenBalance(position,vh)
            }
            return view!!

        } else {
            val view = this.inflator.inflate(R.layout.wallet_add_nep5_token_row, parent, false)
            view.findViewById<Button>(R.id.addNEP5TokenButton).setOnClickListener {
                mFragment.addNewNEP5Token()
            }
            return view
        }

    }

    public fun updateAdapter(assets: Array<AccountAsset>) {
        this.arrayOfAccountAssets = assets
        notifyDataSetChanged()
    }
}

public class AccountAssetRow(row: View?) {
    val assetNameTextView: TextView
    val assetAmountTextView: TextView

    init {
        this.assetNameTextView = row?.findViewById<TextView>(R.id.assetName) as TextView
        this.assetAmountTextView = row?.findViewById<TextView>(R.id.assetAmount) as TextView
    }
}