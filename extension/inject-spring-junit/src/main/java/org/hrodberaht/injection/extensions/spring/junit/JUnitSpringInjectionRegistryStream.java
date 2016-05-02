package org.hrodberaht.injection.extensions.spring.junit;

import org.hrodberaht.injection.extensions.junit.stream.SchemaResourceFunc;
import org.hrodberaht.injection.extensions.junit.stream.SchemaResources;
import org.hrodberaht.injection.extensions.spring.stream.SpringInjectionRegistryStream;
import org.hrodberaht.injection.extensions.spring.stream.SpringModuleFunc;
import org.hrodberaht.injection.extensions.spring.stream.SpringModulesFunc;
import org.hrodberaht.injection.stream.RegisterModuleFunc;
import org.hrodberaht.injection.stream.RegisterResourceFunc;
import org.hrodberaht.injection.stream.ScanModuleFunc;
import org.hrodberaht.injection.stream.ScanModulesFunc;

public class JUnitSpringInjectionRegistryStream extends SpringInjectionRegistryStream {

    private JUnitSpringContainerConfigBase configBase;

    public JUnitSpringInjectionRegistryStream(JUnitSpringContainerConfigBase configBase) {
        super(configBase);
        this.configBase = configBase;
    }

    @Override
    public JUnitSpringInjectionRegistryStream scan(ScanModuleFunc scanModuleFunc) {
        super.scan(scanModuleFunc);
        return this;
    }

    @Override
    public JUnitSpringInjectionRegistryStream scan(ScanModulesFunc scanModuleFunc) {
        super.scan(scanModuleFunc);
        return this;
    }

    @Override
    public JUnitSpringInjectionRegistryStream register(RegisterModuleFunc scanModuleFunc) {
        super.register(scanModuleFunc);
        return this;
    }

    @Override
    public JUnitSpringInjectionRegistryStream resource(RegisterResourceFunc registerResourceFunc) {
        super.resource(registerResourceFunc);
        return this;
    }

    public JUnitSpringInjectionRegistryStream addSQLSchemas(SchemaResourceFunc registerResourceFunc) {
        SchemaResources schemaResources = new SchemaResources();
        registerResourceFunc.createResource(schemaResources);
        configBase.junitSQLContainerService.addSQLSchemas(schemaResources.getSchema(), schemaResources.getPath());
        return this;
    }

    public JUnitSpringInjectionRegistryStream springConfig(SpringModuleFunc scanModuleFunc) {
        super.springConfig(scanModuleFunc);
        return this;
    }

    public JUnitSpringInjectionRegistryStream springConfig(SpringModulesFunc scanModuleFunc) {
        super.springConfig(scanModuleFunc);
        return this;
    }
}
