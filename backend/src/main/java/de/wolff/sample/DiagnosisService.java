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
public class DiagnosisService extends PatientService.PatientSubResource{

    @PersistenceContext
    private EntityManager em;

    private long patientId;

    @GET
    public PatientEntity getDiagnoses(){
        return em.find(PatientEntity.class, getPatientId());
    }

    @POST
    public void addDiagnosis(DiagnosisValue diagnosis) {
        PatientEntity patient = em.find(PatientEntity.class, getPatientId());
        patient.getDiagnoses().add(diagnosis);
        em.merge(patient);
    }

    @PUT
    public void updateDiagnosis(List<DiagnosisValue> diagnoses){
        PatientEntity patient = em.find(PatientEntity.class, getPatientId());
        patient.setDiagnoses(diagnoses);
        em.merge(patientId);
    }

}
