package com.vg.subtitle.native.mapper

import androidx.annotation.Keep
import com.vg.subtitle.api.model.Segment

/**
 * Maps native results to SDK models.
 */
@Keep
object NativeResultMapper {
    /**
     * Called from JNI to create a Segment list.
     * Note: JNI can create the list directly, this provides a hook if needed.
     */
    fun mapSegments(rawSegments: List<Segment>): List<Segment> {
        return rawSegments
    }
}
