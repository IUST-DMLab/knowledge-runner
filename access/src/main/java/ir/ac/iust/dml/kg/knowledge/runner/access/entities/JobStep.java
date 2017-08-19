package ir.ac.iust.dml.kg.knowledge.runner.access.entities;

import java.util.Map;

public class JobStep {
    private String command;
    private String[] arguments;
    private Map<String, String> environment;

    public JobStep() {
    }

    public JobStep(String command, String[] arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public JobStep(String command, String[] arguments, Map<String, String> environment) {
        this.command = command;
        this.arguments = arguments;
        this.environment = environment;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }
}
