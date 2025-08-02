//package entity;
//
//import java.util.Objects;
//import java.util.UUID;
//
//public class CommonWord implements Word {
//    private final UUID   id;
//    private final String text;
//
//    public CommonWord(UUID id, String text) {
//        Objects.requireNonNull(id,   "id not null or blank");
//        if (text == null || text.isBlank()) {
//            throw new IllegalArgumentException("text cannot be null or blank");
//        }
//        this.id             = id;
//        this.text           = text;
//    }
//
//    @Override
//    public UUID getId() {
//        return id;
//    }
//
//    @Override
//    public String getText() {
//        return text;
//    }
//}
//
