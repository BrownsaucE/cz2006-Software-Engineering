package com.example.zhaoyong.softwareengineering;

/**
 * Appointment object which contains all of the appointment details
 * Information includes Name, date, time, pet tpe, notes to vet, clinic and contact
 * Information can be retrieve from object
 * @author Edwin
 */

public class Appointment {
    private String aname;
    private String adate;
    private String atime;
    private String pet;
    private String notes;
    private String clinic;
    private String contact;

    /**
     * Constructor for Appointment Object
     */
    public Appointment() {
    }

    /**
     * Set the variable for the Appointment Object
     * @param aname /Pet Owner's Name
     * @param adate /Appointment Date
     * @param atime /Appointment Time
     * @param pet /Pet Type
     * @param contact /Contact Number
     * @param notes /Notes to Vet
     * @param clinic /Clinic Name
     */
    public Appointment(String aname, String adate, String atime, String pet, String contact, String notes, String clinic){
        this.aname=aname;
        this.adate=adate;
        this.atime=atime;
        this.pet=pet;
        this.contact=contact;
        this.notes=notes;
        this.clinic=clinic;
    }

    /**
     * Get Pet Owner's Name
     * @return Aname, Pet Owner's Name
     */
    public String getAname() {
        return this.aname;
    }

    /**
     * Get Appointment Date
     * @return Adate, Appointment Date
     */

    public String getAdate() {
        return this.adate;
    }

    /**
     * Get Appointment Time
     * @return Atime, Appointment Time
     */

    public String getAtime() {
        return this.atime;
    }

    /**
     * Get Pet Type
     * @return pet, Pet Type
     */

    public String getPet() {
        return this.pet;
    }

    /**
     * Get Note to Vet
     * @return notes, Notes to Vet
     */
    public String getNotes() {
        return this.notes;
    }

    /**
     * Get Clinic Name
     * @return clinic, Clinic Name
     */

    public String getClinic() {
        return this.clinic;
    }

    /**
     * Get Contact
     * @return contact, Contact
     */

    public String getContact() {
        return this.contact;
    }

}
