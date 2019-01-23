package com.pstickney.argos.dependency;

import lombok.Data;
import lombok.ToString;

@Data
public abstract class Dependency
{
    private String pkgName;
    @ToString.Exclude private String[] pkgCmd;

    public abstract boolean exists();
}
