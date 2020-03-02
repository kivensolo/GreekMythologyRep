package com.kingz.config;

import java.util.ArrayList;
import java.util.List;

/**
 * author：KingZ
 * date：2019/12/4
 * description：
 */
@Deprecated
public final class SampleGroup {

    public final String title;
    public final List<Sample> samples;

    public SampleGroup(String title) {
        this.title = title;
        this.samples = new ArrayList<>();
    }

    public Sample getSampleByIndex(int index){
        return samples.get(index);
    }
}
