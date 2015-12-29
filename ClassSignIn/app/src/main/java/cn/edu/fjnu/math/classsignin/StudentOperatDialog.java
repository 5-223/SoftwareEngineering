package cn.edu.fjnu.math.classsignin;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.io.IOException;

import cn.edu.fjnu.math.classsignin.R;
import cn.edu.fjnu.math.classsignin.business.Server;
import cn.edu.fjnu.math.classsignin.business.User;

public class StudentOperatDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = StudentOperatDialog.class.getSimpleName();
    Button mDeleteButton;
    Button mSignInDelete;
    TextView mTextView;
    ProgressBar mProgressBar;
    Handler mHandler = new Handler();

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_student_operator, null);
        mDeleteButton = (Button) view.findViewById(R.id.btnDelete);
        mSignInDelete = (Button) view.findViewById(R.id.btnSignIn);
        mTextView = (TextView) view.findViewById(R.id.textView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mDeleteButton.setOnClickListener(this);
        mSignInDelete.setOnClickListener(this);
        Bundle bundle = getArguments();//获取传入的参数
        mTextView.setText(bundle.getString("id"));
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelete:
                SpeakerVerifier mVerifier;
                SpeechUtility.createUtility(getActivity(), "appid=55e9614f");
                mVerifier = SpeakerVerifier.createVerifier(getActivity(), new InitListener() {
                    @Override
                    public void onInit(int errorCode) {
                        if (ErrorCode.SUCCESS == errorCode) {
                            Log.i(TAG, "引擎初始化成功");
                        } else {
                            Toast.makeText(getActivity(), "引擎初始化失败，错误码：" + errorCode, Toast.LENGTH_LONG).show();
                            Log.i(TAG, "引擎初始化失败，错误码：" + errorCode);
                        }
                    }
                });
                mVerifier.setParameter(SpeechConstant.PARAMS, null);
                mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + 3);
                mVerifier.sendRequest("del", "s" + mTextView.getText().toString(), null);
                break;
            case R.id.btnSignIn:
                mProgressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            User.signIn(mTextView.getText().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                            showHint("无法连接服务器");
                            return;
                        }
                        Log.i(TAG, "手动签到成功");
                        showHint("签到成功");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    private void showHint(final String s) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
