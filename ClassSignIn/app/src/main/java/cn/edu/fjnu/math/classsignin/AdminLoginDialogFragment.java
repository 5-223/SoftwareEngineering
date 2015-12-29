package cn.edu.fjnu.math.classsignin;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * 学委登陆对话框，若登陆成功则启动AdminActivity。
 */
public class AdminLoginDialogFragment extends DialogFragment
        implements DialogInterface.OnClickListener {
    public static final String EXTRA_PASSWD = "passwd";
    private EditText mPasswdEdit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_text, null);
        mPasswdEdit = (EditText) view.findViewById(R.id.editText);
        builder.setView(view)
                .setPositiveButton("确认", this)
                .setNegativeButton("取消", this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                this.dismiss();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                Intent intent = new Intent(getActivity(), AdminActivity.class);
                intent.putExtra(EXTRA_PASSWD, mPasswdEdit.getText().toString());
                startActivity(intent);
                break;
        }
    }
}
