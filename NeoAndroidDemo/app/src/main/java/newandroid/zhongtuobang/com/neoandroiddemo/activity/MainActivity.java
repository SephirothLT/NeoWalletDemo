package newandroid.zhongtuobang.com.neoandroiddemo.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import neowallet.Wallet;
import newandroid.zhongtuobang.com.neoandroiddemo.Account;
import newandroid.zhongtuobang.com.neoandroiddemo.R;
import newandroid.zhongtuobang.com.neoandroiddemo.activity.send.SendActivity;
import newandroid.zhongtuobang.com.neoandroiddemo.api.AccountAsset;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NeoDemo";
    private Button mCreateWallet;
    private Button mSendActivity;
    private Button mWalletInfo;
    private Button mRestore;
    private EditText mWifKey;

    private Wallet wallet;
    private String wifKey;//WIF 地址

    private List<AccountAsset> assets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        initOnClick();
        initData();


    }

    private void initData() {
//        wallet = Account.INSTANCE.getWallet();
//        if (wallet != null) {
//            Toast.makeText(this, wallet.getAddress(), Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wallet == null) {
            Wallet wallet = Account.INSTANCE.getWallet();
            if (wallet != null) {
                this.wallet = wallet;
            }

        }
    }

    private void initWidget() {
        mCreateWallet = findViewById(R.id.create_Wallet);
        mSendActivity = findViewById(R.id.send_activity);
        mWalletInfo = findViewById(R.id.get_Wallet);
        mRestore = findViewById(R.id.restore_Wallet);
        mWifKey = findViewById(R.id.wif_number);
    }

    private void initOnClick() {
        mCreateWallet.setOnClickListener(this);
        mSendActivity.setOnClickListener(this);
        mWalletInfo.setOnClickListener(this);
        mRestore.setOnClickListener(this);
        mWifKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 0) {
                    wifKey = s.toString();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_Wallet:
                createNewWallet();//创建新的钱包
                break;
            case R.id.send_activity:
                Intent intent = new Intent(this, SendActivity.class);
                intent.putExtra("assets", "");
                intent.putExtra("address", "");
                startActivity(intent);
                break;
            case R.id.get_Wallet:
                if (wallet == null) {
                    Toast.makeText(this, "No Wallet Info", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Address:=" + wallet.getAddress() + "\n" + "WIF:=" + wallet.getWIF(), Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.restore_Wallet: //恢复钱包
                if (wifKey.length() <= 0) {
                    Toast.makeText(this, "请先输入WIF!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean b = Account.INSTANCE.fromWIF(wifKey);
                    if(b){
                        Toast.makeText(this, "Success！", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, R.string.invalid_wif, Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createNewWallet() {
        //此处Demo 不进行判断 点击 即生成
        Account.INSTANCE.createNewWallet();
        startActivity(new Intent(this, CreateWalletActivity.class));

    }

    private void lockWallet() {
        KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (!manager.isKeyguardSecure()) {
            Toast.makeText(this,
                    "Secure lock screen hasn't set up.\n"
                            + "Go to 'Settings -> Security -> Screenlock' to set up a lock screen",
                    Toast.LENGTH_LONG).show();
        } else {
            Intent intent = manager.createConfirmDeviceCredentialIntent(null, null);
            if (intent != null) {
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == RESULT_OK) {
                if (requestCode == -1) {
                    //恢复钱包
                    Account.INSTANCE.restoreWalletFromDevice();

                }
            }

        }
    }
    //    /**
//     * 创建钱包
//     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void createNewWallet() {
//        SecureRandom random = new SecureRandom();
//        byte[] bytes = new byte[32];
//        random.nextBytes(bytes);
//        String hex =CryptoExtensionsKt.toHex(bytes);
//        try {
//            wallet = Neowallet.generatePublicKeyFromPrivateKey(hex);
//            String walletString = wallet.toString();
//            Log.i(TAG, "createNewWallet: "+walletString);
//
//            storeEncryptedKeyOnDevice();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    //本机存储加密Key SP中
//    private void storeEncryptedKeyOnDevice() {
//        String wif = wallet.getWIF();
//        Encryptor encryptor = new Encryptor();
//        String alias = "MyKey";
//        byte[] encryptWIF = encryptor.encryptText(alias, wif);
//        byte[] iv = encryptor.getIv();
//        assert iv != null;
//        assert encryptWIF != null;
//        EncryptedSettingsRepository.INSTANCE.setProperty(alias, CryptoExtensionsKt.toHex(encryptWIF), iv, this);
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void storeColdStorageKeyFragmentOnDevice(String keyFragment) {
//        String alias = "Cold Storage Key Fragment";
//        Encryptor encryptor = new Encryptor();
//        byte[] encryptText = encryptor.encryptText(alias, keyFragment);
//        byte[] iv = encryptor.getIv();
//        EncryptedSettingsRepository.INSTANCE.setProperty(alias, CryptoExtensionsKt.toHex(encryptText), iv, this);
//
//    }
//
//    /**
//     * 还原、解密
//     *
//     * @return
//     */
//    private String getColdStorageKeyFragmentOnDevice() {
//        String alias = "Cold Storage Key Fragment";
//        EncryptedInfo property = EncryptedSettingsRepository.INSTANCE.getProperty(alias, this);
//        if (property.getData() == null) {
//            return "";
//        }
//        byte[] storedEncryptedFragment = CryptoExtensionsKt.hexStringToByteArray(property.getData());
//        if (storedEncryptedFragment == null || storedEncryptedFragment.length == 0) {
//            return "";
//        }
//        byte[] iv = property.getIv();
//        Decryptor decryptor = new Decryptor();
//        return decryptor.decrypt(alias, storedEncryptedFragment, iv);
//    }
//
//    /**
//     * 删除Key
//     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void removeColdStorageKeyFragment() {
//        storeColdStorageKeyFragmentOnDevice("");//存储空的alias 覆盖
//    }
//
//    /**
//     * 现在是否是加密的钱包
//     *
//     * @return
//     */
//    private boolean isEncryptedWalletPresent() {
//        String alias = "MyKey";
//        EncryptedInfo property = EncryptedSettingsRepository.INSTANCE.getProperty(alias, this);
//        if (property.getData() == null) {
//            return false;
//        } else {
//            byte[] storedEncryptedWIF = CryptoExtensionsKt.hexStringToByteArray(property.getData());
//            if (storedEncryptedWIF == null || storedEncryptedWIF.length == 0) {
//                return false;
//            }
//            return true;
//        }
//
//    }
//
//    /**
//     * 从设备中还原钱包
//     */
//    private void restoreWalletFromDevice() {
//        String alias = "MyKey";
//        EncryptedInfo storedInfo = EncryptedSettingsRepository.INSTANCE.getProperty(alias, this);
//        byte[] storedEncrypedWIF = CryptoExtensionsKt.hexStringToByteArray(storedInfo.getData());
//        byte[] storedIv = storedInfo.getIv();
//        Decryptor decryptor = new Decryptor();
//        assert storedIv != null;
//        String decrypt = decryptor.decrypt(alias, storedEncrypedWIF, storedIv);
//        try {
//            wallet = Neowallet.generateFromWIF(decrypt);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("NeoDemo", "Can't restore Wallet From Device");
//        }
//
//    }
//
//    /**
//     * 从设备中删除key  关键信息置空
//     */
//    private void deleteKeyFromDevice() {
//        String alias = "MyKey";
//        EncryptedSettingsRepository.INSTANCE.setProperty(alias, "", new byte[0], this);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private boolean fromWIF(String wif) {
//        try {
//            wallet = Neowallet.generateFromWIF(wif);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        storeEncryptedKeyOnDevice();
//        return true;
//    }
//
//    private Wallet getWallet() {
//        return wallet;
//    }
//

//    //byte 转成 hex
//    public static String bytesToHexString(byte[] src) {
//        StringBuilder stringBuilder = new StringBuilder("");
//        if (src == null || src.length <= 0) {
//            return null;
//        }
//        for (int i = 0; i < src.length; i++) {
//            int v = src[i] & 0xFF;
//            String hv = Integer.toHexString(v);
//            if (hv.length() < 2) {
//                stringBuilder.append(0);
//            }
//            stringBuilder.append(hv);
//        }
//        return stringBuilder.toString();
//    }

//    /**
//     * bytes字符串转换为Byte值
//     *
//     * @param  src Byte字符串，每个Byte之间没有分隔符
//     * @return byte[]
//     */
//    public static byte[] hexStr2Bytes(String src) {
//        int m = 0, n = 0;
//        int l = src.length() / 2;
//        System.out.println(l);
//        byte[] ret = new byte[l];
//        for (int i = 0; i < l; i++) {
//            m = i * 2 + 1;
//            n = m + 1;
//            ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
//        }
//        return ret;
//    }

//    public static String str2HexStr(String str) {
//
//        char[] chars = "0123456789ABCDEF".toCharArray();
//        StringBuilder sb = new StringBuilder("");
//        byte[] bs = str.getBytes();
//        int bit;
//
//        for (int i = 0; i < bs.length; i++) {
//            bit = (bs[i] & 0x0f0) >> 4;
//            sb.append(chars[bit]);
//            bit = bs[i] & 0x0f;
//            sb.append(chars[bit]);
//            sb.append(' ');
//        }
//        return sb.toString().trim();
//    }
//
//    /**
//     * 十六进制转换字符串
//     *
//     * @param   Byte字符串(Byte之间无分隔符 如:[616C6B])
//     * @return String 对应的字符串
//     */
//    public static String hexStr2Str(String hexStr) {
//        String str = "0123456789ABCDEF";
//        char[] hexs = hexStr.toCharArray();
//        byte[] bytes = new byte[hexStr.length() / 2];
//        int n;
//
//        for (int i = 0; i < bytes.length; i++) {
//            n = str.indexOf(hexs[2 * i]) * 16;
//            n += str.indexOf(hexs[2 * i + 1]);
//            bytes[i] = (byte) (n & 0xff);
//        }
//        return new String(bytes);
//    }
}
