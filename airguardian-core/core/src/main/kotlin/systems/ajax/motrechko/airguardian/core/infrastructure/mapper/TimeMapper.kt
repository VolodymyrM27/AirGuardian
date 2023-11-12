package systems.ajax.motrechko.airguardian.core.infrastructure.mapper

import com.google.protobuf.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toProtoTimestampBuilder(): Timestamp.Builder {
    val instant = this.atZone(ZoneOffset.UTC).toInstant()
    return Timestamp.newBuilder()
        .setSeconds(instant.epochSecond)
        .setNanos(instant.nano)
}

fun Timestamp.toLocalDateTime(): LocalDateTime {
    val instant = Instant.ofEpochSecond(seconds, nanos.toLong())
    return instant.atZone(ZoneOffset.UTC).toLocalDateTime()
}

