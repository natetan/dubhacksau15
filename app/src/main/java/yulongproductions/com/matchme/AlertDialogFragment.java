package yulongproductions.com.matchme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Yulong on 10/17/2015.
 */
public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.notice_title))
                .setMessage(context.getString(R.string.empty_fields_error))
                .setPositiveButton("OK", null);
        return builder.create();
    }
}
