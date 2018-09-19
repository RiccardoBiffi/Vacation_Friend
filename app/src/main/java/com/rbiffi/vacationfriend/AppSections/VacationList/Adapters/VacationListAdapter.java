package com.rbiffi.vacationfriend.AppSections.VacationList.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationListClickEvents;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;

import java.util.List;


public class VacationListAdapter extends RecyclerView.Adapter<VacationListAdapter.VacationViewHolder> {

    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private static final int VIEW_TYPE_FOOTER = 2;
    private static final int VIEW_TYPE_HEADER = 3;

    private final LayoutInflater inflater;
    private Context context;
    private IVacationListClickEvents listener;

    private List<Vacation> vacationListNow;
    private List<Vacation> vacationListNext;
    private List<Vacation> vacationListPrevious;


    public VacationListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private static Uri resourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID));
    }

    @Override
    public VacationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                view = inflater.inflate(R.layout.vacationlist_list_header, parent, false);
                break;
            case VIEW_TYPE_OBJECT_VIEW:
                view = inflater.inflate(R.layout.vacationlist_list_row, parent, false);
                break;
            case VIEW_TYPE_FOOTER:
                view = inflater.inflate(R.layout.vacationlist_list_footer, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.vacationlist_list_row, parent, false);
                break;
        }
        return new VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VacationViewHolder holder, int position) {
        if (isHeader(position)) {
            if (position == 0) {
                if (vacationListNow.size() == 0 && vacationListNext.size() == 0) {
                    holder.vacationHeaderView.setText(R.string.vacationlist_header_previous);
                } else if (vacationListNow.size() == 0) {
                    holder.vacationHeaderView.setText(R.string.vacationlist_header_next);
                } else {
                    holder.vacationHeaderView.setText(R.string.vacationlist_header_now);
                }

            } else if (position == vacationListNow.size() + 1) {
                if (vacationListNext.size() == 0) {
                    holder.vacationHeaderView.setText(R.string.vacationlist_header_previous);
                } else {
                    holder.vacationHeaderView.setText(R.string.vacationlist_header_next);
                }

            } else { // ultimo header
                holder.vacationHeaderView.setText(R.string.vacationlist_header_previous);
            }

        } else if (isFooter(position)) {
            // do nothing

        } else {
            Vacation current;
            int elPosition;
            if (isNowList(position))
                current = vacationListNow.get(position - 1);

            else if (isNextList(position)) {
                elPosition = position - vacationListNow.size();
                elPosition -= vacationListNow.isEmpty() ? 1 : 2;
                current = vacationListNext.get(elPosition);

            } else { // isPreviousList
                elPosition = position - vacationListNow.size() - vacationListNext.size();
                elPosition -=
                        vacationListNow.isEmpty() && vacationListNext.isEmpty() ? 1 :
                                vacationListNow.isEmpty() || vacationListNext.isEmpty() ? 2 :
                                        3;
                current = vacationListPrevious.get(elPosition);
            }

            final Vacation finalVacation = current;
            holder.vacationTitleView.setText(current.title);
            holder.vacationTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVacationClick(finalVacation);
                    }
                }
            });

            Uri photoUri = !current.photo.toString().equals("") ? current.photo : resourceToUri(context, R.drawable.vacation_default_photo);
            holder.vacationImageView.setImageURI(photoUri);
            holder.vacationImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVacationClick(finalVacation);
                    }
                }
            });

            holder.vacationOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onOverflowClick(v, finalVacation);
                    }
                }
            });
        }
    }

    private boolean isNowList(int position) {
        return position <= vacationListNow.size();
    }

    private boolean isNextList(int position) {
        if (vacationListNow.isEmpty()) return position <= vacationListNext.size();
        return position > vacationListNow.size() + 1 && position <= vacationListNow.size() + 1 + vacationListNext.size();
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (vacationListNow == null && vacationListNext == null && vacationListPrevious == null)
            return itemCount;
        if (vacationListNow != null && vacationListNow.size() != 0)
            itemCount += vacationListNow.size() + 1;
        if (vacationListNext != null && vacationListNext.size() != 0)
            itemCount += vacationListNext.size() + 1;
        if (vacationListPrevious != null && vacationListPrevious.size() != 0)
            itemCount += vacationListPrevious.size() + 1;
        return itemCount + 1; // +1 per il footer
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position))
            return VIEW_TYPE_HEADER;
        if (isFooter(position))
            return VIEW_TYPE_FOOTER;
        return VIEW_TYPE_OBJECT_VIEW;
    }

    private boolean isHeader(int position) {
        if (vacationListNow.isEmpty() && vacationListNext.isEmpty() && vacationListPrevious.isEmpty())
            return false;

        if (vacationListNow.isEmpty() && vacationListNext.isEmpty() ||
                vacationListNow.isEmpty() && vacationListPrevious.isEmpty() ||
                vacationListNext.isEmpty() && vacationListPrevious.isEmpty())
            return position == 0;

        if (vacationListNow.isEmpty())
            return position == 0 || position == vacationListNext.size() + 1;
        if (vacationListNext.isEmpty())
            return position == 0 || position == vacationListNow.size() + 1;
        if (vacationListPrevious.isEmpty())
            return position == 0 || position == vacationListNow.size() + 1;

        return position == 0 || position == vacationListNow.size() + 1 || position == vacationListNow.size() + vacationListNext.size() + 2;
    }

    private boolean isFooter(int position) {
        if (vacationListNow.isEmpty() && vacationListNext.isEmpty() && vacationListPrevious.isEmpty())
            return false;

        if (vacationListNow.isEmpty() && vacationListNext.isEmpty())
            return position == vacationListPrevious.size() + 1;
        if (vacationListNow.isEmpty() && vacationListPrevious.isEmpty())
            return position == vacationListNext.size() + 1;
        if (vacationListNext.isEmpty() && vacationListPrevious.isEmpty())
            return position == vacationListNow.size() + 1;

        if (vacationListNow.isEmpty())
            return position == vacationListNext.size() + vacationListPrevious.size() + 2;
        if (vacationListNext.isEmpty())
            return position == vacationListNow.size() + vacationListPrevious.size() + 2;
        if (vacationListPrevious.isEmpty())
            return position == vacationListNow.size() + vacationListNext.size() + 2;

        return position == vacationListNow.size() + vacationListNext.size() + vacationListPrevious.size() + 3;
    }

    public void setListener(IVacationListClickEvents listener) {
        this.listener = listener;
    }

    public void setAllVacations(List<Vacation> vacationsNow, List<Vacation> vacationListNext, List<Vacation> vacationListPrevious) {
        this.vacationListNow = vacationsNow;
        this.vacationListNext = vacationListNext;
        this.vacationListPrevious = vacationListPrevious;
        notifyDataSetChanged();
    }


    class VacationViewHolder extends RecyclerView.ViewHolder {

        private final CardView vacationListRowEl;
        private final TextView vacationTitleView;
        private final ImageView vacationImageView;
        private final ImageButton vacationOverflow;
        private final TextView vacationHeaderView;

        VacationViewHolder(View itemView) {
            super(itemView);
            vacationListRowEl = itemView.findViewById(R.id.card_view);
            vacationTitleView = itemView.findViewById(R.id.vacation_title);
            vacationImageView = itemView.findViewById(R.id.vacation_image);
            vacationOverflow = itemView.findViewById(R.id.vacation_oveflow_menu);
            vacationHeaderView = itemView.findViewById(R.id.vacationlist_header);
        }
    }

}