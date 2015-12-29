package cn.edu.fjnu.math.classsignin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.fjnu.math.classsignin.business.Common;
import cn.edu.fjnu.math.classsignin.business.Server;
import cn.edu.fjnu.math.classsignin.business.User;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mSpeechButton;
    private ProgressBar mProgressBar;
    private TextView mUserIdView;
    private TextView mHintView;
    private TextView mNumberPwdView;
    private TextView mStatusView;
    private String mUserId;
    private boolean mHasRegisted;
    public static SpeakerVerifier mVerifier;
    private boolean mRegisting = false;
    public static boolean sInClassroom = false;
    private String mServerHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate()");
        //初始化引擎
        SpeechUtility.createUtility(this, "appid=55e9614f");
        mVerifier = SpeakerVerifier.createVerifier(this, new InitListener() {
            @Override
            public void onInit(int errorCode) {
                if (ErrorCode.SUCCESS == errorCode) {
                    Log.i(TAG, "引擎初始化成功");
                } else {
                    Toast.makeText(MainActivity.this, "引擎初始化失败，错误码：" + errorCode, Toast.LENGTH_LONG).show();
                    Log.i(TAG, "引擎初始化失败，错误码：" + errorCode);
                }
            }
        });

        //获取用户名
        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getStringExtra(LoginActivity.EXTRA_USERID);
            Log.i(TAG, "user id:" + mUserId);
        }
        if (mUserId == null || mUserId.equals("")) {
            finish();
        }

        //查询是否已经注册声纹
        mVerifier.setParameter(SpeechConstant.PARAMS, null);
        mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + 3);
        mVerifier.sendRequest("que", mUserId, mQueryListener);
        Log.i(TAG, "正在查询是否已经存在声纹");

        initViews();
        mHintView.setText("");
        mSpeechButton.setText("等下");
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVerifier.cancel(false);
        mRegisting = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //todo 获取周围蓝牙信息
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        BluetoothReceiver bluetoothReceiver = new BluetoothReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    private void initViews() {
        mSpeechButton = (Button) findViewById(R.id.speechButton);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mUserIdView = (TextView) findViewById(R.id.userIdTextView);
        mHintView = (TextView) findViewById(R.id.hintTextView);
        mNumberPwdView = (TextView) findViewById(R.id.numberPwdTextView);
        mStatusView = (TextView) findViewById(R.id.statusTextView);

        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRegisting) {
                    mVerifier.cancel(false);
                    initViews();
                    mRegisting = false;
                } else {
                    if (mHasRegisted) {
                        //开始签到
                        verify(mUserId);
                    } else {
                        getPasswd();
                    }
                }
            }
        });

        mUserIdView.setText("你的学号是：" + mUserId);
        if (mHasRegisted) {
            mHintView.setText(R.string.please_verify);
            mSpeechButton.setText(R.string.button_verify);
        } else {
            mHintView.setText(R.string.please_register);
            mSpeechButton.setText(R.string.button_register);
        }
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 获取声纹注册密码
     */
    private void getPasswd() {
        mVerifier.cancel(false);
        mVerifier.setParameter(SpeechConstant.PARAMS, null);
        mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + 3);
        mVerifier.getPasswordList(mPwdListener);
    }

    /**
     * 注册声纹
     */
    private void register() {
        mVerifier.setParameter(SpeechConstant.PARAMS, null);
//        mVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
//                Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test.pcm");
        mVerifier.setParameter(SpeechConstant.ISV_PWD, mPasswd);
        mVerifier.setParameter(SpeechConstant.AUTH_ID, mUserId);
        // 设置业务类型为注册
        mVerifier.setParameter(SpeechConstant.ISV_SST, "train");
        // 设置声纹密码类型
        mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + 3);
        // 开始注册
        mVerifier.startListening(mRegisterListener);
        mHintView.setText("请读出：" + mNumPwdSegs[0]);
        mSpeechButton.setText("取消录入");
    }

    /**
     * 验证声纹是否正确
     * @param id
     */
    private void verify(String id) {
        //todo 开始验证声纹
        mProgressBar.setVisibility(View.VISIBLE);
        Log.i(TAG, "开始验证声纹");
        mVerifier.setParameter(SpeechConstant.PARAMS, null);
        mVerifier = SpeakerVerifier.getVerifier();
        // 设置业务类型为验证
        mVerifier.setParameter(SpeechConstant.ISV_SST, "verify");
        // 数字密码注册需要传入密码
        String verifyPwd = mVerifier.generatePassword(8);
        mVerifier.setParameter(SpeechConstant.ISV_PWD, verifyPwd);
        mHintView.setText("请读出：" + verifyPwd);
        // 设置auth_id，不能设置为空
        mVerifier.setParameter(SpeechConstant.AUTH_ID, id);
        mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + 3);
        // 开始验证
        mVerifier.startListening(mVerifyListener);
    }

    /**
     * 查询声纹是否存在
     * 结果会设置到mHasRegisted并显示在界面上
     */
    private SpeechListener mQueryListener = new SpeechListener() {
        @Override
        public void onEvent(int i, Bundle bundle) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            String result = new String(buffer);
            try {
                JSONObject object = new JSONObject(result);
                String cmd = object.getString("cmd");
                int ret = object.getInt("ret");
                if (ret == ErrorCode.SUCCESS) {
                    mHasRegisted = true;
                    initViews();
                } else if (ret == ErrorCode.MSP_ERROR_FAIL) {
                    mHasRegisted = false;
                    initViews();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (null != error && ErrorCode.SUCCESS != error.getErrorCode()) {
//                Toast.makeText(MainActivity.this, "操作失败：" + error.getPlainDescription(true), Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, "声纹未录入，点击按钮开始录入声纹", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "声纹未录入");
                mHasRegisted = false;
                initViews();
            }
        }
    };

    private String[] mNumPwdSegs;
    private String mPasswd;
    /**
     *
     */
    private SpeechListener mPwdListener = new SpeechListener() {
        @Override
        public void onEvent(int i, Bundle bundle) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            String result = new String(buffer);
            StringBuffer numberString = new StringBuffer();
            try {
                JSONObject object = new JSONObject(result);
                if (!object.has("num_pwd")) {
                    return;
                }

                JSONArray pwdArray = object.optJSONArray("num_pwd");
                numberString.append(pwdArray.get(0));
                for (int i = 1; i < pwdArray.length(); i++) {
                    numberString.append("-" + pwdArray.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String numPwd = numberString.toString();
            mNumPwdSegs = numPwd.split("-");
            mPasswd = numPwd;
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            register();
        }
    };

    private VerifierListener mRegisterListener = new VerifierListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {
            mProgressBar.setVisibility(View.VISIBLE);
            mRegisting = true;
        }

        @Override
        public void onEndOfSpeech() {
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onResult(VerifierResult result) {
            if (result.ret == ErrorCode.SUCCESS) {
                switch (result.err) {
                    case VerifierResult.MSS_ERROR_IVP_GENERAL:
//                        mShowMsgTextView.setText("内核异常");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_EXTRA_RGN_SOPPORT:
//                        mShowRegFbkTextView.setText("训练达到最大次数");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
//                        mShowRegFbkTextView.setText("出现截幅");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
//                        mShowRegFbkTextView.setText("太多噪音");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
//                        mShowRegFbkTextView.setText("录音太短");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
//                        mShowRegFbkTextView.setText("训练失败，您所读的文本不一致");
                        Toast.makeText(MainActivity.this, "你读错了", Toast.LENGTH_SHORT).show();
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
//                        mShowRegFbkTextView.setText("音量太低");
                        Toast.makeText(MainActivity.this, "你读太小声了", Toast.LENGTH_SHORT).show();
                        break;
                    case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
//                        mShowMsgTextView.setText("音频长达不到自由说的要求");
                    default:
//                        mShowRegFbkTextView.setText("");
                        break;
                }

                if (result.suc == result.rgn) {
                    Toast.makeText(MainActivity.this, "录入成功", Toast.LENGTH_SHORT).show();
                    mHasRegisted = true;
                    initViews();
                    Log.i(TAG, "您的数字密码声纹ID：\n" + result.vid);
                } else {
                    int nowTimes = result.suc + 1;
                    int leftTimes = result.rgn - nowTimes;
                    mHintView.setText("请读出：" + mNumPwdSegs[nowTimes - 1]);
                    mHintView.append("\n第" + nowTimes + "遍，剩余" + leftTimes + "遍");
                }
            } else {
                mHintView.setText("录入失败，请重新开始。");
                initViews();
            }
        }

        @Override
        public void onError(SpeechError error) {
            Toast.makeText(MainActivity.this, "onError Code：" + error.getPlainDescription(true), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    /**
     * 验证声纹的监听器
     */
    private VerifierListener mVerifyListener = new VerifierListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onResult(VerifierResult result) {
            if (result.ret == 0) {
                // 验证通过
                Log.i(TAG, "声纹验证通过");
                Toast.makeText(MainActivity.this, "声纹正确", Toast.LENGTH_SHORT).show();
                new SignInAsyncTask().execute(mUserId);
            } else {
                // 验证不通过
                Log.i(TAG, "声纹验证失败");
                switch (result.err) {
                    case VerifierResult.MSS_ERROR_IVP_GENERAL:
                        mHintView.setText("内核异常");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                        mHintView.setText("出现截幅");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                        mHintView.setText("太多噪音");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                        mHintView.setText("录音太短");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                        mHintView.setText("验证不通过，您所读的文本不一致");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                        mHintView.setText("音量太低");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                        mHintView.setText("音频长达不到自由说的要求");
                        break;
                    default:
                        mHintView.setText("验证不通过");
                        break;
                }
            }
        }

        @Override
        public void onError(SpeechError error) {
            switch (error.getErrorCode()) {
                case ErrorCode.MSP_ERROR_NOT_FOUND:
                    mHintView.setText("声纹不存在，请先注册");
                    break;
                default:
                    mHintView.setText("onError Code：" + error.getPlainDescription(true));
                    break;
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };


    Handler mHandler = new Handler();
    private void hintMsg(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHintView.setText(msg);
            }
        });
    }

    /**
     * 根据蓝牙mac地址判断是否在教室，如果在教室就签到
     */
    class SignInAsyncTask extends AsyncTask<String, Void, Void> {
        private boolean currect;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (mHintView != null && currect) {
                mHintView.setText("已签到");
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            String id = params[0];
            mServerHost = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("server_host", "http://172.17.67.158/");
            Log.i(TAG, "server host:" + mServerHost);

            try {
                String address = User.getAddress();
                if (!BluetoothReceiver.sAdressList.contains(address)) {
                    hintMsg("你不在教室，或距离学委太远");
                    return null;
                }
                User.signIn(id.substring(id.indexOf('s')+1));
                Log.i(TAG, id + "已经签到");
                currect = true;
            } catch (java.io.IOException e) {
                e.printStackTrace();
                hintMsg("无法连接服务器");
            }
            return null;
        }
    }


    public void delete(String id) {
        mVerifier.setParameter(SpeechConstant.PARAMS, null);
        mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + 3);
        mVerifier.sendRequest("del", id, null);
    }
}
