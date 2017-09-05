package de.wolff.sample.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.executable.ValidateOnExecution;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Patient {

    private long id;

    @FormParam("gender")
    @Pattern(regexp = "[mM]|[fF]")
    @Size(min = 1, max = 1)
    private String gender;

    private Date birthday;

    private List<Medication> medications;

    private List<Diagnosis> diagnoses;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Past
    @ValidateOnExecution
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }

    public List<Diagnosis> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @FormParam("birthday")
    public void setBirthdayAsDate(String birthday) throws ParseException{
        this.birthday = Util.dateFromWWWForm(birthday);
    }
}
