package de.wolff.sample;

import de.wolff.sample.entities.PatientEntity;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientService {

    @PersistenceContext
    private EntityManager em;

    @GET
    public List<PatientEntity> loadPatients(){
        return em.createQuery("select p from patients p", PatientEntity.class).getResultList();
    }

    @POST
    public void addPatient(PatientEntity patient){
        em.persist(patient);
        if (patient.getMedications() != null) patient.getMedications().forEach(em::persist);
        if (patient.getDiagnoses() != null) patient.getDiagnoses().forEach(em::persist);
    }

    @GET
    @Path("{patientId}")
    public PatientEntity loadPatient(@PathParam("patientId") long patientId){
        return em.find(PatientEntity.class, patientId);
    }

    @PUT
    @Path("{patientId}")
    public void updatePatient(@PathParam("patientId") long patientId, PatientEntity patient){
        patient.setId(patientId);
        em.merge(patient);
    }

    @DELETE
    @Path("{patientId}")
    public void deletepatient(@PathParam("patientId") long patientId){
        PatientEntity patient = em.getReference(PatientEntity.class, patientId);
        if (patient != null){
            em.remove(patient);
        }
    }

    @Path("{patientId}/medications")
    public PatientSubResource getMedicationService(@PathParam("patientId") long patientId){
        return getSubResource(patientId, "java:module/MedicationService");
    }

    @Path("{patientId}/diagnoses")
    public PatientSubResource getDiagnosisService(@PathParam("patientId") long patientId){
        return getSubResource(patientId, "java:module/DiagnosisService");
    }

    private PatientSubResource getSubResource(long patientId, String jndiName){
        try {
            PatientSubResource subResource = InitialContext.doLookup(jndiName);
            subResource.setPatientId(patientId);
            return subResource;
        } catch (NamingException e){
            throw new EJBException(e);
        }
    }

    public static abstract class PatientSubResource{

        private long patientId;

        public long getPatientId() {
            return patientId;
        }

        public void setPatientId(long patientId) {
            this.patientId = patientId;
        }
    }
}
