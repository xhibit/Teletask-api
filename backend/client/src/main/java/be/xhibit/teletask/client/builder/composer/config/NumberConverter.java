package be.xhibit.teletask.client.builder.composer.config;

import java.nio.ByteBuffer;

public enum NumberConverter {
    BYTE(1, new Converter() {
        @Override
        public Number toNumber(ByteBuffer buffer) {
            return buffer.get();
        }

        @Override
        public Number convert(String value) {
            return value == null ? null : Byte.valueOf(value);
        }

        @Override
        public Number cast(Number number) {
            return number.byteValue();
        }

        @Override
        public ByteBuffer putBytes(ByteBuffer buffer, Number number) {
            return buffer.put((Byte) number);
        }
    }),
    SHORT(2, new Converter() {
        @Override
        public Number toNumber(ByteBuffer buffer) {
            return buffer.getShort();
        }

        @Override
        public Number convert(String value) {
            return value == null ? null : Short.valueOf(value);
        }

        @Override
        public Number cast(Number number) {
            return number.shortValue();
        }

        @Override
        public ByteBuffer putBytes(ByteBuffer buffer, Number number) {
            return buffer.putShort((Short) number);
        }
    }),
    INTEGER(4, new Converter() {
        @Override
        public Number toNumber(ByteBuffer buffer) {
            return buffer.getInt();
        }

        @Override
        public ByteBuffer putBytes(ByteBuffer buffer, Number number) {
            return buffer.putInt((Integer) number);
        }

        @Override
        public Number convert(String value) {
            return value == null ? null : Integer.valueOf(value);
        }

        @Override
        public Number cast(Number number) {
            return number.intValue();
        }
    });

    private final int byteSize;
    private final Converter converter;

    NumberConverter(int byteSize, Converter converter) {
        this.byteSize = byteSize;
        this.converter = converter;
    }

    public byte[] read(byte[] source, int startIndex) {
        byte[] read = new byte[this.byteSize];
        System.arraycopy(source, startIndex, read, 0, this.byteSize);
        return read;
    }

    public Number convert(byte[] bytes) {
        return this.converter.toNumber(ByteBuffer.wrap(bytes));
    }

    public byte[] convert(Number number) {
        return this.converter.putBytes(ByteBuffer.allocate(this.byteSize), this.converter.cast(number)).array();
    }

    public byte[] convert(String value) {
        return this.convert(this.converter.convert(value));
    }

    private interface Converter {
        Number toNumber(ByteBuffer buffer);

        ByteBuffer putBytes(ByteBuffer buffer, Number number);

        Number convert(String value);

        Number cast(Number number);
    }
}
