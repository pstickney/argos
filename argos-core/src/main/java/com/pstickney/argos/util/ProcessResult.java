package com.pstickney.argos.util;

import lombok.Data;

import java.util.List;

@Data
public class ProcessResult
{
    private int exitStatus;
    private List<String> stdout;
    private List<String> stderr;
}
