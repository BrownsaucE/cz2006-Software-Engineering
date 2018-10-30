package com.example.zhaoyong.softwareengineering;

/**
 * Clinic Object that stores all the clinic information
 * Information includes Clinic Name, Fax, Postal Code, Address, Unique Clinic ID, Contact Number, and Clinic Type
 * Information can be retrieve from the Clinic Object
 * @ Zhao Yong
 */
public class Clinic {
    String cname, cpostal, cfax, cAdd, cId, Ctype, Ccontact;

    /**
     * Constructor for Clinic Object
     */
    public  Clinic(){
        //Constructor for clinic object
    }

    /**
     * Set the Variables of the Clinic Object
     * @param Cname / Clinic Name
     * @param Fax_office / Fax Number
     * @param Postalcode /  Postal Code
     * @param address / Clinic Address
     * @param id / Clinic ID
     * @param Contact / Clinic Contact Number
     * @param CType / Clinic Type
     */
    public Clinic(String Cname, String Fax_office, String Postalcode, String address, String id, String Contact, String CType){
        cname = Cname; // Clinic Name
        cpostal = Postalcode; // Postal Code
        cfax = Fax_office; // Fax Number
        cAdd = address;// Clinic Address
        cId = id; // Clinic ID
        Ccontact = Contact;// Clinic Contact Number
        Ctype = CType; // Clinic Type
    }

    /**
     * Get clinic name
     * @return cname, Clinic Name
     */
    public String getClinicname(){
        return cname;
    }

    /**
     *Get Clinic Postal Code
     * @return cpostal, Clinic Postal Code
     */
    public String getClinicPostal(){
        return cpostal;
    }

    /**
     * Get Clinic Fax Number
     * @return cfax, Clinic Fax
     */
    public String getFax_office(){
        return cfax;
    }

    /**
     * Get Clinic Address
     * @return cAdd, Clinic Address
     */
    public String getClinicAddress(){
        return cAdd;
    }

    /**
     * Get Clinic Unique ID
     * @return cID, Clinic Unique ID
     */
    public String getClinicID(){
        return cId;
    }

    /**
     * Get Type of Clinic
     * @return Ctype, Clinic Type
     */
    public String getClinicType(){
        return Ctype;
    }

    /**
     * Get Clinic Contact Number
     * @return Ccontact, Clinic Contact Number
     */
    public String getClinicContact(){
        return Ccontact;
    }
}
