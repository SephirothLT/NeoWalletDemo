package newandroid.zhongtuobang.com.neoandroiddemo.activity.send

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import newandroid.zhongtuobang.com.neoandroiddemo.R
import newandroid.zhongtuobang.com.neoandroiddemo.api.AccountAsset

/**
 * Created by drei on 1/18/18.
 */

class AssetSelectorAdapter(context: Context, fragment: AssetSelectionBottomSheet,
                           assets: ArrayList<AccountAsset>): BaseAdapter() {
    private val mContext: Context
    private val mFragment: AssetSelectionBottomSheet
    private val mAssets: ArrayList<AccountAsset>
    private val inflator: LayoutInflater

    init {
        mContext = context
        mFragment = fragment
        mAssets = assets
        inflator = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return mAssets.count()  + 2
    }

    override fun getItem(position: Int): AccountAsset? {
        return when(position) {
            0 ->  null
            1,2 -> mAssets[position - 1]
            3 -> null
            else -> mAssets[position - 2]
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)
        when (position) {
            0 -> {
                val view = inflator.inflate(R.layout.wallet_send_fragment_asset_row_header, parent, false)
                view.findViewById<TextView>(R.id.headerTextView).text = mContext.resources.getString(R.string.native_assets_header)
                return view
            }
            3 -> {
                val view = inflator.inflate(R.layout.wallet_send_fragment_asset_row_header, parent, false)
                view.findViewById<TextView>(R.id.headerTextView).text = mContext.resources.getString(R.string.token_assets_header)
                return view
            }
            1, 2 -> {
                val view = inflator.inflate(R.layout.wallet_send_fragment_native_asset_row, parent, false)
                view.findViewById<TextView>(R.id.nativeAssetName).text = item!!.name
                view.findViewById<TextView>(R.id.assetAmountTextView).text = item!!.value.toString()
                setListenerForRow(view, item.assetID, true, item.symbol)
                return view
            } else -> {
                val view = inflator.inflate(R.layout.wallet_send_fragment_token_row, parent, false)
                view.findViewById<TextView>(R.id.assetShortNameTextView).text = item!!.symbol
                view.findViewById<TextView>(R.id.assetLongNameTextView).text = item!!.name
                view.findViewById<TextView>(R.id.assetAmountTextView).text = item!!.value.toString()
                setListenerForRow(view, item.assetID, false, item.symbol)
                return view
            }
        }
    }

     private fun setListenerForRow(view: View, assetID: String, isNativeAsset: Boolean, shortName: String) {
        view.setOnClickListener {
            val sendScreen = mFragment.activity as SendActivity
            sendScreen.isNativeAsset = isNativeAsset
            sendScreen.assetID = assetID
            sendScreen.shortName = shortName
            sendScreen.updateSelectedAsset()
            mFragment.dismiss()
        }
    }
}