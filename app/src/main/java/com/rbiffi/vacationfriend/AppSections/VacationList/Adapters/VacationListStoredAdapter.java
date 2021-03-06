package com.rbiffi.vacationfriend.AppSections.VacationList.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationListClickEvents;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;

import java.io.InputStream;
import java.util.List;

public class VacationListStoredAdapter extends RecyclerView.Adapter<VacationListStoredAdapter.VacationStoredViewHolder> {

    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private static final int VIEW_TYPE_FOOTER = 2;
    private final LayoutInflater inflater;
    private final Context context;
    private IVacationListClickEvents listener;
    private List<Vacation> vacationList;


    public VacationListStoredAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private static Uri resourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID));
    }

    @NonNull
    @Override
    public VacationStoredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_OBJECT_VIEW:
                view = inflater.inflate(R.layout.vacationlist_list_row, parent, false);
                break;
            case VIEW_TYPE_FOOTER:
                view = inflater.inflate(R.layout.lists_space_footer, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.vacationlist_list_row, parent, false);
                break;
        }
        return new VacationStoredViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationStoredViewHolder holder, int position) {
        if (position != vacationList.size()) {
            //rimpiazza i dati ed assegna i click per la posizione corrente
            final Vacation current = vacationList.get(position);
            holder.vacationTitleView.setText(current.title);
            holder.vacationTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVacationClick(current);
                    }
                }
            });

            Uri photoUri = !current.photo.toString().equals("") ? current.photo : resourceToUri(context, R.drawable.vacation_default_photo);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(photoUri);
                if (inputStream.available() <= 2097152) { // immagine più piccola di 2MB
                    holder.vacationImageView.setImageURI(photoUri);
                } else {
                    int compressionFactor = inputStream.available() <= 4194304 ? 2 : 4;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = compressionFactor; // comprimo di un fattore di 2 o 4 l'immagine
                    Bitmap imageBit = BitmapFactory.decodeStream(inputStream, null, options);
                    Drawable userImage = new BitmapDrawable(context.getResources(), imageBit);
                    holder.vacationImageView.setImageDrawable(userImage);
                }
            } catch (Exception fnf) {
                Toast.makeText(context, R.string.err_photo_not_found, Toast.LENGTH_SHORT).show();
            }
            holder.vacationImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVacationClick(current);
                    }
                }
            });

            holder.vacationOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onOverflowClick(v, current);
                    }
                }
            });
        }
        //footer
    }

    @Override
    public int getItemCount() {
        if (vacationList == null) return 0;
        return vacationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == vacationList.size())
            return VIEW_TYPE_FOOTER;
        return VIEW_TYPE_OBJECT_VIEW;
    }

    public void setListener(IVacationListClickEvents listener) {
        this.listener = listener;
    }

    public void setVacations(List<Vacation> vacations) {
        vacationList = vacations;
        notifyDataSetChanged();
    }

    class VacationStoredViewHolder extends RecyclerView.ViewHolder {

        private final TextView vacationTitleView;
        private final ImageView vacationImageView;
        private final ImageButton vacationOverflow;

        VacationStoredViewHolder(View itemView) {
            super(itemView);
            vacationTitleView = itemView.findViewById(R.id.vacation_title);
            vacationImageView = itemView.findViewById(R.id.vacation_image);
            vacationOverflow = itemView.findViewById(R.id.vacation_oveflow_menu);
        }
    }
}
