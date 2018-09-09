package com.rbiffi.vacationfriend.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

// ListView quando è dentro alla RecyclerView non fa correttamente il wrap content
// Questa classe estende il metodo che misura la lunghezza e risolve il problema
public class ExtendedListView extends ListView {

    public ExtendedListView(Context context) {
        super(context);
    }

    public ExtendedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
