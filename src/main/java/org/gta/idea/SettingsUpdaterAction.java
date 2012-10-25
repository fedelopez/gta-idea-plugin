package org.gta.idea;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.DataConstantsEx;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import org.apache.commons.io.FilenameUtils;
import org.gta.SettingsUpdater;

import java.io.File;

/**
 * @author fede lopez
 */
public class SettingsUpdaterAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(SettingsUpdaterAction.class.getName());

    public static final String MODULE_DIR = "$MODULE_DIR$";

    private static final String PATH_MISSING = "GTA Settings file path not existing: ";

    private SettingsApplicationComponent applicationComponent;
    private SettingsUpdater.Builder settingsUpdaterBuilder;

    private String packageName;
    private String className;
    private String methodName;
    private Module module;

    @Override
    public void actionPerformed(AnActionEvent event) {
        if (!settingsFileExists()) {
            LOG.warn(PATH_MISSING + "'" + getGtaApplicationComponent().getGTASettingsFilePath() + "'");
            return;
        }
        DataContext dataContext = event.getDataContext();
        Object data = dataContext.getData(DataConstantsEx.PSI_FILE);
        module = (Module) dataContext.getData(DataConstantsEx.MODULE);
        if (data instanceof PsiJavaFile) {
            PsiJavaFile javaFile = (PsiJavaFile) data;
            Object element = dataContext.getData(DataConstantsEx.PSI_ELEMENT);
            try {
                createSettingsUpdater(javaFile, element).update();
            } catch (Exception e) {
                LOG.warn(e);
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
        String filePath = getGtaApplicationComponent().getGTASettingsFilePath();
        if (filePath.startsWith(MODULE_DIR)) {
            filePath = filePath.replace(MODULE_DIR, FilenameUtils.getFullPath(module.getModuleFilePath()));
        }
        return new File(filePath);
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
        return path != null && (path.startsWith(MODULE_DIR) || new File(path).exists());
    }
}
