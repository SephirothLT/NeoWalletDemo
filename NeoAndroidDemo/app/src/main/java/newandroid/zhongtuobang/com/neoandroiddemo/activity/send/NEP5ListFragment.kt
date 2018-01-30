package newandroid.zhongtuobang.com.neoandroiddemo.activity.send

import android.support.design.widget.BottomSheetDialogFragment


class NEP5ListFragment() : BottomSheetDialogFragment() {

//    public var delegate: TokenListProtocol? = null
//    var nep5Model: NEP5ViewModel? = null
//    private lateinit var listView: ListView
//    override fun setupDialog(dialog: Dialog?, style: Int) {
//        super.setupDialog(dialog, style)
//        val contentView = View.inflate(context, R.layout.wallet_fragment_nep5_list,null)
//        dialog!!.setContentView(contentView)
//    }
//
//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater!!.inflate(R.layout.wallet_fragment_nep5_list, container, false)
//        listView = view.findViewById<ListView>(R.id.nep5TokenListView)
//        val nep5adapter = NEP5TokenListAdapter(context)
//        listView.adapter = nep5adapter
//        nep5Model = NEP5ViewModel()
//        nep5Model?.getNodesFromModel(refresh = true)?.observe(this,  Observer<Array<NEP5Token>> { tokens ->
//            nep5adapter.setData(tokens!!)
//        })
//        return view
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//    }
//
//    override fun onCancel(dialog: DialogInterface?) {
//        super.onCancel(dialog)
//        if (delegate != null) {
//            delegate!!.reloadTokenList()
//        }
//
//    }
}
