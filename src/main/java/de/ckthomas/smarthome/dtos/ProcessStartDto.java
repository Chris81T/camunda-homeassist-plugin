package de.ckthomas.smarthome.dtos;

import java.util.HashMap;
import java.util.Map;

public class ProcessStartDto {
    private String processDefinitionKey = null;
    private String businessKey = null;
    private Map<String, Object> variables = new HashMap<>();

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "ProcessStartDto{" +
                "processDefinitionKey='" + processDefinitionKey + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", variables=" + variables +
                '}';
    }
}
