package newandroid.zhongtuobang.com.neoandroiddemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

import newandroid.zhongtuobang.com.neoandroiddemo.Account;
import newandroid.zhongtuobang.com.neoandroiddemo.R;

/**
 * Created by litao on 2018/1/24.
 */

public class CreateWalletActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startButton;
    private TextView mAddress;
    private TextView privateKey;
    private ImageView mQRcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_activity_create_wallet);
        startButton = findViewById(R.id.StartButton);
        mAddress = findViewById(R.id.addressTextView);
        privateKey = findViewById(R.id.wifTextView);
        mQRcode = findViewById(R.id.qrView);

        startButton.setOnClickListener(this);

        initTextView();

    }

    private void initTextView() {
        mAddress.setText(Account.INSTANCE.getWallet().getAddress());
        privateKey.setText(Account.INSTANCE.getWallet().getWIF());
        Bitmap bitmap = QRCode.from(Account.INSTANCE.getWallet().getWIF()).withSize(1000, 1000).bitmap();
        mQRcode.setImageBitmap(bitmap);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.StartButton:
                break;
        }
    }
}
