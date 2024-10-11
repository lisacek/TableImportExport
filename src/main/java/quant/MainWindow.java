package quant;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {

    public static void main(String[] args) {
        new MainWindow().setVisible(true);
    }

    public MainWindow() {
        setTitle("Table Import / Export");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        add(getButtonsPanel(), BorderLayout.NORTH);
        add(new JScrollPane(getDataTable()), BorderLayout.CENTER);
    }

    private JPanel buttonsPanel;
    private JPanel getButtonsPanel() {
        if (buttonsPanel == null) {
            buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            buttonsPanel.add(getImportFromCsvButton());
            buttonsPanel.add(getExportToCsvButton());
        }
        return buttonsPanel;
    }

    private JButton importFromCsvButton;
    private JButton getImportFromCsvButton() {
        if (importFromCsvButton == null) {
            importFromCsvButton = new JButton(new AbstractAction("Import from CSV") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO implement
                }
            });
        }
        return importFromCsvButton;
    }

    private JButton exportToCsvButton;
    private JButton getExportToCsvButton() {
        if (exportToCsvButton == null) {
            exportToCsvButton = new JButton(new AbstractAction("Export to CSV") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO implement
                }
            });
        }
        return exportToCsvButton;
    }

    private JTable dataTable;
    private JTable getDataTable() {
        if (dataTable == null) {
            dataTable = new JTable(getDataTableModel());
            dataTable.setFillsViewportHeight(true);
        }
        return dataTable;
    }

    private TableModel dataTableModel;
    private TableModel getDataTableModel() {
        if (dataTableModel == null) {
            // TODO
        }
        return dataTableModel;
    }
}
