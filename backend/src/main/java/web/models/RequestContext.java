package web.models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestContext {
    private final Map<String, String> parameters;
    private DataFromRequest data;
    private final String rawQuery;
    private boolean wasThereHit;
    private final long startTime;
    private List<String> errorMessages;
    private String sessionId;

    public RequestContext(String rawQuery) {
        this.rawQuery = rawQuery;
        this.sessionId = null;
        this.parameters = new HashMap<>();
        this.errorMessages = new ArrayList<>();
        this.startTime = System.nanoTime();
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parsedParameters) {
        this.parameters.putAll(parsedParameters);
    }

    public DataFromRequest getData() {
        return data;
    }

    public boolean wasThereHit() {
        return wasThereHit;
    }

    public void setWasThereHit(boolean wasThereHit) {
        this.wasThereHit = wasThereHit;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setData(DataFromRequest data) {
        this.data = data;
    }

    public String getRawQuery() {
        return rawQuery;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
