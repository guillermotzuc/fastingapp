package com.health.personaltracker.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.health.personaltracker.R;
import com.health.personaltracker.dao.FastingDao;
import com.health.personaltracker.model.FragmentDialogBase;

public class DeleteHistoryDialogFragment extends FragmentDialogBase {
    private static final String ARG_MESSAGE = "message";
    private static final String ID = "uid";
    private static final String POSITION = "position";
    private static final String ACTIVITY_SOURCE = "source_activity";

    // Factory method to create the dialog with arguments
    public static DeleteHistoryDialogFragment newInstance(long id, int position, String label) {
        DeleteHistoryDialogFragment fragment = new DeleteHistoryDialogFragment();
        Bundle args = new Bundle();
        String message = String.format("Desea eliminar el periodo de ayuno [%s] ?", label);
        args.putString(ARG_MESSAGE, message);
        args.putString(ID, String.valueOf(id));
        args.putString(POSITION, String.valueOf(position));
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve the passed argument
        Bundle args = getArguments();
        String message = args != null ? args.getString(ARG_MESSAGE) : "Default Message";

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Eliminar Registro")
                .setIcon(R.drawable.sharp_brightness_alert_24)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle OK button click
                        long id = Long.parseLong(args.getString(ID));
                        int position = Integer.parseInt(args.getString(POSITION));

                        // Delete fasting
                        FastingDao fastingDao = getFastingDao();
                        fastingDao.deleteById(id);

                        Toast.makeText(getActivity(), "Registro eliminado", Toast.LENGTH_SHORT).show();

                        Bundle bundle = new Bundle();
                        bundle.putString(ID, String.valueOf(id));
                        bundle.putString(POSITION, String.valueOf(position));
                        bundle.putString(ACTIVITY_SOURCE, "DeleteHistoryDialogFragment");

                        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                                .findFragmentById(R.id.nav_host_fragment_activity_main);
                        NavController navController = navHostFragment.getNavController();
                        navController.navigate(R.id.navigation_history, bundle);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle Cancel button click
                        Toast.makeText(getActivity(), "Operacion cancelada", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }
}
