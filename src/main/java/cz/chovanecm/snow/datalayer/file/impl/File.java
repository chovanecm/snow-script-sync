package cz.chovanecm.snow.datalayer.file.impl;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@Value
@Builder
public class File {
    @Builder.Default
    private ZonedDateTime lastModified = ZonedDateTime.now();
    private String textContent;
    private byte[] byteContent;
    @NonNull
    private Path filePath;

    public byte[] getByteContent() {
        return Objects.requireNonNullElseGet(byteContent, () -> textContent.getBytes(UTF_8));
    }
}
