package org.gta.idea;

import org.apache.commons.io.FilenameUtils;
import org.uispec4j.*;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.interception.FileChooserHandler;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author fede lopez
 */
public class PluginFormTest extends UISpecTestCase {

    private final static Logger LOG = Logger.getLogger(PluginFormTest.class.getName());

    private TextBox gtaSettingsFilePath;
    private Button btnOpenFileChooser;

    private CheckBox updateClasses;
    private TextBox classesPath;

    private String filePath;

    private PluginForm pluginForm;

    public void testClickOpenFileChooser() {
        if (isLinux()) {
            LOG.info("This test cannot run on headless environments (No X11 DISPLAY variable).");
            return;
        }

        assertTrue(gtaSettingsFilePath.textIsEmpty());

        WindowHandler windowHandler = FileChooserHandler.init()
                .assertAcceptsFilesOnly()
                .select(filePath);

        WindowInterceptor
                .init(btnOpenFileChooser.triggerClick())
                .process(windowHandler)
                .run();

        waitUntil(new Assertion() {
            @Override
            public void check() {
                pluginForm.isModified();
            }
        }, 2000);

        filePath = FilenameUtils.normalize(FilenameUtils.getPath(filePath));
        assertTrue(gtaSettingsFilePath.getText().contains("GTASettings.txt"));
    }

    public void testModulePathVariable() {

        gtaSettingsFilePath.setText("$MODULE_DIR$/src/GTASettings.txt");

        assertTrue(pluginForm.isModified());
    }

    public void testIsModified() {
        assertFalse(pluginForm.isModified());

        gtaSettingsFilePath.setText("somefile.txt");
        assertTrue(pluginForm.isModified());

        gtaSettingsFilePath.setText(filePath);
        assertTrue(pluginForm.isModified());
    }

    public void testIsModifiedWhenSelectingToUpdateClasses() {
        assertFalse(pluginForm.isModified());

        updateClasses.click();

        assertTrue(pluginForm.isModified());
    }

    public void testIsModifiedWhenSettingClassesDirectory() {
        assertFalse(pluginForm.isModified());

        updateClasses.click();
        pluginForm.setModified(false);

        classesPath.setText("C:\\my-classes-dir");
        assertTrue(pluginForm.isModified());
    }

    public void testShouldEnableTextFieldWhenSelectingUpdateClassesDirectory() {
        assertFalse(updateClasses.isSelected());
        assertFalse(classesPath.isEditable());

        updateClasses.click();
        assertThat(classesPath.isEditable());
        updateClasses.click();
        assertFalse(classesPath.isEditable());
    }

    public void testSetData() {
        SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);
        when(settingsUpdaterAction.getGTASettingsFilePath()).thenReturn("C:\\GTASettings.txt");
        when(settingsUpdaterAction.isUpdateClassesDirectory()).thenReturn(true);
        when(settingsUpdaterAction.getClassesDirectory()).thenReturn("C:\\dev\\classes");

        pluginForm.setData(settingsUpdaterAction);

        assertThat(gtaSettingsFilePath.textEquals("C:\\GTASettings.txt"));
        assertThat(updateClasses.isSelected());
        assertThat(classesPath.textEquals("C:\\dev\\classes"));
    }

    public void testGetData() {
        gtaSettingsFilePath.setText("C:\\helloGTA");
        updateClasses.click();
        classesPath.setText("C:\\helloGTAClasses");

        SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);
        pluginForm.getData(settingsUpdaterAction);
        verify(settingsUpdaterAction).setGTASettingsFilePath("C:\\helloGTA");
        verify(settingsUpdaterAction).setUpdateClassesDirectory(true);
        verify(settingsUpdaterAction).setClassesDirectory("C:\\helloGTAClasses");
    }

    private static boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return (!os.contains("win") && !os.contains("mac"));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        pluginForm = new PluginForm();
        Panel pluginFormPanel = new Panel(pluginForm.getContentPane());
        gtaSettingsFilePath = pluginFormPanel.getTextBox("txtFilePath");
        btnOpenFileChooser = pluginFormPanel.getButton();
        updateClasses = pluginFormPanel.getCheckBox();
        classesPath = pluginFormPanel.getTextBox("classesPath");

        filePath = getClass().getResource("GTASettings.txt").getFile();
    }

}
