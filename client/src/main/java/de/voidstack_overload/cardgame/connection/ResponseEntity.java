package de.voidstack_overload.cardgame.connection;

public class ResponseEntity<T> {

    private boolean success;
    private T body;
    private String errorMessage;

    public ResponseEntity(boolean success, T body) {
        this.success = success;
        this.body = body;
    }

    public ResponseEntity(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return new ResponseEntity<>(true, body);
    }

    public static <T> ResponseEntity<T[]> ok(T[] body) {
        return new ResponseEntity<>(true, body);
    }

    public static <T> ResponseEntity<T> error(String errorMessage) {
        return new ResponseEntity<>(false, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getBody() {
        return body;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString()
    {
        return "ResponseEntity{" +
               "success=" + success +
               ", body=" + body +
               ", errorMessage='" + errorMessage + '\'' +
               '}';
    }
}
