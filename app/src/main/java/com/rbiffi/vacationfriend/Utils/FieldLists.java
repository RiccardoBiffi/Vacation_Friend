package com.rbiffi.vacationfriend.Utils;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;

import java.util.Arrays;
import java.util.List;

public class FieldLists {

    public static List<String> getEditFieldList(Class type) {
        if (type == Vacation.class) {
            return Arrays.asList(
                    Constants.F_TITLE,
                    Constants.F_PERIOD,
                    Constants.F_PLACE,
                    Constants.F_PARTECIP,
                    Constants.F_PHOTO);
        }
        return null;
    }

    public static List<String> getReadFieldList(Class type) {
        if (type == Vacation.class) {
            return Arrays.asList(
                    Constants.F_PERIOD,
                    Constants.F_PLACE,
                    Constants.F_PARTECIP);
        }
        return null;
    }
}
