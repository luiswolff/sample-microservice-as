package de.wolff.sample;

import de.wolff.sample.entities.DiagnosisValue;
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
public class DiagnosisService extends PatientService.PatientSubResource{

    @PersistenceContext
    private EntityManager em;

    @GET
    public PatientEntity getDiagnoses(){
        EntityGraph<PatientEntity> graph = em.createEntityGraph(PatientEntity.class);
        graph.addAttributeNodes("gender", "birthday", "diagnoses");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);
        return em.find(PatientEntity.class, getPatientId(), hints);
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
        em.merge(patient);
    }

}
