#!/usr/bin/env pwsh
<#
PowerShell script: compile sources, create runnable JAR and run the app.
Usage: right-click -> Run with PowerShell or open PowerShell and run: .\build_and_run.ps1
#>
Set-StrictMode -Version Latest
$proj = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $proj

if (!(Get-Command javac -ErrorAction SilentlyContinue)) {
    Write-Error "javac not found in PATH. Make sure JDK is installed and JAVA_HOME/bin is on PATH."; exit 1
}

if (!(Test-Path out)) { New-Item -ItemType Directory out | Out-Null }

Write-Host "Compiling Java sources..."
javac -d out src\*.java 2>&1 | Tee-Object build.log
if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed. See build.log for details."; exit 1
}

$manifest = "Main-Class: App`r`n"
$mf = Join-Path $proj "manifest.txt"
Set-Content -Path $mf -Value $manifest -NoNewline -Encoding ASCII

Write-Host "Creating JAR..."
jar cfm quanlydiem.jar $mf -C out .
if ($LASTEXITCODE -ne 0) { Write-Error "Jar creation failed"; exit 1 }

Write-Host "Running application..."
java -jar quanlydiem.jar

Write-Host "Done. If you want to re-run, edit sources and run this script again."
