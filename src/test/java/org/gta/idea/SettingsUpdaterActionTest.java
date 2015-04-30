package org.gta.idea;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.DataConstantsEx;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import org.gta.SettingsUpdater;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author fede lopez
 */
public class SettingsUpdaterActionTest {

    public static final String CLASSES_ROOT_DIR = "C:\\temp";
    private final String filePath = getClass().getResource("GTASettings.txt").getPath();

    private SettingsUpdaterAction action;

    private AnActionEvent actionEvent;
    private DataContext dataContext;

    private PsiJavaFile javaFile;
    private PsiMethod selectedMethod;

    private PsiPackage selectedPackage;
    private SettingsUpdater settingsUpdater;
    private SettingsUpdater.Builder settingsUpdaterBuilder;
    private SettingsApplicationComponent applicationComponent;

    @Test
    public void actionPerformed() throws IOException {
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("RippledownTest.java");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(javaFile).getPackageName();
        verify(javaFile).getName();

        verify(settingsUpdaterBuilder).className("RippledownTest.java");
        verify(settingsUpdaterBuilder).packageName("au.com.pks.rippledown.test");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedWithMethod() throws IOException {
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

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).methodName("doItTest");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedWithPackage() throws IOException {
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedPackage);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("IgnoreMe.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).packageName("au.com.pks.rippledown.test");
        verify(settingsUpdaterBuilder).className("");
        verify(settingsUpdaterBuilder).methodName("");

        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedNullGTAFile() throws IOException {
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(null);

        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");
        when(selectedMethod.getName()).thenReturn("doIt");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(new File(SettingsUpdaterAction.DEFAULT_GTA_SETTINGS_FILE))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedNonExistingGTAFile() throws IOException {
        when(applicationComponent.getGTASettingsFilePath()).thenReturn("I-Dont-Exist");

        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");
        when(selectedMethod.getName()).thenReturn("doIt");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedModulePathEnvironmentVariableGTAFile() throws IOException {
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("RippledownTest.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(applicationComponent.getGTASettingsFilePath()).thenReturn(SettingsUpdaterAction.MODULE_DIR + "/GTASettings.txt");

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).gtaSettingsFile(new File(filePath));
    }

    @Test
    public void actionPerformedNonTestClass() throws IOException {
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName("au.com.pks.rippledown")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className("Rippledown.java")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(javaFile).getPackageName();
        verify(javaFile).getName();

        verify(settingsUpdaterBuilder).packageName("au.com.pks.rippledown");
        verify(settingsUpdaterBuilder).className("Rippledown.java");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedNonTestMethod() throws IOException {
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

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).methodName("doIt");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedFunctionTest() throws IOException {
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

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).className("AssignTranslationPermissions.java");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedLoadTest() throws IOException {
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

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).className("AssignTranslationPermissions.java");
        verify(settingsUpdater).update();
    }

    @Test
    public void shouldSetTheDefaultClassesOutputDirectoryWhenNotSpecified() throws IOException {
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);

        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).classesDirectory(CLASSES_ROOT_DIR);
    }

    @Test
    public void shouldSetACustomClassesOutputDirectory() throws IOException {
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(applicationComponent.getClassesDirectory()).thenReturn("C:/home/joe/project-a/classes");

        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).classesDirectory("C:/home/joe/project-a/classes");
    }

    @Test
    public void shouldReplaceModuleDirConstantOnClassesOutputDirectory() throws IOException {
        when(applicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(applicationComponent.getClassesDirectory()).thenReturn("$MODULE_DIR$/classes");

        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(settingsUpdaterBuilder).classesDirectory(captor.capture());
        Assert.assertThat(captor.getValue(), not("$MODULE_DIR$/classes"));
        Assert.assertTrue(captor.getValue().endsWith("/classes"));
    }

    @Before
    public void init() {
        action = new SettingsUpdaterAction();
        javaFile = mock(PsiJavaFile.class);
        dataContext = mock(DataContext.class);
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        selectedMethod = mock(PsiMethod.class);
        selectedPackage = mock(PsiPackage.class);
        settingsUpdater = mock(SettingsUpdater.class);
        settingsUpdaterBuilder = mock(SettingsUpdater.Builder.class);
        actionEvent = new AnActionEvent(null, dataContext, "", new Presentation(), null, 0);
        applicationComponent = mock(SettingsApplicationComponent.class);

        action.setGTAApplicationComponent(applicationComponent);
        action.setSettingsUpdaterBuilder(settingsUpdaterBuilder);

        ModuleMock module = new ModuleMock(filePath, CLASSES_ROOT_DIR);
        when(dataContext.getData(DataConstantsEx.MODULE)).thenReturn(module);
    }

}
