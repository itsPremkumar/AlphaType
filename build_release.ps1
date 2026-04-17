# PowerShell script to build the AlphaType Production APK

Write-Host "Starting AlphaType Production Build..." -ForegroundColor Cyan

# 1. Ensure keys are generated
if (-not (Test-Path "keystore.properties")) {
    Write-Host "Signing configuration not found. Running generate_release_key.ps1 first..." -ForegroundColor Yellow
    .\generate_release_key.ps1
}

# 2. Run Gradle build
Write-Host "Building APK via Gradle..." -ForegroundColor Cyan
.\gradlew clean assembleRelease

if ($LASTEXITCODE -eq 0) {
    Write-Host "Build Successful!" -ForegroundColor Green
    
    # 3. Locate and copy the APK
    $apkPath = "app/build/outputs/apk/release/AlphaType-release.apk"
    $outputPath = "AlphaType-Production.apk"
    
    if (Test-Path $apkPath) {
        Copy-Item -Path $apkPath -Destination $outputPath -Force
        Write-Host "`nDONE! Your APK is ready for sharing:" -ForegroundColor Green
        Write-Host "File: $(Get-Item $outputPath | Select-Object -ExpandProperty FullName)" -ForegroundColor Cyan
        Write-Host "`nYou can now send 'AlphaType-Production.apk' to your friend." -ForegroundColor Magenta
    } else {
        Write-Error "Could not find build output at $apkPath"
    }
} else {
    Write-Error "Build failed. Please check the Gradle output above for errors."
    exit 1
}
