package com.rbiffi.vacationfriend.AppSections.Spese.Adapters;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rbiffi.vacationfriend.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResolutionViewHolder extends GroupViewHolder {

    private TextView action;
    private TextView groupValue;
    private TextView actionDirection;
    private CircleImageView personIcon;
    private TextView personName;
    private Button solveButton;

    public ResolutionViewHolder(View itemView) {
        super(itemView);
        this.action = itemView.findViewById(R.id.resolution_action_label);
        this.groupValue = itemView.findViewById(R.id.resolution_group_value);
        this.actionDirection = itemView.findViewById(R.id.resolution_action_direction);
        this.personIcon = itemView.findViewById(R.id.resolution_person_icon);
        this.personName = itemView.findViewById(R.id.resolution_person_name);
        this.solveButton = itemView.findViewById(R.id.resolution_solve_button);
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

}
