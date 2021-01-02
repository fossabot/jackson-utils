package ru.olegcherednik.utils.jackson;

import org.testng.annotations.Test;

import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 02.01.2021
 */
@Test
public class ZoneIdZonedDateTimeSerializerTest {

    public void shouldCreateNewInstanceWhenCallWithFeature() {
        ZoneId zone = ZoneId.of("Asia/Singapore");
        LocalZonedDateTimeSerializer ser = new LocalZonedDateTimeSerializer(zone);

        assertThat(ser.withFeatures(false)).isExactlyInstanceOf(ZoneIdZonedDateTimeSerializer.class);
        assertThat(ser.withFeatures(false, false)).isExactlyInstanceOf(ZoneIdZonedDateTimeSerializer.class);
    }

    private static final class LocalZonedDateTimeSerializer extends ZoneIdZonedDateTimeSerializer {

        private static final long serialVersionUID = 1319340992384997514L;

        public LocalZonedDateTimeSerializer(ZoneId zone) {
            super(zone);
        }

        @Override
        public ZoneIdZonedDateTimeSerializer withFeatures(Boolean writeZoneId) {
            return super.withFeatures(writeZoneId);
        }

        @Override
        public ZoneIdZonedDateTimeSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
            return super.withFeatures(writeZoneId, writeNanoseconds);
        }
    }
}
