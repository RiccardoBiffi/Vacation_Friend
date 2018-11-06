package com.rbiffi.vacationfriend.AppSections.Spese.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.ResolutionItem;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResolutionItemViewHolder extends ChildViewHolder {

    private ViewGroup item;
    private CircleImageView payerIcon;
    private TextView expenseTitle;
    private TextView expenseSign;
    private TextView expenseValue;
    private TextView expenseDate;
    private View tagStart;
    private View tagEnd;

    public ResolutionItemViewHolder(View itemView) {
        super(itemView);
        this.item = itemView.findViewById(R.id.resolution_item);
        this.payerIcon = itemView.findViewById(R.id.resolution_payer_icon);
        this.expenseTitle = itemView.findViewById(R.id.resolution_expense_title);
        this.expenseSign = itemView.findViewById(R.id.resolution_expense_sign);
        this.expenseValue = itemView.findViewById(R.id.resolution_expense_value);
        this.expenseDate = itemView.findViewById(R.id.resolution_expense_date);
        this.tagStart = itemView.findViewById(R.id.resolution_item_start);
        this.tagEnd = itemView.findViewById(R.id.resolution_item_end);
    }

    public void onBind(final ResolutionItem ri, final Context context) {
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, context.getString(R.string.expense_details), Toast.LENGTH_SHORT).show();
            }
        });

        this.payerIcon.setImageURI(ri.getPayerIcon());
        this.expenseTitle.setText(ri.getExpenseTitle());
        this.expenseSign.setText(ri.getExpenseSign());
        this.expenseValue.setText(ri.getExpenseValue());
        this.expenseDate.setText(ri.getExpenseDate());
    }

    public void showNeededAction(int color) {
        tagStart.setBackgroundColor(color);
        tagEnd.setBackgroundColor(color);
    }
}
