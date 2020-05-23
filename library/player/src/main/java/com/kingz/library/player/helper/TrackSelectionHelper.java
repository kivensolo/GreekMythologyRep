/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kingz.library.player.helper;

import android.text.TextUtils;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.util.MimeTypes;
import com.kingz.library.player.exo.ExoPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Helper class for displaying track selection dialogs.
 */
public final class TrackSelectionHelper {


    private TrackGroupArray trackGroups;
    //renderer track index
    private int rendererIndex;
    private MappingTrackSelector selector;
    private TrackSelection.Factory adaptiveTrackSelectionFactory;

    public TrackSelectionHelper(MappingTrackSelector selector,
                                TrackSelection.Factory adaptiveTrackSelectionFactory) {
        this.selector = selector;
        this.adaptiveTrackSelectionFactory = adaptiveTrackSelectionFactory;
    }

    public void set(ExoPlayer.ExoBit exobit) {
        MappingTrackSelector.SelectionOverride override =
                new MappingTrackSelector.SelectionOverride(adaptiveTrackSelectionFactory, exobit.groupIndex, exobit.trackIndex);
        selector.setSelectionOverride(rendererIndex, trackGroups, override);
    }

    public List<ExoPlayer.ExoBit> get(MappedTrackInfo trackInfo, int rendererIndex) {
        this.rendererIndex = rendererIndex;
        trackGroups = trackInfo.getTrackGroups(rendererIndex);


        List<ExoPlayer.ExoBit> exobits = new ArrayList<>();

        for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
            TrackGroup group = trackGroups.get(groupIndex);
            for (int trackIndex = 0; trackIndex < group.length; trackIndex++) {
                if (trackInfo.getTrackFormatSupport(rendererIndex, groupIndex, trackIndex)
                        == RendererCapabilities.FORMAT_HANDLED) {
                    exobits.add(new ExoPlayer.ExoBit(buildTrackName(group.getFormat(trackIndex)), groupIndex, trackIndex));
                }
            }
        }
        return exobits;
    }

    public static String buildTrackName(Format format) {
        String trackName;
        if (MimeTypes.isVideo(format.sampleMimeType)) {
            trackName = joinWithSeparator(joinWithSeparator(joinWithSeparator(
                    buildResolutionString(format), buildBitrateString(format)), buildTrackIdString(format)),
                    buildSampleMimeTypeString(format));
        } else if (MimeTypes.isAudio(format.sampleMimeType)) {
            trackName = joinWithSeparator(joinWithSeparator(joinWithSeparator(joinWithSeparator(
                    buildLanguageString(format), buildAudioPropertyString(format)),
                    buildBitrateString(format)), buildTrackIdString(format)),
                    buildSampleMimeTypeString(format));
        } else {
            trackName = joinWithSeparator(joinWithSeparator(joinWithSeparator(buildLanguageString(format),
                    buildBitrateString(format)), buildTrackIdString(format)),
                    buildSampleMimeTypeString(format));
        }
        return trackName.length() == 0 ? "unknown" : trackName;
    }


    private static String buildResolutionString(Format format) {
        return format.width == Format.NO_VALUE || format.height == Format.NO_VALUE
                ? "" : format.width + "x" + format.height;
    }

    private static String buildAudioPropertyString(Format format) {
        return format.channelCount == Format.NO_VALUE || format.sampleRate == Format.NO_VALUE
                ? "" : format.channelCount + "ch, " + format.sampleRate + "Hz";
    }

    private static String buildLanguageString(Format format) {
        return TextUtils.isEmpty(format.language) || "und".equals(format.language) ? ""
                : format.language;
    }

    private static String buildBitrateString(Format format) {
        return format.bitrate == Format.NO_VALUE ? ""
                : String.format(Locale.US, "%.2fMbit", format.bitrate / 1000000f);
    }

    private static String joinWithSeparator(String first, String second) {
        return first.length() == 0 ? second : (second.length() == 0 ? first : first + ", " + second);
    }

    private static String buildTrackIdString(Format format) {
        return format.id == null ? "" : ("id:" + format.id);
    }

    private static String buildSampleMimeTypeString(Format format) {
        return format.sampleMimeType == null ? "" : format.sampleMimeType;
    }
}
