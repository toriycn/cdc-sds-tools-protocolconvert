package com.changan.cdc.sds.tools.jidullmsdk.llmsdk;

import java.nio.ByteBuffer;
import java.util.UUID;
public class UuidUtils {
        public static byte[] asBytes(UUID uuid) {
            ByteBuffer bb = ByteBuffer.allocate(16);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            return bb.array();
        }

        public static UUID asUuid(byte[] bytes) {
            ByteBuffer bb = ByteBuffer.wrap(bytes);
            long high = bb.getLong();
            long low = bb.getLong();
            return new UUID(high, low);
        }
}
