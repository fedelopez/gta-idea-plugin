package org.gta.idea;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.DataConstantsEx;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import org.gta.SettingsUpdater;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @author fede lopez */
public class SettingsUpdaterAction extends AnAction {

    private static final Logger LOG = Logger.getLogger(SettingsUpdaterAction.class.getName());

    private static final String PLUGIN_NOT_CONFIGURED = "GTA Idea plugin is not properly configured.";
    private static final String PATH_MISSING = "Please provide a valid GTA settings file.";

    private SettingsApplicationComponent applicationComponent;
    private SettingsUpdater.Builder settingsUpdaterBuilder;

    private String packageName;
    private String className;
    private String methodName;

    @Override
    public void actionPerformed(AnActionEvent event) {
        if (!settingsFileExists()) {
            try {
                Messages.showMessageDialog(PATH_MISSING, PLUGIN_NOT_CONFIGURED, Messages.getInformationIcon());
            } catch (Throwable exception) {
                //not possible to call showMessageDialog within the tests? hmmm
            }
            return;
        }
        DataContext dataContext = event.getDataContext();
        Object data = dataContext.getData(DataConstantsEx.PSI_FILE);
        if (data instanceof PsiJavaFile) {
            PsiJavaFile javaFile = (PsiJavaFile) data;
            Object element = dataContext.getData(DataConstantsEx.PSI_ELEMENT);
            try {
                createSettingsUpdater(javaFile, element).update();
            } catch (Exception e) {
                LOG.log(Level.WARNING, e.getLocalizedMessage());
            }
        }

    }

    private SettingsUpdater createSettingsUpdater(PsiJavaFile javaFile, Object element) {
        SettingsUpdater.Builder builder = getSettingsUpdaterBuilder();

        packageName = javaFile.getPackageName();
        className = javaFile.getName();
        methodName = "";

        if (element instanceof PsiMethod) {
            configureForMethod(builder, element);
        } else if (element instanceof PsiPackage) {
            configureForPackage(builder);
        } else {
            configureForClass(builder);
        }
        return builder.build();
    }

    private File gtaSettingsFile() {
        return new File(getGtaApplicationComponent().getGTASettingsFilePath());
    }

    private void configureForPackage(SettingsUpdater.Builder builder) {
        builder.packageName(packageName)
                .className("")
                .methodName("")
                .gtaSettingsFile(gtaSettingsFile());
    }

    private void configureForClass(SettingsUpdater.Builder builder) {
        builder.packageName(packageName)
                .className(className)
                .methodName("")
                .gtaSettingsFile(gtaSettingsFile());
    }

    private void configureForMethod(SettingsUpdater.Builder builder, Object element) {
        methodName = ((PsiMethod) element).getName();
        builder.packageName(packageName)
                .className(className)
                .methodName(methodName)
                .gtaSettingsFile(gtaSettingsFile());
    }

    SettingsApplicationComponent getGtaApplicationComponent() {
        if (applicationComponent == null) {
            Application application = ApplicationManager.getApplication();
            applicationComponent = application.getComponent(SettingsApplicationComponent.class);
        }

        return applicationComponent;
    }

    void setGTAApplicationComponent(SettingsApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public SettingsUpdater.Builder getSettingsUpdaterBuilder() {
        if (settingsUpdaterBuilder == null) {
            settingsUpdaterBuilder = new SettingsUpdater.Builder();
        }
        return settingsUpdaterBuilder;
    }

    void setSettingsUpdaterBuilder(SettingsUpdater.Builder settingsUpdaterBuilder) {
        this.settingsUpdaterBuilder = settingsUpdaterBuilder;
    }

    private boolean settingsFileExists() {
        String path = getGtaApplicationComponent().getGTASettingsFilePath();
        return path != null && new File(path).exists();
    }
}
