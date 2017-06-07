package de.wolff.sample;

import de.wolff.sample.entities.MedicationValue;
import de.wolff.sample.entities.PatientEntity;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import java.util.List;

@Stateful
public class MedicationService extends PatientService.PatientSubResource{

    @PersistenceContext
    private EntityManager em;

    @GET
    public PatientEntity getMedications(){
        return em.find(PatientEntity.class, getPatientId());
    }

    @POST
    public void addMedication(MedicationValue medication){
        PatientEntity patient = em.find(PatientEntity.class, getPatientId());
        patient.getMedications().add(medication);
        em.merge(patient);
    }

    @PUT
    public void updateMedication(List<MedicationValue> medications){
        PatientEntity patient = em.find(PatientEntity.class, getPatientId());
        patient.setMedications(medications);
        em.merge(patient);
    }
}
