package org.gta.idea;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.DataConstantsEx;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import org.gta.SettingsUpdater;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * @author fede lopez
 */
public class SettingsUpdaterActionTest {

    private final String filePath = getClass().getResource("GTASettings.txt").getPath();

    private SettingsUpdaterAction sut;

    private AnActionEvent actionEvent;
    private DataContext dataContext;

    private Module module;
    private PsiJavaFile javaFile;
    private PsiMethod selectedMethod;

    private PsiPackage selectedPackage;
    private SettingsUpdater settingsUpdater;
    private SettingsUpdater.Builder settingsUpdaterBuilder;
    private SettingsApplicationComponent applicationComponent;

    @Test
    public void actionPerformed() throws IOException {
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("RippledownTest.java");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        sut.actionPerformed(actionEvent);

        verify(javaFile).getPackageName();
        verify(javaFile).getName();

        verify(settingsUpdaterBuilder).className("RippledownTest.java");
        verify(settingsUpdaterBuilder).packageName("au.com.pks.rippledown.test");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedWithMethod() throws IOException {
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("RippledownTest.java");
        when(selectedMethod.getName()).thenReturn("doItTest");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        sut.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).methodName("doItTest");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedWithPackage() throws IOException {
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedPackage);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("IgnoreMe.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        sut.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).packageName("au.com.pks.rippledown.test");
        verify(settingsUpdaterBuilder).className("");
        verify(settingsUpdaterBuilder).methodName("");

        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedNullGTAFile() throws IOException {
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(null);

        sut.actionPerformed(actionEvent);

        verify(settingsUpdater, never()).update();
    }

    @Test
    public void actionPerformedNonExistingGTAFile() throws IOException {
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        when(applicationComponent.getGTASettingsFilePath()).thenReturn("I-Dont-Exist");

        sut.actionPerformed(actionEvent);

        verify(settingsUpdater, never()).update();
    }

    @Test
    public void actionPerformedModulePathEnvironmentVariableGTAFile() throws IOException {
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("RippledownTest.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(applicationComponent.getGTASettingsFilePath()).thenReturn(SettingsUpdaterAction.MODULE_DIR + "/GTASettings.txt");
        when(module.getModuleFilePath()).thenReturn(filePath);

        sut.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).gtaSettingsFile(new File(filePath));
    }

    @Test
    public void actionPerformedNonTestClass() throws IOException {
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName("au.com.pks.rippledown")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className("Rippledown.java")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        sut.actionPerformed(actionEvent);

        verify(javaFile).getPackageName();
        verify(javaFile).getName();

        verify(settingsUpdaterBuilder).packageName("au.com.pks.rippledown");
        verify(settingsUpdaterBuilder).className("Rippledown.java");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedNonTestMethod() throws IOException {
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");
        when(selectedMethod.getName()).thenReturn("doIt");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        sut.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).methodName("doIt");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedFunctionTest() throws IOException {
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("rippledown.translation.functiontest");
        when(javaFile.getName()).thenReturn("AssignTranslationPermissions.java");
        when(selectedMethod.getName()).thenReturn("runTestUnsafely");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        sut.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).className("AssignTranslationPermissions.java");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedLoadTest() throws IOException {
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("rippledown.translation.loadtest");
        when(javaFile.getName()).thenReturn("AssignTranslationPermissions.java");
        when(selectedMethod.getName()).thenReturn("runTestUnsafely");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        sut.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).className("AssignTranslationPermissions.java");
        verify(settingsUpdater).update();
    }

    @Before
    public void init() {
        sut = new SettingsUpdaterAction();
        module = mock(Module.class);
        dataContext = mock(DataContext.class);
        javaFile = mock(PsiJavaFile.class);
        selectedMethod = mock(PsiMethod.class);
        selectedPackage = mock(PsiPackage.class);
        settingsUpdater = mock(SettingsUpdater.class);
        settingsUpdaterBuilder = mock(SettingsUpdater.Builder.class);
        actionEvent = new AnActionEvent(null, dataContext, "", new Presentation(), null, 0);
        applicationComponent = mock(SettingsApplicationComponent.class);

        sut.setGTAApplicationComponent(applicationComponent);
        sut.setSettingsUpdaterBuilder(settingsUpdaterBuilder);

        when(dataContext.getData(DataConstantsEx.MODULE)).thenReturn(module);
    }

}
