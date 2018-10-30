package com.rbiffi.vacationfriend.AppSections.Spese.Adapters;

import android.view.View;
import android.widget.TextView;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.ResolutionItem;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResolutionItemViewHolder extends ChildViewHolder {

    private CircleImageView payerIcon;
    private TextView expenseTitle;
    private TextView expenseSign;
    private TextView expenseValue;
    private TextView expenseDate;

    public ResolutionItemViewHolder(View itemView) {
        super(itemView);
        this.payerIcon = itemView.findViewById(R.id.resolution_payer_icon);
        this.expenseTitle = itemView.findViewById(R.id.resolution_expense_title);
        this.expenseSign = itemView.findViewById(R.id.resolution_expense_sign);
        this.expenseValue = itemView.findViewById(R.id.resolution_group_value);
        this.expenseDate = itemView.findViewById(R.id.resolution_expense_date);
    }

    public void onBind(ResolutionItem ri) {
        this.payerIcon.setImageURI(ri.getPayerIcon());
        this.expenseTitle.setText(ri.getExpenseTitle());
        this.expenseSign.setText(ri.getExpenseSign());
        this.expenseValue.setText(ri.getExpenseValue());
        this.expenseDate.setText(ri.getExpenseDate());
    }

}
