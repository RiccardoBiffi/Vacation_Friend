package com.rbiffi.vacationfriend.Utils;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.JoinVacationParticipant;

import java.util.List;

// classe wrapper di una lista di JoinVacationParticipant, per evitare il warning del compilatore
// il warning era: unchecked generics array creation for varargs parameter asynctask
public class JoinVacationParticipantListWrapper {

    private List<JoinVacationParticipant> jvpl;

    public JoinVacationParticipantListWrapper(List<JoinVacationParticipant> jvpl) {
        this.jvpl = jvpl;
    }

    public List<JoinVacationParticipant> unwrap() {
        return jvpl;
    }
}
