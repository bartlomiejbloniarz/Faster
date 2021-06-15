package com.bloniarz.faster.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bloniarz.faster.GameActivity;
import com.bloniarz.faster.R;
import com.bloniarz.faster.game.GameView;

public class PauseDialogFragment extends DialogFragment {

    GameView gameView;
    GameActivity gameActivity;

    public PauseDialogFragment(GameView gameView, GameActivity gameActivity){
        this.gameView = gameView;
        this.gameActivity = gameActivity;
        this.setCancelable(false);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext(), R.style.pause_alert_dialog)
                .setTitle("Game paused")
                .setNegativeButton("Resume", (dialog, which) -> {gameView.resume();} )
                .setNeutralButton("Restart", (dialog, which) -> {
                    gameView.reset();
                    gameView.resume();
                })
                .setPositiveButton("Exit", (dialog, which) -> {gameActivity.gameOver(gameView.getScore());} )
                .create();
    }

    public static String TAG = "PauseDialog";
}
