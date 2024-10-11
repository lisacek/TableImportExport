package com.quant;

import com.quant.cons.CSVFile;
import com.quant.cons.ProductsImport;
import com.quant.exceptions.InvalidFileException;
import com.quant.exceptions.UnsupportedFileTypeException;
import com.quant.managers.Managers;
import com.quant.managers.impl.ColumnsManager;
import com.quant.managers.impl.ProductsManager;
import com.quant.utils.CsvUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvImportTest {

    @BeforeEach
    public void setupManagers() {
        Managers.add(ColumnsManager.class);
        Managers.getManager(ProductsManager.class);
        Managers.init();
        Managers.start();
    }

    @Test
    public void testValidCsvFile() throws UnsupportedFileTypeException {
        var file = new File("src/test/resources/valid.csv");
        var csvFile = new CSVFile(file);

        var expectedData = List.of(
                List.of("Name", "Product ID", "Brand", "Price", "Amount"),
                List.of("Rabbit - Frozen", "1", "Ambi Even and Clear Daily Moisturizer", "3250.24", "3338"),
                List.of("Mikes Hard Lemonade", "2", "Pramipexole Dihydrochloride", "3567.01", "9121"),
                List.of("Sweet Pea Sprouts", "3", "BeneFIX", "5752.0", "4223")
        );

        assertEquals(3, csvFile.calculateLinesAmount());
        assertEquals(expectedData, csvFile.getData());
    }

    @Test
    public void testInvalidCsvFile() {
        var file = new File("src/test/resources/invalid.csv");
        var csvFile = new CSVFile(file);

        Exception exception = null;
        try {
            csvFile.loadData();
            CsvUtils.loadProducts(file, null);
        } catch (InvalidFileException | UnsupportedFileTypeException e) {
            exception = e;
        }

        assertTrue(exception instanceof InvalidFileException);
    }

    @Test
    public void testIfFileIsCsv() {
        var file = new File("src/test/resources/not-a-csv");
        var csvFile = new CSVFile(file);

        Exception exception = null;
        try {
            csvFile.loadData();
        } catch (Exception e) {
            exception = e;
        }

        assertTrue(exception instanceof UnsupportedFileTypeException);
    }

    @Test
    public void testProductsImport() throws InvalidFileException, UnsupportedFileTypeException {
        var file = new File("src/test/resources/valid.csv");
        var productsImport = new ProductsImport(List.of(file));

        CsvUtils.loadProducts(productsImport);

        assertEquals(3, productsImport.getProducts().size());
        assertEquals("Rabbit - Frozen", productsImport.getProducts().get(0).getName());
        assertEquals("Mikes Hard Lemonade", productsImport.getProducts().get(1).getName());
        assertEquals(3, productsImport.getProducts().get(2).getId());
    }
}
