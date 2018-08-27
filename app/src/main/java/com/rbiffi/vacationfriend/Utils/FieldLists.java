package com.rbiffi.vacationfriend.Utils;

import java.util.Arrays;
import java.util.List;

public class FieldLists {

    public static List<String> getVacationFieldList() {
        return Arrays.asList(
                Constants.F_TITLE,
                Constants.F_PERIOD,
                Constants.F_PLACE,
                Constants.F_PARTECIP,
                Constants.F_PHOTO);
    }
}
