package com.sytiqhub.tinga.beans;

public class UserBean {

    private String uid,fname,lname,phone_number,email;
    private int mobile_verified;

    public UserBean(){

    }

    public UserBean(String uidArgs,String fnameArgs,String lnameArgs,String phone_numberArgs,String emailArgs,int mobile_verifiedArgs){

        this.uid = uidArgs;
        this.fname = fnameArgs;
        this.lname = lnameArgs;
        this.phone_number = phone_numberArgs;
        this.email = emailArgs;
        this.mobile_verified = mobile_verifiedArgs;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getMobile_verified() {
        return mobile_verified;
    }

    public void setMobile_verified(int mobile_verified) {
        this.mobile_verified = mobile_verified;
    }
}
