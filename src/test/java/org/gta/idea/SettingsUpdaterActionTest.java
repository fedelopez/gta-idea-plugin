package org.gta.idea;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.DataConstantsEx;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import org.gta.SettingsUpdater;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

/** @author fede lopez */
public class SettingsUpdaterActionTest {

    private final String filePath = getClass().getResource("GTASettings.txt").getPath();

    private SettingsUpdaterAction sut;

    private AnActionEvent mockEvent;
    private DataContext mockDataContext;

    private PsiJavaFile mockJavaFile;
    private PsiMethod mockSelectedMethod;
    private PsiPackage mockSelectedPackage;

    private SettingsUpdater mockSettingsUpdater;
    private SettingsUpdater.Builder mockSettingsUpdaterBuilder;
    private SettingsApplicationComponent mockApplicationComponent;

    @Test
    public void actionPerformed() throws IOException {
        when(mockDataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(mockJavaFile);
        when(mockApplicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(mockJavaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(mockJavaFile.getName()).thenReturn("RippledownTest.java");

        when(mockSettingsUpdaterBuilder.className(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.packageName(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.methodName("")).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(mockSettingsUpdaterBuilder);

        when(mockSettingsUpdaterBuilder.build()).thenReturn(mockSettingsUpdater);

        sut.actionPerformed(mockEvent);

        verify(mockJavaFile).getPackageName();
        verify(mockJavaFile).getName();

        verify(mockSettingsUpdaterBuilder).className("RippledownTest.java");
        verify(mockSettingsUpdaterBuilder).packageName("au.com.pks.rippledown.test");
        verify(mockSettingsUpdater).update();
    }

    @Test
    public void actionPerformedWithMethod() throws IOException {
        when(mockDataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(mockJavaFile);
        when(mockApplicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(mockDataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(mockSelectedMethod);
        when(mockJavaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(mockJavaFile.getName()).thenReturn("RippledownTest.java");
        when(mockSelectedMethod.getName()).thenReturn("doItTest");

        when(mockSettingsUpdaterBuilder.className(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.packageName(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.methodName(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(mockSettingsUpdaterBuilder);

        when(mockSettingsUpdaterBuilder.build()).thenReturn(mockSettingsUpdater);

        sut.actionPerformed(mockEvent);

        verify(mockSettingsUpdaterBuilder).methodName("doItTest");
        verify(mockSettingsUpdater).update();
    }

    @Test
    public void actionPerformedWithPackage() throws IOException {
        when(mockDataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(mockJavaFile);
        when(mockApplicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(mockDataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(mockSelectedPackage);
        when(mockJavaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(mockJavaFile.getName()).thenReturn("IgnoreMe.java");

        when(mockSettingsUpdaterBuilder.packageName(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.className("")).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.methodName("")).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(mockSettingsUpdaterBuilder);

        when(mockSettingsUpdaterBuilder.build()).thenReturn(mockSettingsUpdater);

        sut.actionPerformed(mockEvent);

        verify(mockSettingsUpdaterBuilder).packageName("au.com.pks.rippledown.test");
        verify(mockSettingsUpdaterBuilder).className("");
        verify(mockSettingsUpdaterBuilder).methodName("");

        verify(mockSettingsUpdater).update();
    }

    @Test
    public void actionPerformedNullGTAFile() throws IOException {
        when(mockDataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(mockJavaFile);
        when(mockApplicationComponent.getGTASettingsFilePath()).thenReturn(null);

        sut.actionPerformed(mockEvent);

        verify(mockSettingsUpdater, never()).update();
    }

    @Test
    public void actionPerformedNonExistingGTAFile() throws IOException {
        when(mockDataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(mockJavaFile);
        when(mockApplicationComponent.getGTASettingsFilePath()).thenReturn("I-Dont-Exist");

        sut.actionPerformed(mockEvent);

        verify(mockSettingsUpdater, never()).update();
    }

    @Test
    public void actionPerformedNonTestClass() throws IOException {
        when(mockDataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(mockJavaFile);
        when(mockApplicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(mockJavaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(mockJavaFile.getName()).thenReturn("Rippledown.java");

        when(mockSettingsUpdaterBuilder.packageName("au.com.pks.rippledown")).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.className("Rippledown.java")).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.methodName("")).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(mockSettingsUpdaterBuilder);

        when(mockSettingsUpdaterBuilder.build()).thenReturn(mockSettingsUpdater);

        sut.actionPerformed(mockEvent);

        verify(mockJavaFile).getPackageName();
        verify(mockJavaFile).getName();

        verify(mockSettingsUpdaterBuilder).packageName("au.com.pks.rippledown");
        verify(mockSettingsUpdaterBuilder).className("Rippledown.java");
        verify(mockSettingsUpdater).update();
    }

    @Test
    public void actionPerformedNonTestMethod() throws IOException {
        when(mockDataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(mockJavaFile);
        when(mockApplicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(mockDataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(mockSelectedMethod);
        when(mockJavaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(mockJavaFile.getName()).thenReturn("Rippledown.java");
        when(mockSelectedMethod.getName()).thenReturn("doIt");

        when(mockSettingsUpdaterBuilder.className(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.packageName(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.methodName(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(mockSettingsUpdaterBuilder);

        when(mockSettingsUpdaterBuilder.build()).thenReturn(mockSettingsUpdater);

        sut.actionPerformed(mockEvent);

        verify(mockSettingsUpdaterBuilder).methodName("doIt");
        verify(mockSettingsUpdater).update();
    }

    @Test
    public void actionPerformedFunctionTest() throws IOException {
        when(mockDataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(mockJavaFile);
        when(mockApplicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(mockDataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(mockSelectedMethod);
        when(mockJavaFile.getPackageName()).thenReturn("rippledown.translation.functiontest");
        when(mockJavaFile.getName()).thenReturn("AssignTranslationPermissions.java");
        when(mockSelectedMethod.getName()).thenReturn("runTestUnsafely");

        when(mockSettingsUpdaterBuilder.className(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.packageName(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.methodName(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(mockSettingsUpdaterBuilder);

        when(mockSettingsUpdaterBuilder.build()).thenReturn(mockSettingsUpdater);

        sut.actionPerformed(mockEvent);

        verify(mockSettingsUpdaterBuilder).className("AssignTranslationPermissions.java");
        verify(mockSettingsUpdater).update();
    }

    @Test
    public void actionPerformedLoadTest() throws IOException {
        when(mockDataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(mockJavaFile);
        when(mockApplicationComponent.getGTASettingsFilePath()).thenReturn(filePath);
        when(mockDataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(mockSelectedMethod);
        when(mockJavaFile.getPackageName()).thenReturn("rippledown.translation.loadtest");
        when(mockJavaFile.getName()).thenReturn("AssignTranslationPermissions.java");
        when(mockSelectedMethod.getName()).thenReturn("runTestUnsafely");

        when(mockSettingsUpdaterBuilder.className(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.packageName(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.methodName(anyString())).thenReturn(mockSettingsUpdaterBuilder);
        when(mockSettingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(mockSettingsUpdaterBuilder);

        when(mockSettingsUpdaterBuilder.build()).thenReturn(mockSettingsUpdater);

        sut.actionPerformed(mockEvent);

        verify(mockSettingsUpdaterBuilder).className("AssignTranslationPermissions.java");
        verify(mockSettingsUpdater).update();
    }

    @Before
    public void init() {
        sut = new SettingsUpdaterAction();
        mockDataContext = mock(DataContext.class);
        mockJavaFile = mock(PsiJavaFile.class);
        mockSelectedMethod = mock(PsiMethod.class);
        mockSelectedPackage = mock(PsiPackage.class);
        mockSettingsUpdater = mock(SettingsUpdater.class);
        mockSettingsUpdaterBuilder = mock(SettingsUpdater.Builder.class);
        mockEvent = new AnActionEvent(null, mockDataContext, "", new Presentation(), null, 0);
        mockApplicationComponent = mock(SettingsApplicationComponent.class);

        sut.setGTAApplicationComponent(mockApplicationComponent);
        sut.setSettingsUpdaterBuilder(mockSettingsUpdaterBuilder);
    }

}
