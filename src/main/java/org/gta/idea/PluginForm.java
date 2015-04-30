package org.gta.idea;

import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author fede lopez
 */
public class PluginForm {
    private JTextField txtFilePath;
    private JButton btnOpenFileChooser;
    private JPanel contentPane;
    private JTextField classesPath;
    private boolean isModified;

    public PluginForm() {
        initListeners();
    }

    private void initListeners() {
        btnOpenFileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileFilter(new GTAFileFilter());
                        int result = fileChooser.showOpenDialog(contentPane);
                        if (JFileChooser.APPROVE_OPTION == result) {
                            txtFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                        }
                    }
                });

            }
        });
        txtFilePath.getDocument().addDocumentListener(new ApplyIsModifiedListener());
        classesPath.getDocument().addDocumentListener(new ApplyIsModifiedListener());
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public void setData(SettingsApplicationComponent data) {
        txtFilePath.setText(data.getGTASettingsFilePath());
        classesPath.setText(data.getClassesDirectory());
    }

    public void getData(SettingsApplicationComponent data) {
        data.setGTASettingsFilePath(txtFilePath.getText());
        data.setClassesDirectory(classesPath.getText());
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    private void applyIsModified() {
        isModified = true;
    }

    private static class GTAFileFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File file) {
            return file.isDirectory() || FilenameUtils.isExtension(file.getName(), new String[]{"txt", "properties", "xml"});
        }

        @Override
        public String getDescription() {
            return "(.txt, .properties, .xml)";
        }
    }

    private class ApplyIsModifiedListener implements DocumentListener {
        public void insertUpdate(DocumentEvent documentEvent) {
            applyIsModified();
        }

        public void removeUpdate(DocumentEvent documentEvent) {
            applyIsModified();
        }

        public void changedUpdate(DocumentEvent documentEvent) {
            applyIsModified();
        }
    }
}
