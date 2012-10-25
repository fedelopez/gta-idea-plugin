package org.gta.idea;

import org.apache.commons.io.FilenameUtils;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.interception.FileChooserHandler;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import java.util.logging.Logger;

/**
 * @author fede lopez
 */
public class PluginFormTest extends UISpecTestCase {

    private final static Logger LOG = Logger.getLogger(PluginFormTest.class.getName());

    private TextBox txtFilePath;
    private Button btnOpenFileChooser;

    private String filePath;

    private PluginForm pluginForm;

    public void testClickOpenFileChooser() {
        if (isLinux()) {
            LOG.info("This test cannot run on headless environments (No X11 DISPLAY variable).");
            return;
        }

        assertTrue(txtFilePath.textIsEmpty());

        WindowHandler windowHandler = FileChooserHandler.init()
                .assertAcceptsFilesOnly()
                .select(filePath);

        WindowInterceptor
                .init(btnOpenFileChooser.triggerClick())
                .process(windowHandler)
                .run();

        assertTrue(pluginForm.isModified());

        filePath = FilenameUtils.normalize(FilenameUtils.getPath(filePath));
        assertTrue(txtFilePath.getText().contains("GTASettings.txt"));
    }

    public void testModulePathVariable() {

        txtFilePath.setText("$MODULE_DIR$/src/GTASettings.txt");

        assertTrue(pluginForm.isModified());
    }

    public void testInvalidModulePathVariable() {

        txtFilePath.setText("MODULE_DIR/src/GTASettings.txt");

        assertFalse(pluginForm.isModified());

    }

    public void testIsModified() {
        assertFalse(pluginForm.isModified());

        txtFilePath.setText("invalid file");
        assertFalse(pluginForm.isModified());

        txtFilePath.setText(filePath);
        assertTrue(pluginForm.isModified());
    }


    public static boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return (!os.contains("win") && !os.contains("mac"));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        pluginForm = new PluginForm();
        Panel pluginFormPanel = new Panel(pluginForm.getContentPane());
        txtFilePath = pluginFormPanel.getTextBox("txtFilePath");
        btnOpenFileChooser = pluginFormPanel.getButton();

        filePath = getClass().getResource("GTASettings.txt").getFile();
    }

}
