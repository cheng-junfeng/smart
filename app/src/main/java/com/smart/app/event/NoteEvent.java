package com.smart.app.event;

public class NoteEvent {
    private final NoteType type;

    private NoteEvent(NoteEvent.Builder builder) {
        this.type = builder.type;
    }

    public NoteType getType() {
        return this.type;
    }

    public static class Builder {
        private final NoteType type;

        public Builder(NoteType mType) {
            this.type = mType;
        }

        public NoteEvent build() {
            return new NoteEvent(this);
        }
    }
}
