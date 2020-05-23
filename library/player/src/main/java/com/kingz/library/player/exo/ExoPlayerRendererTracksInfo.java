package com.kingz.library.player.exo;

import java.util.Collections;
import java.util.List;

public class ExoPlayerRendererTracksInfo {

    /**
     * The exo player renderer track indexes
     */
    public final List<Integer> rendererTrackIndexes;
    /**
     * The renderer track index related to the requested <code>groupIndex</code>
     */
    public final int rendererTrackIndex;
    /**
     * The corrected exoplayer group index which may be used to obtain proper track group from the renderer
     */
    public final int rendererTrackGroupIndex;

    public ExoPlayerRendererTracksInfo(List<Integer> rendererTrackIndexes, int rendererTrackIndex, int rendererTrackGroupIndex) {
        this.rendererTrackIndexes = Collections.unmodifiableList(rendererTrackIndexes);
        this.rendererTrackIndex = rendererTrackIndex;
        this.rendererTrackGroupIndex = rendererTrackGroupIndex;
    }

}
