package de.suzufa.screwbox.core.audio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SoundTest {

    @Test
    void fromFile_noWav_exception() {
        assertThatThrownBy(() -> Sound.fromFile("not-a-wav.txt"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Audio only supports WAV-Files at the moment.");
    }

    @Test
    void fromFile_existingWav_hasContent() {
        Sound sound = Sound.fromFile("kill.Wav");

        assertThat(sound.content()).hasSizeGreaterThan(10000);
    }

    @Test
    void fromFile_fileNameNull_exception() {
        assertThatThrownBy(() -> Sound.fromFile(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }
}
