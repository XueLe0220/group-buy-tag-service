package cn.xuele.tag.infrastructure.redis;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Redis bitmap key and offset algorithm for crowd tag matching.
 */
public final class TagBitmapUtils {

    private static final String TAG_BITMAP_KEY_PREFIX = "crowd:tag:bitmap:";
    private static final int BITMAP_MAX_SIZE = 100_000_000;

    private TagBitmapUtils() {
    }

    public static String tagBitmapKey(String tagId) {
        return TAG_BITMAP_KEY_PREFIX + tagId;
    }

    public static long offsetOf(String userId) {
        int hash32 = Hashing.murmur3_32_fixed()
                .hashString(userId, StandardCharsets.UTF_8)
                .asInt();
        int index = hash32 & 0x7FFFFFFF;
        return index % BITMAP_MAX_SIZE;
    }

}
