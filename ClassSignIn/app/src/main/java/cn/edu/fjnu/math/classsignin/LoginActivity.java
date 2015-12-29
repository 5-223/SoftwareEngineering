package cn.edu.fjnu.math.classsignin;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import cn.edu.fjnu.math.classsignin.business.Common;
import cn.edu.fjnu.math.classsignin.model.Ids;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String EXTRA_USERID = "id";
    private static final String KEY_USERID = "id";
    private ProgressBar mProgressBar;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
//        String userId = sp.getString(KEY_USERID, "");
//
//        if (!userId.isEmpty()) {
//            onSelectedUserId(userId);
//        }

        initViews(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initViews(View v) {
        mListView = (ListView) findViewById(R.id.idsListView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        new GetIdsAsyncTask().execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] ids = Ids.ids;
                String selectedUserId = ids[position];

//                SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString(KEY_USERID, selectedUserId);
//                editor.apply();

                onSelectedUserId(selectedUserId);
            }
        });
    }

    private void onSelectedUserId(String userId) {
        userId = "s" + userId;
        Log.i(TAG, "user id: " + userId);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(EXTRA_USERID, userId);
        startActivity(intent);
    }

    class GetIdsAsyncTask extends AsyncTask<Void, Void, Void> {
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
            if (resultStr != null) {
                Toast.makeText(LoginActivity.this, resultStr, Toast.LENGTH_LONG).show();
            } else {
                ListAdapter adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, Ids.ids);
                mListView.setAdapter(adapter);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Ids.ids = Common.getIds();
            } catch (IOException e) {
                e.printStackTrace();
                resultStr = "无法连接服务器";
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        DialogFragment fragment;
        switch (id) {
            case R.id.action_settings:
                fragment = new SetServerDialogFragment();
                fragment.show(getFragmentManager(), "设置服务器地址");
                return true;
            case R.id.action_admin:
                //show dialog, input password
                fragment = new AdminLoginDialogFragment();
                fragment.show(getFragmentManager(), "输入密码");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
