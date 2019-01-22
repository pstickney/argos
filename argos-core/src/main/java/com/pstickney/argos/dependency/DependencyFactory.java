package com.pstickney.argos.dependency;

public class DependencyFactory
{
    private DependencyFactory() {}

    public static PackageDependency createPackageDependencySysstat() {
        return new PackageDependency("sysstat");
    }

    public static PackageDependency createPackageDependencyIfstat() {
        return new PackageDependency("ifstat");
    }

    public static PackageDependency createPackageDependencyNicstat() {
        return new PackageDependency("nicstat");
    }

    public static PackageDependency createPackageDependencyLmsensors() {
        return new PackageDependency("lm-sensors");
    }

    public static PackageDependency createPackageDependencyHddtemp() {
        return new PackageDependency("hddtemp");
    }

    public static PackageDependency createPackageDependencyProcps() {
        return new PackageDependency("procps");
    }
}
