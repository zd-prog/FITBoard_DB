package by.ziziko.fitboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener Listener;

    public CustomDialogFragment(DialogInterface.OnClickListener listener)
    {
        this.Listener = listener;
    }
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder.setTitle("Диалоговое окно").setMessage("Заблокировать пользователя?").setPositiveButton("Да", Listener).setNegativeButton("Нет", null).create();
    }
}
