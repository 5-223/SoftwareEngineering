package cn.edu.fjnu.math.classsignin;

import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

import cn.edu.fjnu.math.classsignin.business.Admin;
import cn.edu.fjnu.math.classsignin.model.Ids;

/**
 * 学委界面
 */
public class AdminActivity extends AppCompatActivity {
    private static final String TAG = AdminActivity.class.getSimpleName();
    private Button mStartButton;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private Button mRefershButton;
    private Button mClearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Intent intent = getIntent();
        String passwd = intent.getStringExtra(AdminLoginDialogFragment.EXTRA_PASSWD);

        initViews();

        new LoginAsyncTask().execute(passwd);
    }

    private void initViews() {
        mStartButton = (Button) findViewById(R.id.btnStart);
        mRefershButton = (Button) findViewById(R.id.btnRefresh);
        mListView = (ListView) findViewById(R.id.listView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mClearButton = (Button) findViewById(R.id.btnClear);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearAsyncTask().execute();
            }
        });

        mRefershButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RefreshAsyncTask().execute();
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 开启蓝牙
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                }
                Intent discoverIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverIntent);

                String address = bluetoothAdapter.getAddress();
                Log.i(TAG, "bluetooth address:" + address);

                new StartAsyncTask().execute(address);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String stuId = Ids.ids[position];
                Log.i(TAG, "selected id :" + stuId);
                DialogFragment fragment = new StudentOperatDialog();
                Bundle bundle = new Bundle();
                bundle.putString("id", stuId);
                fragment.setArguments(bundle);
                fragment.show(getFragmentManager(), "");
                return false;
            }
        });
    }

    /**
     * 登陆失败则结束Activity
     */
    class LoginAsyncTask extends AsyncTask<String, Void, Void> {
        private String resultStr;
        private boolean isValid;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.INVISIBLE);
            showHint(resultStr);
            if (!isValid) {
                finish();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Log.i(TAG, "check login:" + params[0]);
                isValid = cn.edu.fjnu.math.classsignin.business.Admin.check(params[0]);
                if (isValid) {
                    resultStr = "登陆成功";
                } else {
                    resultStr = "密码错误";
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
                resultStr = "无法连接服务器，请检查网络连接";
            }
            return null;
        }
    }

    /**
     * 开始签到，设置蓝牙地址。
     */
    class StartAsyncTask extends AsyncTask<String, Void, Void> {
        String resultStr;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.INVISIBLE);
            showHint(resultStr);
        }

        @Override
        protected Void doInBackground(String... params) {
            String address = params[0];
            try {
                boolean resultBoolean = Admin.setMacAddress(address);
                if (resultBoolean) {
                    resultStr = "成功";
                } else {
                    resultStr = "无法开始签到，请重试";
                }
            } catch (IOException e) {
                e.printStackTrace();
                resultStr = "无法连接服务器";
            }
            return null;
        }
    }

    /**
     * 获取签到情况，设置adapter并添加到ListView中。
     */
    class RefreshAsyncTask extends AsyncTask<Void, Void, Void> {
        ArrayAdapter<String> adapter;
        String resultStr;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.INVISIBLE);
            mListView.setAdapter(adapter);
            if (resultStr != null) {
                showHint(resultStr);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            adapter = new ArrayAdapter<String>(AdminActivity.this, android.R.layout.simple_list_item_1);
            try {
                Map<String, Boolean> map = Admin.getSigninStatus();
                for (String id : map.keySet()) {
                    if (map.get(id)) {
                        adapter.add(id + ", 已签到");
                    } else {
                        adapter.add(id + ", 未签到");
                    }
                }
            } catch (JSONException e) {
                resultStr = "服务器异常";
            } catch (IOException e) {
                resultStr = "无法连接服务器";
            }
            return null;
        }
    }

    /**
     *  清空已签到信息
     */
    class ClearAsyncTask extends AsyncTask<Void, Void, Void> {
        String hintStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (hintStr != null) {
                showHint(hintStr);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Admin.initSignin();
            } catch (IOException e) {
                e.printStackTrace();
                hintStr = "无法连接服务器";
            }

            return null;
        }
    }

    Handler mHandler = new Handler();
    private void showHint(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AdminActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
