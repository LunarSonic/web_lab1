package web.models;

public enum HttpResponseTemplates {

    HTTP_RESPONSE("""
            Connection: keep-alive
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """),
    HTTP_RESPONSE_WITH_COOKIE("""
            Set-Cookie: sessionId=%s; Path=/; HttpOnly
            Connection: keep-alive
            Content-Type: application/json
            Content-Length: %d
           
            %s
            """),
    NOT_FOUND_RESPONSE("""
            Content-Type: text/plain
            
            Not Found
            """);

    private final String template;

    HttpResponseTemplates(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
