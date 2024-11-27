package com.github.michele.cianni.nosqlite.core.storage;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.michele.cianni.nosqlite.core.json.JsonHandler;
import com.github.michele.cianni.nosqlite.core.models.Entry;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FileDataStorageTest {

    @TempDir
    private File tempDir;

    private File tempFile;

    private JsonHandler jsonHandler;

    private FileDataStorage underTest;

    @BeforeEach
    void setUp() {
        tempFile = new File(tempDir, "test.json");
        jsonHandler = mock(JsonHandler.class);
        underTest = new FileDataStorage(jsonHandler, tempFile.getAbsolutePath());
    }

    @Test
    void testSave_shouldPersistEntryAndCallSerializeEntries() throws IOException {
        Entry entry = new Entry("key1", JsonNodeFactory.instance.textNode("value1"));
        underTest.save("key1", entry);

        verify(jsonHandler).serializeEntries(anyCollection(), eq(tempFile));
        assertThat(underTest.load("key1")).isEqualTo(entry);
    }

    @Test
    void testLoad_shouldReturnExistingEntry() {
        Entry entry = new Entry("key1", JsonNodeFactory.instance.textNode("value1"));
        underTest.save("key1", entry);

        Entry result = underTest.load("key1");
        assertThat(result).isEqualTo(entry);
    }

    @Test
    void testLoad_shouldReturnNullForNonexistentKey() {
        Entry result = underTest.load("nonexistentKey");
        assertThat(result).isNull();
    }

    @Test
    void testDelete_shouldRemoveEntryAndPersistRemainingEntries() throws IOException {
        Entry entry1 = new Entry("key1", JsonNodeFactory.instance.textNode("value1"));
        Entry entry2 = new Entry("key2", JsonNodeFactory.instance.textNode("value2"));

        underTest.save("key1", entry1);
        underTest.save("key2", entry2);

        underTest.delete("key1");

        verify(jsonHandler, atLeastOnce()).serializeEntries(anyCollection(), eq(tempFile));
        assertThat(underTest.load("key1")).isNull();
        assertThat(underTest.load("key2")).isEqualTo(entry2);
    }

    @Test
    void testLoadAll_shouldReturnAllEntries() {
        Entry entry1 = new Entry("key1", JsonNodeFactory.instance.textNode("value1"));
        Entry entry2 = new Entry("key2", JsonNodeFactory.instance.textNode("value2"));

        underTest.save("key1", entry1);
        underTest.save("key2", entry2);

        Collection<Entry> allEntries = underTest.loadAll();
        assertThat(allEntries).containsExactlyInAnyOrder(entry1, entry2);
    }

    @Test
    void testSerializationError_shouldHandleExceptionGracefully() throws IOException {
        doThrow(new IOException("Serialization error")).when(jsonHandler).serializeEntries(anyCollection(), eq(tempFile));

        Entry entry = new Entry("key1", JsonNodeFactory.instance.textNode("value1"));
        underTest.save("key1", entry);

        verify(jsonHandler, atLeastOnce()).serializeEntries(anyCollection(), eq(tempFile));
    }

    @Test
    void testDeserializationError_shouldHandleExceptionGracefully() throws IOException {
        doThrow(new IOException("Deserialization error")).when(jsonHandler).deserializeEntries(tempFile);

        assertThat(underTest.loadAll()).isEmpty();
    }


    @Test
    void testUnsupportedDataType_shouldHandleGracefully() throws IOException {
        doThrow(new IOException("Unsupported data type")).when(jsonHandler).serializeEntries(anyCollection(), eq(tempFile));

        Entry entry = new Entry("key1", JsonNodeFactory.instance.binaryNode(new byte[]{0x00}));
        underTest.save("key1", entry);

        verify(jsonHandler).serializeEntries(anyCollection(), eq(tempFile));
        assertThat(underTest.load("key1")).isEqualTo(entry); // Verify in-memory storage still works
    }

    @Test
    void testSaveWithNullKey_shouldThrowException() {
        Entry entry = new Entry(null, JsonNodeFactory.instance.textNode("value1"));

        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> underTest.save(null, entry));

        assertThat(exception.getMessage()).contains("Key must not be null");
    }

    @Test
    void testSaveWithNullValue_shouldStoreNullValue() throws IOException {
        Entry entry = new Entry("key1", null);
        underTest.save("key1", entry);

        verify(jsonHandler).serializeEntries(anyCollection(), eq(tempFile));
        assertThat(underTest.load("key1")).isEqualTo(entry);
    }

    @Test
    void testLoadAllWithEmptyFile_shouldReturnEmptyCollection() {
        assertThat(underTest.loadAll()).isEmpty();
    }

}
