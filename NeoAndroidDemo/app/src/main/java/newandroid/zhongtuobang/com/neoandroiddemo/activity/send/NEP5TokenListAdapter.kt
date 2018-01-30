package newandroid.zhongtuobang.com.neoandroiddemo.activity.send

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import newandroid.zhongtuobang.com.neoandroiddemo.PersistentStore
import newandroid.zhongtuobang.com.neoandroiddemo.R
import newandroid.zhongtuobang.com.neoandroiddemo.api.NEP5Token



class NEP5TokenListAdapter(context: Context, list: ArrayList<NEP5Token>) : BaseAdapter() {
    private val list = list
    private val inflator: LayoutInflater
    private val selectedList = PersistentStore.getSelectedNEP5Tokens()

    init {
        this.inflator = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return list.count()
    }

    override fun getItem(p0: Int): NEP5Token {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val vh: NEP5TokenRow
        if (convertView == null) {
            view = this.inflator.inflate(R.layout.wallet_nep5_token_row, parent, false)
            vh = NEP5TokenRow(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as NEP5TokenRow
        }

        vh.tokenNameTextView.text = getItem(position).name
        vh.tokenSymbolTextView.text = getItem(position).symbol

        vh.checkbox.isChecked = selectedList.get(getItem(position).assetID) != null
        vh.checkbox.setOnClickListener {
            if (vh.checkbox.isChecked == true) {
                PersistentStore.addToken(getItem(position))
            } else {
                PersistentStore.removeToken(getItem(position))
            }
        }

        return view!!
    }

}

private class NEP5TokenRow(row: View?) {
    public val tokenNameTextView: TextView
    public val tokenSymbolTextView: TextView
    public val checkbox: CheckBox

    init {
        this.tokenNameTextView = row?.findViewById<TextView>(R.id.tokenNameTextView) as TextView
        this.tokenSymbolTextView = row?.findViewById<TextView>(R.id.tokenSymbolTextView) as TextView
        this.checkbox = row?.findViewById<CheckBox>(R.id.nep5TokenCheckbox) as CheckBox
    }
}