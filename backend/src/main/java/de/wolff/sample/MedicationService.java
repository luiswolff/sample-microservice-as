package de.wolff.sample;

import de.wolff.sample.entities.MedicationValue;
import de.wolff.sample.entities.PatientEntity;

import javax.ejb.Stateful;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateful
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicationService extends PatientService.PatientSubResource{

    @PersistenceContext
    private EntityManager em;

    @GET
    public PatientEntity getMedications(){
        EntityGraph<PatientEntity> graph = em.createEntityGraph(PatientEntity.class);
        graph.addAttributeNodes("gender", "birthday", "medications");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);
        return em.find(PatientEntity.class, getPatientId(), hints);
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
