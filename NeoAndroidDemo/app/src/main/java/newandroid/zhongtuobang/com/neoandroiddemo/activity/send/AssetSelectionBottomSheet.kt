package newandroid.zhongtuobang.com.neoandroiddemo.activity.send

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import newandroid.zhongtuobang.com.neoandroiddemo.R
import newandroid.zhongtuobang.com.neoandroiddemo.api.AccountAsset

class AssetSelectionBottomSheet : BottomSheetDialogFragment() {
    var assets: ArrayList<AccountAsset> = arrayListOf()

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.wallet_send_fragment_asset_selection_bottom_sheet, null)
        dialog.setContentView(contentView)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.wallet_send_fragment_asset_selection_bottom_sheet, container, false)
        val listView = view.findViewById<ListView>(R.id.assetListView)
        val adapter = AssetSelectorAdapter(this.context!!, this, assets)
        listView.adapter = adapter
        return view
    }

    companion object {
        fun newInstance(): AssetSelectionBottomSheet {
            return AssetSelectionBottomSheet()
        }
    }
}
