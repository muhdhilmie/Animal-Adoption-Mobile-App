package com.example.animaladoptionapp;

public class AdminAddAnimalClass {

    String idPet, namePet, sexPet, agePet, breedPet, conditionPet, addressPet, statusPet, imgPet;

    public AdminAddAnimalClass() {
    }

    public AdminAddAnimalClass(String idPet, String namePet, String sexPet, String agePet, String breedPet, String conditionPet, String addressPet, String statusPet, String imgPet) {
        this.idPet = idPet;
        this.namePet = namePet;
        this.sexPet = sexPet;
        this.agePet = agePet;
        this.breedPet = breedPet;
        this.conditionPet = conditionPet;
        this.addressPet = addressPet;
        this.statusPet = statusPet;
        this.imgPet = imgPet;
    }

    public String getAddressPet() {
        return addressPet;
    }

    public void setAddressPet(String addressPet) {
        this.addressPet = addressPet;
    }

    public String getStatusPet() {
        return statusPet;
    }

    public void setStatusPet(String statusPet) {
        this.statusPet = statusPet;
    }

    public String getIdPet() {
        return idPet;
    }

    public void setIdPet(String idPet) {
        this.idPet = idPet;
    }

    public String getNamePet() {
        return namePet;
    }

    public void setNamePet(String namePet) {
        this.namePet = namePet;
    }

    public String getSexPet() {
        return sexPet;
    }

    public void setSexPet(String sexPet) {
        this.sexPet = sexPet;
    }

    public String getAgePet() {
        return agePet;
    }

    public void setAgePet(String agePet) {
        this.agePet = agePet;
    }

    public String getBreedPet() {
        return breedPet;
    }

    public void setBreedPet(String breedPet) {
        this.breedPet = breedPet;
    }

    public String getConditionPet() {
        return conditionPet;
    }

    public void setConditionPet(String conditionPet) {
        this.conditionPet = conditionPet;
    }

    public String getImgPet() {
        return imgPet;
    }

    public void setImgPet(String imgPet) {
        this.imgPet = imgPet;
    }
}
