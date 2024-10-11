package com.quant.components.topbar.filters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class CSVFileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        return file.getName().toLowerCase().endsWith(".csv") || file.isDirectory();
    }

    @Override
    public String getDescription() {
        return "CSV files";
    }
}
