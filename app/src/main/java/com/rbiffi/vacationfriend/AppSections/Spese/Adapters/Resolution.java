package com.rbiffi.vacationfriend.AppSections.Spese.Adapters;

import android.net.Uri;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.ResolutionItem;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

// un Resolution è l'header sempre visibile dell'accordion.
// formato da azione, costo totale, verso, immagine e nome
// inoltre contiene una lista di figli, che sono i singoli ResolutionItem
public class Resolution extends ExpandableGroup<ResolutionItem> {

    private String action;
    private String groupValue;
    private String actionDirection;
    private Uri personIcon;
    private String personName;

    public Resolution(String title, List<ResolutionItem> items) {
        super(title, items);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getGroupValue() {
        return groupValue;
    }

    public void setGroupValue(String groupValue) {
        this.groupValue = groupValue;
    }

    public String getActionDirection() {
        return actionDirection;
    }

    public void setActionDirection(String actionDirection) {
        this.actionDirection = actionDirection;
    }

    public Uri getPersonIcon() {
        return personIcon;
    }

    public void setPersonIcon(Uri personIcon) {
        this.personIcon = personIcon;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
