package com.misczak.joinmybridge;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by misczak on 4/5/15.
 */
public class AboutDialog extends DialogPreference {

    Button okayButton;


    public AboutDialog(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.dialog_about);

    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle(null);
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    public void onBindDialogView(View view) {

        okayButton = (Button)view.findViewById(R.id.about_okay_button);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        super.onBindDialogView(view);

    }


}
