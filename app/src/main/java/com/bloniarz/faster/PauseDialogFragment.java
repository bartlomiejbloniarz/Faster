package com.bloniarz.faster;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PauseDialogFragment extends DialogFragment {

    GameView gameView;

    public PauseDialogFragment(GameView gameView){
        this.gameView = gameView;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage("Game paused")
                .setPositiveButton("Resume", (dialog, which) -> {} )
                .create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        gameView.resume();
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        ///gameView.resume();
        super.onCancel(dialog);
    }

    public static String TAG = "PauseDialog";
}
