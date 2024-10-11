package com.quant.managers.impl;

import com.quant.frames.Frame;
import com.quant.managers.Manager;
import com.quant.utils.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class FramesManager implements Manager {

    private final List<Frame> frames = new ArrayList<>();

    @Override
    public void init() {
        var framesPackage = Frame.class.getPackage().getName() + ".impl";
        var classes = ReflectionUtils.getClassesInPackage(framesPackage);

        for (var clazz : classes) {
            try {
                frames.add((Frame) clazz.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        frames.forEach(Frame::create);
    }

    @Override
    public void stop() {

    }

    public void addFrame(Frame frame) {
        frames.add(frame);
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public <T extends Frame> T getFrame(Class<T> clazz) {
        return frames.stream().filter(clazz::isInstance).map(clazz::cast).findFirst().orElse(null);
    }

}
