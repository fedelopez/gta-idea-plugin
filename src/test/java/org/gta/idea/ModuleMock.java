package org.gta.idea;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.ComponentConfig;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModel;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vcs.vfs.AbstractVcsVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.pom.PomModule;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;
import org.picocontainer.PicoContainer;

import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author FedericoL
 */
class ModuleMock implements Module {

    private final String moduleFilePath;
    private final String classesOutputRoot;
    private VirtualFileSystem vfs;

    public ModuleMock(String filePath, String classesOutputRoot) {
        this.moduleFilePath = filePath;
        this.classesOutputRoot = classesOutputRoot;
        vfs = mock(VirtualFileSystem.class);
        when(vfs.extractPresentableUrl(anyString())).thenReturn(classesOutputRoot);

    }

    @Override
    public VirtualFile getModuleFile() {
        return null;
    }

    @NotNull
    @Override
    public String getModuleFilePath() {
        return moduleFilePath;
    }

    @NotNull
    @Override
    public ModuleType getModuleType() {
        return null;
    }

    @NotNull
    @Override
    public Project getProject() {
        return null;
    }

    @NotNull
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isDisposed() {
        return false;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean isSavePathsRelative() {
        return false;
    }

    @Override
    public void setSavePathsRelative(boolean b) {
    }

    @Override
    public void setOption(@NotNull String s, @NotNull String s1) {
    }

    @Override
    public void clearOption(@NotNull String s) {
    }

    @Override
    public String getOptionValue(@NotNull String s) {
        return null;
    }

    @NotNull
    @Override
    public PomModule getPom() {
        return null;
    }

    @Override
    public GlobalSearchScope getModuleScope() {
        return null;
    }

    @Override
    public GlobalSearchScope getModuleWithLibrariesScope() {
        return null;
    }

    @Override
    public GlobalSearchScope getModuleWithDependenciesScope() {
        return null;
    }

    @Override
    public GlobalSearchScope getModuleWithDependenciesAndLibrariesScope(boolean b) {
        return null;
    }

    @Override
    public GlobalSearchScope getModuleWithDependentsScope() {
        return null;
    }

    @Override
    public GlobalSearchScope getModuleTestsWithDependentsScope() {
        return null;
    }

    @Override
    public GlobalSearchScope getModuleRuntimeScope(boolean b) {
        return null;
    }

    @Override
    public LanguageLevel getLanguageLevel() {
        return null;
    }

    @NotNull
    @Override
    public LanguageLevel getEffectiveLanguageLevel() {
        return null;
    }

    @Override
    public BaseComponent getComponent(String s) {
        return null;
    }

    @Override
    public <T> T getComponent(Class<T> tClass) {
        ModuleRootModel baseComponent = mock(ModuleRootManager.class);
        when(baseComponent.getCompilerOutputPath()).thenReturn(classesOutputDir(vfs));
        return (T) baseComponent;
    }

    @Override
    public <T> T getComponent(Class<T> tClass, T t) {
        return null;
    }

    @NotNull
    @Override
    public Class[] getComponentInterfaces() {
        return new Class[0];
    }

    @Override
    public boolean hasComponent(@NotNull Class aClass) {
        return false;
    }

    @NotNull
    @Override
    public <T> T[] getComponents(Class<T> tClass) {
        return null;
    }

    @NotNull
    @Override
    public PicoContainer getPicoContainer() {
        return null;
    }

    @Override
    public MessageBus getMessageBus() {
        return null;
    }

    @NotNull
    @Override
    public ComponentConfig[] getComponentConfigurations() {
        return new ComponentConfig[0];
    }

    @Override
    public Object getComponent(ComponentConfig componentConfig) {
        return null;
    }

    @Override
    public <T> T[] getExtensions(ExtensionPointName<T> tExtensionPointName) {
        return null;
    }

    @Override
    public ComponentConfig getConfig(Class aClass) {
        return null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public <T> T getUserData(Key<T> tKey) {
        return null;
    }

    @Override
    public <T> void putUserData(Key<T> tKey, T t) {
    }

    private VirtualFile classesOutputDir(final VirtualFileSystem vfs) {
        return new AbstractVcsVirtualFile(classesOutputRoot, vfs) {
            @Override
            public byte[] contentsToByteArray() throws IOException {
                return new byte[0];
            }

            @Override
            public boolean isDirectory() {
                return true;
            }
        };
    }
}
