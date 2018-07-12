package com.kingz.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        println "           [PLUGIN]--->[CustomPlugin apply invoked! ]"
    }
}