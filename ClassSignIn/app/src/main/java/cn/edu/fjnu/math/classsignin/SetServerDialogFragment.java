package cn.edu.fjnu.math.classsignin;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.edu.fjnu.math.classsignin.business.Server;

public class SetServerDialogFragment extends DialogFragment
        implements DialogInterface.OnClickListener{
    private EditText mEditText;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_text, null);
        mEditText = (EditText) view.findViewById(R.id.editText);
        mEditText.setHint(Server.mServerHost);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
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
                String host = mEditText.getText().toString();
                if (host != null && !host.isEmpty()) {
                    Server.mServerHost = host;
                }
                break;
        }
    }
}
