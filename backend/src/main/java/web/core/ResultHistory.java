package web.core;
import web.models.Result;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResultHistory {
    private static ResultHistory instance;
    private final Map<String, List<Result>> sessions = new ConcurrentHashMap<>();

    private ResultHistory() {}

    public static ResultHistory getInstance() {
        if (instance == null) {
            instance = new ResultHistory();
        }
        return instance;
    }

    public void addResult(String sessionId, Result result) {
        sessions.computeIfAbsent(sessionId, k -> new CopyOnWriteArrayList<>()).add(result);
    }

    public List<Result> getHistory(String sessionId) {
        return sessions.getOrDefault(sessionId, new CopyOnWriteArrayList<>());
    }

    public void clearHistory(String sessionId) {
        sessions.remove(sessionId);
    }
}
