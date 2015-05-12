package org.gta.idea;

import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author fede lopez
 */
public class PluginFormTest extends UISpecTestCase {

    private TextBox gtaSettingsFilePath;
    private TextBox classesPath;
    private String filePath;
    private PluginForm pluginForm;

    public void testModulePathVariable() {
        gtaSettingsFilePath.setText("$MODULE_DIR$/src/GTASettings.txt");
        assertTrue(pluginForm.isModified());
    }

    public void testIsModifiedWhenGTASettingsFilePath() {
        assertFalse(pluginForm.isModified());

        gtaSettingsFilePath.setText("somefile.txt");
        assertTrue(pluginForm.isModified());

        gtaSettingsFilePath.setText(filePath);
        assertTrue(pluginForm.isModified());
    }

    public void testIsModifiedWhenSettingClassesDirectory() {
        assertFalse(pluginForm.isModified());

        classesPath.setText("C:\\some-dir");
        assertTrue(pluginForm.isModified());
    }

    public void testSetData() {
        SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);
        when(settingsUpdaterAction.getGTASettingsFilePath()).thenReturn("C:\\GTASettings.txt");
        when(settingsUpdaterAction.getClassesDirectory()).thenReturn("C:\\dev\\classes");

        pluginForm.setData(settingsUpdaterAction);

        assertThat(gtaSettingsFilePath.textEquals("C:\\GTASettings.txt"));
        assertThat(classesPath.textEquals("C:\\dev\\classes"));
    }

    public void testGetData() {
        gtaSettingsFilePath.setText("C:\\helloGTA");
        classesPath.setText("C:\\helloGTAClasses");

        SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);
        pluginForm.getData(settingsUpdaterAction);
        verify(settingsUpdaterAction).setGTASettingsFilePath("C:\\helloGTA");
        verify(settingsUpdaterAction).setClassesDirectory("C:\\helloGTAClasses");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        pluginForm = new PluginForm();
        Panel pluginFormPanel = new Panel(pluginForm.getContentPane());
        gtaSettingsFilePath = pluginFormPanel.getTextBox("txtFilePath");
        classesPath = pluginFormPanel.getTextBox("classesPath");
        filePath = getClass().getResource("GTASettings.txt").getFile();
    }

}
