package com.ccdp.appmember.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Member implements Serializable {

    private int id;
    @SerializedName("member_name")
    private String memberName;
    @SerializedName("member_email")
    private String memberEmail;
    @SerializedName("member_birthdate")
    private String memberBirthDate;
    @SerializedName("member_sex")
    private String memberSex;
    @SerializedName("member_religion")
    private String memberReligion;
    @SerializedName("member_address")
    private String memberAddress;

    public Member(){

    }

    public Member(int id, String memberName, String memberEmail,
                  String memberBirthDate, String memberSex,
                  String memberReligion, String memberAddress) {
        this.id = id;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberBirthDate = memberBirthDate;
        this.memberSex = memberSex;
        this.memberReligion = memberReligion;
        this.memberAddress = memberAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberBirthDate() {
        return memberBirthDate;
    }

    public void setMemberBirthDate(String memberBirthDate) {
        this.memberBirthDate = memberBirthDate;
    }

    public String getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(String memberSex) {
        this.memberSex = memberSex;
    }

    public String getMemberReligion() {
        return memberReligion;
    }

    public void setMemberReligion(String memberReligion) {
        this.memberReligion = memberReligion;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (id != member.id) return false;
        if (!memberName.equals(member.memberName)) return false;
        if (!memberEmail.equals(member.memberEmail)) return false;
        if (!memberBirthDate.equals(member.memberBirthDate)) return false;
        if (!memberSex.equals(member.memberSex)) return false;
        if (!memberReligion.equals(member.memberReligion)) return false;
        return memberAddress.equals(member.memberAddress);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + memberName.hashCode();
        result = 31 * result + memberEmail.hashCode();
        result = 31 * result + memberBirthDate.hashCode();
        result = 31 * result + memberSex.hashCode();
        result = 31 * result + memberReligion.hashCode();
        result = 31 * result + memberAddress.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", memberName='" + memberName + '\'' +
                ", memberEmail='" + memberEmail + '\'' +
                ", memberBirthDate='" + memberBirthDate + '\'' +
                ", memberSex='" + memberSex + '\'' +
                ", memberReligion='" + memberReligion + '\'' +
                ", memberAddress='" + memberAddress + '\'' +
                '}';
    }
}
