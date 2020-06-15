package com.example.shrinkage;

public class Model {

    String employee_id,g_activity_Desc, percentage;

    public Model() {
    }

    public Model(String employee_id, String g_activity_Desc, String percentage) {
        this.employee_id = employee_id;
        this.g_activity_Desc = g_activity_Desc;
        this.percentage = percentage;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getG_activity_Desc() {
        return g_activity_Desc;
    }

    public void setG_activity_Desc(String g_activity_Desc) {
        this.g_activity_Desc = g_activity_Desc;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
