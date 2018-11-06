package com.rbiffi.vacationfriend.AppSections.Spese.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class ResolutionViewHolder extends GroupViewHolder {

    private ImageView arrow;
    private TextView action;
    private TextView groupValue;
    private TextView actionDirection;
    private CircleImageView personIcon;
    private TextView personName;
    private ViewGroup background;
    private Button solveButton;

    public ResolutionViewHolder(View itemView) {
        super(itemView);
        this.arrow = itemView.findViewById(R.id.resolution_dropdown_icon);
        this.action = itemView.findViewById(R.id.resolution_action_label);
        this.groupValue = itemView.findViewById(R.id.resolution_group_value);
        this.actionDirection = itemView.findViewById(R.id.resolution_action_direction);
        this.personIcon = itemView.findViewById(R.id.resolution_person_icon);
        this.personName = itemView.findViewById(R.id.resolution_person_name);
        this.solveButton = itemView.findViewById(R.id.resolution_solve_button);
        this.background = itemView.findViewById(R.id.resolution_background);
    }

    public void setAction(Resolution resolution) {
        this.action.setText(resolution.getAction());
    }

    public void setGroupValue(Resolution resolution) {
        this.groupValue.setText(resolution.getGroupValue());
    }

    public void setActionDirection(Resolution resolution) {
        this.actionDirection.setText(resolution.getActionDirection());
    }

    public void setPersonIcon(Resolution resolution) {
        this.personIcon.setImageURI(resolution.getPersonIcon());
    }

    public void setPersonName(Resolution resolution) {
        this.personName.setText(resolution.getPersonName());
    }

    public void setBackground(int color) {
        this.background.setBackgroundColor(color);
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    public void setButtonListener(final Context context) {
        solveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action.getText().equals(context.getString(R.string.resolution_receive))) {
                    Toast.makeText(context, R.string.completed_, Toast.LENGTH_SHORT).show();
                    // dovrei comunicare ad un listener che l'elemento non c'è più
                } else {
                    AlertDialog.Builder builderConferma = new AlertDialog.Builder(context);
                    builderConferma.setTitle(R.string.resolution_done_dialog_title);
                    builderConferma.setMessage(R.string.resolution_done_dialog_message);
                    builderConferma.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            solveButton.setText(R.string.button_wait);
                        }
                    });

                    builderConferma.setNegativeButton(R.string.button_ignore, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    });

                    builderConferma.create().show();
                }
            }
        });
    }
}
