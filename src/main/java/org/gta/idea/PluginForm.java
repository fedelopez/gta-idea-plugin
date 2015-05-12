package org.gta.idea;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author fede lopez
 */
public class PluginForm {
    private JTextField txtFilePath;
    private JPanel contentPane;
    private JTextField classesPath;
    private boolean isModified;

    public PluginForm() {
        initListeners();
    }

    private void initListeners() {
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
