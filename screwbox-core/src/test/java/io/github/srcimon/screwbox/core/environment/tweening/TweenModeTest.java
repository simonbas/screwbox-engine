package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Percent;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

class TweenModeTest {

    @ParameterizedTest
    @CsvSource({
            "LINEAR_IN,0.2,0.2",
            "LINEAR_IN,0.8,0.8",
            "LINEAR_OUT,0.2,0.8",
            "LINEAR_OUT,0.8,0.2",
            "SINE_IN,0.2,0.31",
            "SINE_IN,0.4,0.58",
            "SINE_IN,0.8,0.95",
            "SINE_IN_OUT,0,0",
            "SINE_IN_OUT,0.5,1",
            "SINE_IN_OUT,0.3,0.65",
            "SINE_IN_OUT,1,0",
    })
    void applyOn_inputValid_returnsUpdatedOutput(String modeName, double in, double out) {
        Percent input = Percent.of(in);

        var output = TweenMode.valueOf(modeName).applyOn(input);

        assertThat(output.value()).isEqualTo(out, offset(0.1));
    }
}
