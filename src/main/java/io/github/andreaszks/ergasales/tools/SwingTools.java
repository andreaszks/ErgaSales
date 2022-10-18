package io.github.andreaszks.ergasales.tools;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/** Contains methods that modify or create Swing components. */
public final class SwingTools {

  /**
   * Resizes table so the small cells do not take much space if there are cells with more content.
   *
   * <p>More info at <a
   * href="https://tips4java.wordpress.com/2008/11/10/table-column-adjuster/">tipis4java</a>
   *
   * @param table
   */
  public static void resizeColumns(JTable table) {
    for (int column = 0; column < table.getColumnCount(); column++) {
      TableColumn tableColumn = table.getColumnModel().getColumn(column);
      int preferredWidth = tableColumn.getMinWidth();
      int maxWidth = tableColumn.getMaxWidth();

      for (int row = 0; row < table.getRowCount(); row++) {
        TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
        Component c = table.prepareRenderer(cellRenderer, row, column);
        int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
        preferredWidth = Math.max(preferredWidth, width);

        if (preferredWidth >= maxWidth) {
          preferredWidth = maxWidth;
          break;
        }
      }

      tableColumn.setPreferredWidth(preferredWidth);
    }
  }

  /**
   * Shows a JOptionPane error message dialog that describe an XML error.
   *
   * @param message
   */
  public static void showXmlErrorDialog(String message) {
    JOptionPane.showMessageDialog(null, "XML File: " + message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Shows a JOptionPane error message dialog.
   *
   * @param message
   */
  public static void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Creates a Save FileChooser and returns the selected file.
   *
   * <p>If the File already exists, ask for overwrite confirmation.
   *
   * @param extension the extension of the file, written without the dot
   * @return the selected File or null if cancelled
   */
  public static File showExportFileChooser(String extension) {
    JFileChooser fc = new JFileChooser();
    if (extension == null || extension.length() == 0) return null;
    fc.addChoosableFileFilter(
        new FileNameExtensionFilter(extension.toUpperCase() + " Files", extension));
    fc.setAcceptAllFileFilterUsed(false);

    int action = fc.showSaveDialog(null);

    if (action == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();

      if (!file.getName().endsWith("." + extension)) {
        file = new File(file.getAbsolutePath() + "." + extension);
      }

      if (file.exists()) {
        int result =
            JOptionPane.showConfirmDialog(
                null,
                "File already exists, overwrite?",
                "Existing file",
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (result == JOptionPane.YES_OPTION) return file;

      } else {
        return file;
      }
    }
    // Close window/Cancel
    return null;
  }

  /**
   * Shows a JFileChooser open dialog that lists only the files of specified extension.
   *
   * @param extension
   * @return the file you select, null if dialog is closed/cancelled
   */
  public static File showImportFileChooser(String extension) {
    JFileChooser fc = new JFileChooser();
    if (extension == null || extension.length() == 0) return null;
    fc.addChoosableFileFilter(
        new FileNameExtensionFilter(extension.toUpperCase() + " Files", extension));
    fc.setAcceptAllFileFilterUsed(false);

    int action = fc.showOpenDialog(null);

    if (action == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      if (file.exists() && file.getName().endsWith("." + extension)) {
        return file;
      }
    }
    // Close window/Cancel
    return null;
  }

  private SwingTools() {}
}
