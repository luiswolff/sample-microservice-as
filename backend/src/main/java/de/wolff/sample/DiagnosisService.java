package de.wolff.sample;

import de.wolff.sample.entities.DiagnosisValue;
import de.wolff.sample.entities.PatientEntity;

import javax.ejb.Stateful;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateful
public class DiagnosisService {

    @PersistenceContext
    private EntityManager em;

    private long patientId;

    public void setPatientId(long patientId){
        this.patientId = patientId;
    }

    @GET
    public PatientEntity getDiagnoses(){
        return em.find(PatientEntity.class, patientId);
    }

    @POST
    public void addDiagnosis(DiagnosisValue diagnosis) {
        PatientEntity patient = em.find(PatientEntity.class, patientId);
        patient.getDiagnoses().add(diagnosis);
        em.merge(patient);
    }

    @PUT
    public void updateDiagnosis(List<DiagnosisValue> diagnoses){
        PatientEntity patient = em.find(PatientEntity.class, patientId);
        patient.setDiagnoses(diagnoses);
        em.merge(patientId);
    }

}
