package com.quant;

import com.quant.frames.impl.MainFrame;
import com.quant.managers.impl.ColumnsManager;
import com.quant.managers.Managers;
import com.quant.managers.impl.ComponentsManager;
import com.quant.managers.impl.FramesManager;
import com.quant.managers.impl.ProductsManager;

public class Main {

    public static void main(String[] args) {
        var main = new Main();
        main.init();
        main.start();
    }

    public void init() {
        var runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread(this::stop));

        Managers.add(ColumnsManager.class);
        Managers.add(ProductsManager.class);
        Managers.add(ComponentsManager.class);
        Managers.add(FramesManager.class);

        Managers.init();
    }

    public void start() {
        Managers.start();

        Managers.getManager(FramesManager.class)
                .getFrame(MainFrame.class)
                .setVisible(true);
    }

    public void stop() {
        Managers.stop();
    }

}
