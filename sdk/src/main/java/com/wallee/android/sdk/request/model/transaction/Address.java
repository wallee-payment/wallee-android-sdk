package com.wallee.android.sdk.request.model.transaction;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallee.android.sdk.request.model.base.LegalOrganizationForm;
import com.wallee.android.sdk.request.model.base.Gender;

/**
 * Address class as defined in the web API.
 */

public final class Address implements Parcelable{
    private String city;
    private String commercialRegisterNumber;
    private String country;
    private String dateOfBirth;
    private String dependentLocality;
    private String emailAddress;
    private String familyName;
    private Gender gender;
    private String givenName;
    private LegalOrganizationForm legalOrganizationForm;
    private String mobilePhoneNumber;
    private String organizationName;
    private String phoneNumber;
    private String postCode;
    private String postalState;
    private String salesTaxNumber;
    private String salutation;
    private String socialSecurityNumber;
    private String sortingCode;
    private String street;

    protected Address(Parcel in) {
        city = in.readString();
        commercialRegisterNumber = in.readString();
        country = in.readString();
        dateOfBirth = in.readString();
        dependentLocality = in.readString();
        emailAddress = in.readString();
        familyName = in.readString();
        gender = in.readParcelable(Gender.class.getClassLoader());
        givenName = in.readString();
        legalOrganizationForm = in.readParcelable(LegalOrganizationForm.class.getClassLoader());
        mobilePhoneNumber = in.readString();
        organizationName = in.readString();
        phoneNumber = in.readString();
        postCode = in.readString();
        postalState = in.readString();
        salesTaxNumber = in.readString();
        salutation = in.readString();
        socialSecurityNumber = in.readString();
        sortingCode = in.readString();
        street = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getCity() {
        return city;
    }

    public String getCommercialRegisterNumber() {
        return commercialRegisterNumber;
    }

    public String getCountry() {
        return country;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDependentLocality() {
        return dependentLocality;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFamilyName() {
        return familyName;
    }

    public Gender getGender() {
        return gender;
    }

    public String getGivenName() {
        return givenName;
    }

    public LegalOrganizationForm getLegalOrganizationForm() {
        return legalOrganizationForm;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getPostalState() {
        return postalState;
    }

    public String getSalesTaxNumber() {
        return salesTaxNumber;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public String getSortingCode() {
        return sortingCode;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(city);
        parcel.writeString(commercialRegisterNumber);
        parcel.writeString(country);
        parcel.writeString(dateOfBirth);
        parcel.writeString(dependentLocality);
        parcel.writeString(emailAddress);
        parcel.writeString(familyName);
        parcel.writeParcelable(gender, i);
        parcel.writeString(givenName);
        parcel.writeParcelable(legalOrganizationForm, i);
        parcel.writeString(mobilePhoneNumber);
        parcel.writeString(organizationName);
        parcel.writeString(phoneNumber);
        parcel.writeString(postCode);
        parcel.writeString(postalState);
        parcel.writeString(salesTaxNumber);
        parcel.writeString(salutation);
        parcel.writeString(socialSecurityNumber);
        parcel.writeString(sortingCode);
        parcel.writeString(street);
    }
}
