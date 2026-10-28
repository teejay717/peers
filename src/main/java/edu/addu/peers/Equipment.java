package edu.addu.peers;

public class Equipment {
    private int equipmentId;
    private String equipmentTypeName;
    private String equipmentType;
    private int totalQuantity;
    private int availableQuantity;

    // for adding
    public Equipment(String equipmentTypeName, String equipmentType, int totalQuantity) {
        this.equipmentTypeName = equipmentTypeName;
        this.equipmentType = equipmentType;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity;
    }

    // for updating
    public Equipment(
            int equipmentId,
            String equipmentTypeName,
            String equipmentType,
            int totalQuantity) {
        this.equipmentId = equipmentId;
        this.equipmentTypeName = equipmentTypeName;
        this.equipmentType = equipmentType;
        this.totalQuantity = totalQuantity;
    }

    // for adding complete row
    public Equipment(
            int equipmentId,
            String equipmentTypeName,
            String equipmentType,
            int totalQuantity,
            int availableQuantity) {
        this.equipmentId = equipmentId;
        this.equipmentTypeName = equipmentTypeName;
        this.equipmentType = equipmentType;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public String getEquipmentTypeName() {
        return equipmentTypeName;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    @Override
    public String toString() {
        return equipmentId + " | "
                + equipmentTypeName + " | "
                + equipmentType + " | "
                + availableQuantity + "/"
                + totalQuantity;
    }
}
