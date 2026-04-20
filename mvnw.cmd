@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements. See the NOTICE file.
@REM Maven Wrapper startup batch script for Windows.
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0__%"=="" SET __MVNW_ARG0__=%~dpnx0
@SET __MVNW_PSMODULEP_SAVE=%PSModulePath%
@SET PSModulePath=
@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$scriptDir='%~dp0'; $it = get-content -Path (Join-Path $scriptDir '.mvn\wrapper\maven-wrapper.properties') -ErrorAction SilentlyContinue | Where-Object {$_ -match 'distributionUrl=(.+)'}; if ($it) { $url = $Matches[1]; $path = Join-Path $env:USERPROFILE '.m2\wrapper\dists'; $zip = Join-Path $path ($url.Substring($url.LastIndexOf('/')+1)); if (-not (Test-Path $zip)) { $parent = Split-Path $zip; if (-not (Test-Path $parent)) { New-Item -Path $parent -ItemType Directory -Force | Out-Null }; Write-Host 'Downloading Maven...'; Invoke-WebRequest -Uri $url -OutFile $zip }; $extracted = Join-Path $parent ($zip.Replace('.zip','')|Split-Path -Leaf); if (-not (Test-Path $extracted)) { Expand-Archive $zip -DestinationPath $parent }; $mvnHome = Get-ChildItem $extracted -Filter 'bin' -Recurse -Directory | Select-Object -First 1; Write-Output ('MAVEN_HOME=' + $mvnHome.Parent.FullName) } }"`) DO @SET "%%A=%%B"
@SET PSModulePath=%__MVNW_PSMODULEP_SAVE%

@IF NOT EXIST "%MAVEN_HOME%\bin\mvn.cmd" (
  @echo Could not find Maven. Please check your Maven Wrapper configuration.
  @exit /b 1
)

"%MAVEN_HOME%\bin\mvn.cmd" %*
