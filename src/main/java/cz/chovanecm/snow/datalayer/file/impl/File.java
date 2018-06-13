package cz.chovanecm.snow.datalayer.file.impl;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;
import java.time.ZonedDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;

@Value
@Builder
public class File {
    @Builder.Default
    private ZonedDateTime lastModified = ZonedDateTime.now();
    private String textContent;
    private byte[] byteContent;
    @NonNull
    private Path file;

    public byte[] getByteContent() {
        if (byteContent == null) {
            return textContent.getBytes(UTF_8);
        } else {
            return byteContent;
        }
    }
}
