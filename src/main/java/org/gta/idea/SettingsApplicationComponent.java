package org.gta.idea;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author fede lopez
 */
@State(
        name = SettingsApplicationComponent.COMPONENT_NAME,
        storages = {@Storage(id = "other", file = "$APP_CONFIG$/other.xml")}
)
public class SettingsApplicationComponent implements ApplicationComponent, Configurable, PersistentStateComponent<SettingsApplicationComponent> {

    public static final String COMPONENT_NAME = "GTASettingsApplicationComponent";

    private String settingsFilePath;
    private String prodClassesDirectory;
    private String classesDirectory;

    @Transient
    private PluginForm pluginForm = new PluginForm();

    public SettingsApplicationComponent() {
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public String getSettingsFilePath() {
        return settingsFilePath;
    }

    public void setSettingsFilePath(String settingsFilePath) {
        this.settingsFilePath = settingsFilePath;
    }

    public String getProdClassesDirectory() {
        return prodClassesDirectory;
    }

    public void setProdClassesDirectory(String filePath) {
        prodClassesDirectory = filePath;
    }

    public String getClassesDirectory() {
        return classesDirectory;
    }

    public void setClassesDirectory(String classesDirectory) {
        this.classesDirectory = classesDirectory;
    }

    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    @Nls
    public String getDisplayName() {
        return "Grand Test Auto";
    }

    public Icon getIcon() {
        return null;
    }

    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {
        return pluginForm.getContentPane();
    }

    public boolean isModified() {
        return pluginForm.isModified();
    }

    public void apply() throws ConfigurationException {
        pluginForm.getData(this);
        pluginForm.setModified(false);
    }

    public void reset() {
        pluginForm.setData(this);
        pluginForm.setModified(false);
    }

    public void disposeUIResources() {
    }

    public SettingsApplicationComponent getState() {
        return this;
    }

    public void loadState(SettingsApplicationComponent settingsApplicationComponent) {
        XmlSerializerUtil.copyBean(settingsApplicationComponent, this);
    }
}
