package com.example.ukk2025_xiipplg1_7871;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class menuDialog extends BottomSheetDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu);

        // Menyesuaikan ukuran dialog agar setengah layar dari kiri ke tengah
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(getScreenWidth() / 1/2, WindowManager.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.START); // Muncul dari kiri
            window.setBackgroundDrawableResource(android.R.color.transparent); // Hindari background gelap
        }
        TextView tvNextPage = dialog.findViewById(R.id.kategori);
        tvNextPage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), kategori.class);
            startActivity(intent);
        });
        return dialog;
    }
    // Metode untuk mendapatkan lebar layar
    private int getScreenWidth() {
        return requireContext().getResources().getDisplayMetrics().widthPixels;
    }
}
